package vn.ngocanh.timetable.genetic.service;

import vn.ngocanh.timetable.genetic.model.TimetableChromosome;

/**
 * Kết quả của thuật toán di truyền
 * Chứa thông tin về quá trình tối ưu hóa và kết quả cuối cùng
 */
public class GeneticAlgorithmResult {

    private boolean success;
    private String errorMessage;

    // Kết quả tối ưu hóa
    private TimetableChromosome bestChromosome;
    private Double bestFitness;
    private Double averageFitness;

    // Thông tin quá trình
    private Integer generations;
    private Long executionTime; // milliseconds
    private Integer finalPopulationSize;
    private Double populationDiversity;

    // Thống kê chi tiết
    private Integer totalConflicts;
    private Integer consecutiveViolations;
    private Integer scheduledCourses;
    private Integer requiredCourses;

    // Constructor
    public GeneticAlgorithmResult() {
    }

    // Getters và Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TimetableChromosome getBestChromosome() {
        return bestChromosome;
    }

    public void setBestChromosome(TimetableChromosome bestChromosome) {
        this.bestChromosome = bestChromosome;

        // Tự động cập nhật thống kê
        if (bestChromosome != null) {
            this.totalConflicts = bestChromosome.countTimeConflicts();
            this.consecutiveViolations = bestChromosome.countConsecutiveSessionViolations();
            this.scheduledCourses = bestChromosome.getCourseIds().size();
        }
    }

    public Double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(Double bestFitness) {
        this.bestFitness = bestFitness;
    }

    public Double getAverageFitness() {
        return averageFitness;
    }

    public void setAverageFitness(Double averageFitness) {
        this.averageFitness = averageFitness;
    }

    public Integer getGenerations() {
        return generations;
    }

    public void setGenerations(Integer generations) {
        this.generations = generations;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Integer getFinalPopulationSize() {
        return finalPopulationSize;
    }

    public void setFinalPopulationSize(Integer finalPopulationSize) {
        this.finalPopulationSize = finalPopulationSize;
    }

    public Double getPopulationDiversity() {
        return populationDiversity;
    }

    public void setPopulationDiversity(Double populationDiversity) {
        this.populationDiversity = populationDiversity;
    }

    public Integer getTotalConflicts() {
        return totalConflicts;
    }

    public void setTotalConflicts(Integer totalConflicts) {
        this.totalConflicts = totalConflicts;
    }

    public Integer getConsecutiveViolations() {
        return consecutiveViolations;
    }

    public void setConsecutiveViolations(Integer consecutiveViolations) {
        this.consecutiveViolations = consecutiveViolations;
    }

    public Integer getScheduledCourses() {
        return scheduledCourses;
    }

    public void setScheduledCourses(Integer scheduledCourses) {
        this.scheduledCourses = scheduledCourses;
    }

    public Integer getRequiredCourses() {
        return requiredCourses;
    }

    public void setRequiredCourses(Integer requiredCourses) {
        this.requiredCourses = requiredCourses;
    }

    // Helper methods

    /**
     * Alias for isSuccess() để tương thích với controller
     */
    public boolean isSuccessful() {
        return isSuccess();
    }

    /**
     * Alias for getGenerations()
     */
    public Integer getGenerationsExecuted() {
        return getGenerations();
    }

    /**
     * Alias for getExecutionTime()
     */
    public Long getExecutionTimeMs() {
        return getExecutionTime();
    }

    /**
     * Alias for getTotalConflicts()
     */
    public Integer getConflicts() {
        return getTotalConflicts();
    }

    /**
     * Alias for getConsecutiveViolations()
     */
    public Integer getViolations() {
        return getConsecutiveViolations();
    }

    /**
     * Alias for getCompletionRate()
     */
    public Double getCompleteness() {
        return getCompletionRate();
    }

    /**
     * Kiểm tra xem kết quả có chấp nhận được không
     */
    public boolean isAcceptable() {
        return success && bestChromosome != null &&
                (totalConflicts == null || totalConflicts == 0) &&
                (consecutiveViolations == null || consecutiveViolations == 0);
    }

    /**
     * Tính tỷ lệ hoàn thành (%)
     */
    public double getCompletionRate() {
        if (requiredCourses == null || requiredCourses == 0)
            return 100.0;
        if (scheduledCourses == null)
            return 0.0;

        return Math.min(100.0, (double) scheduledCourses / requiredCourses * 100.0);
    }

    /**
     * Lấy thời gian chạy theo định dạng dễ đọc
     */
    public String getFormattedExecutionTime() {
        if (executionTime == null)
            return "N/A";

        if (executionTime < 1000) {
            return executionTime + "ms";
        } else if (executionTime < 60000) {
            return String.format("%.1fs", executionTime / 1000.0);
        } else {
            long minutes = executionTime / 60000;
            long seconds = (executionTime % 60000) / 1000;
            return String.format("%dm %ds", minutes, seconds);
        }
    }

    /**
     * Lấy thông tin tổng quan
     */
    public String getSummary() {
        if (!success) {
            return String.format("FAILED: %s (Time: %s)",
                    errorMessage, getFormattedExecutionTime());
        }

        return String.format(
                "SUCCESS: Fitness=%.3f, Conflicts=%d, Completion=%.1f%%, Generations=%d, Time=%s",
                bestFitness != null ? bestFitness : 0.0,
                totalConflicts != null ? totalConflicts : 0,
                getCompletionRate(),
                generations != null ? generations : 0,
                getFormattedExecutionTime());
    }

    @Override
    public String toString() {
        return "GeneticAlgorithmResult{" +
                "success=" + success +
                ", bestFitness=" + bestFitness +
                ", generations=" + generations +
                ", executionTime=" + executionTime +
                ", conflicts=" + totalConflicts +
                ", consecutiveViolations=" + consecutiveViolations +
                ", completion=" + getCompletionRate() + "%" +
                '}';
    }
}
