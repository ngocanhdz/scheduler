package vn.ngocanh.timetable.domain.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.ngocanh.timetable.service.valid.HustEmail;
import vn.ngocanh.timetable.service.valid.RegisterChecked;
import vn.ngocanh.timetable.service.valid.StrongPassword;

@RegisterChecked
public class RegisterDTO {
    @Size(min = 2, message = "Tên phải có tối thiểu 2 kí tự")
    @NotNull(message = "Tên không được để trống")
    private String firstName;

    @Size(min = 2, message = "Họ phải có tối thiểu 2 kí tự")
    @NotNull(message = "Họ không được để trống")
    private String lastName;

    @NotNull(message = "Mật khẩu không được để trống")
    @StrongPassword
    private String password;

    @NotNull(message = "Email không được để trống")
    @HustEmail
    private String email;

    @NotNull(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
