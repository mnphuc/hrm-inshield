package vn.ts.insight.service;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ts.insight.domain.common.SystemRoleName;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.domain.user.Role;
import vn.ts.insight.domain.user.UserAccount;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.repository.RoleRepository;
import vn.ts.insight.repository.UserAccountRepository;
import vn.ts.insight.web.dto.auth.RegisterUserRequest;
import vn.ts.insight.web.dto.auth.RegisterUserResponse;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        UserAccountRepository userAccountRepository,
        RoleRepository roleRepository,
        EmployeeRepository employeeRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        validateUnique(request);

        UserAccount account = new UserAccount();
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<SystemRoleName> requestedRoles = request.getRoles();
        if (requestedRoles == null || requestedRoles.isEmpty()) {
            requestedRoles = Set.of(SystemRoleName.EMPLOYEE);
        }

        Set<Role> roles = new HashSet<>();
        for (SystemRoleName roleName : requestedRoles) {
            Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role " + roleName + " not configured"));
            roles.add(role);
        }
        account.setRoles(roles);

        userAccountRepository.save(account);

        Employee employee = null;
        if (request.getFullName() != null && !request.getFullName().isBlank()) {

            if (userAccountRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            employee = new Employee();
            employee.setCode(resolveEmployeeCode(request, account));
            employee.setFullName(request.getFullName());
            employee.setEmail(request.getEmail());
            employee.setPhone(request.getPhone());
            employee.setDepartment(request.getDepartment());
            employee.setPosition(request.getPosition());
            employee.setHireDate(request.getHireDate() != null ? request.getHireDate() : java.time.LocalDate.now());
            employee.setEmploymentStatus("ACTIVE");
            employee.setBaseSalary(request.getBaseSalary());
            employee.setAccount(account);
            employeeRepository.save(employee);
        }

        RegisterUserResponse response = new RegisterUserResponse();
        response.setUserId(account.getId());
        response.setUsername(account.getUsername());
        response.setEmail(account.getEmail());
        response.setRoles(requestedRoles);
        if (employee != null) {
            response.setEmployeeId(employee.getId());
            response.setEmployeeCode(employee.getCode());
        }
        return response;
    }

    private void validateUnique(RegisterUserRequest request) {
        if (userAccountRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private String resolveEmployeeCode(RegisterUserRequest request, UserAccount account) {
        if (request.getEmployeeCode() != null && !request.getEmployeeCode().isBlank()) {
            return request.getEmployeeCode();
        }
        return "EMP-" + account.getUsername().toUpperCase();
    }
}
