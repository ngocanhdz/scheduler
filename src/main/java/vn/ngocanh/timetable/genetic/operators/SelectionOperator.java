package vn.ngocanh.timetable.genetic.operators;

import org.springframework.stereotype.Component;
import vn.ngocanh.timetable.genetic.config.GeneticAlgorithmConfig;
import vn.ngocanh.timetable.genetic.model.TimetableChromosome;
import vn.ngocanh.timetable.genetic.model.TimetableGene;

import java.util.*;

/**
 * Toán tử chọn lọc (Selection) cho thuật toán di truyền
 * Chọn ra những chromosome tốt nhất để làm cha mẹ cho thế hệ tiếp theo
 */
@Component
public class SelectionOperator {

    private Random random = new Random();

    /**
     * Chọn lọc Tournament Selection
     * Chọn ngẫu nhiên k chromosome và lấy chromosome tốt nhất trong nhóm
     */
    public List<TimetableChromosome> tournamentSelection(
            List<TimetableChromosome> population,
            int selectionSize,
            GeneticAlgorithmConfig config) {

        List<TimetableChromosome> selectedParents = new ArrayList<>();
        int tournamentSize = Math.max(2, population.size() / 10); // 10% population size

        for (int i = 0; i < selectionSize; i++) {
            TimetableChromosome winner = runTournament(population, tournamentSize);
            selectedParents.add(winner.copy());
        }

        return selectedParents;
    }

    /**
     * Chạy một tournament để chọn chromosome tốt nhất
     */
    private TimetableChromosome runTournament(List<TimetableChromosome> population, int tournamentSize) {
        List<TimetableChromosome> tournament = new ArrayList<>();

        // Chọn ngẫu nhiên các chromosome tham gia tournament
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }

        // Trả về chromosome có fitness cao nhất
        return tournament.stream()
                .filter(c -> c.getFitness() != null)
                .max(Comparator.comparing(TimetableChromosome::getFitness))
                .orElse(tournament.get(0));
    }

    /**
     * Chọn lọc Roulette Wheel Selection (Fitness Proportionate Selection)
     */
    public List<TimetableChromosome> rouletteWheelSelection(
            List<TimetableChromosome> population,
            int selectionSize) {

        List<TimetableChromosome> selectedParents = new ArrayList<>();

        // Tính tổng fitness
        double totalFitness = population.stream()
                .filter(c -> c.getFitness() != null)
                .mapToDouble(TimetableChromosome::getFitness)
                .sum();

        if (totalFitness <= 0) {
            // Nếu tất cả fitness đều âm hoặc 0, chọn ngẫu nhiên
            return randomSelection(population, selectionSize);
        }

        // Chọn từng chromosome theo xác suất fitness
        for (int i = 0; i < selectionSize; i++) {
            double randomValue = random.nextDouble() * totalFitness;
            double cumulativeFitness = 0.0;

            for (TimetableChromosome chromosome : population) {
                if (chromosome.getFitness() != null) {
                    cumulativeFitness += chromosome.getFitness();
                    if (cumulativeFitness >= randomValue) {
                        selectedParents.add(chromosome.copy());
                        break;
                    }
                }
            }
        }

        return selectedParents;
    }

    /**
     * Chọn lọc Rank Selection
     * Sắp xếp theo fitness và chọn theo thứ hạng
     */
    public List<TimetableChromosome> rankSelection(
            List<TimetableChromosome> population,
            int selectionSize) {

        // Sắp xếp population theo fitness
        List<TimetableChromosome> sortedPopulation = population.stream()
                .filter(c -> c.getFitness() != null)
                .sorted(Comparator.comparing(TimetableChromosome::getFitness).reversed())
                .toList();

        if (sortedPopulation.isEmpty()) {
            return randomSelection(population, selectionSize);
        }

        List<TimetableChromosome> selectedParents = new ArrayList<>();

        // Tạo xác suất chọn theo rank (rank cao = xác suất cao)
        int totalRank = sortedPopulation.size() * (sortedPopulation.size() + 1) / 2;

        for (int i = 0; i < selectionSize; i++) {
            double randomValue = random.nextDouble() * totalRank;
            double cumulativeRank = 0.0;

            for (int rank = 0; rank < sortedPopulation.size(); rank++) {
                cumulativeRank += (sortedPopulation.size() - rank);
                if (cumulativeRank >= randomValue) {
                    selectedParents.add(sortedPopulation.get(rank).copy());
                    break;
                }
            }
        }

        return selectedParents;
    }

    /**
     * Chọn lọc Elite - giữ lại những chromosome tốt nhất
     */
    public List<TimetableChromosome> eliteSelection(
            List<TimetableChromosome> population,
            GeneticAlgorithmConfig config) {

        int eliteCount = (int) (population.size() * config.getEliteRate());

        return population.stream()
                .filter(c -> c.getFitness() != null)
                .sorted(Comparator.comparing(TimetableChromosome::getFitness).reversed())
                .limit(eliteCount)
                .map(TimetableChromosome::copy)
                .toList();
    }

    /**
     * Chọn lọc ngẫu nhiên
     */
    public List<TimetableChromosome> randomSelection(
            List<TimetableChromosome> population,
            int selectionSize) {

        List<TimetableChromosome> selectedParents = new ArrayList<>();

        for (int i = 0; i < selectionSize; i++) {
            int randomIndex = random.nextInt(population.size());
            selectedParents.add(population.get(randomIndex).copy());
        }

        return selectedParents;
    }

    /**
     * Chọn lọc kết hợp (Hybrid Selection)
     * Kết hợp Elite + Tournament để đảm bảo đa dạng
     */
    public List<TimetableChromosome> hybridSelection(
            List<TimetableChromosome> population,
            int selectionSize,
            GeneticAlgorithmConfig config) {

        List<TimetableChromosome> selectedParents = new ArrayList<>();

        // 1. Chọn Elite (30%)
        int eliteCount = Math.max(1, selectionSize * 30 / 100);
        List<TimetableChromosome> elites = eliteSelection(population, config);
        selectedParents.addAll(elites.subList(0, Math.min(eliteCount, elites.size())));

        // 2. Chọn Tournament cho phần còn lại (70%)
        int tournamentCount = selectionSize - selectedParents.size();
        if (tournamentCount > 0) {
            List<TimetableChromosome> tournamentSelected = tournamentSelection(
                    population, tournamentCount, config);
            selectedParents.addAll(tournamentSelected);
        }

        return selectedParents;
    }

    /**
     * Chọn lọc chính cho thuật toán
     */
    public List<TimetableChromosome> selectParents(
            List<TimetableChromosome> population,
            GeneticAlgorithmConfig config) {

        int selectionSize = population.size(); // Chọn cùng số lượng với population

        // Sử dụng Hybrid Selection làm phương pháp chính
        return hybridSelection(population, selectionSize, config);
    }

    /**
     * Thay thế thế hệ cũ bằng thế hệ mới
     */
    public List<TimetableChromosome> environmentalSelection(
            List<TimetableChromosome> parents,
            List<TimetableChromosome> offspring,
            GeneticAlgorithmConfig config) {

        // Kết hợp cha mẹ và con cái
        List<TimetableChromosome> combined = new ArrayList<>();
        combined.addAll(parents);
        combined.addAll(offspring);

        // Sắp xếp theo fitness
        combined.sort(Comparator.comparing(
                (TimetableChromosome c) -> c.getFitness() != null ? c.getFitness() : 0.0).reversed());

        // Chọn những chromosome tốt nhất
        int populationSize = config.getPopulationSize();
        List<TimetableChromosome> nextGeneration = new ArrayList<>();

        // 1. Giữ Elite
        int eliteCount = (int) (populationSize * config.getEliteRate());
        for (int i = 0; i < Math.min(eliteCount, combined.size()); i++) {
            nextGeneration.add(combined.get(i));
        }

        // 2. Điền phần còn lại
        while (nextGeneration.size() < populationSize && nextGeneration.size() < combined.size()) {
            // Chọn theo Tournament trong phần còn lại
            List<TimetableChromosome> remaining = combined.subList(
                    nextGeneration.size(), combined.size());

            if (!remaining.isEmpty()) {
                TimetableChromosome selected = runTournament(remaining,
                        Math.max(2, remaining.size() / 5));
                nextGeneration.add(selected);
                combined.remove(selected);
            }
        }

        return nextGeneration;
    }

    /**
     * Thống kê về diversity của population
     */
    public double calculatePopulationDiversity(List<TimetableChromosome> population) {
        if (population.size() < 2)
            return 0.0;

        double totalDistance = 0.0;
        int comparisons = 0;

        for (int i = 0; i < population.size(); i++) {
            for (int j = i + 1; j < population.size(); j++) {
                double distance = calculateChromosomeDistance(population.get(i), population.get(j));
                totalDistance += distance;
                comparisons++;
            }
        }

        return comparisons > 0 ? totalDistance / comparisons : 0.0;
    }

    /**
     * Tính khoảng cách giữa 2 chromosome
     */
    private double calculateChromosomeDistance(TimetableChromosome c1, TimetableChromosome c2) {
        Set<String> c1TimeSlots = c1.getGenes().stream()
                .filter(g -> g.isValid())
                .map(TimetableGene::getTimeSlotKey)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        Set<String> c2TimeSlots = c2.getGenes().stream()
                .filter(g -> g.isValid())
                .map(TimetableGene::getTimeSlotKey)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        // Tính Jaccard Distance
        Set<String> intersection = new HashSet<>(c1TimeSlots);
        intersection.retainAll(c2TimeSlots);

        Set<String> union = new HashSet<>(c1TimeSlots);
        union.addAll(c2TimeSlots);

        if (union.isEmpty())
            return 0.0;

        return 1.0 - (double) intersection.size() / union.size();
    }
}
