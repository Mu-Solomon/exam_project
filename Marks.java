package exam_project;

/**
 * Marks class (child of Student, parent of Details)
 * Stores the marks of a student in the six subjects, and calculates the average and grades.
 * Inheritance chain: Student -> Marks -> Details
 */
public class Marks extends Student {

    private int oopMarks;
    private int mathMarks;
    private int dictMarks;               // Digital Innovation
    private int architectureMarks;       // Computer Organisation and Architecture
    private int literacyMarks;           // Computer Literacy
    private int communicationSkillsMarks;

    private double averageMarks;

    // Parameterized constructor for the Marks class
    public Marks(String studentName, String studentNumber, int oopMarks, int mathMarks, int dictMarks,
                 int architectureMarks, int literacyMarks, int communicationSkillsMarks) {
        super(studentName, studentNumber);   // calls the Student constructor
        this.oopMarks = oopMarks;
        this.mathMarks = mathMarks;
        this.dictMarks = dictMarks;
        this.architectureMarks = architectureMarks;
        this.literacyMarks = literacyMarks;
        this.communicationSkillsMarks = communicationSkillsMarks;
        calculateAverage();
    }

    // Getter methods (each returns the plain mark)
    public int getOopMarks() {
        return oopMarks;
    }

    public int getMathMarks() {
        return mathMarks;
    }

    public int getDictMarks() {
        return dictMarks;
    }

    public int getArchitectureMarks() {
        return architectureMarks;
    }

    public int getLiteracyMarks() {
        return literacyMarks;
    }

    public int getCommunicationSkillsMarks() {
        return communicationSkillsMarks;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    // Returns the grade (A+, A, B+ ...) of the average mark
    public String getAverageGrade() {
        return gradeCalc(averageMarks);
    }

    // Setter method: changes all six marks together, but only if ALL of them are valid.
    // Returns true if the marks were changed, false if they were rejected.
    public boolean setAllMarks(int oop, int math, int dict, int architecture, int literacy, int communicationSkills) {
        if (!Validator.isValidMark(oop) || !Validator.isValidMark(math) || !Validator.isValidMark(dict)
                || !Validator.isValidMark(architecture) || !Validator.isValidMark(literacy)
                || !Validator.isValidMark(communicationSkills)) {
            return false;
        }
        this.oopMarks = oop;
        this.mathMarks = math;
        this.dictMarks = dict;
        this.architectureMarks = architecture;
        this.literacyMarks = literacy;
        this.communicationSkillsMarks = communicationSkills;
        calculateAverage();   // the average must always match the marks
        return true;
    }

    // Calculates the average of the six marks (kept as a decimal so it is accurate)
    private void calculateAverage() {
        int total = oopMarks + mathMarks + dictMarks + architectureMarks + literacyMarks + communicationSkillsMarks;
        averageMarks = total / 6.0;
    }

    // Method to calculate the grade based on a mark
    public static String gradeCalc(double mark) {
        if (mark < 0 || mark > 100) {
            return "Invalid";
        } else if (mark >= 90) {
            return "A+";
        } else if (mark >= 80) {
            return "A";
        } else if (mark >= 75) {
            return "B+";
        } else if (mark >= 70) {
            return "B";
        } else if (mark >= 65) {
            return "C+";
        } else if (mark >= 60) {
            return "C";
        } else if (mark >= 55) {
            return "D+";
        } else if (mark >= 50) {
            return "D";
        } else {
            return "F";
        }
    }
}
