package Models;

import com.mainmicroservice.mainmicroservice.Entities.User;

import java.util.Date;

public class UserAndGroupUserModel {
    private Long userId;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date birthday;
    private String role;

    public UserAndGroupUserModel(User user) {
        userId = user.getUserid();
        email = user.getEmail();
        firstName = user.getFirstname();
        lastName = user.getLastname();
        role = user.getRole().getRoleName();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserAndGroupUserModel() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
