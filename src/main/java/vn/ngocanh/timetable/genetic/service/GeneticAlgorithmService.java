package vn.ngocanh.timetable.genetic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngocanh.timetable.genetic.config.GeneticAlgorithmConfig;
import vn.ngocanh.timetable.genetic.fitness.TimetableFitnessFunction;
import vn.ngocanh.timetable.genetic.generator.PopulationGenerator;
import vn.ngocanh.timetable.genetic.model.TimetableChromosome;
import vn.ngocanh.timetable.genetic.operators.CrossoverOperator;
import vn.ngocanh.timetable.genetic.operators.MutationOperator;
import vn.ngocanh.timetable.genetic.operators.SelectionOperator;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;

/**
 * Service chính cho Genetic Algorithm
 * Điều phối toàn bộ quá trình tối ưu hóa thời khóa biểu
 */
@Service
public class GeneticAlgorithmService {

    private static final Logger logger = LoggerFactory.getLogger(GeneticAlgorithmService.class);
    @Autowired
    private PopulationGenerator populationGenerator;

    @Autowired
    private TimetableFitnessFunction fitnessFunction;

    @Autowired
    private SelectionOperator selectionOperator;

    @Autowired
    private CrossoverOperator crossoverOperator;

    @Autowired
    private MutationOperator mutationOperator;

    @Autowired
    private vn.ngocanh.timetable.repository.TimetableRepository timetableRepository;

    /**
     * Chạy thuật toán di truyền để tối ưu hóa thời khóa biểu
     */
    public GeneticAlgorithmResult optimizeTimetable(
            List<Long> courseIds,
            Long semesterConfigId,
            GeneticAlgorithmConfig config) {

        logger.info("Starting Genetic Algorithm with {} courses, population size: {}, max generations: {}",
                courseIds.size(), config.getPopulationSize(), config.getMaxGenerations());

        long startTime = System.currentTimeMillis();
        try {
            // 1. Lấy existing time slots từ database
            Set<String> existingTimeSlots = getExistingTimeSlots(semesterConfigId);
            logger.info("Found {} existing time slots", existingTimeSlots.size());

            // 2. Tạo quần thể ban đầu
            List<TimetableChromosome> population = populationGenerator.generateInitialPopulation(
                    courseIds, semesterConfigId, config, existingTimeSlots);

            if (population.isEmpty()) {
                return createFailureResult("Cannot generate initial population", startTime);
            }

            // 2. Đánh giá fitness ban đầu
            evaluatePopulation(population, courseIds, config);

            // 3. Thống kê ban đầu
            TimetableChromosome bestChromosome = getBestChromosome(population);
            double initialBestFitness = bestChromosome.getFitness();

            logger.info("Initial population - Best fitness: {:.4f}, Average fitness: {:.4f}",
                    initialBestFitness, getAverageFitness(population));

            // 4. Vòng lặp chính của GA
            GeneticAlgorithmResult result = runGeneticAlgorithm(
                    population, courseIds, config, startTime);

            // 5. Log kết quả cuối cùng
            logger.info("GA completed - Best fitness: {:.4f}, Generations: {}, Time: {}ms",
                    result.getBestFitness(), result.getGenerations(), result.getExecutionTime());

            return result;

        } catch (Exception e) {
            logger.error("Error during Genetic Algorithm execution", e);
            return createFailureResult("GA execution failed: " + e.getMessage(), startTime);
        }
    }

    /**
     * Vòng lặp chính của thuật toán di truyền
     */
    private GeneticAlgorithmResult runGeneticAlgorithm(
            List<TimetableChromosome> population,
            List<Long> courseIds,
            GeneticAlgorithmConfig config,
            long startTime) {

        TimetableChromosome bestOverall = getBestChromosome(population);
        int stagnationCount = 0;
        int generation = 0;

        for (generation = 1; generation <= config.getMaxGenerations(); generation++) {

            // Kiểm tra timeout
            if (System.currentTimeMillis() - startTime > config.getMaxExecutionTime()) {
                logger.info("GA stopped due to timeout at generation {}", generation);
                break;
            }

            // 1. Selection - chọn cha mẹ
            List<TimetableChromosome> parents = selectionOperator.selectParents(population, config);

            // 2. Crossover - lai ghép
            List<TimetableChromosome> offspring = crossoverOperator.crossoverPopulation(parents, config);

            // 3. Mutation - đột biến
            mutationOperator.mutatePopulation(offspring, config);

            // 4. Đánh giá fitness cho thế hệ mới
            evaluatePopulation(offspring, courseIds, config);

            // 5. Environmental Selection - chọn thế hệ tiếp theo
            population = selectionOperator.environmentalSelection(population, offspring, config);

            // 6. Kiểm tra cải thiện
            TimetableChromosome currentBest = getBestChromosome(population);
            if (currentBest.getFitness() > bestOverall.getFitness()) {
                bestOverall = currentBest.copy();
                stagnationCount = 0;

                logger.debug("New best fitness at generation {}: {:.4f}", generation, bestOverall.getFitness());
            } else {
                stagnationCount++;
            } // 7. Kiểm tra điều kiện dừng
            if (shouldStop(bestOverall, stagnationCount, courseIds, config)) {
                logger.info("GA stopped at generation {} due to convergence criteria", generation);
                break;
            } // 8. Log tiến trình
            if (config.isEnableLogging() && generation % config.getLogInterval() == 0) {
                logProgress(generation, population, bestOverall, stagnationCount, courseIds, config);
            }
        }

        return createSuccessResult(bestOverall, generation, startTime, population);
    }

    /**
     * Đánh giá fitness cho toàn bộ quần thể
     */
    private void evaluatePopulation(List<TimetableChromosome> population,
            List<Long> courseIds,
            GeneticAlgorithmConfig config) {

        for (TimetableChromosome chromosome : population) {
            if (!chromosome.isEvaluated()) {
                double fitness = fitnessFunction.calculateFitness(chromosome, courseIds, config);
                chromosome.setFitness(fitness);
            }
        }
    }

    /**
     * Kiểm tra điều kiện dừng
     */
    private boolean shouldStop(TimetableChromosome bestChromosome,
            int stagnationCount,
            List<Long> courseIds,
            GeneticAlgorithmConfig config) {

        // Dừng nếu đạt fitness mục tiêu
        if (bestChromosome.getFitness() >= config.getTargetFitness()) {
            return true;
        }

        // Dừng nếu không cải thiện quá lâu
        if (stagnationCount >= config.getStagnationLimit()) {
            return true;
        }

        // Dừng nếu tìm được lời giải chấp nhận được
        if (fitnessFunction.isAcceptable(bestChromosome, courseIds, config)) {
            return true;
        }

        return false;
    }

    /**
     * Lấy chromosome tốt nhất trong quần thể
     */
    private TimetableChromosome getBestChromosome(List<TimetableChromosome> population) {
        return population.stream()
                .filter(c -> c.getFitness() != null)
                .max(Comparator.comparing(TimetableChromosome::getFitness))
                .orElse(population.get(0));
    }

    /**
     * Tính fitness trung bình của quần thể
     */
    private double getAverageFitness(List<TimetableChromosome> population) {
        return population.stream()
                .filter(c -> c.getFitness() != null)
                .mapToDouble(TimetableChromosome::getFitness)
                .average()
                .orElse(0.0);
    }

    /**
     * Log tiến trình
     */
    private void logProgress(int generation,
            List<TimetableChromosome> population,
            TimetableChromosome bestOverall,
            int stagnationCount,
            List<Long> courseIds,
            GeneticAlgorithmConfig config) {

        double avgFitness = getAverageFitness(population);
        double diversity = selectionOperator.calculatePopulationDiversity(population);

        logger.info("Generation {}: Best={:.4f}, Avg={:.4f}, Diversity={:.4f}, Stagnation={}",
                generation, bestOverall.getFitness(), avgFitness, diversity, stagnationCount);

        // Log chi tiết về chromosome tốt nhất
        if (generation % (config.getLogInterval() * 5) == 0) {
            TimetableFitnessFunction.FitnessDetails details = fitnessFunction.calculateDetailedFitness(bestOverall,
                    courseIds, config);
            logger.info("Best chromosome details: {}", details);
        }
    }

    /**
     * Tạo kết quả thành công
     */
    private GeneticAlgorithmResult createSuccessResult(TimetableChromosome bestChromosome,
            int generations,
            long startTime,
            List<TimetableChromosome> finalPopulation) {

        long executionTime = System.currentTimeMillis() - startTime;
        double averageFitness = getAverageFitness(finalPopulation);
        double diversity = selectionOperator.calculatePopulationDiversity(finalPopulation);

        GeneticAlgorithmResult result = new GeneticAlgorithmResult();
        result.setSuccess(true);
        result.setBestChromosome(bestChromosome);
        result.setBestFitness(bestChromosome.getFitness());
        result.setAverageFitness(averageFitness);
        result.setGenerations(generations);
        result.setExecutionTime(executionTime);
        result.setPopulationDiversity(diversity);
        result.setFinalPopulationSize(finalPopulation.size());

        return result;
    }

    /**
     * Tạo kết quả thất bại
     */
    private GeneticAlgorithmResult createFailureResult(String errorMessage, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;

        GeneticAlgorithmResult result = new GeneticAlgorithmResult();
        result.setSuccess(false);
        result.setErrorMessage(errorMessage);
        result.setExecutionTime(executionTime);

        return result;
    }

    /**
     * Chạy GA với config nhanh để test
     */
    public GeneticAlgorithmResult quickOptimize(List<Long> courseIds, Long semesterConfigId) {
        GeneticAlgorithmConfig config = GeneticAlgorithmConfig.createQuickTestConfig();
        return optimizeTimetable(courseIds, semesterConfigId, config);
    }

    /**
     * Chạy GA với config production
     */
    public GeneticAlgorithmResult productionOptimize(List<Long> courseIds, Long semesterConfigId) {
        GeneticAlgorithmConfig config = GeneticAlgorithmConfig.createProductionConfig();
        return optimizeTimetable(courseIds, semesterConfigId, config);
    }

    /**
     * Method chính để tạo timetable - sử dụng config production
     */
    public GeneticAlgorithmResult generateTimetable(List<Long> courseIds, Long semesterConfigId) {
        logger.info("Generating timetable for {} courses in semester {}", courseIds.size(), semesterConfigId);
        return productionOptimize(courseIds, semesterConfigId);
    }

    /**
     * Lấy danh sách các time slots đã được sử dụng trong học kỳ hiện tại
     */
    private Set<String> getExistingTimeSlots(Long semesterConfigId) {
        Set<String> existingTimeSlots = new HashSet<>();

        try {
            // Lấy tất cả timetable trong học kỳ hiện tại
            List<vn.ngocanh.timetable.domain.Timetable> existingTimetables = timetableRepository
                    .findBySemesterConfigId(semesterConfigId);

            // Tạo time slot keys từ existing timetables
            for (vn.ngocanh.timetable.domain.Timetable timetable : existingTimetables) {
                String timeSlotKey = timetable.getPeriod().getId() + "_" + timetable.getClassRoom().getId();
                existingTimeSlots.add(timeSlotKey);
            }

            logger.info("Found {} existing time slots: {}", existingTimeSlots.size(), existingTimeSlots);
        } catch (Exception e) {
            logger.error("Error getting existing time slots: {}", e.getMessage());
            // Return empty set nếu có lỗi - tốt hơn là crash
        }

        return existingTimeSlots;
    }
}
