import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private List<Student> studentList;
    private List<Course> courseList;
    private List<Enrollment> enrollmentList;

    public DataStore() {
        studentList = new ArrayList<>();
        courseList = new ArrayList<>();
        enrollmentList = new ArrayList<>();
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public List<Enrollment> getEnrollmentList() {
        return enrollmentList;
    }

    public void addStudent(Student s) {
        studentList.add(s);
    }

    public void addCourse(Course c) {
        courseList.add(c);
    }

    public void addEnrollment(Enrollment e) {
        enrollmentList.add(e);
    }
}
