package com.example.userapi.controller;

import com.example.userapi.model.User;
import com.example.userapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller layer handles incoming HTTP requests and returns HTTP responses.
 * This is the entry point for all client interactions with the API.
 *
 * @RestController - Combines two annotations:
 *   1. @Controller - Marks this as a Spring MVC controller (handles web requests)
 *   2. @ResponseBody - Tells Spring to serialize return values directly to the HTTP
 *      response body as JSON (using Jackson), rather than resolving a view/template.
 *      So when a method returns a User object, Jackson converts it to:
 *      {"id": 1, "name": "Alice", "email": "alice@example.com"}
 *
 * @RequestMapping("/api/users") - Sets the base URL path for ALL endpoints in this controller.
 *      Every method's path is relative to this, so @GetMapping("/{id}") becomes GET /api/users/{id}.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * The service is injected via constructor injection, same pattern as in UserService.
     * The controller delegates all business logic to the service layer -
     * it only handles HTTP concerns (request parsing, response status codes).
     */
    private final UserService userService;

    /**
     * Spring injects the UserService bean automatically.
     * The flow of dependency injection is:
     *   Spring creates UserRepository -> injects it into UserService -> injects UserService into UserController
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users
     *
     * Retrieves all users. Returns a JSON array.
     *
     * @GetMapping - Maps HTTP GET requests to this method. Since no path is specified,
     *               it uses the class-level path: /api/users
     *
     * Return type is List<User> - Spring/Jackson automatically serializes this to a JSON array:
     *   [{"id":1,"name":"Alice","email":"alice@example.com"}, {"id":2,...}]
     *
     * Always returns HTTP 200 OK (Spring's default for non-ResponseEntity returns).
     * Returns an empty array [] if no users exist.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * GET /api/users/{id}
     *
     * Retrieves a single user by ID. Returns 200 with the user, or 404 if not found.
     *
     * @GetMapping("/{id}") - The {id} is a path variable, e.g. GET /api/users/3 -> id = 3
     *
     * @PathVariable - Extracts the {id} value from the URL and binds it to the 'id' parameter.
     *                 Spring automatically converts the string "3" to Long 3.
     *
     * ResponseEntity<User> - A wrapper that lets us control the HTTP status code and body.
     *   - .map(ResponseEntity::ok) - If user is found: wraps it in 200 OK response
     *     (ResponseEntity::ok is a method reference equivalent to user -> ResponseEntity.ok(user))
     *   - .orElse(ResponseEntity.notFound().build()) - If user not found: returns 404 Not Found
     *     with an empty body
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/users
     *
     * Creates a new user. Expects a JSON body like: {"name": "Alice", "email": "alice@example.com"}
     * Returns 201 Created with the saved user (including the auto-generated ID).
     *
     * @PostMapping - Maps HTTP POST requests to /api/users
     *
     * @RequestBody - Tells Spring to deserialize the JSON request body into a User object.
     *                Jackson reads the JSON fields and calls the matching setters:
     *                {"name": "Alice"} -> user.setName("Alice")
     *
     * ResponseEntity.status(HttpStatus.CREATED) - Sets the HTTP status to 201 Created
     *   (more appropriate than 200 OK for resource creation, follows REST conventions).
     * .body(created) - Sets the response body to the saved user JSON.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/users/{id}
     *
     * Updates an existing user. Expects a full JSON body with the new values.
     * Returns 200 OK with the updated user, or 404 if the user doesn't exist.
     *
     * @PutMapping("/{id}") - Maps HTTP PUT requests to /api/users/{id}
     *
     * Takes two parameters:
     *   - @PathVariable Long id: the user ID from the URL
     *   - @RequestBody User user: the new data from the JSON body
     *
     * Uses the same Optional pattern as getUserById to return 200 or 404.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/users/{id}
     *
     * Deletes a user by ID. Returns 204 No Content on success, or 404 if not found.
     *
     * @DeleteMapping("/{id}") - Maps HTTP DELETE requests to /api/users/{id}
     *
     * ResponseEntity<Void> - The response has no body (Void), since we're deleting.
     *   - 204 No Content (noContent()) - Standard REST response for successful deletion.
     *     Means "the request succeeded and there is nothing to return".
     *   - 404 Not Found - Returned when no user with the given ID exists.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
