package com.anurag.sms.utility;

/**
 * Length limits for text typed into the forms and the CSV import (#39).
 *
 * Every text column is VARCHAR(255) (V1__baseline.sql). A longer value used
 * to fail in the database instead of on the form: the user lost what they
 * typed and saw the 409 page, which says nothing was changed. The messages
 * name the field, so a CSV row error still says which column is too long.
 */
public final class FieldLimits {

    // The length of every text column
    public static final int TEXT = 255;

    // Used as "Address" + TEXT_TOO_LONG. Constant expressions, so the
    // validation annotations can use them.
    public static final String TEXT_TOO_LONG = " must be " + TEXT + " characters or fewer";

    // Lower than TEXT: usernames are also written to activity_log.username,
    // which holds 100 characters (#40)
    public static final int USERNAME = 50;
    public static final String USERNAME_TOO_LONG = "Username must be " + USERNAME + " characters or fewer";

    private FieldLimits() {
    }
}
