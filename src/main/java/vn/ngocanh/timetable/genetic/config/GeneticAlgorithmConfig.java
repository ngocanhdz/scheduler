package vn.ngocanh.timetable.genetic.config;

/**
 * Cấu hình cho Genetic Algorithm
 * Chứa tất cả các tham số điều chỉnh thuật toán
 */
public class GeneticAlgorithmConfig {

    // Kích thước quần thể (số lượng chromosome trong mỗi thế hệ)
    private int populationSize = 100;

    // Số thế hệ tối đa
    private int maxGenerations = 1000;

    // Tỷ lệ crossover (lai ghép)
    private double crossoverRate = 0.8;

    // Tỷ lệ mutation (đột biến)
    private double mutationRate = 0.1;

    // Tỷ lệ elite (giữ lại những cá thể tốt nhất)
    private double eliteRate = 0.1;

    // Số thế hệ không cải thiện thì dừng (early stopping)
    private int stagnationLimit = 100;

    // Fitness threshold - ngưỡng dừng khi đạt được fitness tốt
    private double targetFitness = 0.95;

    // Thời gian tối đa chạy algorithm (milliseconds)
    private long maxExecutionTime = 300000; // 5 phút

    // Có in log quá trình không
    private boolean enableLogging = true;

    // Khoảng cách giữa các lần in log (theo generation)
    private int logInterval = 50;

    // Trọng số cho các yếu tố fitness
    private FitnessWeights fitnessWeights = new FitnessWeights();

    // Constructor mặc định
    public GeneticAlgorithmConfig() {
    }

    // Constructor với các tham số cơ bản
    public GeneticAlgorithmConfig(int populationSize, int maxGenerations,
            double crossoverRate, double mutationRate) {
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    // Getters và Setters
    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = Math.max(10, populationSize);
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = Math.max(1, maxGenerations);
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = Math.max(0.0, Math.min(1.0, crossoverRate));
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = Math.max(0.0, Math.min(1.0, mutationRate));
    }

    public double getEliteRate() {
        return eliteRate;
    }

    public void setEliteRate(double eliteRate) {
        this.eliteRate = Math.max(0.0, Math.min(0.5, eliteRate));
    }

    public int getStagnationLimit() {
        return stagnationLimit;
    }

    public void setStagnationLimit(int stagnationLimit) {
        this.stagnationLimit = Math.max(10, stagnationLimit);
    }

    public double getTargetFitness() {
        return targetFitness;
    }

    public void setTargetFitness(double targetFitness) {
        this.targetFitness = Math.max(0.0, Math.min(1.0, targetFitness));
    }

    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(long maxExecutionTime) {
        this.maxExecutionTime = Math.max(10000, maxExecutionTime);
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public int getLogInterval() {
        return logInterval;
    }

    public void setLogInterval(int logInterval) {
        this.logInterval = Math.max(1, logInterval);
    }

    public FitnessWeights getFitnessWeights() {
        return fitnessWeights;
    }

    public void setFitnessWeights(FitnessWeights fitnessWeights) {
        this.fitnessWeights = fitnessWeights;
    }

    /**
     * Tạo config mặc định cho môi trường development (nhanh)
     */
    public static GeneticAlgorithmConfig createDevelopmentConfig() {
        GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();
        config.setPopulationSize(50);
        config.setMaxGenerations(200);
        config.setMaxExecutionTime(60000); // 1 phút
        config.setStagnationLimit(50);
        return config;
    }

    /**
     * Tạo config cho môi trường production (chất lượng cao)
     */
    public static GeneticAlgorithmConfig createProductionConfig() {
        GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();
        config.setPopulationSize(200);
        config.setMaxGenerations(2000);
        config.setMaxExecutionTime(600000); // 10 phút
        config.setStagnationLimit(200);
        config.setTargetFitness(0.98);
        return config;
    }

    /**
     * Tạo config nhanh để test
     */
    public static GeneticAlgorithmConfig createQuickTestConfig() {
        GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();
        config.setPopulationSize(20);
        config.setMaxGenerations(50);
        config.setMaxExecutionTime(10000); // 10 giây
        config.setStagnationLimit(20);
        config.setEnableLogging(false);
        return config;
    }

    @Override
    public String toString() {
        return "GeneticAlgorithmConfig{" +
                "populationSize=" + populationSize +
                ", maxGenerations=" + maxGenerations +
                ", crossoverRate=" + crossoverRate +
                ", mutationRate=" + mutationRate +
                ", eliteRate=" + eliteRate +
                ", stagnationLimit=" + stagnationLimit +
                ", targetFitness=" + targetFitness +
                ", maxExecutionTime=" + maxExecutionTime +
                '}';
    }

    /**
     * Class con để định nghĩa trọng số cho các yếu tố fitness
     */
    public static class FitnessWeights {
        private double timeConflictWeight = -10.0; // Phạt xung đột thời gian (càng nhiều càng tệ)
        private double completenessWeight = 5.0; // Thưởng cho việc xếp đủ môn học
        private double classUtilizationWeight = 2.0; // Thưởng cho việc sử dụng phòng học hiệu quả
        private double distributionWeight = 1.0; // Thưởng cho việc phân bố đều các môn

        public double getTimeConflictWeight() {
            return timeConflictWeight;
        }

        public void setTimeConflictWeight(double timeConflictWeight) {
            this.timeConflictWeight = timeConflictWeight;
        }

        public double getCompletenessWeight() {
            return completenessWeight;
        }

        public void setCompletenessWeight(double completenessWeight) {
            this.completenessWeight = completenessWeight;
        }

        public double getClassUtilizationWeight() {
            return classUtilizationWeight;
        }

        public void setClassUtilizationWeight(double classUtilizationWeight) {
            this.classUtilizationWeight = classUtilizationWeight;
        }

        public double getDistributionWeight() {
            return distributionWeight;
        }

        public void setDistributionWeight(double distributionWeight) {
            this.distributionWeight = distributionWeight;
        }
    }
}
