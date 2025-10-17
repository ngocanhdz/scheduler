package vn.ngocanh.timetable.controller.client;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.ngocanh.timetable.domain.User;
import vn.ngocanh.timetable.domain.DTO.RegisterDTO;
import vn.ngocanh.timetable.service.UploadService;
import vn.ngocanh.timetable.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UploadService uploadService;

    public HomeController(UserService userService, PasswordEncoder passwordEncoder, UploadService uploadService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.uploadService = uploadService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("newRegisterDTO", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("newRegisterDTO") RegisterDTO newRegisterDTO,
            BindingResult userBindingResult) {
        // TODO: process POST request
        if (userBindingResult.hasErrors()) {
            return "client/auth/register";
        }

        User user = this.userService.MappedDTO(newRegisterDTO);
        String password = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRole(this.userService.getRoleByName("STUDENT"));
        this.userService.handleSaveUser(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "client/auth/login";
    }

    @GetMapping("/access-deny")
    public String getDenyPage() {
        return "client/auth/deny";
    }

    @GetMapping("/client/detail")
    public String getClientDetail(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = this.userService.getUserById((long) session.getAttribute("id"));
        model.addAttribute("user", user);
        return "client/auth/detail";
    }

    @PostMapping("/client/detail/update")
    public String postClientUpdate(@ModelAttribute("user") User currentUser,
            @RequestParam("userFile") MultipartFile file) {
        // TODO: process POST request
        String avatar = this.uploadService.handleUploadFile(file, "avatar");
        User user = this.userService.getUserById(currentUser.getId());
        user.setAvatar(avatar);
        user.setFullName(currentUser.getFullName());
        user.setPhoneNumber(currentUser.getPhoneNumber());
        user.setAddress(currentUser.getAddress());
        user.setCollege(this.userService.getCollegeByName(currentUser.getCollege().getName()));
        this.userService.handleSaveUser(user);
        return "redirect:/client/detail";
    }

}
