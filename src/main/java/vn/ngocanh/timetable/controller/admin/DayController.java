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
import vn.ngocanh.timetable.domain.Day;
import vn.ngocanh.timetable.service.DayService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class DayController {

    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    @GetMapping("/admin/day")
    public String getDayPage(Model model) {
        List<Day> days = this.dayService.getAllDay();
        model.addAttribute("days", days);
        return "admin/day/show";
    }

    @GetMapping("/admin/day/create")
    public String getCreateDayPage(Model model) {
        model.addAttribute("newDay", new Day());
        return "admin/day/create";
    }

    @PostMapping("/admin/day/create")
    public String createDay(Model model, @ModelAttribute("newDay") @Valid Day day,
            BindingResult newDayBindingResult,
            RedirectAttributes redirectAttributes) {

        // Validation cho tên day trùng
        if (this.dayService.isDayNameExist(day.getName())) {
            newDayBindingResult.rejectValue("name", "day.name.exist",
                    "Tên ngày này đã tồn tại");
        }

        if (newDayBindingResult.hasErrors()) {
            return "admin/day/create";
        }

        this.dayService.handleSaveDay(day);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo ngày thành công!");
        return "redirect:/admin/day";
    }

    @GetMapping("/admin/day/update/{id}")
    public String getUpdateDayPage(Model model, @PathVariable long id) {
        Day currentDay = this.dayService.getDayById(id);
        model.addAttribute("currentDay", currentDay);
        return "admin/day/update";
    }

    @PostMapping("/admin/day/update")
    public String postUpdateDay(@ModelAttribute("currentDay") @Valid Day day,
            BindingResult currentDayBindingResult,
            RedirectAttributes redirectAttributes) {

        if (currentDayBindingResult.hasErrors()) {
            return "admin/day/update";
        }

        this.dayService.handleSaveDay(day);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật ngày thành công!");
        return "redirect:/admin/day";
    }

    @GetMapping("/admin/day/delete/{id}")
    public String deleteDay(@PathVariable long id, RedirectAttributes redirectAttributes) {
        this.dayService.deleteDayById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa ngày thành công!");
        return "redirect:/admin/day";
    }

    // Excel Export
    @GetMapping("/admin/day/export")
    public ResponseEntity<InputStreamResource> exportDaysToExcel() throws IOException {
        ByteArrayInputStream excelStream = dayService.exportDaysToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=days.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(excelStream));
    }

    // Excel Import
    @PostMapping("/admin/day/import")
    public String importDaysFromExcel(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel!");
            return "redirect:/admin/day";
        }

        String result = dayService.importDaysFromExcel(file);
        redirectAttributes.addFlashAttribute("successMessage", result);
        return "redirect:/admin/day";
    }

    // Excel Template Download
    @GetMapping("/admin/day/template")
    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
        ByteArrayInputStream templateStream = dayService.createExcelTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=day_template.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(templateStream));
    }
}
