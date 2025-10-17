package vn.ngocanh.timetable.genetic.fitness;

import org.springframework.stereotype.Component;
import vn.ngocanh.timetable.genetic.config.GeneticAlgorithmConfig;
import vn.ngocanh.timetable.genetic.model.TimetableChromosome;
import vn.ngocanh.timetable.genetic.model.TimetableGene;

import java.util.*;

/**
 * Tính toán fitness cho chromosome trong thuật toán di truyền
 * Fitness càng cao = thời khóa biểu càng tốt
 */
@Component
public class TimetableFitnessFunction {

    /**
     * Tính fitness chính cho chromosome
     */
    public double calculateFitness(TimetableChromosome chromosome,
            List<Long> requiredCourseIds,
            GeneticAlgorithmConfig config) {

        if (chromosome == null || chromosome.isEmpty()) {
            return 0.0;
        }
        // Các component của fitness
        double conflictPenalty = calculateConflictPenalty(chromosome, config);
        double consecutivePenalty = calculateConsecutiveSessionPenalty(chromosome, config);
        double completenessBonus = calculateCompletenessBonus(chromosome, requiredCourseIds, config);
        double utilizationBonus = calculateUtilizationBonus(chromosome, config);
        double distributionBonus = calculateDistributionBonus(chromosome, config);

        // Tổng hợp fitness
        double totalFitness = conflictPenalty + consecutivePenalty + completenessBonus + utilizationBonus
                + distributionBonus;

        // Normalize về [0, 1] range
        return Math.max(0.0, Math.min(1.0, (totalFitness + 100) / 200.0));
    }

    /**
     * Tính penalty cho xung đột thời gian
     * Xung đột là khi có 2 môn học cùng thời gian và phòng học
     */
    private double calculateConflictPenalty(TimetableChromosome chromosome,
            GeneticAlgorithmConfig config) {

        int conflicts = chromosome.countTimeConflicts();
        return conflicts * config.getFitnessWeights().getTimeConflictWeight();
    }

    /**
     * Tính penalty cho các kíp không liên tiếp
     * Mỗi lỗi kíp không liên tiếp sẽ bị phạt nặng
     */
    private double calculateConsecutiveSessionPenalty(TimetableChromosome chromosome,
            GeneticAlgorithmConfig config) {

        int violations = chromosome.countConsecutiveSessionViolations();
        return violations * config.getFitnessWeights().getTimeConflictWeight(); // Dùng chung weight với conflict
    }

    /**
     * Tính bonus cho việc xếp đủ các môn học yêu cầu
     */
    private double calculateCompletenessBonus(TimetableChromosome chromosome,
            List<Long> requiredCourseIds,
            GeneticAlgorithmConfig config) {

        if (requiredCourseIds == null || requiredCourseIds.isEmpty()) {
            return 0.0;
        }

        Set<Long> scheduledCourses = chromosome.getCourseIds();
        int scheduledRequiredCourses = 0;

        for (Long courseId : requiredCourseIds) {
            if (scheduledCourses.contains(courseId)) {
                scheduledRequiredCourses++;
            }
        }

        double completenessRatio = (double) scheduledRequiredCourses / requiredCourseIds.size();
        return completenessRatio * config.getFitnessWeights().getCompletenessWeight();
    }

    /**
     * Tính bonus cho việc sử dụng phòng học hiệu quả
     * Khuyến khích sử dụng đa dạng phòng học, tránh tập trung vào ít phòng
     */
    private double calculateUtilizationBonus(TimetableChromosome chromosome,
            GeneticAlgorithmConfig config) {

        if (chromosome.isEmpty()) {
            return 0.0;
        }

        // Đếm số lượng phòng học được sử dụng
        Set<Long> usedClasses = new HashSet<>();
        for (TimetableGene gene : chromosome.getGenes()) {
            if (gene.isValid()) {
                usedClasses.add(gene.getClassId());
            }
        }

        // Thưởng khi sử dụng nhiều phòng học khác nhau
        double utilizationRatio = Math.min(1.0, usedClasses.size() / 10.0); // Giả sử có tối đa 10 phòng
        return utilizationRatio * config.getFitnessWeights().getClassUtilizationWeight();
    }

    /**
     * Tính bonus cho việc phân bố đều các môn học theo thời gian
     * Tránh tập trung quá nhiều môn vào một vài tiết
     */
    private double calculateDistributionBonus(TimetableChromosome chromosome,
            GeneticAlgorithmConfig config) {

        if (chromosome.isEmpty()) {
            return 0.0;
        }

        // Đếm số môn học theo từng period
        Map<Long, Integer> periodCount = new HashMap<>();
        for (TimetableGene gene : chromosome.getGenes()) {
            if (gene.isValid()) {
                periodCount.put(gene.getPeriodId(),
                        periodCount.getOrDefault(gene.getPeriodId(), 0) + 1);
            }
        }

        if (periodCount.isEmpty()) {
            return 0.0;
        }

        // Tính độ lệch chuẩn - càng thấp càng đều
        double mean = periodCount.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double variance = periodCount.values().stream()
                .mapToDouble(count -> Math.pow(count - mean, 2))
                .average().orElse(0.0);
        double standardDeviation = Math.sqrt(variance);

        // Chuyển đổi thành bonus (độ lệch chuẩn thấp = bonus cao)
        double distributionScore = Math.max(0.0, 1.0 - (standardDeviation / mean));
        return distributionScore * config.getFitnessWeights().getDistributionWeight();
    }

    /**
     * Tính fitness chi tiết với thông tin debug
     */
    public FitnessDetails calculateDetailedFitness(TimetableChromosome chromosome,
            List<Long> requiredCourseIds,
            GeneticAlgorithmConfig config) {
        FitnessDetails details = new FitnessDetails();

        details.conflicts = chromosome.countTimeConflicts();
        details.consecutiveViolations = chromosome.countConsecutiveSessionViolations();
        details.conflictPenalty = calculateConflictPenalty(chromosome, config);
        details.consecutivePenalty = calculateConsecutiveSessionPenalty(chromosome, config);
        details.completenessBonus = calculateCompletenessBonus(chromosome, requiredCourseIds, config);
        details.utilizationBonus = calculateUtilizationBonus(chromosome, config);
        details.distributionBonus = calculateDistributionBonus(chromosome, config);

        details.totalFitness = calculateFitness(chromosome, requiredCourseIds, config);

        // Thêm thống kê
        details.scheduledCourses = chromosome.getCourseIds().size();
        details.requiredCourses = requiredCourseIds != null ? requiredCourseIds.size() : 0;

        Set<Long> scheduledRequired = new HashSet<>(chromosome.getCourseIds());
        if (requiredCourseIds != null) {
            scheduledRequired.retainAll(requiredCourseIds);
        }
        details.scheduledRequiredCourses = scheduledRequired.size();

        return details;
    }

    /**
     * Kiểm tra xem chromosome có đạt tiêu chuẩn chấp nhận được không
     */
    public boolean isAcceptable(TimetableChromosome chromosome,
            List<Long> requiredCourseIds,
            GeneticAlgorithmConfig config) {
        // Không được có xung đột thời gian
        if (chromosome.countTimeConflicts() > 0) {
            return false;
        }

        // Không được có kíp không liên tiếp
        if (chromosome.countConsecutiveSessionViolations() > 0) {
            return false;
        }

        // Phải xếp được ít nhất 80% môn học yêu cầu
        if (requiredCourseIds != null && !requiredCourseIds.isEmpty()) {
            Set<Long> scheduledCourses = chromosome.getCourseIds();
            int scheduledRequiredCount = 0;
            for (Long courseId : requiredCourseIds) {
                if (scheduledCourses.contains(courseId)) {
                    scheduledRequiredCount++;
                }
            }

            double completenessRatio = (double) scheduledRequiredCount / requiredCourseIds.size();
            if (completenessRatio < 0.8) {
                return false;
            }
        }

        return true;
    }

    /**
     * Class chứa thông tin chi tiết về fitness
     */
    public static class FitnessDetails {
        public int conflicts;
        public int consecutiveViolations;
        public double conflictPenalty;
        public double consecutivePenalty;
        public double completenessBonus;
        public double utilizationBonus;
        public double distributionBonus;
        public double totalFitness;

        // Thống kê
        public int scheduledCourses;
        public int requiredCourses;
        public int scheduledRequiredCourses;

        @Override
        public String toString() {
            return String.format(
                    "FitnessDetails{fitness=%.3f, conflicts=%d, consecutiveViolations=%d, scheduled=%d/%d, " +
                            "penalties=[conflict=%.2f, consecutive=%.2f], bonuses=[complete=%.2f, util=%.2f, dist=%.2f]}",
                    totalFitness, conflicts, consecutiveViolations, scheduledRequiredCourses, requiredCourses,
                    conflictPenalty, consecutivePenalty, completenessBonus, utilizationBonus, distributionBonus);
        }
    }
}
