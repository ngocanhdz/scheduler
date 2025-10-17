package vn.ngocanh.timetable.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ngocanh.timetable.service.CourseService;

@Controller
public class ItemController {
    private final CourseService courseService;

    public ItemController(CourseService courseService) {
        this.courseService = courseService;

    }

    @GetMapping("/course")
    public String getAllCourse(Model model,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) Integer credit,
            @RequestParam(required = false) Integer lectureHour) {

        model.addAttribute("courses", this.courseService.searchCourses(code, name, college, credit, lectureHour));
        return "client/course/show";
    }

    @GetMapping("/course/{id}")
    public String getCourseDetail(@PathVariable long id, Model model) {
        // TODO: process POST request
        model.addAttribute("course", this.courseService.getCourseById(id));
        return "client/course/detail";
    }

}
