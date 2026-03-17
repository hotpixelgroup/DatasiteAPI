package com.example.userapi.repository;

import com.example.userapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The repository layer handles all database operations for the User entity.
 *
 * By extending JpaRepository<User, Long>, Spring Data JPA automatically provides
 * full CRUD operations without us writing any implementation code. The two type
 * parameters are:
 *   - User: the entity type this repository manages
 *   - Long: the type of the entity's primary key (@Id field)
 *
 * Methods we inherit for free (no code needed):
 *   - findAll()        -> SELECT * FROM users                (returns List<User>)
 *   - findById(id)     -> SELECT * FROM users WHERE id = ?   (returns Optional<User>)
 *   - save(user)       -> INSERT or UPDATE (if ID already exists)
 *   - deleteById(id)   -> DELETE FROM users WHERE id = ?
 *   - existsById(id)   -> SELECT COUNT(*) > 0 WHERE id = ?  (returns boolean)
 *   - count()          -> SELECT COUNT(*) FROM users
 *   ...and many more.
 *
 * Spring Data JPA creates a concrete implementation of this interface at runtime
 * using a proxy. We never write SQL - the framework generates it from the method names.
 *
 * This is also a Spring-managed bean (JpaRepository is detected automatically by
 * @SpringBootApplication's component scan), so it can be injected into other classes
 * like UserService via constructor injection.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // No additional methods needed - JpaRepository provides all basic CRUD operations.
    // You could add custom queries here, e.g.:
    //   Optional<User> findByEmail(String email);
    // Spring would auto-generate the SQL from the method name.
}
