public class Enrollment {
    private Student student;
    private Course course;
    private String grade;

    public Enrollment(Student student, Course course, String grade) {
        this.student = student;
        this.course = course;
        this.grade = grade;
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
