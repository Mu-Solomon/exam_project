package exam_project;

/**
 * Validator class
 * ---------------
 * Holds all the validation rules for the application in ONE place.
 * Every check method returns an empty String ("") when the input is valid,
 * or a clear error message when the input is NOT valid.
 *
 * Rules:
 *  - Student name  : 2 to 50 characters, starts with a letter, and contains only
 *                    letters, spaces, hyphens (-) and apostrophes (')
 *  - Student number: exactly 10 characters, digits only
 *  - Marks         : a whole number from 0 to 100 (digits only, so no decimals,
 *                    no negative numbers, no letters and no blank input)
 */
public class Validator {

    public static final int MIN_MARK = 0;
    public static final int MAX_MARK = 100;
    public static final int STUDENT_NUMBER_LENGTH = 10;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;

    // Private constructor: this class only has static methods, so no objects are needed.
    private Validator() {
    }

    // Checks the student name. Returns "" if valid, otherwise an error message.
    public static String checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Student name cannot be empty.";
        }
     
        String trimmed = name.trim();

        if (trimmed.length() < MIN_NAME_LENGTH || trimmed.length() > MAX_NAME_LENGTH) {
            return "Student name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters.";
        }

        if (!Character.isLetter(trimmed.charAt(0))) {
            return "Student name must start with a letter.";
        }

        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (!Character.isLetter(c) && c != ' ' && c != '-' && c != '\'') {
                return "Student name can only contain letters, spaces, hyphens and apostrophes (no digits or symbols).";
            }
        }

        return "";
    }

    // Checks the student number. Returns "" if valid, otherwise an error message.
    public static String checkStudentNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            return "Student number cannot be empty.";
        }

        String trimmed = number.trim();

        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c < '0' || c > '9') {
                return "Student number must contain digits only (0-9).";
            }
        }

        if (trimmed.length() != STUDENT_NUMBER_LENGTH) {
            return "Student number must be exactly " + STUDENT_NUMBER_LENGTH + " digits (you entered " + trimmed.length() + ").";
        }

        return "";
    }

    // Checks one mark typed in a text field. 'subject' is only used to make the message clear.
    public static String checkMark(String subject, String text) {
        if (text == null || text.trim().isEmpty()) {
            return subject + " marks cannot be empty.";
        }

        String trimmed = text.trim();

        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c < '0' || c > '9') {
                return subject + " marks must be a whole number (digits only, no letters, decimals or negative sign).";
            }
        }

        // More than 3 digits can never be inside 0-100 (this also avoids a number that is too big to convert).
        if (trimmed.length() > 3 || Integer.parseInt(trimmed) > MAX_MARK) {
            return subject + " marks must be between " + MIN_MARK + " and " + MAX_MARK + ".";
        }

        return "";
    }

    // Checks a mark that is already an int (used by the Marks class).
    public static boolean isValidMark(int mark) {
        return mark >= MIN_MARK && mark <= MAX_MARK;
    }
}
