package com.anurag.sms.service.impl;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.anurag.sms.service.UserSessionService;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    // Filled by Spring Security at each sign-in (SecurityConfig)
    private final SessionRegistry sessionRegistry;

    public UserSessionServiceImpl(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public int endSessions(String username) {

        int ended = 0;

        for (Object principal : sessionRegistry.getAllPrincipals()) {

            if (!(principal instanceof UserDetails user) || !user.getUsername().equals(username)) {
                continue;
            }

            // Marked expired here; Spring Security's ConcurrentSessionFilter
            // signs the browser out on its next request
            for (SessionInformation session : sessionRegistry.getAllSessions(principal, false)) {
                session.expireNow();
                ended++;
            }
        }

        return ended;
    }
}
