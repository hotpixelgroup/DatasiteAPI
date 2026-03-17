package com.example.userapi.service;

import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The service layer contains business logic and sits between the controller and repository.
 *
 * @Service - Marks this class as a Spring-managed service bean. This is a specialisation
 *            of @Component, meaning Spring will:
 *            1. Create a single instance (singleton) of this class at startup
 *            2. Make it available for dependency injection into other classes (like UserController)
 *            Functionally identical to @Component, but @Service communicates intent - this
 *            class holds business logic.
 *
 * Why have a service layer between controller and repository?
 *   - Separation of concerns: controllers handle HTTP, services handle logic, repositories handle data
 *   - Business rules (validation, transformations) go here, not in the controller
 *   - Multiple controllers could reuse the same service methods
 *   - Easier to unit test business logic in isolation
 */
@Service
public class UserService {

    /**
     * The repository is injected via constructor injection (see constructor below).
     * Marked 'final' because:
     *   - It should never be reassigned after construction
     *   - Guarantees the dependency is set (constructor enforces it)
     */
    private final UserRepository userRepository;

    /**
     * Constructor injection - Spring automatically passes the UserRepository bean here.
     * This is the recommended way to inject dependencies in Spring (over @Autowired on fields)
     * because:
     *   - Dependencies are explicit and required (can't create UserService without a repository)
     *   - The object is fully initialised after construction
     *   - Easier to test (just pass a mock repository in the constructor)
     *
     * When Spring creates UserService, it sees this constructor requires a UserRepository,
     * looks it up in its bean container, and passes it in automatically.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the database.
     * Delegates to JpaRepository's findAll(), which runs: SELECT * FROM users
     *
     * @return a List of all User entities (empty list if no users exist)
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a single user by their ID.
     * Delegates to JpaRepository's findById(), which runs: SELECT * FROM users WHERE id = ?
     *
     * @param id the user's primary key
     * @return Optional<User> - contains the User if found, or empty if no user with that ID exists.
     *         Optional is used instead of returning null to avoid NullPointerExceptions
     *         and force the caller to handle the "not found" case explicitly.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Creates a new user in the database.
     * Delegates to JpaRepository's save(), which runs: INSERT INTO users (name, email) VALUES (?, ?)
     * The database auto-generates the ID, and save() returns the User with the ID populated.
     *
     * @param user the User object from the request body (without an ID)
     * @return the saved User with its auto-generated ID
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user's name and email.
     *
     * The logic:
     *   1. findById(id) looks up the existing user -> returns Optional<User>
     *   2. .map() runs only if the user exists:
     *      - Copies the new name and email onto the existing entity
     *      - Calls save() which runs: UPDATE users SET name=?, email=? WHERE id=?
     *      - save() detects this is an update (not insert) because the entity already has an ID
     *   3. If findById returns empty (user not found), .map() is skipped and Optional.empty() is returned
     *
     * @param id          the ID of the user to update
     * @param updatedUser the new data from the request body
     * @return Optional containing the updated User, or empty if user was not found
     */
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            return userRepository.save(user);
        });
    }

    /**
     * Deletes a user by their ID.
     *
     * First checks if the user exists with existsById() to avoid throwing an exception
     * from deleteById() on a non-existent ID. Returns a boolean so the controller
     * can respond with the appropriate HTTP status (204 No Content vs 404 Not Found).
     *
     * @param id the ID of the user to delete
     * @return true if the user existed and was deleted, false if no user with that ID was found
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
