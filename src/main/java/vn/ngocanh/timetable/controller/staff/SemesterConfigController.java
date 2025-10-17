package vn.ngocanh.timetable.controller.staff;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import vn.ngocanh.timetable.domain.SemesterConfig;
import vn.ngocanh.timetable.service.SemesterConfigService;

@Controller
public class SemesterConfigController {

    private final SemesterConfigService semesterConfigService;

    public SemesterConfigController(SemesterConfigService semesterConfigService) {
        this.semesterConfigService = semesterConfigService;
    }

    @GetMapping("/staff/semester-config")
    public String getConfigPage(Model model) {
        model.addAttribute("semesterConfig", new SemesterConfig());
        model.addAttribute("semesters", semesterConfigService.getAllDistinctSemesterNames());
        model.addAttribute("currentConfigs", semesterConfigService.getAllSemesterConfigs());
        return "staff/semester/config";
    }

    @PostMapping("/staff/semester-config/save")
    public String saveSemesterConfig(@Valid @ModelAttribute("semesterConfig") SemesterConfig semesterConfig,
            BindingResult bindingResult,
            @RequestParam("actionMode") String actionMode,
            RedirectAttributes attributes,
            Model model) {
        try {
            // Check for validation errors
            if (bindingResult.hasErrors()) {
                model.addAttribute("semesters", semesterConfigService.getAllDistinctSemesterNames());
                model.addAttribute("currentConfigs", semesterConfigService.getAllSemesterConfigs());
                model.addAttribute("error", "Vui lòng kiểm tra lại thông tin đã nhập!");
                return "staff/semester/config";
            }

            // Additional validation
            if (semesterConfig.getName() == null || semesterConfig.getName().trim().isEmpty()) {
                attributes.addFlashAttribute("error", "Tên học kỳ không được để trống!");
                return "redirect:/staff/semester-config";
            }

            if (semesterConfig.getStart() == null || semesterConfig.getEnd() == null) {
                attributes.addFlashAttribute("error", "Thời gian bắt đầu và kết thúc không được để trống!");
                return "redirect:/staff/semester-config";
            }

            if (semesterConfig.getStart().after(semesterConfig.getEnd())) {
                attributes.addFlashAttribute("error", "Thời gian bắt đầu phải trước thời gian kết thúc!");
                return "redirect:/staff/semester-config";
            }

            semesterConfigService.saveSemesterConfig(semesterConfig);
            if ("create".equals(actionMode)) {
                attributes.addFlashAttribute("message", "Tạo cấu hình học kỳ mới thành công!");
            } else {
                attributes.addFlashAttribute("message", "Lưu cấu hình thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            attributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/staff/semester-config";
    }

    @GetMapping("/staff/semester-config/delete/{id}")
    public String deleteSemesterConfig(@PathVariable("id") long id, RedirectAttributes attributes) {
        try {
            semesterConfigService.deleteSemesterConfig(id);
            attributes.addFlashAttribute("message", "Xóa cấu hình thành công!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/staff/semester-config";
    }
}
