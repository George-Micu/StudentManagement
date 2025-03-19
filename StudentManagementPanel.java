import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentManagementPanel extends JPanel {
    private DataStore dataStore;
    
    // Input fields and buttons.
    private JTextField txtStudentId, txtStudentName, txtStudentAge;
    private JButton btnAddStudent, btnUpdateStudent, btnViewStudents;
    private JTable tblStudents;
    private DefaultTableModel studentTableModel;

    public StudentManagementPanel(DataStore dataStore) {
        this.dataStore = dataStore;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        initComponents();
    }

    private void initComponents() {
        // Create an input panel using GridLayout for close label-field pairing.
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBackground(Color.DARK_GRAY);

        JLabel lblId = new JLabel("Student ID:");
        lblId.setForeground(Color.WHITE);
        inputPanel.add(lblId);
        txtStudentId = new JTextField();
        inputPanel.add(txtStudentId);

        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.WHITE);
        inputPanel.add(lblName);
        txtStudentName = new JTextField();
        inputPanel.add(txtStudentName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setForeground(Color.WHITE);
        inputPanel.add(lblAge);
        txtStudentAge = new JTextField();
        inputPanel.add(txtStudentAge);

        // Button panel.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.DARK_GRAY);
        btnAddStudent = new JButton("Add Student");
        btnUpdateStudent = new JButton("Update Student");
        btnViewStudents = new JButton("View Students");

        // Set dark mode button colors.
        btnAddStudent.setBackground(new Color(70, 130, 180));
        btnAddStudent.setForeground(Color.WHITE);
        btnUpdateStudent.setBackground(new Color(70, 130, 180));
        btnUpdateStudent.setForeground(Color.WHITE);
        btnViewStudents.setBackground(new Color(70, 130, 180));
        btnViewStudents.setForeground(Color.WHITE);

        buttonPanel.add(btnAddStudent);
        buttonPanel.add(btnUpdateStudent);
        buttonPanel.add(btnViewStudents);

        // Combine input and button panels.
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table for displaying student details.
        studentTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Age"}, 0);
        tblStudents = new JTable(studentTableModel);
        tblStudents.setFillsViewportHeight(true);
        // Apply dark theme styling to the table.
        tblStudents.setBackground(Color.DARK_GRAY);
        tblStudents.setForeground(Color.WHITE);
        tblStudents.setGridColor(Color.GRAY);
        tblStudents.getTableHeader().setBackground(new Color(70, 130, 180));
        tblStudents.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(tblStudents);
        scrollPane.getViewport().setBackground(Color.DARK_GRAY);
        add(scrollPane, BorderLayout.CENTER);

        // Register event listeners.
        btnAddStudent.addActionListener(e -> addStudent());
        btnUpdateStudent.addActionListener(e -> showUpdateStudentDialog());
        btnViewStudents.addActionListener(e -> refreshStudentTable());
    }

    private void addStudent() {
        try {
            int id = Integer.parseInt(txtStudentId.getText().trim());
            String name = txtStudentName.getText().trim();
            int age = Integer.parseInt(txtStudentAge.getText().trim());

            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }

            // Prevent duplicate student IDs.
            for (Student s : dataStore.getStudentList()) {
                if (s.getId() == id) {
                    JOptionPane.showMessageDialog(this, "A student with this ID already exists.", 
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Student student = new Student(id, name, age);
            dataStore.addStudent(student);
            JOptionPane.showMessageDialog(this, "Student successfully added.", 
                                          "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshStudentTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ensure that both ID and Age are valid numbers.", 
                                          "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                                          "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // New method to show update student dialog.
    private void showUpdateStudentDialog() {
        List<Student> students = dataStore.getStudentList();
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students available to update.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a modal dialog.
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Update Student", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.DARK_GRAY);

        // Panel for student selection.
        JPanel selectionPanel = new JPanel(new FlowLayout());
        selectionPanel.setBackground(Color.DARK_GRAY);
        JLabel lblSelect = new JLabel("Select Student:");
        lblSelect.setForeground(Color.WHITE);
        selectionPanel.add(lblSelect);

        JComboBox<String> cmbStudents = new JComboBox<>();
        for (Student s : students) {
            cmbStudents.addItem(s.getId() + " - " + s.getName());
        }
        selectionPanel.add(cmbStudents);
        dialog.add(selectionPanel, BorderLayout.NORTH);

        // Panel for updating info.
        JPanel updatePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        updatePanel.setBackground(Color.DARK_GRAY);
        updatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblId = new JLabel("Student ID:");
        lblId.setForeground(Color.WHITE);
        JTextField txtId = new JTextField();
        txtId.setEditable(false);
        updatePanel.add(lblId);
        updatePanel.add(txtId);

        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.WHITE);
        JTextField txtName = new JTextField();
        updatePanel.add(lblName);
        updatePanel.add(txtName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setForeground(Color.WHITE);
        JTextField txtAge = new JTextField();
        updatePanel.add(lblAge);
        updatePanel.add(txtAge);

        dialog.add(updatePanel, BorderLayout.CENTER);

        // When a student is selected, prepopulate the fields.
        cmbStudents.addActionListener(e -> {
            String selected = (String) cmbStudents.getSelectedItem();
            if (selected != null) {
                int selectedId = Integer.parseInt(selected.split(" - ")[0]);
                for (Student s : students) {
                    if (s.getId() == selectedId) {
                        txtId.setText(String.valueOf(s.getId()));
                        txtName.setText(s.getName());
                        txtAge.setText(String.valueOf(s.getAge()));
                        break;
                    }
                }
            }
        });
        cmbStudents.setSelectedIndex(0); // Trigger initial population.

        // Panel for dialog buttons.
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.DARK_GRAY);
        JButton btnUpdate = new JButton("Update");
        JButton btnCancel = new JButton("Cancel");
        btnUpdate.setBackground(new Color(70, 130, 180));
        btnUpdate.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(70, 130, 180));
        btnCancel.setForeground(Color.WHITE);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnUpdate.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                int age = Integer.parseInt(txtAge.getText().trim());
                if(name.isEmpty()){
                    throw new IllegalArgumentException("Name cannot be empty.");
                }
                int id = Integer.parseInt(txtId.getText().trim());
                boolean updated = false;
                for (Student s : students) {
                    if (s.getId() == id) {
                        s.setName(name);
                        s.setAge(age);
                        updated = true;
                        break;
                    }
                }
                if (updated) {
                    JOptionPane.showMessageDialog(dialog, "Student updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshStudentTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Ensure that Age is a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        for (Student s : dataStore.getStudentList()) {
            studentTableModel.addRow(new Object[]{s.getId(), s.getName(), s.getAge()});
        }
    }
}
