import java.awt.*;
import javax.swing.*;

public class CourseEnrollmentPanel extends JPanel {
    private DataStore dataStore;
    private JComboBox<String> cmbCourses;
    private JList<String> lstEligibleStudents;
    private JButton btnEnroll;

    public CourseEnrollmentPanel(DataStore dataStore) {
        this.dataStore = dataStore;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        initComponents();
    }

    private void initComponents() {
        // Top panel for course selection.
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.DARK_GRAY);
        JLabel lblSelectCourse = new JLabel("Select Course:");
        lblSelectCourse.setForeground(Color.WHITE);
        topPanel.add(lblSelectCourse);

        cmbCourses = new JComboBox<>();
        for (Course c : dataStore.getCourseList()){
            cmbCourses.addItem(c.getCode() + " - " + c.getName());
        }
        topPanel.add(cmbCourses);

        btnEnroll = new JButton("Enroll Student");
        btnEnroll.setBackground(new Color(70, 130, 180));
        btnEnroll.setForeground(Color.WHITE);
        topPanel.add(btnEnroll);
        add(topPanel, BorderLayout.NORTH);

        // List for eligible students.
        lstEligibleStudents = new JList<>();
        lstEligibleStudents.setBackground(Color.DARK_GRAY);
        lstEligibleStudents.setForeground(Color.WHITE);
        refreshEligibleStudents();
        JScrollPane scrollPane = new JScrollPane(lstEligibleStudents);
        scrollPane.getViewport().setBackground(Color.DARK_GRAY);
        add(scrollPane, BorderLayout.CENTER);

        btnEnroll.addActionListener(e -> enrollStudent());
    }

    private void refreshEligibleStudents() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Student s : dataStore.getStudentList()){
            listModel.addElement(s.getId() + " - " + s.getName());
        }
        lstEligibleStudents.setModel(listModel);
    }

    private void enrollStudent() {
        String selectedCourse = (String) cmbCourses.getSelectedItem();
        String selectedStudent = lstEligibleStudents.getSelectedValue();
        if (selectedCourse == null || selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Select both a course and a student.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
        // Prevent duplicate enrollments.
        for (Enrollment e : dataStore.getEnrollmentList()){
            if(e.getStudent().getId() == studentId && e.getCourse().getCode().equals(selectedCourse.split(" - ")[0])){
                JOptionPane.showMessageDialog(this, "This student is already enrolled in the course.", "Enrollment Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // Find the student and course objects.
        Student selectedStudentObj = null;
        for (Student s : dataStore.getStudentList()){
            if(s.getId() == studentId){
                selectedStudentObj = s;
                break;
            }
        }
        String courseCode = selectedCourse.split(" - ")[0];
        Course selectedCourseObj = null;
        for (Course c : dataStore.getCourseList()){
            if(c.getCode().equals(courseCode)){
                selectedCourseObj = c;
                break;
            }
        }
        if (selectedStudentObj != null && selectedCourseObj != null){
            Enrollment enrollment = new Enrollment(selectedStudentObj, selectedCourseObj, "");
            dataStore.addEnrollment(enrollment);
            JOptionPane.showMessageDialog(this, "Enrollment completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
