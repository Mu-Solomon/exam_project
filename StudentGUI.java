/*
 * ============================================================
 *  CSC 1102: Structured and Object-Oriented Programming
 *  Practical Exam Project - Student Marks Management System
 * ============================================================
 *  GROUP DETAILS (fill in before submitting)
 *  Group name   : Group 24
 *  Group leader : Lubega Michael
 *  Members      :
 *    Name               Student No.   Registration No.
 *    Sebalemba Dennis   2600726046    26/U/26046/PS
 *    Lubega Michael     2600703548    26/U/03548/PS
 *    Muyonjo Solomon    2600703581    26/U/03581/PS
 *    Rob Wilbert        2600726537    26/X/26537/PS
 *    Ssenfuma Emmanuel  2600703644    26/U/03644/PS
 *  Session      : Day / Afternoon
 *  GitHub link  : https://github.com/c-eo/oop-practical
 * ============================================================
 *  MAIN FILE: run this class (StudentGUI) to start the application.
 */
package exam_project;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * StudentGUI class
 * Modern Graphical User Interface (Java Swing) for managing student records.
 * Features live search filtering, individual subject grade columns, and a modal update form.
 */
public class StudentGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static final int MAX_STUDENTS = 50;

    private static final String[] SUBJECTS = {
        "OOP", "Mathematics", "Digital Innovation", "Architecture", "Literacy", "Communication Skills"
    };

    // Color Palette
    private static final Color PRIMARY_DARK = new Color(24, 39, 64);      // Deep Navy Header
    private static final Color ACCENT_BLUE = new Color(37, 99, 235);      // Modern Royal Blue
    private static final Color SUCCESS_GREEN = new Color(22, 163, 74);    // Add Button
    private static final Color WARNING_AMBER = new Color(217, 119, 6);    // Update Button
    private static final Color DANGER_RED = new Color(220, 38, 38);       // Delete / Exit
    private static final Color BG_LIGHT = new Color(244, 246, 250);       // Window Background
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color ALT_ROW_COLOR = new Color(248, 250, 252);
    private static final Color BORDER_GRAY = new Color(218, 224, 232);

    // Data storage
    private Details[] students = new Details[MAX_STUDENTS];
    private int studentCount = 0;

    // GUI Components
    private JTextField liveSearchField;
    private JButton clearSearchButton;

    private JTextField nameField;
    private JTextField numberField;
    private JTextField[] markFields = new JTextField[SUBJECTS.length];

    private JButton addButton;
    private JButton reportButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton sortButton;
    private JButton clearButton;
    private JButton exitButton;

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;

    public StudentGUI() {
        setTitle("Student Marks Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1420, 660);
        setMinimumSize(new Dimension(1180, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_LIGHT);
        setLayout(new BorderLayout(12, 12));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout(12, 12));
        centerContainer.setOpaque(false);
        centerContainer.setBorder(new EmptyBorder(0, 14, 0, 14));
        centerContainer.add(createFormPanel(), BorderLayout.WEST);
        centerContainer.add(createTablePanel(), BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        add(createBottomPanel(), BorderLayout.SOUTH);

        refreshTable();
    }

    // ---------- Top Header & Live Search Bar ----------

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(15, 10));
        header.setBackground(PRIMARY_DARK);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel titleBox = new JPanel(new GridBagLayout());
        titleBox.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Student Marks Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleBox.add(titleLabel, gbc);

        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("CSC 1102 Practical Exam Project  |  Academic Records & Automated Grading");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(185, 200, 224));
        titleBox.add(subtitleLabel, gbc);

        header.add(titleBox, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        searchPanel.setOpaque(false);

        JLabel searchPrompt = new JLabel("Live Search:");
        searchPrompt.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchPrompt.setForeground(Color.WHITE);

        liveSearchField = new JTextField(22);
        liveSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        liveSearchField.setToolTipText("Type a student name or 10-digit student number to filter instantly");
        liveSearchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 125, 165), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));

        liveSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });

        clearSearchButton = createStyledButton("Clear", new Color(71, 85, 105), Color.WHITE);
        clearSearchButton.setPreferredSize(new Dimension(72, 32));
        clearSearchButton.addActionListener(e -> {
            liveSearchField.setText("");
            liveSearchField.requestFocus();
        });

        searchPanel.add(searchPrompt);
        searchPanel.add(liveSearchField);
        searchPanel.add(clearSearchButton);

        header.add(searchPanel, BorderLayout.EAST);
        return header;
    }

    // ---------- Left Side: Student Input Form ----------

    private JPanel createFormPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_GRAY, 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));
        card.setPreferredSize(new Dimension(340, 0));

        JLabel formHeader = new JLabel("Add New Student");
        formHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        formHeader.setForeground(PRIMARY_DARK);
        formHeader.setBorder(new EmptyBorder(0, 0, 12, 0));
        card.add(formHeader, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 4, 5, 4);

        nameField = createStyledTextField("Letters, spaces, hyphens and apostrophes (2-50 chars)");
        numberField = createStyledTextField("Exactly 10 digits (e.g., 2600712345)");

        addFormRow(fieldsPanel, gbc, 0, "Student Name:", nameField);
        addFormRow(fieldsPanel, gbc, 1, "Student Number:", numberField);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 4, 6, 4);
        JLabel marksSectionLabel = new JLabel("Subject Marks (0 - 100)");
        marksSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        marksSectionLabel.setForeground(ACCENT_BLUE);
        fieldsPanel.add(marksSectionLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 4, 5, 4);

        for (int i = 0; i < SUBJECTS.length; i++) {
            markFields[i] = createStyledTextField("Whole number from 0 to 100");
            addFormRow(fieldsPanel, gbc, i + 3, SUBJECTS[i] + ":", markFields[i]);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(fieldsPanel, BorderLayout.NORTH);
        card.add(wrapper, BorderLayout.CENTER);

        JLabel tipLabel = new JLabel("<html><i>Tip: Select a student in the table and click Update Student to open the edit modal.</i></html>");
        tipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tipLabel.setForeground(new Color(100, 116, 139));
        tipLabel.setBorder(new EmptyBorder(10, 4, 0, 4));
        card.add(tipLabel, BorderLayout.SOUTH);

        return card;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.42;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(51, 65, 85));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.58;
        panel.add(field, gbc);
    }

    private JTextField createStyledTextField(String tooltip) {
        JTextField tf = new JTextField(14);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setToolTipText(tooltip);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    // ---------- Centre: Table with Individual Subject Grade Columns ----------

    private JPanel createTablePanel() {
        String[] columns = {
            "Rank", "Student No.", "Name",
            "OOP", "Gr.",
            "Math", "Gr.",
            "Digital Inn.", "Gr.",
            "Arch.", "Gr.",
            "Literacy", "Gr.",
            "Comm.", "Gr.",
            "Average", "Overall"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(230, 235, 242));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(235, 240, 248));
        header.setForeground(PRIMARY_DARK);
        header.setPreferredSize(new Dimension(0, 34));
        header.setReorderingAllowed(false);

        int[] widths = {
            42, 95, 145,       
            45, 55,            
            45, 58,            
            68, 55,            
            48, 58,            
            55, 52,            
            50, 62,            
            62, 58             
        };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        StudentTableRenderer renderer = new StudentTableRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_GRAY, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel tableHeaderLabel = new JLabel("Student Performance & Subject Grades");
        tableHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeaderLabel.setForeground(PRIMARY_DARK);
        tableHeaderLabel.setBorder(new EmptyBorder(0, 2, 10, 0));
        card.add(tableHeaderLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(CARD_WHITE);
        scrollPane.setBorder(new LineBorder(BORDER_GRAY, 1));
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    // ---------- Bottom Panel: Action Buttons & Status Bar ----------

    private JPanel createBottomPanel() {
        addButton = createStyledButton("Add Student", SUCCESS_GREEN, Color.WHITE);
        reportButton = createStyledButton("View Report", ACCENT_BLUE, Color.WHITE);
        updateButton = createStyledButton("Update Student", WARNING_AMBER, Color.WHITE);
        deleteButton = createStyledButton("Delete Selected", DANGER_RED, Color.WHITE);
        sortButton = createStyledButton("Sort by Average", new Color(15, 118, 110), Color.WHITE);
        clearButton = createStyledButton("Clear Form", new Color(100, 116, 139), Color.WHITE);
        exitButton = createStyledButton("Exit", new Color(51, 65, 85), Color.WHITE);

        JButton[] buttons = {addButton, reportButton, updateButton, deleteButton, sortButton, clearButton, exitButton};

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        buttonPanel.setOpaque(false);
        for (JButton btn : buttons) {
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(232, 237, 245));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_GRAY),
                new EmptyBorder(7, 18, 7, 18)
        ));

        statusLabel = new JLabel("Ready.");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(PRIMARY_DARK);
        statusBar.add(statusLabel, BorderLayout.WEST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(buttonPanel, BorderLayout.CENTER);
        wrapper.add(statusBar, BorderLayout.SOUTH);
        return wrapper;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        return button;
    }

    // ---------- Handling Button Clicks ----------

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == addButton) {
            addStudent();
        } else if (source == reportButton) {
            showStudentReport();
        } else if (source == updateButton) {
            openUpdateModal();
        } else if (source == deleteButton) {
            deleteStudent();
        } else if (source == sortButton) {
            sortStudents();
        } else if (source == clearButton) {
            clearForm();
            table.clearSelection();
            setStatus("Form cleared.");
        } else if (source == exitButton) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to exit the application?",
                    "Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    // ---------- Core Features ----------

    private void addStudent() {
        if (studentCount >= MAX_STUDENTS) {
            showError("The list is full. The maximum number of students is " + MAX_STUDENTS + ".");
            return;
        }

        if (!validateForm()) {
            return;
        }

        String number = numberField.getText().trim();
        if (findStudent(number) != -1) {
            showError("A student with the number " + number + " already exists.");
            numberField.requestFocus();
            return;
        }

        int[] marks = readMarks();
        students[studentCount] = new Details(nameField.getText().trim(), number,
                marks[0], marks[1], marks[2], marks[3], marks[4], marks[5]);
        studentCount++;

        refreshTable();
        clearForm();
        setStatus("Student added successfully.");
    }

    private void showStudentReport() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a student record in the table first, then click View Report.");
            return;
        }

        String studentNumber = table.getValueAt(selectedRow, 1).toString();
        int index = findStudent(studentNumber);
        if (index == -1) {
            showError("No student was found with that student number.");
            return;
        }

        Details found = students[index];
        JOptionPane.showMessageDialog(this, found.getReport(), "Student Report Card", JOptionPane.INFORMATION_MESSAGE);
        setStatus("Viewing report for " + found.getStudentName() + " (" + found.getStudentNumber() + ").");
    }

    // Opens a modal dialog form pre-filled with the selected student's data for editing
    private void openUpdateModal() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a student record in the table first, then click Update Student.");
            return;
        }

        String studentNumber = table.getValueAt(selectedRow, 1).toString();
        int index = findStudent(studentNumber);
        if (index == -1) {
            showError("No student was found with that student number.");
            return;
        }

        Details student = students[index];

        // Create Modal Dialog
        JDialog updateDialog = new JDialog(this, "Update Student Record", true);
        updateDialog.setSize(440, 520);
        updateDialog.setLocationRelativeTo(this);
        updateDialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        formPanel.setBackground(CARD_WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        // Student Number (Read-Only)
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        JLabel numLbl = new JLabel("Student Number:");
        numLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(numLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        JTextField modalNumField = new JTextField(student.getStudentNumber(), 14);
        modalNumField.setEditable(false);
        modalNumField.setBackground(new Error() instanceof Object ? new Color(241, 245, 249) : Color.LIGHT_GRAY);
        modalNumField.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(modalNumField, gbc);

        // Student Name (Editable)
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel nameLbl = new JLabel("Student Name:");
        nameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(nameLbl, gbc);

        gbc.gridx = 1;
        JTextField modalNameField = new JTextField(student.getStudentName(), 14);
        modalNameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(modalNameField, gbc);

        // Subject Mark Fields (Pre-filled)
        JTextField[] modalMarkFields = new JTextField[SUBJECTS.length];
        int[] currentMarks = {
            student.getOopMarks(),
            student.getMathMarks(),
            student.getDictMarks(),
            student.getArchitectureMarks(),
            student.getLiteracyMarks(),
            student.getCommunicationSkillsMarks()
        };

        for (int i = 0; i < SUBJECTS.length; i++) {
            gbc.gridy = i + 2;
            gbc.gridx = 0;
            JLabel subLbl = new JLabel(SUBJECTS[i] + " (0-100):");
            subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            formPanel.add(subLbl, gbc);

            gbc.gridx = 1;
            modalMarkFields[i] = new JTextField(String.valueOf(currentMarks[i]), 14);
            modalMarkFields[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            formPanel.add(modalMarkFields[i], gbc);
        }

        updateDialog.add(formPanel, BorderLayout.CENTER);

        // Dialog Bottom Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(BG_LIGHT);
        btnPanel.setBorder(new EmptyBorder(0, 15, 12, 15));

        JButton saveButton = createStyledButton("Save Changes", SUCCESS_GREEN, Color.WHITE);
        JButton cancelButton = createStyledButton("Cancel", new Color(100, 116, 139), Color.WHITE);

        saveButton.addActionListener(e -> {
            // Validate name
            String nameError = Validator.checkName(modalNameField.getText());
            if (!nameError.isEmpty()) {
                JOptionPane.showMessageDialog(updateDialog, nameError, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                modalNameField.requestFocus();
                return;
            }

            // Validate marks
            int[] newMarks = new int[SUBJECTS.length];
            for (int i = 0; i < SUBJECTS.length; i++) {
                String markError = Validator.checkMark(SUBJECTS[i], modalMarkFields[i].getText());
                if (!markError.isEmpty()) {
                    JOptionPane.showMessageDialog(updateDialog, markError, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    modalMarkFields[i].requestFocus();
                    return;
                }
                newMarks[i] = Integer.parseInt(modalMarkFields[i].getText().trim());
            }

            // Apply updates
            boolean nameChanged = student.setStudentName(modalNameField.getText());
            boolean marksChanged = student.setAllMarks(newMarks[0], newMarks[1], newMarks[2], newMarks[3], newMarks[4], newMarks[5]);

            if (nameChanged && marksChanged) {
                refreshTable();
                setStatus("Student " + student.getStudentNumber() + " updated successfully.");
                updateDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(updateDialog, "Update rejected due to invalid data constraints.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> updateDialog.dispose());

        btnPanel.add(saveButton);
        btnPanel.add(cancelButton);
        updateDialog.add(btnPanel, BorderLayout.SOUTH);

        updateDialog.setVisible(true);
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Please click a student in the table first, then click Delete Selected.");
            return;
        }

        String selectedStudentNumber = table.getValueAt(row, 1).toString();
        int actualIndex = findStudent(selectedStudentNumber);
        if (actualIndex == -1) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete " + students[actualIndex].getStudentName() + " (" + selectedStudentNumber + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        for (int i = actualIndex; i < studentCount - 1; i++) {
            students[i] = students[i + 1];
        }
        students[studentCount - 1] = null;
        studentCount--;

        refreshTable();
        setStatus("Student deleted.");
    }

    private void sortStudents() {
        if (studentCount < 2) {
            showError("There must be at least 2 students to sort.");
            return;
        }

        for (int i = 0; i < studentCount - 1; i++) {
            int highest = i;
            for (int j = i + 1; j < studentCount; j++) {
                if (students[j].getAverageMarks() > students[highest].getAverageMarks()) {
                    highest = j;
                }
            }
            Details temp = students[i];
            students[i] = students[highest];
            students[highest] = temp;
        }

        refreshTable();
        setStatus("Students sorted by average marks (highest to lowest).");
    }

    // ---------- Helper Methods ----------

    private boolean validateForm() {
        String errors = "";
        JTextField firstBad = null;

        String message = Validator.checkName(nameField.getText());
        if (!message.isEmpty()) {
            errors += "- " + message + "\n";
            firstBad = nameField;
        }

        message = Validator.checkStudentNumber(numberField.getText());
        if (!message.isEmpty()) {
            errors += "- " + message + "\n";
            if (firstBad == null) {
                firstBad = numberField;
            }
        }

        for (int i = 0; i < SUBJECTS.length; i++) {
            message = Validator.checkMark(SUBJECTS[i], markFields[i].getText());
            if (!message.isEmpty()) {
                errors += "- " + message + "\n";
                if (firstBad == null) {
                    firstBad = markFields[i];
                }
            }
        }

        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please correct the following:\n\n" + errors,
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            firstBad.requestFocus();
            return false;
        }
        return true;
    }

    private int[] readMarks() {
        int[] marks = new int[SUBJECTS.length];
        for (int i = 0; i < SUBJECTS.length; i++) {
            marks[i] = Integer.parseInt(markFields[i].getText().trim());
        }
        return marks;
    }

    private int findStudent(String studentNumber) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getStudentNumber().equals(studentNumber)) {
                return i;
            }
        }
        return -1;
    }

    private int calculateRank(int index) {
        int rank = 1;
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getAverageMarks() > students[index].getAverageMarks()) {
                rank++;
            }
        }
        return rank;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        double total = 0;
        int visibleCount = 0;

        String query = (liveSearchField != null) ? liveSearchField.getText().trim().toLowerCase() : "";

        for (int i = 0; i < studentCount; i++) {
            Details s = students[i];
            total += s.getAverageMarks();

            boolean matches = query.isEmpty()
                    || s.getStudentName().toLowerCase().contains(query)
                    || s.getStudentNumber().toLowerCase().contains(query);

            if (matches) {
                int oop = s.getOopMarks();
                int math = s.getMathMarks();
                int dict = s.getDictMarks();
                int arch = s.getArchitectureMarks();
                int lit = s.getLiteracyMarks();
                int comm = s.getCommunicationSkillsMarks();

                tableModel.addRow(new Object[] {
                    calculateRank(i),
                    s.getStudentNumber(),
                    s.getStudentName(),
                    oop, Marks.gradeCalc(oop),
                    math, Marks.gradeCalc(math),
                    dict, Marks.gradeCalc(dict),
                    arch, Marks.gradeCalc(arch),
                    lit, Marks.gradeCalc(lit),
                    comm, Marks.gradeCalc(comm),
                    String.format("%.1f", s.getAverageMarks()),
                    s.getAverageGrade()
                });
                visibleCount++;
            }
        }

        if (studentCount == 0) {
            setStatus("No students yet. Fill in the form on the left and click Add Student.");
        } else if (!query.isEmpty()) {
            setStatus("Showing " + visibleCount + " of " + studentCount + " students matching \"" + liveSearchField.getText().trim() + "\"");
        } else {
            setStatus("Total Students: " + studentCount + "   |   Class Average: "
                    + String.format("%.1f", total / studentCount) + " (" + Marks.gradeCalc(total / studentCount) + ")");
        }
    }

    private void clearForm() {
        nameField.setText("");
        numberField.setText("");
        for (JTextField markField : markFields) {
            markField.setText("");
        }
        nameField.requestFocus();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    // ---------- Custom Cell Renderer for Grade Styling & Alignment ----------

    private static class StudentTableRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 2) {
                setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                setHorizontalAlignment(SwingConstants.CENTER);
            }

            boolean isGradeColumn = (column >= 4 && column <= 16 && column % 2 == 0);

            if (isSelected) {
                c.setBackground(new Color(219, 234, 254));
                c.setForeground(PRIMARY_DARK);
            } else {
                c.setBackground(row % 2 == 0 ? CARD_WHITE : ALT_ROW_COLOR);
                c.setForeground(new Color(30, 41, 59));
            }

            if (isGradeColumn && value != null) {
                String grade = value.toString();
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (!isSelected) {
                    if (grade.startsWith("A")) {
                        c.setForeground(new Color(21, 128, 61));       
                        c.setBackground(new Color(240, 253, 244));
                    } else if (grade.startsWith("B")) {
                        c.setForeground(new Color(29, 78, 216));       
                    } else if (grade.startsWith("C")) {
                        c.setForeground(new Color(180, 83, 9));        
                    } else if (grade.startsWith("D")) {
                        c.setForeground(new Color(194, 65, 12));       
                    } else if (grade.equals("F")) {
                        c.setForeground(new Color(185, 28, 28));       
                        c.setBackground(new Color(254, 242, 242));
                    }
                }
            } else if (column == 0 || column == 15) {
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else {
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
            }

            return c;
        }
    }

    // ---------- Program Entry Point ----------

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new StudentGUI().setVisible(true));
    }
}