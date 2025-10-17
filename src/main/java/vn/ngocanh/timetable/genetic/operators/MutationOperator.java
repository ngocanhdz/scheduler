package vn.ngocanh.timetable.genetic.operators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ngocanh.timetable.domain.Period;
import vn.ngocanh.timetable.genetic.config.GeneticAlgorithmConfig;
import vn.ngocanh.timetable.genetic.model.TimetableChromosome;
import vn.ngocanh.timetable.genetic.model.TimetableGene;
import vn.ngocanh.timetable.service.ClassService;
import vn.ngocanh.timetable.service.PeriodService;

import java.util.*;

/**
 * Toán tử đột biến (Mutation) cho thuật toán di truyền
 * Thay đổi ngẫu nhiên một số gene để tạo đa dạng và tránh cực trị cục bộ
 */
@Component
public class MutationOperator {

    @Autowired
    private PeriodService periodService;

    @Autowired
    private ClassService classService;

    private Random random = new Random();

    /**
     * Thực hiện đột biến cho một chromosome
     */
    public void mutate(TimetableChromosome chromosome, GeneticAlgorithmConfig config) {
        if (chromosome.isEmpty())
            return;

        List<TimetableGene> genes = chromosome.getGenes();

        for (int i = 0; i < genes.size(); i++) {
            if (random.nextDouble() < config.getMutationRate()) {
                // Thực hiện đột biến cho gene này
                mutateGene(chromosome, i);
            }
        }

        // Reset fitness vì chromosome đã thay đổi
        chromosome.setFitness(null);
    }

    /**
     * Đột biến một gene cụ thể
     */
    private void mutateGene(TimetableChromosome chromosome, int geneIndex) {
        TimetableGene gene = chromosome.getGene(geneIndex);
        if (gene == null || !gene.isValid())
            return;

        // Chọn loại đột biến ngẫu nhiên
        int mutationType = random.nextInt(3);

        switch (mutationType) {
            case 0:
                mutateTimeSlot(chromosome, geneIndex);
                break;
            case 1:
                mutateClassroom(chromosome, geneIndex);
                break;
            case 2:
                mutateWholeClass(chromosome, gene);
                break;
        }
    }

    /**
     * Đột biến thời gian (period) - di chuyển cả lớp học sang thời gian khác
     */
    private void mutateTimeSlot(TimetableChromosome chromosome, int geneIndex) {
        TimetableGene gene = chromosome.getGene(geneIndex);

        // Lấy tất cả gene của cùng lớp học
        String classKey = gene.getClassKey();
        List<TimetableGene> classGenes = getGenesByClassKey(chromosome, classKey);

        if (classGenes.isEmpty())
            return;

        // Tìm dãy kíp liên tiếp mới
        List<Period> availablePeriods = periodService.getAllPeriod();
        List<Period> newConsecutivePeriods = findAvailableConsecutivePeriods(
                availablePeriods, classGenes.size(), chromosome, gene.getClassId(), classKey);

        if (newConsecutivePeriods.size() == classGenes.size()) {
            // Cập nhật period cho tất cả gene của lớp học
            for (int i = 0; i < classGenes.size(); i++) {
                TimetableGene classGene = classGenes.get(i);
                Period newPeriod = newConsecutivePeriods.get(i);
                classGene.setPeriodId(newPeriod.getId());
            }
        }
    }

    /**
     * Đột biến phòng học - chuyển cả lớp học sang phòng khác
     */
    private void mutateClassroom(TimetableChromosome chromosome, int geneIndex) {
        TimetableGene gene = chromosome.getGene(geneIndex);

        // Lấy tất cả gene của cùng lớp học
        String classKey = gene.getClassKey();
        List<TimetableGene> classGenes = getGenesByClassKey(chromosome, classKey);

        if (classGenes.isEmpty())
            return;

        // Chọn phòng học mới
        List<vn.ngocanh.timetable.domain.Class> availableClasses = classService.getAllClass();
        vn.ngocanh.timetable.domain.Class newClass = selectRandomClass(availableClasses, gene.getClassId());

        if (newClass != null) {
            // Kiểm tra xung đột với phòng học mới
            boolean hasConflict = false;
            for (TimetableGene classGene : classGenes) {
                if (hasTimeSlotConflict(chromosome, classGene.getPeriodId(), newClass.getId(), classKey)) {
                    hasConflict = true;
                    break;
                }
            }

            // Nếu không có xung đột, cập nhật phòng học
            if (!hasConflict) {
                for (TimetableGene classGene : classGenes) {
                    classGene.setClassId(newClass.getId());
                }
            }
        }
    }

    /**
     * Đột biến toàn bộ lớp học - tạo lại hoàn toàn
     */
    private void mutateWholeClass(TimetableChromosome chromosome, TimetableGene sampleGene) {
        String classKey = sampleGene.getClassKey();
        List<TimetableGene> classGenes = getGenesByClassKey(chromosome, classKey);

        if (classGenes.isEmpty())
            return;

        // Xóa tất cả gene của lớp học cũ
        chromosome.getGenes().removeAll(classGenes);

        // Tạo lại lớp học mới
        List<Period> availablePeriods = periodService.getAllPeriod();
        List<vn.ngocanh.timetable.domain.Class> availableClasses = classService.getAllClass();

        // Chọn phòng học mới
        vn.ngocanh.timetable.domain.Class newClass = availableClasses.get(
                random.nextInt(availableClasses.size()));

        // Tìm dãy kíp liên tiếp
        List<Period> consecutivePeriods = findAvailableConsecutivePeriods(
                availablePeriods, classGenes.size(), chromosome, newClass.getId(), classKey);

        if (consecutivePeriods.size() == classGenes.size()) {
            // Tạo gene mới
            for (int i = 0; i < classGenes.size(); i++) {
                TimetableGene newGene = new TimetableGene(
                        sampleGene.getCourseId(),
                        consecutivePeriods.get(i).getId(),
                        newClass.getId(),
                        sampleGene.getClassIndex(),
                        i + 1);
                chromosome.addGene(newGene);
            }
        } else {
            // Nếu không tìm được, khôi phục gene cũ
            for (TimetableGene oldGene : classGenes) {
                chromosome.addGene(oldGene);
            }
        }
    }

    /**
     * Đột biến swap - hoán đổi 2 lớp học
     */
    public void swapMutation(TimetableChromosome chromosome) {
        List<TimetableGene> genes = chromosome.getGenes();
        if (genes.size() < 2)
            return;

        // Nhóm gene theo lớp học
        Map<String, List<TimetableGene>> classGroups = new HashMap<>();
        for (TimetableGene gene : genes) {
            if (gene.isValid()) {
                String classKey = gene.getClassKey();
                classGroups.computeIfAbsent(classKey, k -> new ArrayList<>()).add(gene);
            }
        }

        List<String> classKeys = new ArrayList<>(classGroups.keySet());
        if (classKeys.size() < 2)
            return;

        // Chọn 2 lớp học ngẫu nhiên
        String classKey1 = classKeys.get(random.nextInt(classKeys.size()));
        String classKey2 = classKeys.get(random.nextInt(classKeys.size()));

        if (!classKey1.equals(classKey2)) {
            List<TimetableGene> class1Genes = classGroups.get(classKey1);
            List<TimetableGene> class2Genes = classGroups.get(classKey2);

            // Hoán đổi thời gian và phòng học
            swapClassSchedules(class1Genes, class2Genes);
        }

        chromosome.setFitness(null);
    }

    /**
     * Đột biến cải thiện cục bộ
     */
    public void localImprovementMutation(TimetableChromosome chromosome) {
        // Tìm và sửa các xung đột thời gian
        List<TimetableGene> conflictingGenes = chromosome.getConflictingGenes();

        for (TimetableGene gene : conflictingGenes) {
            // Thử di chuyển lớp học này sang thời gian khác
            mutateTimeSlot(chromosome, chromosome.getGenes().indexOf(gene));
        }

        chromosome.setFitness(null);
    }

    // Helper methods

    private List<TimetableGene> getGenesByClassKey(TimetableChromosome chromosome, String classKey) {
        return chromosome.getGenes().stream()
                .filter(gene -> gene.isValid() && classKey.equals(gene.getClassKey()))
                .sorted(Comparator.comparing(TimetableGene::getSessionIndex))
                .toList();
    }

    private List<Period> findAvailableConsecutivePeriods(
            List<Period> availablePeriods,
            int sessionCount,
            TimetableChromosome chromosome,
            Long classId,
            String excludeClassKey) {

        // Lấy danh sách time slot đã sử dụng (trừ lớp học hiện tại)
        Set<String> usedTimeSlots = new HashSet<>();
        for (TimetableGene gene : chromosome.getGenes()) {
            if (gene.isValid() && !excludeClassKey.equals(gene.getClassKey())) {
                usedTimeSlots.add(gene.getTimeSlotKey());
            }
        }

        // Sắp xếp period theo number
        List<Period> sortedPeriods = availablePeriods.stream()
                .sorted(Comparator.comparing(Period::getNumber))
                .toList();

        // Tìm dãy liên tiếp
        for (int i = 0; i <= sortedPeriods.size() - sessionCount; i++) {
            List<Period> candidate = new ArrayList<>();
            boolean isValid = true;

            for (int j = 0; j < sessionCount; j++) {
                Period period = sortedPeriods.get(i + j);

                // Kiểm tra liên tiếp
                if (j > 0) {
                    Period prevPeriod = sortedPeriods.get(i + j - 1);
                    if (period.getNumber() != prevPeriod.getNumber() + 1) {
                        isValid = false;
                        break;
                    }
                }

                // Kiểm tra xung đột
                String timeSlotKey = period.getId() + "_" + classId;
                if (usedTimeSlots.contains(timeSlotKey)) {
                    isValid = false;
                    break;
                }

                candidate.add(period);
            }

            if (isValid) {
                return candidate;
            }
        }

        return new ArrayList<>();
    }

    private vn.ngocanh.timetable.domain.Class selectRandomClass(
            List<vn.ngocanh.timetable.domain.Class> availableClasses, Long excludeClassId) {
        List<vn.ngocanh.timetable.domain.Class> candidates = availableClasses.stream()
                .filter(c -> c.getId() != excludeClassId.longValue())
                .toList();

        return candidates.isEmpty() ? null : candidates.get(random.nextInt(candidates.size()));
    }

    private boolean hasTimeSlotConflict(TimetableChromosome chromosome, Long periodId,
            Long classId, String excludeClassKey) {
        String timeSlotKey = periodId + "_" + classId;

        return chromosome.getGenes().stream()
                .anyMatch(gene -> gene.isValid()
                        && !excludeClassKey.equals(gene.getClassKey())
                        && timeSlotKey.equals(gene.getTimeSlotKey()));
    }

    private void swapClassSchedules(List<TimetableGene> class1Genes, List<TimetableGene> class2Genes) {
        if (class1Genes.size() != class2Genes.size())
            return;

        // Lưu thông tin thời gian và phòng học của lớp 1
        List<Long> class1Periods = new ArrayList<>();
        List<Long> class1Classes = new ArrayList<>();

        for (TimetableGene gene : class1Genes) {
            class1Periods.add(gene.getPeriodId());
            class1Classes.add(gene.getClassId());
        }

        // Cập nhật lớp 1 với thông tin của lớp 2
        for (int i = 0; i < class1Genes.size(); i++) {
            TimetableGene gene1 = class1Genes.get(i);
            TimetableGene gene2 = class2Genes.get(i);

            gene1.setPeriodId(gene2.getPeriodId());
            gene1.setClassId(gene2.getClassId());
        }

        // Cập nhật lớp 2 với thông tin cũ của lớp 1
        for (int i = 0; i < class2Genes.size(); i++) {
            TimetableGene gene2 = class2Genes.get(i);
            gene2.setPeriodId(class1Periods.get(i));
            gene2.setClassId(class1Classes.get(i));
        }
    }

    /**
     * Thực hiện đột biến cho toàn bộ quần thể
     */
    public void mutatePopulation(List<TimetableChromosome> population, GeneticAlgorithmConfig config) {
        for (TimetableChromosome chromosome : population) {
            mutate(chromosome, config);
        }
    }
}
