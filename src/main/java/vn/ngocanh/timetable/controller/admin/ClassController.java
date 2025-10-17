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
import vn.ngocanh.timetable.domain.Class;
import vn.ngocanh.timetable.service.ClassService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping("/admin/class")
    public String getClassPage(Model model) {
        List<Class> classes = this.classService.getAllClass();
        model.addAttribute("classes", classes);
        return "admin/class/show";
    }

    @GetMapping("/admin/class/create")
    public String getCreateClassPage(Model model) {
        model.addAttribute("newClass", new Class());
        return "admin/class/create";
    }

    @PostMapping("/admin/class/create")
    public String createClass(Model model, @ModelAttribute("newClass") @Valid Class classEntity,
            BindingResult newClassBindingResult,
            RedirectAttributes redirectAttributes) {

        // Validation cho tên class trùng
        if (this.classService.isClassNameExist(classEntity.getName())) {
            newClassBindingResult.rejectValue("name", "class.name.exist",
                    "Tên phòng học này đã tồn tại");
        }

        if (newClassBindingResult.hasErrors()) {
            return "admin/class/create";
        }

        this.classService.handleSaveClass(classEntity);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo phòng học thành công!");
        return "redirect:/admin/class";
    }

    @GetMapping("/admin/class/update/{id}")
    public String getUpdateClassPage(Model model, @PathVariable long id) {
        Class currentClass = this.classService.getClassById(id);
        model.addAttribute("currentClass", currentClass);
        return "admin/class/update";
    }

    @PostMapping("/admin/class/update")
    public String postUpdateClass(@ModelAttribute("currentClass") @Valid Class classEntity,
            BindingResult currentClassBindingResult,
            RedirectAttributes redirectAttributes) {

        if (currentClassBindingResult.hasErrors()) {
            return "admin/class/update";
        }

        this.classService.handleSaveClass(classEntity);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật phòng học thành công!");
        return "redirect:/admin/class";
    }

    @GetMapping("/admin/class/delete/{id}")
    public String deleteClass(@PathVariable long id, RedirectAttributes redirectAttributes) {
        this.classService.deleteClassById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa phòng học thành công!");
        return "redirect:/admin/class";
    }

    // Excel Export
    @GetMapping("/admin/class/export")
    public ResponseEntity<InputStreamResource> exportClassesToExcel() throws IOException {
        ByteArrayInputStream excelStream = classService.exportClassesToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=classes.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(excelStream));
    }

    // Excel Import
    @PostMapping("/admin/class/import")
    public String importClassesFromExcel(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel!");
            return "redirect:/admin/class";
        }

        String result = classService.importClassesFromExcel(file);
        redirectAttributes.addFlashAttribute("successMessage", result);
        return "redirect:/admin/class";
    }

    // Excel Template Download
    @GetMapping("/admin/class/template")
    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
        ByteArrayInputStream templateStream = classService.createExcelTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=class_template.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(templateStream));
    }
}
