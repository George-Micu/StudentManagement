import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GradeManagementPanel extends JPanel {
    private DataStore dataStore;
    private JComboBox<String> cmbStudentsForGrade;
    private JComboBox<String> cmbCoursesForGrade;
    private JTextField txtGrade;
    private JButton btnAssignGrade;
    private JTable tblGrades;
    private DefaultTableModel gradeTableModel;

    public GradeManagementPanel(DataStore dataStore) {
        this.dataStore = dataStore;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        initComponents();
    }

    private void initComponents() {
        // Top panel for grade assignment.
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.DARK_GRAY);
        JLabel lblSelectStudent = new JLabel("Select Student:");
        lblSelectStudent.setForeground(Color.WHITE);
        topPanel.add(lblSelectStudent);

        cmbStudentsForGrade = new JComboBox<>();
        refreshStudentDropdown();
        topPanel.add(cmbStudentsForGrade);

        JLabel lblSelectCourse = new JLabel("Select Course:");
        lblSelectCourse.setForeground(Color.WHITE);
        topPanel.add(lblSelectCourse);

        cmbCoursesForGrade = new JComboBox<>();
        refreshCoursesForGrade();
        topPanel.add(cmbCoursesForGrade);

        JLabel lblGrade = new JLabel("Grade:");
        lblGrade.setForeground(Color.WHITE);
        topPanel.add(lblGrade);

        txtGrade = new JTextField(5);
        topPanel.add(txtGrade);

        btnAssignGrade = new JButton("Assign Grade");
        btnAssignGrade.setBackground(new Color(70, 130, 180));
        btnAssignGrade.setForeground(Color.WHITE);
        topPanel.add(btnAssignGrade);

        add(topPanel, BorderLayout.NORTH);

        // Table for grade records.
        gradeTableModel = new DefaultTableModel(new Object[]{"Student ID", "Course", "Grade"}, 0);
        tblGrades = new JTable(gradeTableModel);
        tblGrades.setFillsViewportHeight(true);
        tblGrades.setBackground(Color.DARK_GRAY);
        tblGrades.setForeground(Color.WHITE);
        tblGrades.setGridColor(Color.GRAY);
        tblGrades.getTableHeader().setBackground(new Color(70, 130, 180));
        tblGrades.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(tblGrades);
        scrollPane.getViewport().setBackground(Color.DARK_GRAY);
        add(scrollPane, BorderLayout.CENTER);

        // Listeners.
        cmbStudentsForGrade.addActionListener(e -> refreshCoursesForGrade());
        btnAssignGrade.addActionListener(e -> assignGrade());
    }

    private void refreshStudentDropdown() {
        cmbStudentsForGrade.removeAllItems();
        for (Student s : dataStore.getStudentList()){
            cmbStudentsForGrade.addItem(s.getId() + " - " + s.getName());
        }
    }

    private void refreshCoursesForGrade() {
        cmbCoursesForGrade.removeAllItems();
        String selectedStudent = (String) cmbStudentsForGrade.getSelectedItem();
        if (selectedStudent == null) return;
        int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
        for (Enrollment e : dataStore.getEnrollmentList()){
            if(e.getStudent().getId() == studentId){
                cmbCoursesForGrade.addItem(e.getCourse().getCode() + " - " + e.getCourse().getName());
            }
        }
    }

    private void assignGrade() {
        String selectedStudent = (String) cmbStudentsForGrade.getSelectedItem();
        String selectedCourse = (String) cmbCoursesForGrade.getSelectedItem();
        String grade = txtGrade.getText().trim();

        if(selectedStudent == null || selectedCourse == null || grade.isEmpty()){
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate that the grade contains only numbers.
        try {
            Integer.parseInt(grade);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Grade must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
        String courseCode = selectedCourse.split(" - ")[0];
        boolean found = false;
        for (Enrollment e : dataStore.getEnrollmentList()){
            if(e.getStudent().getId() == studentId && e.getCourse().getCode().equals(courseCode)){
                e.setGrade(grade);
                found = true;
                break;
            }
        }
        if(found){
            JOptionPane.showMessageDialog(this, "Grade assigned.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshGradeTable();
        } else {
            JOptionPane.showMessageDialog(this, "Enrollment record not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshGradeTable() {
        gradeTableModel.setRowCount(0);
        for (Enrollment e : dataStore.getEnrollmentList()){
            gradeTableModel.addRow(new Object[]{e.getStudent().getId(), e.getCourse().getCode(), e.getGrade()});
        }
    }
}
