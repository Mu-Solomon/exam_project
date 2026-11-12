package exam_project;

/**
 * Student class (parent / super class)
 * Stores the personal details of a student: name and student number.
 */
public class Student {

    // private: can only be accessed through the getters and setters (encapsulation)
    private String studentName;
    private String studentNumber;

    // Parameterized constructor
    public Student(String studentName, String studentNumber) {
        this.studentName = studentName.trim();
        this.studentNumber = studentNumber.trim();
    }

    // Getter methods
    public String getStudentName() {
        if (studentName == null || studentName.isEmpty()) {
            return "Unknown";
        }
        return studentName;
    }

    public String getStudentNumber() {
        if (studentNumber == null || studentNumber.length() != Validator.STUDENT_NUMBER_LENGTH) {
            return "Unknown";
        }
        return studentNumber;
    }

    // Setter method for the name. It only changes the name if the new name is valid.
    // Returns true if the name was changed, false if it was rejected.
    // (There is no setter for the student number because a student number never changes.)
    public boolean setStudentName(String newName) {
        if (Validator.checkName(newName).isEmpty()) {
            this.studentName = newName.trim();
            return true;
        }
        return false;
    }
}
