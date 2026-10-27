package com.anurag.sms.utility;

/**
 * The one password rule, used by registration, the change-password form
 * and AdminSeeder (#36). Registration used to accept 6 characters while the
 * change form asked for 8.
 *
 * The maximum is BCrypt's: it reads at most 72 bytes, and Spring Security's
 * BCryptPasswordEncoder rejects anything longer, so a longer password would
 * fail when it is saved rather than on the form.
 */
public final class PasswordPolicy {

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 72;

    // A constant expression, so the validation annotations can use it
    public static final String LENGTH_MESSAGE =
            "Password must be " + MIN_LENGTH + " to " + MAX_LENGTH + " characters";

    private PasswordPolicy() {
    }

    public static boolean isAcceptable(String password) {
        return password != null
                && password.length() >= MIN_LENGTH
                && password.length() <= MAX_LENGTH;
    }
}
