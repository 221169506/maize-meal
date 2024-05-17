package za.ac.tut.maizemeal_shop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import za.ac.tut.maizemeal_shop.exception.ResourceNotFoundException;
import za.ac.tut.maizemeal_shop.model.User;
import za.ac.tut.maizemeal_shop.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Login method
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            User loginRequest = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

            System.out.println(user.getEmail() + " " + user.getPassword());

            // Verify password
            if (!loginRequest.getPassword().equals(user.getPassword())) {
                return ResponseEntity.badRequest().body("Incorrect password");
            }

            // Store the username or email in the session
            session.setAttribute("loggedInUser", loginRequest.getUsername());
            System.out.println(loginRequest.getUsername());

            return ResponseEntity.ok("index.html");
            // return ResponseEntity.ok("dashboard.html"); // Assuming the dashboard page is
            // named "dashboard.html"

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid login request");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        // Check if the provided email or username already exists in the database
        Optional<User> existingUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("User with email or username already exists: " + 
                    user.getEmail() + " or " + user.getUsername());
        }

        // You can add more validations here if needed, such as checking for password
        // strength, etc.
        user.setRole("Customer");
        // If all validations pass, save the user
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok("User with ID " + savedUser.getUserId() + " added successfully");
    }

    @PostMapping
    public ResponseEntity<String> signIn(@RequestBody User loginRequest) {
        // Check if the provided username or email exists in the database
        User user = userRepository.findByEmailOrUsername(loginRequest.getEmail(), loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email or username", loginRequest.getEmail()));

        // Verify password
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }

        // Store the username or email in the session
        session.setAttribute("loggedInUser", user.getUsername());

        return ResponseEntity.ok("User logged in successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setPassword(userDetails.getPassword());
        user.setRole(userDetails.getRole());

        userRepository.save(user);
        return ResponseEntity.ok("User with ID " + id + " updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);
        return ResponseEntity.ok("User with ID " + id + " deleted successfully");
    }

    // Get total count of products
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUserCount() {
        try {
            long count = userRepository.count();
            System.out.println(count);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}