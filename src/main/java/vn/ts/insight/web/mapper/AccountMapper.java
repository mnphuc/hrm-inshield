package vn.ts.insight.web.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import vn.ts.insight.domain.user.Role;
import vn.ts.insight.domain.user.UserAccount;
import vn.ts.insight.repository.RoleRepository;
import vn.ts.insight.web.dto.auth.RegisterUserRequest;
import vn.ts.insight.web.dto.auth.RegisterUserResponse;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AccountMapper {
    @Autowired
    RoleRepository roleRepository;
    public RegisterUserResponse toResponse(UserAccount userAccount) {
        RegisterUserResponse response = new RegisterUserResponse();
        response.setUserId(userAccount.getId());
        response.setEnabled(userAccount.isEnabled());
        response.setAccountNonLocked(userAccount.isAccountNonLocked());
        response.setEmployeeCode(userAccount.getEmployee().getCode());
        response.setUsername(userAccount.getUsername());
        response.setEmail(userAccount.getEmail());
        response.setFullName(userAccount.getEmployee().getFullName());
        response.setRoles(
                userAccount.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
        return response;
    }

    public void updateAccount(UserAccount userAccount, RegisterUserRequest request) {
        Set<Role> roles = (request.getRoles() != null && !request.getRoles().isEmpty())
                ? request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                .collect(Collectors.toSet())
                : new HashSet<>();
        userAccount.setRoles(roles);
        userAccount.setUsername(request.getUsername());
        userAccount.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String encodedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
            userAccount.setPassword(encodedPassword);
        }
    }
}
