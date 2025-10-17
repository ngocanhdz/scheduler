package vn.ngocanh.timetable.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.ngocanh.timetable.domain.College;
import vn.ngocanh.timetable.domain.Role;
import vn.ngocanh.timetable.domain.User;
import vn.ngocanh.timetable.domain.DTO.RegisterDTO;
import vn.ngocanh.timetable.repository.CollegeRepository;
import vn.ngocanh.timetable.repository.RoleRepository;
import vn.ngocanh.timetable.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CollegeRepository collegeRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            CollegeRepository collegeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.collegeRepository = collegeRepository;
    }

    public User handleSaveUser(User user) {
        return this.userRepository.save(user);
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findOneByName(name);
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public User getUserById(long id) {
        return this.userRepository.findOneById(id);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public College getCollegeByName(String name) {
        return this.collegeRepository.findOneByName(name);
    }

    public User MappedDTO(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setPassword(registerDTO.getPassword());
        user.setEmail(registerDTO.getEmail());

        return user;
    }

    public Boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    // Phân trang và tìm kiếm
    public Page<User> getUsersWithFilters(String fullName, Long yearLevel, String roleName, String collegeName,
            int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository.findUsersWithFilters(fullName, yearLevel, roleName, collegeName, pageable);
    }

    // Lấy dữ liệu cho filter dropdowns
    public List<String> getAllRoleNames() {
        return userRepository.findAllRoleNames();
    }

    public List<String> getAllCollegeNames() {
        return userRepository.findAllCollegeNames();
    }

    public List<Long> getAllYearLevels() {
        return userRepository.findAllYearLevels();
    }
}
