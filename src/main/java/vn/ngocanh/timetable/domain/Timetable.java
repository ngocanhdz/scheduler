package vn.ngocanh.timetable.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "timetables", uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "period_id", "class_id",
        "semester_config_id" }))
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Quan hệ ManyToOne với Course - Mỗi môn học có thể có nhiều lịch (khác học kỳ)
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Môn học không được để trống")
    private Course course;

    // Quan hệ ManyToOne với Period - Mỗi tiết học có thể có nhiều môn học
    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    @NotNull(message = "Tiết học không được để trống")
    private Period period;

    // Quan hệ ManyToOne với Class - Mỗi phòng học có thể có nhiều môn học
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    @NotNull(message = "Phòng học không được để trống")
    private Class classRoom;

    // Quan hệ ManyToOne với SemesterConfig - Mỗi học kỳ có thể có nhiều lịch học
    @ManyToOne
    @JoinColumn(name = "semester_config_id", nullable = false)
    @NotNull(message = "Học kỳ không được để trống")
    private SemesterConfig semesterConfig;

    // Thời gian tạo lịch
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Trạng thái lịch học (active, cancelled, pending)
    @Column(name = "status", length = 20)
    private String status = "active";

    // Ghi chú thêm
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Constructor mặc định
    public Timetable() {
        this.createdAt = new Date();
    }

    // Constructor đầy đủ
    public Timetable(Course course, Period period, Class classRoom, SemesterConfig semesterConfig) {
        this();
        this.course = course;
        this.period = period;
        this.classRoom = classRoom;
        this.semesterConfig = semesterConfig;
    }

    // Getters và Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Class getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(Class classRoom) {
        this.classRoom = classRoom;
    }

    public SemesterConfig getSemesterConfig() {
        return semesterConfig;
    }

    public void setSemesterConfig(SemesterConfig semesterConfig) {
        this.semesterConfig = semesterConfig;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Helper methods

    /**
     * Kiểm tra xem lịch học có đang hoạt động không
     */
    public boolean isActive() {
        return "active".equals(this.status);
    }

    /**
     * Lấy tên môn học
     */
    public String getCourseName() {
        return this.course != null ? this.course.getName() : "";
    }

    /**
     * Lấy tên phòng học
     */
    public String getClassRoomName() {
        return this.classRoom != null ? this.classRoom.getName() : "";
    }

    /**
     * Lấy thông tin tiết học (tên + thời gian)
     */
    public String getPeriodInfo() {
        if (this.period != null) {
            return this.period.getName() + " (" + this.period.getStart() + "h-" + this.period.getEnd() + "h)";
        }
        return "";
    }

    /**
     * Lấy tên học kỳ
     */
    public String getSemesterName() {
        return this.semesterConfig != null ? this.semesterConfig.getName() : "";
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "id=" + id +
                ", course=" + (course != null ? course.getName() : "null") +
                ", period=" + (period != null ? period.getName() : "null") +
                ", classRoom=" + (classRoom != null ? classRoom.getName() : "null") +
                ", semesterConfig=" + (semesterConfig != null ? semesterConfig.getName() : "null") +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
