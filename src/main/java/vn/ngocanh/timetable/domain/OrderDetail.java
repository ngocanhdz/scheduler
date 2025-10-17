package vn.ngocanh.timetable.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name = "semester_config_id")
    private SemesterConfig semesterConfig;
    private long yearLevel;
    private String college;

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public SemesterConfig getSemesterConfig() {
        return semesterConfig;
    }

    public void setSemesterConfig(SemesterConfig semesterConfig) {
        this.semesterConfig = semesterConfig;
    }

    public long getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(long yearLevel) {
        this.yearLevel = yearLevel;
    }

}
