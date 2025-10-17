package vn.ngocanh.timetable.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.ngocanh.timetable.domain.User;
import vn.ngocanh.timetable.service.UploadService;
import vn.ngocanh.timetable.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UploadService uploadService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, UploadService uploadService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String postCreatePage(@ModelAttribute("newUser") User newUser,
            @RequestParam("userFile") MultipartFile file) {
        String password = this.passwordEncoder.encode(newUser.getPassword());
        // TODO: process POST request
        String avatar = this.uploadService.handleUploadFile(file, "avatar");
        newUser.setAvatar(avatar);
        newUser.setPassword(password);
        newUser.setRole(this.userService.getRoleByName(newUser.getRole().getName()));
        newUser.setYearLevel(newUser.getYearLevel());
        newUser.setCollege(this.userService.getCollegeByName(newUser.getCollege().getName()));
        this.userService.handleSaveUser(newUser);
        return "redirect:/admin/user";
    }

    @PostMapping("/admin/user/import")
    public String importUserByExcel(@RequestParam("file") MultipartFile file) {
        // TODO: process POST request
        this.uploadService.importUsersFromExcel(file);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/export")
    public void exportUsers(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        try (OutputStream outputStream = response.getOutputStream()) {
            this.uploadService.exportUsersToExcel(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error exporting users: " + e.getMessage());
        }
    }

    @GetMapping("/admin/user")
    public String getTableUser(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) Long yearLevel,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String collegeName) {

        org.springframework.data.domain.Page<User> userPage = this.userService.getUsersWithFilters(fullName, yearLevel,
                roleName, collegeName, page, size, sortBy, sortDir);

        model.addAttribute("userPage", userPage);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalElements", userPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        // Filter parameters
        model.addAttribute("fullName", fullName);
        model.addAttribute("yearLevel", yearLevel);
        model.addAttribute("roleName", roleName);
        model.addAttribute("collegeName", collegeName);

        // Data for filter dropdowns
        model.addAttribute("roleNames", this.userService.getAllRoleNames());
        model.addAttribute("collegeNames", this.userService.getAllCollegeNames());
        model.addAttribute("yearLevels", this.userService.getAllYearLevels());

        return "admin/user/show";
    }

    @GetMapping("/admin/user/update/{id}")
    public String getUpdatePage(@PathVariable long id, Model model) {
        model.addAttribute("currentUser", this.userService.getUserById(id));

        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String updateUser(@ModelAttribute("currentUser") User currentUser) {

        // TODO: process POST request
        User user = this.userService.getUserById(currentUser.getId());
        user.setFullName(currentUser.getFullName());
        user.setPhoneNumber(currentUser.getPhoneNumber());
        user.setAddress(currentUser.getAddress());
        user.setRole(this.userService.getRoleByName(currentUser.getRole().getName()));
        user.setYearLevel(currentUser.getYearLevel());
        user.setCollege(this.userService.getCollegeByName(currentUser.getCollege().getName()));
        this.userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/{id}")
    public String getMethodName(@PathVariable long id, Model model) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeletePage(@PathVariable long id, Model model) {
        model.addAttribute("oldUser", this.userService.getUserById(id));
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String deleteUser(@ModelAttribute("oldUser") User oldUser) {

        // TODO: process POST request
        this.userService.handleDeleteUser(oldUser.getId());
        return "redirect:/admin/user";
    }

}
