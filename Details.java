package exam_project;

/**
 * Details class (child of Marks)
 * Represents ONE complete student record: personal details (from Student) plus marks (from Marks).
 * The GUI stores objects of this class in an array: Details[] students.
 * Inheritance chain: Student -> Marks -> Details
 */
public class Details extends Marks {

    // Parameterized constructor
    public Details(String studentName, String studentNumber, int oopMarks, int mathMarks, int dictMarks,
                   int architectureMarks, int literacyMarks, int communicationSkillsMarks) {
        super(studentName, studentNumber, oopMarks, mathMarks, dictMarks, architectureMarks, literacyMarks,
                communicationSkillsMarks);
    }

    // Returns a full multi-line report of this student (used when displaying a student's details)
    public String getReport() {
        return "Student Name: " + getStudentName() + "\n"
                + "Student Number: " + getStudentNumber() + "\n"
                + "OOP: " + getOopMarks() + " (" + gradeCalc(getOopMarks()) + ")\n"
                + "Mathematics: " + getMathMarks() + " (" + gradeCalc(getMathMarks()) + ")\n"
                + "Digital Innovation: " + getDictMarks() + " (" + gradeCalc(getDictMarks()) + ")\n"
                + "Architecture: " + getArchitectureMarks() + " (" + gradeCalc(getArchitectureMarks()) + ")\n"
                + "Literacy: " + getLiteracyMarks() + " (" + gradeCalc(getLiteracyMarks()) + ")\n"
                + "Communication Skills: " + getCommunicationSkillsMarks() + " ("
                + gradeCalc(getCommunicationSkillsMarks()) + ")\n"
                + "Average: " + String.format("%.1f", getAverageMarks()) + " (" + getAverageGrade() + ")";
    }

    // toString() gives a short one-line description of the student
    @Override
    public String toString() {
        return getStudentNumber() + " - " + getStudentName() + " - Average: "
                + String.format("%.1f", getAverageMarks()) + " (" + getAverageGrade() + ")";
    }
}
