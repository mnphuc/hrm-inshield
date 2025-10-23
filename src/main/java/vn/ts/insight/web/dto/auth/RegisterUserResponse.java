package vn.ts.insight.web.dto.auth;

import java.util.Set;
import vn.ts.insight.domain.common.SystemRoleName;

public class RegisterUserResponse {
    private Long userId;
    private Long employeeId;
    private String employeeCode;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private boolean enabled;
    private boolean accountNonLocked;
    private Set<SystemRoleName> roles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName(){
        return fullName;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public Set<SystemRoleName> getRoles() {
        return roles;
    }

    public void setRoles(Set<SystemRoleName> roles) {
        this.roles = roles;
    }
}
