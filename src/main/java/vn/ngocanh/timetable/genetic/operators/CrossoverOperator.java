package vn.ngocanh.timetable.genetic.operators;

import org.springframework.stereotype.Component;
import vn.ngocanh.timetable.genetic.config.GeneticAlgorithmConfig;
import vn.ngocanh.timetable.genetic.model.TimetableChromosome;
import vn.ngocanh.timetable.genetic.model.TimetableGene;

import java.util.*;

/**
 * Toán tử lai ghép (Crossover) cho thuật toán di truyền
 * Kết hợp 2 chromosome cha mẹ để tạo ra chromosome con
 */
@Component
public class CrossoverOperator {

    private Random random = new Random();

    /**
     * Lai ghép 2 chromosome để tạo ra 2 chromosome con
     */
    public List<TimetableChromosome> crossover(TimetableChromosome parent1,
            TimetableChromosome parent2,
            GeneticAlgorithmConfig config) {

        List<TimetableChromosome> offspring = new ArrayList<>();

        if (random.nextDouble() < config.getCrossoverRate()) {
            // Thực hiện crossover
            List<TimetableChromosome> children = performCrossover(parent1, parent2);
            offspring.addAll(children);
        } else {
            // Không crossover, copy trực tiếp cha mẹ
            offspring.add(parent1.copy());
            offspring.add(parent2.copy());
        }

        return offspring;
    }

    /**
     * Thực hiện crossover sử dụng Class-based Uniform Crossover
     * Lai ghép theo từng lớp học để đảm bảo tính toàn vẹn của các kíp liên tiếp
     */
    private List<TimetableChromosome> performCrossover(TimetableChromosome parent1,
            TimetableChromosome parent2) {

        // Nhóm gene theo lớp học (courseId + classIndex)
        Map<String, List<TimetableGene>> parent1Classes = groupGenesByClass(parent1);
        Map<String, List<TimetableGene>> parent2Classes = groupGenesByClass(parent2);

        // Tạo 2 chromosome con
        TimetableChromosome child1 = new TimetableChromosome();
        TimetableChromosome child2 = new TimetableChromosome();

        // Lấy tất cả các class key từ cả 2 cha mẹ
        Set<String> allClassKeys = new HashSet<>();
        allClassKeys.addAll(parent1Classes.keySet());
        allClassKeys.addAll(parent2Classes.keySet());

        // Lai ghép theo từng lớp học
        for (String classKey : allClassKeys) {
            List<TimetableGene> class1Genes = parent1Classes.get(classKey);
            List<TimetableGene> class2Genes = parent2Classes.get(classKey);

            if (class1Genes != null && class2Genes != null) {
                // Cả 2 cha mẹ đều có lớp học này
                if (random.nextBoolean()) {
                    // Child1 nhận từ parent1, child2 nhận từ parent2
                    addClassToChromosome(child1, class1Genes);
                    addClassToChromosome(child2, class2Genes);
                } else {
                    // Child1 nhận từ parent2, child2 nhận từ parent1
                    addClassToChromosome(child1, class2Genes);
                    addClassToChromosome(child2, class1Genes);
                }
            } else if (class1Genes != null) {
                // Chỉ parent1 có lớp học này
                if (random.nextBoolean()) {
                    addClassToChromosome(child1, class1Genes);
                } else {
                    addClassToChromosome(child2, class1Genes);
                }
            } else if (class2Genes != null) {
                // Chỉ parent2 có lớp học này
                if (random.nextBoolean()) {
                    addClassToChromosome(child1, class2Genes);
                } else {
                    addClassToChromosome(child2, class2Genes);
                }
            }
        }

        return Arrays.asList(child1, child2);
    }

    /**
     * Thực hiện Single Point Crossover (phương án thay thế)
     */
    private List<TimetableChromosome> singlePointCrossover(TimetableChromosome parent1,
            TimetableChromosome parent2) {

        List<TimetableGene> genes1 = new ArrayList<>(parent1.getGenes());
        List<TimetableGene> genes2 = new ArrayList<>(parent2.getGenes());

        if (genes1.isEmpty() || genes2.isEmpty()) {
            return Arrays.asList(parent1.copy(), parent2.copy());
        }

        // Chọn điểm cắt ngẫu nhiên
        int minSize = Math.min(genes1.size(), genes2.size());
        int crossoverPoint = random.nextInt(minSize);

        // Tạo chromosome con
        List<TimetableGene> child1Genes = new ArrayList<>();
        List<TimetableGene> child2Genes = new ArrayList<>();

        // Phần đầu
        child1Genes.addAll(genes1.subList(0, crossoverPoint));
        child2Genes.addAll(genes2.subList(0, crossoverPoint));

        // Phần sau
        child1Genes.addAll(genes2.subList(crossoverPoint, genes2.size()));
        child2Genes.addAll(genes1.subList(crossoverPoint, genes1.size()));

        return Arrays.asList(
                new TimetableChromosome(child1Genes),
                new TimetableChromosome(child2Genes));
    }

    /**
     * Thực hiện Order Crossover (OX) - Bảo toàn thứ tự
     */
    private List<TimetableChromosome> orderCrossover(TimetableChromosome parent1,
            TimetableChromosome parent2) {

        List<TimetableGene> genes1 = new ArrayList<>(parent1.getGenes());
        List<TimetableGene> genes2 = new ArrayList<>(parent2.getGenes());

        int size = Math.min(genes1.size(), genes2.size());
        if (size <= 2) {
            return Arrays.asList(parent1.copy(), parent2.copy());
        }

        // Chọn 2 điểm cắt
        int point1 = random.nextInt(size - 1);
        int point2 = point1 + 1 + random.nextInt(size - point1 - 1);

        // Tạo con thứ nhất
        TimetableGene[] child1Array = new TimetableGene[size];
        TimetableGene[] child2Array = new TimetableGene[size];

        // Copy đoạn giữa từ cha mẹ
        for (int i = point1; i <= point2; i++) {
            child1Array[i] = genes1.get(i).copy();
            child2Array[i] = genes2.get(i).copy();
        }

        // Điền phần còn lại theo thứ tự từ cha mẹ khác
        fillRemainingGenes(child1Array, genes2, point1, point2);
        fillRemainingGenes(child2Array, genes1, point1, point2);

        return Arrays.asList(
                new TimetableChromosome(Arrays.asList(child1Array)),
                new TimetableChromosome(Arrays.asList(child2Array)));
    }

    /**
     * Nhóm gene theo lớp học (courseId + classIndex)
     */
    private Map<String, List<TimetableGene>> groupGenesByClass(TimetableChromosome chromosome) {
        Map<String, List<TimetableGene>> classGroups = new HashMap<>();

        for (TimetableGene gene : chromosome.getGenes()) {
            if (gene.isValid()) {
                String classKey = gene.getClassKey();
                classGroups.computeIfAbsent(classKey, k -> new ArrayList<>()).add(gene.copy());
            }
        }

        // Sắp xếp gene theo sessionIndex trong mỗi lớp
        for (List<TimetableGene> genes : classGroups.values()) {
            genes.sort(Comparator.comparing(TimetableGene::getSessionIndex));
        }

        return classGroups;
    }

    /**
     * Thêm các gene của một lớp học vào chromosome
     */
    private void addClassToChromosome(TimetableChromosome chromosome, List<TimetableGene> classGenes) {
        for (TimetableGene gene : classGenes) {
            chromosome.addGene(gene.copy());
        }
    }

    /**
     * Điền các gene còn lại cho Order Crossover
     */
    private void fillRemainingGenes(TimetableGene[] childArray, List<TimetableGene> parentGenes,
            int point1, int point2) {
        int childIndex = 0;
        int parentIndex = 0;

        while (childIndex < childArray.length) {
            if (childIndex >= point1 && childIndex <= point2) {
                // Bỏ qua đoạn đã được copy
                childIndex++;
                continue;
            }

            if (parentIndex < parentGenes.size()) {
                TimetableGene parentGene = parentGenes.get(parentIndex);

                // Kiểm tra xem gene này đã tồn tại trong đoạn giữa chưa
                boolean exists = false;
                for (int i = point1; i <= point2; i++) {
                    if (childArray[i] != null && isSimilarGene(childArray[i], parentGene)) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    childArray[childIndex] = parentGene.copy();
                    childIndex++;
                }
                parentIndex++;
            } else {
                // Hết gene cha mẹ, tạo gene mới hoặc bỏ qua
                childIndex++;
            }
        }
    }

    /**
     * Kiểm tra xem 2 gene có tương tự nhau không (cùng courseId và classIndex)
     */
    private boolean isSimilarGene(TimetableGene gene1, TimetableGene gene2) {
        return Objects.equals(gene1.getCourseId(), gene2.getCourseId()) &&
                Objects.equals(gene1.getClassIndex(), gene2.getClassIndex());
    }

    /**
     * Lai ghép nhiều cặp chromosome
     */
    public List<TimetableChromosome> crossoverPopulation(List<TimetableChromosome> parents,
            GeneticAlgorithmConfig config) {

        List<TimetableChromosome> offspring = new ArrayList<>();

        // Lai ghép từng cặp
        for (int i = 0; i < parents.size() - 1; i += 2) {
            TimetableChromosome parent1 = parents.get(i);
            TimetableChromosome parent2 = parents.get(i + 1);

            List<TimetableChromosome> children = crossover(parent1, parent2, config);
            offspring.addAll(children);
        }

        // Nếu số lượng cha mẹ lẻ, copy chromosome cuối cùng
        if (parents.size() % 2 == 1) {
            offspring.add(parents.get(parents.size() - 1).copy());
        }

        return offspring;
    }
}
