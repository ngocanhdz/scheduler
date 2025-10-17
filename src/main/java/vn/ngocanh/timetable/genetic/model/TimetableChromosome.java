package vn.ngocanh.timetable.genetic.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Đại diện cho một chromosome trong GA
 * Mỗi chromosome chứa một danh sách các gene (TimetableGene)
 * đại diện cho một thời khóa biểu hoàn chỉnh
 */
public class TimetableChromosome {

    private List<TimetableGene> genes;
    private Double fitness; // Giá trị fitness (được tính sau)
    private boolean evaluated; // Đã được đánh giá chưa

    // Constructor mặc định
    public TimetableChromosome() {
        this.genes = new ArrayList<>();
        this.fitness = null;
        this.evaluated = false;
    }

    // Constructor với danh sách gene
    public TimetableChromosome(List<TimetableGene> genes) {
        this.genes = new ArrayList<>(genes);
        this.fitness = null;
        this.evaluated = false;
    }

    // Constructor copy
    public TimetableChromosome(TimetableChromosome other) {
        this.genes = other.genes.stream()
                .map(TimetableGene::copy)
                .collect(Collectors.toList());
        this.fitness = other.fitness;
        this.evaluated = other.evaluated;
    }

    // Getters và Setters
    public List<TimetableGene> getGenes() {
        return genes;
    }

    public void setGenes(List<TimetableGene> genes) {
        this.genes = genes;
        this.evaluated = false; // Reset trạng thái đánh giá
        this.fitness = null;
    }

    public Double getFitness() {
        return fitness;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
        this.evaluated = true;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    /**
     * Thêm một gene vào chromosome
     */
    public void addGene(TimetableGene gene) {
        this.genes.add(gene);
        this.evaluated = false;
        this.fitness = null;
    }

    /**
     * Lấy gene tại vị trí cụ thể
     */
    public TimetableGene getGene(int index) {
        if (index >= 0 && index < genes.size()) {
            return genes.get(index);
        }
        return null;
    }

    /**
     * Thay thế gene tại vị trí cụ thể
     */
    public void setGene(int index, TimetableGene gene) {
        if (index >= 0 && index < genes.size()) {
            genes.set(index, gene);
            this.evaluated = false;
            this.fitness = null;
        }
    }

    /**
     * Lấy số lượng gene trong chromosome
     */
    public int size() {
        return genes.size();
    }

    /**
     * Kiểm tra xem chromosome có rỗng không
     */
    public boolean isEmpty() {
        return genes.isEmpty();
    }

    /**
     * Lấy danh sách tất cả các courseId trong chromosome
     */
    public Set<Long> getCourseIds() {
        return genes.stream()
                .map(TimetableGene::getCourseId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Kiểm tra xem có môn học nào bị trùng lặp không
     */
    public boolean hasDuplicateCourses() {
        Set<Long> courseIds = new HashSet<>();
        for (TimetableGene gene : genes) {
            if (gene.getCourseId() != null) {
                if (!courseIds.add(gene.getCourseId())) {
                    return true; // Có trùng lặp
                }
            }
        }
        return false;
    }

    /**
     * Kiểm tra xung đột thời gian (cùng period và class)
     */
    public int countTimeConflicts() {
        Map<String, Integer> timeSlotCount = new HashMap<>();

        for (TimetableGene gene : genes) {
            if (gene.isValid()) {
                String timeSlotKey = gene.getTimeSlotKey();
                timeSlotCount.put(timeSlotKey, timeSlotCount.getOrDefault(timeSlotKey, 0) + 1);
            }
        }

        // Đếm số xung đột (số lần xuất hiện > 1)
        return timeSlotCount.values().stream()
                .mapToInt(count -> Math.max(0, count - 1))
                .sum();
    }

    /**
     * Lấy tất cả các gene có xung đột thời gian
     */
    public List<TimetableGene> getConflictingGenes() {
        Map<String, List<TimetableGene>> timeSlotMap = new HashMap<>();

        // Nhóm gene theo time slot
        for (TimetableGene gene : genes) {
            if (gene.isValid()) {
                String timeSlotKey = gene.getTimeSlotKey();
                timeSlotMap.computeIfAbsent(timeSlotKey, k -> new ArrayList<>()).add(gene);
            }
        }

        // Lấy các gene có xung đột (time slot có > 1 gene)
        return timeSlotMap.values().stream()
                .filter(geneList -> geneList.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Kiểm tra tính hợp lệ của chromosome
     */
    public boolean isValid() {
        // Kiểm tra tất cả gene có hợp lệ
        for (TimetableGene gene : genes) {
            if (!gene.isValid()) {
                return false;
            }
        }

        // Kiểm tra không có trùng lặp môn học trong cùng lớp
        if (hasDuplicateCourses()) {
            return false;
        }

        // Kiểm tra các kíp phải liên tiếp
        if (!hasValidConsecutiveSessions()) {
            return false;
        }

        return true;
    }

    /**
     * Tạo một bản copy của chromosome
     */
    public TimetableChromosome copy() {
        return new TimetableChromosome(this);
    }

    /**
     * Random hóa vị trí các gene (mutation helper)
     */
    public void shuffle(Random random) {
        Collections.shuffle(genes, random);
        this.evaluated = false;
        this.fitness = null;
    }

    /**
     * Kiểm tra tính hợp lệ của các kíp liên tiếp cho từng lớp học
     * Mỗi lớp học phải có các kíp liên tiếp (201-202-203 hoặc 301-302-303)
     */
    public boolean hasValidConsecutiveSessions() {
        // Nhóm gene theo lớp học (courseId + classIndex)
        Map<String, List<TimetableGene>> classSessions = new HashMap<>();

        for (TimetableGene gene : genes) {
            if (gene.isValid()) {
                String classKey = gene.getClassKey();
                classSessions.computeIfAbsent(classKey, k -> new ArrayList<>()).add(gene);
            }
        }

        // Kiểm tra từng lớp học
        for (Map.Entry<String, List<TimetableGene>> entry : classSessions.entrySet()) {
            List<TimetableGene> sessions = entry.getValue();

            // Sắp xếp theo sessionIndex
            sessions.sort(Comparator.comparing(TimetableGene::getSessionIndex));

            // Kiểm tra sessionIndex liên tục (1, 2, 3...)
            for (int i = 0; i < sessions.size(); i++) {
                if (!sessions.get(i).getSessionIndex().equals(i + 1)) {
                    return false; // SessionIndex không liên tục
                }
            }

            // Kiểm tra periodNumber liên tiếp
            if (sessions.size() > 1) {
                sessions.sort(Comparator.comparing(TimetableGene::getPeriodNumber));

                for (int i = 1; i < sessions.size(); i++) {
                    TimetableGene prev = sessions.get(i - 1);
                    TimetableGene curr = sessions.get(i);

                    if (!prev.isConsecutiveWith(curr)) {
                        return false; // Period không liên tiếp
                    }

                    // Phải cùng phòng học
                    if (!prev.getClassId().equals(curr.getClassId())) {
                        return false; // Không cùng phòng học
                    }
                }
            }
        }

        return true;
    }

    /**
     * Đếm số lỗi về kíp không liên tiếp
     */
    public int countConsecutiveSessionViolations() {
        Map<String, List<TimetableGene>> classSessions = new HashMap<>();

        for (TimetableGene gene : genes) {
            if (gene.isValid()) {
                String classKey = gene.getClassKey();
                classSessions.computeIfAbsent(classKey, k -> new ArrayList<>()).add(gene);
            }
        }

        int violations = 0;

        for (List<TimetableGene> sessions : classSessions.values()) {
            if (sessions.size() > 1) {
                sessions.sort(Comparator.comparing(TimetableGene::getPeriodNumber));

                for (int i = 1; i < sessions.size(); i++) {
                    TimetableGene prev = sessions.get(i - 1);
                    TimetableGene curr = sessions.get(i);

                    if (!prev.isConsecutiveWith(curr) || !prev.getClassId().equals(curr.getClassId())) {
                        violations++;
                    }
                }
            }
        }

        return violations;
    }

    /**
     * Lấy thông tin về các lớp học (courseId + số lượng sinh viên)
     */
    public Map<String, Integer> getClassStudentCounts() {
        // Tạm thời return empty map
        // Sẽ cập nhật khi có access đến thông tin sinh viên từ OrderDetail
        return new HashMap<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TimetableChromosome that = (TimetableChromosome) obj;
        return Objects.equals(genes, that.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes);
    }

    @Override
    public String toString() {
        return "TimetableChromosome{" +
                "genes=" + genes.size() +
                ", fitness=" + fitness +
                ", evaluated=" + evaluated +
                ", conflicts=" + countTimeConflicts() +
                ", consecutiveViolations=" + countConsecutiveSessionViolations() +
                '}';
    }
}
