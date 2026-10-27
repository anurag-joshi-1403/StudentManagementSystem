package com.anurag.sms.security;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.anurag.sms.repository.UserRepository;

/**
 * Who is signed in, as records in this app (G11): the student or teacher
 * the account is linked to (G10). Pages that show "my" records read the id
 * from here, never from the URL, so changing a number in the address bar
 * cannot reach someone else's records.
 */
@Component
public class CurrentUser {

    private final UserRepository userRepository;

    public CurrentUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // The signed-in username, or empty when nobody is signed in
    public Optional<String> username() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of(auth.getName());
    }

    // The student the signed-in account is linked to, if any
    public Optional<Long> studentId() {
        return username().flatMap(userRepository::findStudentIdByUsername);
    }

    // The teacher the signed-in account is linked to, if any
    public Optional<Long> teacherId() {
        return username().flatMap(userRepository::findTeacherIdByUsername);
    }
}
