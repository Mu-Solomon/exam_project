# Student Marks Management System

CSC 1102: Structured and Object-Oriented Programming, Practical Exam Project (Group 24).

A Java Swing desktop application for recording students' marks in six subjects, calculating averages and grades automatically, and searching, updating and deleting student records.

## Group 24

Group leader: Lubega Michael

| Name | Student No. | Registration No. |
| --- | --- | --- |
| Sebalemba Dennis | 2600726046 | 26/U/26046/PS |
| Lubega Michael | 2600703548 | 26/U/03548/PS |
| Muyonjo Solomon | 2600703581 | 26/U/03581/PS |
| Rob Wilbert | 2600726537 | 26/X/26537/PS |
| Ssenfuma Emmanuel | 2600703644 | 26/U/03644/PS |

GitHub link: https://github.com/c-eo/oop-practical

## Features

- Add a new student with marks (0 to 100) in six subjects
- Automatic average and grade calculation
- Live search of the student table
- Update a student by selecting a row and clicking **Update Student**
- Input validation for student numbers, names and marks

## Project structure

| File | Purpose |
| --- | --- |
| `Student.java` | Base class: student name and number |
| `Marks.java` | Extends `Student`: six subject marks and the average |
| `Details.java` | Extends `Marks`: grades per subject and overall |
| `Validator.java` | Input validation helpers |
| `StudentGUI.java` | Main class: the Swing user interface |

Inheritance chain: `Student` -> `Marks` -> `Details`.

## Subjects

Object-Oriented Programming, Mathematics, Digital Innovation, Computer Organisation and Architecture, Computer Literacy, Communication Skills.

## Build and run

Requires a JDK (Java 8 or later). The classes are in the `exam_project` package, so compile and run from the directory that contains the `exam_project` folder:

```bash
javac exam_project/*.java
java exam_project.StudentGUI
```

Compiled `.class` files are ignored by git.
