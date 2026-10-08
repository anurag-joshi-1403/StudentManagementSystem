package com.anurag.sms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.anurag.sms.entity.User;
import com.anurag.sms.repository.UserRepository;

/**
 * Creates the first ADMIN account on a fresh database (#3).
 *
 * Self-registration only ever creates ROLE_STUDENT accounts, so without
 * this a new clone would have nobody able to manage records. It runs only
 * when ADMIN_USERNAME and ADMIN_PASSWORD are set and no admin exists yet,
 * so it never creates a second admin and does nothing on existing setups.
 */
@Component
public class AdminSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String username;
    private final String password;
    private final String email;

    public AdminSeeder(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${ADMIN_USERNAME:}") String username,
                       @Value("${ADMIN_PASSWORD:}") String password,
                       @Value("${ADMIN_EMAIL:}") String email) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (userRepository.existsByRole(ADMIN_ROLE)) {
            return;
        }

        if (username.isBlank() || password.isBlank()) {
            log.warn("No admin account exists. Set ADMIN_USERNAME and ADMIN_PASSWORD "
                    + "to create one on the next start.");
            return;
        }

        // Promoting an existing account from an environment variable would
        // let whoever sets it take over that account, so only create.
        if (userRepository.existsByUsername(username)) {
            log.warn("No admin created: username '{}' is already taken. Choose another "
                    + "ADMIN_USERNAME, or promote that account in the database.", username);
            return;
        }

        User admin = new User();
        admin.setFullName("Administrator");
        admin.setUsername(username);
        admin.setEmail(email.isBlank() ? username + "@localhost" : email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(ADMIN_ROLE);
        admin.setEnabled(true);

        userRepository.save(admin);

        log.info("Created admin account '{}'.", username);
    }
}
