import java.awt.*;
import javax.swing.*;

public class StudentManagementSystem extends JFrame {
    private DataStore dataStore;

    public StudentManagementSystem() {
        // Initialize the shared data store.
        dataStore = new DataStore();
        
        // Preload some courses.
        dataStore.addCourse(new Course("CS101", "Intro to Computer Science"));
        dataStore.addCourse(new Course("MATH201", "Calculus I"));
        dataStore.addCourse(new Course("ENG301", "English Literature"));
        
        // Preload some students.
        dataStore.addStudent(new Student(1, "Alice", 20));
        dataStore.addStudent(new Student(2, "Bob", 21));
        dataStore.addStudent(new Student(3, "Charlie", 22));

        initComponents();
    }

    private void initComponents() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.DARK_GRAY);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Create panels for each module.
        StudentManagementPanel studentPanel = new StudentManagementPanel(dataStore);
        CourseEnrollmentPanel coursePanel = new CourseEnrollmentPanel(dataStore);
        GradeManagementPanel gradePanel = new GradeManagementPanel(dataStore);

        tabbedPane.addTab("Student Management", studentPanel);
        tabbedPane.addTab("Course Enrollment", coursePanel);
        tabbedPane.addTab("Grade Management", gradePanel);
        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.setVisible(true);
        });
    }
}
