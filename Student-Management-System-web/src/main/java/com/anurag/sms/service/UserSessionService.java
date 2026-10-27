package com.anurag.sms.service;

/**
 * Signs an account out of every browser it is signed in on (#37). Used when
 * an admin disables an account or changes its role, so the change applies
 * at once instead of from the next sign-in.
 */
public interface UserSessionService {

    // Ends every session of this username and returns how many there were.
    // The person's next request goes to /login?expired=true.
    int endSessions(String username);
}
