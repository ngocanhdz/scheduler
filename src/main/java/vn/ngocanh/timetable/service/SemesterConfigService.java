package vn.ngocanh.timetable.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ngocanh.timetable.domain.SemesterConfig;
import vn.ngocanh.timetable.repository.SemesterConfigRepository;

@Service
public class SemesterConfigService {
    @Autowired
    private SemesterConfigRepository semesterConfigRepository;

    public SemesterConfig getSemesterConfigById(long id) {
        return this.semesterConfigRepository.findOneById(id);
    }

    public List<SemesterConfig> getAllSemesterConfigs() {
        return this.semesterConfigRepository.findAll();
    }

    public List<String> getAllDistinctSemesterNames() {
        return this.semesterConfigRepository.findAll()
                .stream()
                .map(SemesterConfig::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    public SemesterConfig saveSemesterConfig(SemesterConfig semesterConfig) {
        // Đảm bảo chỉ có một config active tại một thời điểm
        Date start = semesterConfig.getStart();
        Date end = semesterConfig.getEnd();
        if (this.semesterConfigRepository.findOneByName(semesterConfig.getName()) != null) {
            semesterConfig = this.semesterConfigRepository.findOneByName(semesterConfig.getName());
        }
        deactivateAllConfigs();
        semesterConfig.setState(true);
        semesterConfig.setStart(start);
        semesterConfig.setEnd(end);
        return this.semesterConfigRepository.save(semesterConfig);
    }

    public void deleteSemesterConfig(long id) {
        this.semesterConfigRepository.deleteById(id);
    }

    private void deactivateAllConfigs() {
        List<SemesterConfig> activeConfigs = semesterConfigRepository.findByState(true);
        for (SemesterConfig config : activeConfigs) {
            config.setState(false);
            semesterConfigRepository.save(config);
        }
    }

    public SemesterConfig getSemesterConfigByState(Boolean state) {
        return this.semesterConfigRepository.findOneByState(true);
    }
}
