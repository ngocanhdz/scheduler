package vn.ngocanh.timetable.genetic.model;

import java.util.Objects;

/**
 * Đại diện cho một gen trong chromosome
 * Mỗi gen chứa thông tin về việc gán một môn học (1 kíp) vào một slot thời gian
 * cụ thể
 * 
 * Lưu ý:
 * - Một môn có thể có nhiều lớp (do sức chứa phòng học)
 * - Một lớp có thể có nhiều kíp (do lectureHour > 1)
 */
public class TimetableGene {

    private Long courseId; // ID của môn học
    private Long periodId; // ID của tiết học (bao gồm cả ngày và giờ)
    private Long classId; // ID của phòng học
    private Integer classIndex; // Lớp thứ mấy của môn học này (1, 2, 3...)
    private Integer sessionIndex; // Kíp thứ mấy trong lớp này (1, 2, 3... tương ứng lectureHour)

    // Constructor mặc định
    public TimetableGene() {
    }

    // Constructor đầy đủ
    public TimetableGene(Long courseId, Long periodId, Long classId, Integer classIndex, Integer sessionIndex) {
        this.courseId = courseId;
        this.periodId = periodId;
        this.classId = classId;
        this.classIndex = classIndex;
        this.sessionIndex = sessionIndex;
    }

    // Constructor copy
    public TimetableGene(TimetableGene other) {
        this.courseId = other.courseId;
        this.periodId = other.periodId;
        this.classId = other.classId;
        this.classIndex = other.classIndex;
        this.sessionIndex = other.sessionIndex;
    }

    // Getters và Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Integer getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(Integer classIndex) {
        this.classIndex = classIndex;
    }

    public Integer getSessionIndex() {
        return sessionIndex;
    }

    public void setSessionIndex(Integer sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    /**
     * Kiểm tra xem gene có hợp lệ không (không có giá trị null)
     */
    public boolean isValid() {
        return courseId != null && periodId != null && classId != null
                && classIndex != null && sessionIndex != null;
    }

    /**
     * Tạo một key duy nhất để kiểm tra xung đột thời gian
     * Format: "periodId_classId"
     */
    public String getTimeSlotKey() {
        return periodId + "_" + classId;
    }

    /**
     * Tạo key duy nhất cho lớp học
     * Format: "courseId_classIndex"
     */
    public String getClassKey() {
        return courseId + "_" + classIndex;
    }

    /**
     * Lấy số thứ tự của period (từ period number)
     * VD: periodId có number = 201 → return 201
     */
    public Integer getPeriodNumber() {
        // Giả sử có cách lấy period number, tạm thời return periodId
        // Sẽ cập nhật khi có access đến Period entity
        return periodId != null ? periodId.intValue() : null;
    }

    /**
     * Kiểm tra xem 2 gene có liên tiếp về thời gian không
     * VD: gene1 có period=201, gene2 có period=202 → liên tiếp
     */
    public boolean isConsecutiveWith(TimetableGene other) {
        if (other == null || !this.isValid() || !other.isValid()) {
            return false;
        }

        Integer thisNumber = this.getPeriodNumber();
        Integer otherNumber = other.getPeriodNumber();

        if (thisNumber == null || otherNumber == null) {
            return false;
        }

        return Math.abs(thisNumber - otherNumber) == 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TimetableGene that = (TimetableGene) obj;
        return Objects.equals(courseId, that.courseId) &&
                Objects.equals(periodId, that.periodId) &&
                Objects.equals(classId, that.classId) &&
                Objects.equals(classIndex, that.classIndex) &&
                Objects.equals(sessionIndex, that.sessionIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, periodId, classId, classIndex, sessionIndex);
    }

    @Override
    public String toString() {
        return "TimetableGene{" +
                "courseId=" + courseId +
                ", periodId=" + periodId +
                ", classId=" + classId +
                ", classIndex=" + classIndex +
                ", sessionIndex=" + sessionIndex +
                '}';
    }

    /**
     * Tạo một bản copy của gene
     */
    public TimetableGene copy() {
        return new TimetableGene(this);
    }
}
