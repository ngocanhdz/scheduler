package vn.ngocanh.timetable.controller.admin;

import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ngocanh.timetable.domain.Period;
import vn.ngocanh.timetable.domain.Day;
import vn.ngocanh.timetable.service.PeriodService;
import vn.ngocanh.timetable.service.DayService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class PeriodController {

    private final PeriodService periodService;
    private final DayService dayService;

    public PeriodController(PeriodService periodService, DayService dayService) {
        this.periodService = periodService;
        this.dayService = dayService;
    }

    @GetMapping("/admin/period")
    public String getPeriodPage(Model model) {
        List<Period> periods = this.periodService.getAllPeriod();
        model.addAttribute("periods", periods);
        return "admin/period/show";
    }

    @GetMapping("/admin/period/create")
    public String getCreatePeriodPage(Model model) {
        model.addAttribute("newPeriod", new Period());
        List<Day> days = this.dayService.getAllDay();
        model.addAttribute("days", days);
        return "admin/period/create";
    }

    @PostMapping("/admin/period/create")
    public String createPeriod(Model model, @ModelAttribute("newPeriod") @Valid Period period,
            @RequestParam("dayName") String dayName,
            BindingResult newPeriodBindingResult,
            RedirectAttributes redirectAttributes) {

        // Tìm Day bằng tên
        Day day = this.dayService.getDayByName(dayName);
        if (day == null) {
            newPeriodBindingResult.rejectValue("day", "period.day.notfound",
                    "Không tìm thấy ngày này");
        } else {
            period.setDay(day);

            // Validation cho tên period trùng trong cùng ngày
            if (this.periodService.isPeriodNameExist(period.getName(), day)) {
                newPeriodBindingResult.rejectValue("name", "period.name.exist",
                        "Tên tiết học này đã tồn tại trong ngày " + dayName);
            }
        }

        if (newPeriodBindingResult.hasErrors()) {
            List<Day> days = this.dayService.getAllDay();
            model.addAttribute("days", days);
            return "admin/period/create";
        }

        this.periodService.handleSavePeriod(period);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo tiết học thành công!");
        return "redirect:/admin/period";
    }

    @GetMapping("/admin/period/update/{id}")
    public String getUpdatePeriodPage(Model model, @PathVariable long id) {
        Period currentPeriod = this.periodService.getPeriodById(id);
        model.addAttribute("currentPeriod", currentPeriod);
        List<Day> days = this.dayService.getAllDay();
        model.addAttribute("days", days);
        return "admin/period/update";
    }

    @PostMapping("/admin/period/update")
    public String postUpdatePeriod(@ModelAttribute("currentPeriod") @Valid Period period,
            @RequestParam("dayName") String dayName,
            BindingResult currentPeriodBindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Tìm Day bằng tên
        Day day = this.dayService.getDayByName(dayName);
        if (day == null) {
            currentPeriodBindingResult.rejectValue("day", "period.day.notfound",
                    "Không tìm thấy ngày này");
        } else {
            period.setDay(day);
        }

        if (currentPeriodBindingResult.hasErrors()) {
            List<Day> days = this.dayService.getAllDay();
            model.addAttribute("days", days);
            return "admin/period/update";
        }

        this.periodService.handleSavePeriod(period);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật tiết học thành công!");
        return "redirect:/admin/period";
    }

    @GetMapping("/admin/period/delete/{id}")
    public String deletePeriod(@PathVariable long id, RedirectAttributes redirectAttributes) {
        this.periodService.deletePeriodById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa tiết học thành công!");
        return "redirect:/admin/period";
    }

    // Excel Export
    @GetMapping("/admin/period/export")
    public ResponseEntity<InputStreamResource> exportPeriodsToExcel() throws IOException {
        ByteArrayInputStream excelStream = periodService.exportPeriodsToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=periods.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(excelStream));
    }

    // Excel Import
    @PostMapping("/admin/period/import")
    public String importPeriodsFromExcel(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel!");
            return "redirect:/admin/period";
        }

        String result = periodService.importPeriodsFromExcel(file);
        redirectAttributes.addFlashAttribute("successMessage", result);
        return "redirect:/admin/period";
    }

    // Excel Template Download
    @GetMapping("/admin/period/template")
    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
        ByteArrayInputStream templateStream = periodService.createExcelTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=period_template.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(templateStream));
    }
}
