package vn.ngocanh.timetable.controller.admin;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.ngocanh.timetable.domain.Course;
import vn.ngocanh.timetable.service.CourseService;
import vn.ngocanh.timetable.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CourseController {

    private final UploadService uploadService;
    private final CourseService courseService;

    public CourseController(CourseService courseService, UploadService uploadService) {
        this.courseService = courseService;
        this.uploadService = uploadService;
    }

    // create course
    @GetMapping("/admin/course/create")
    public String getCreateCoursePage(Model model) {
        model.addAttribute("newCourse", new Course());
        return "admin/course/create";
    }

    @PostMapping("/admin/course/create")
    public String createCourse(@ModelAttribute("newCourse") Course newCourse) {
        newCourse.setCollege(this.courseService.getCollegeByName(newCourse.getCollege().getName()));
        this.courseService.handleSaveCourse(newCourse);
        return "redirect:/admin/course";
    }

    // read course
    @GetMapping("/admin/course")
    public String getCourseTable(Model model) {
        model.addAttribute("courses", this.courseService.getAllCourse());
        return "admin/course/show";
    }

    // delete course
    @GetMapping("/admin/course/delete/{id}")
    public String getDeleteCoursePage(@PathVariable long id, Model model) {
        model.addAttribute("oldCourse", this.courseService.getCourseById(id));
        return "admin/course/delete";
    }

    @PostMapping("/admin/course/delete")
    public String deleteCourse(@ModelAttribute("oldCourse") Course oldCourse) {
        // TODO: process POST request
        this.courseService.handleDeleteCourse(oldCourse.getId());
        return "redirect:/admin/course";
    }

    @GetMapping("/admin/course/{id}")
    public String getCourseDetail(@PathVariable long id, Model model) {
        // TODO: process POST request
        model.addAttribute("course", this.courseService.getCourseById(id));
        return "admin/course/detail";
    }

    @GetMapping("/admin/course/update/{id}")
    public String getUpdateCoursePage(@PathVariable long id, Model model) {
        model.addAttribute("currentCourse", this.courseService.getCourseById(id));
        return "admin/course/update";
    }

    @PostMapping("/admin/course/update")
    public String updateCourse(@ModelAttribute("currentCourse") Course currentCourse) {
        currentCourse.setCollege(this.courseService.getCollegeByName(currentCourse.getCollege().getName()));
        // TODO: process POST request
        this.courseService.handleSaveCourse(currentCourse);
        return "redirect:/admin/course";
    }

    @PostMapping("/admin/course/import")
    public String importExcelCourse(@RequestParam("file") MultipartFile file) {
        // TODO: process POST request
        this.uploadService.importCoursesFromExcel(file);
        return "redirect:/admin/course";
    }

    @GetMapping("/admin/course/export")
    public void exportUsers(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=courses.xlsx");

        try (OutputStream outputStream = response.getOutputStream()) {
            this.uploadService.exportCoursesToExcel(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error exporting users: " + e.getMessage());
        }
    }
}
