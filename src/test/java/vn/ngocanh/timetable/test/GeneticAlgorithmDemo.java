package vn.ngocanh.timetable.test;

import vn.ngocanh.timetable.genetic.model.TimetableGene;
import vn.ngocanh.timetable.genetic.model.TimetableChromosome;
import vn.ngocanh.timetable.genetic.config.GeneticAlgorithmConfig;
import vn.ngocanh.timetable.genetic.service.GeneticAlgorithmResult;

import java.util.List;
import java.util.ArrayList;

/**
 * Demo class để test logic Genetic Algorithm
 */
public class GeneticAlgorithmDemo {

    public static void main(String[] args) {
        System.out.println("=== DEMO GENETIC ALGORITHM FOR TIMETABLING ===");

        // Tạo config test
        GeneticAlgorithmConfig config = GeneticAlgorithmConfig.createQuickTestConfig();
        System.out.println("Config: " + config.getPopulationSize() + " population, " +
                config.getMaxGenerations() + " generations");

        // Tạo một số gene mẫu
        List<TimetableGene> sampleGenes = new ArrayList<>();

        // Môn Java - 3 tiết liên tiếp
        sampleGenes.add(new TimetableGene(1L, 1L, 101L, 1, 1)); // Lớp 1, kíp 1
        sampleGenes.add(new TimetableGene(1L, 2L, 101L, 1, 2)); // Lớp 1, kíp 2
        sampleGenes.add(new TimetableGene(1L, 3L, 101L, 1, 3)); // Lớp 1, kíp 3

        // Môn Database - 2 tiết liên tiếp
        sampleGenes.add(new TimetableGene(2L, 5L, 102L, 1, 1)); // Lớp 1, kíp 1
        sampleGenes.add(new TimetableGene(2L, 6L, 102L, 1, 2)); // Lớp 1, kíp 2

        // Tạo chromosome
        TimetableChromosome chromosome = new TimetableChromosome(sampleGenes);

        System.out.println("\\nSample Chromosome:");
        System.out.println("- Total genes: " + chromosome.getGenes().size());
        System.out.println("- Course IDs: " + chromosome.getCourseIds());
        System.out.println("- Is valid: " + chromosome.isValid());
        System.out.println("- Time conflicts: " + chromosome.countTimeConflicts());
        System.out.println("- Consecutive violations: " + chromosome.countConsecutiveSessionViolations());

        // Test fitness function
        double fitness = chromosome.getFitness();
        System.out.println("- Fitness score: " + fitness);

        // Tạo result mẫu
        GeneticAlgorithmResult result = new GeneticAlgorithmResult();
        result.setSuccess(true);
        result.setBestChromosome(chromosome);
        result.setBestFitness(fitness);
        result.setGenerations(50);
        result.setExecutionTime(2500L);
        result.setRequiredCourses(2);
        result.setScheduledCourses(2);

        System.out.println("\\nGA Result:");
        System.out.println("- Success: " + result.isSuccessful());
        System.out.println("- Completion: " + result.getCompleteness() + "%");
        System.out.println("- Summary: " + result.getSummary());

        System.out.println("\\n=== DEMO COMPLETED ===");
    }
}
