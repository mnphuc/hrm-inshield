package vn.ts.insight.service;

import jakarta.transaction.Transactional;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.domain.user.Role;
import vn.ts.insight.domain.user.UserAccount;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.repository.RoleRepository;
import vn.ts.insight.repository.UserAccountRepository;
import vn.ts.insight.web.dto.employee.EmployeeRequest;
import vn.ts.insight.web.dto.employee.EmployeeResponse;
import vn.ts.insight.web.mapper.EmployeeMapper;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final EmployeeMapper employeeMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";
    public EmployeeService(
            EmployeeRepository employeeRepository,
            UserAccountRepository userAccountRepository,
            EmployeeMapper employeeMapper,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
            .map(employeeMapper::toResponse)
            .toList();
    }

    public Page<EmployeeResponse> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> result = employeeRepository.findAll(pageable);
        List<EmployeeResponse> content = result.getContent().stream()
                .map(employeeMapper::toResponse)
                .toList();
        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(EMPLOYEE_NOT_FOUND));
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        Employee employee = new Employee();
        employeeMapper.updateEntity(employee, request);
        applyRelations(employee, request);
        employeeRepository.save(employee);
        if(request.getEmail() != null && !request.getEmail().isBlank()) {
            UserAccount account = new UserAccount();
            account.setUsername(generateUsername(employee));
            String rawPassword = generateTempPassword();
            account.setPassword(passwordEncoder.encode(rawPassword));
            account.setEmail(employee.getEmail());
            Set<Role> roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            account.setRoles(roles);
            userAccountRepository.save(account);
            employee.setAccount(account);
            employeeRepository.save(employee);
            String body = buildAccountEmailBody(employee.getFullName(), account.getUsername(), rawPassword);
            mailService.sendMail(employee.getEmail(), "Thông tin tài khoản TechShield", body);
        }
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(EMPLOYEE_NOT_FOUND));
        employeeMapper.updateEntity(employee, request);
        applyRelations(employee, request);
        return employeeMapper.toResponse(employee);
    }

    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(EMPLOYEE_NOT_FOUND));

        if (employee.getAccount() != null) {
            userAccountRepository.delete(employee.getAccount());
        }

        employeeRepository.delete(employee);
    }
    private String generateUsername(Employee employee) {
        String fullName = Normalizer.normalize(employee.getFullName(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
        String[] parts = fullName.split("\\s+");
        if (parts.length == 0) return employee.getCode().replace("EMP-", "");
        String lastName = parts[parts.length - 1];
        StringBuilder sb = new StringBuilder(lastName);
        for (int i = 0; i < parts.length - 1; i++) {
            sb.append(parts[i].charAt(0));
        }
        String codeSuffix = employee.getCode().replaceAll("\\D", "");
        sb.append(codeSuffix);

        return sb.toString();
    }

    private String generateTempPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    private void applyRelations(Employee employee, EmployeeRequest request) {
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            employee.setManager(manager);
        } else {
            employee.setManager(null);
        }

        if (request.getUserId() != null) {
            UserAccount account = userAccountRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User account not found"));
            employee.setAccount(account);
        } else {
            employee.setAccount(null);
        }
    }
    private String buildAccountEmailBody(String fullName, String username, String rawPassword) {
        return String.join(
                "\n",
                "Chào " + fullName + ",",
                "",
                "Tài khoản của bạn đã được tạo:",
                "Username: " + username,
                "Password: " + rawPassword,
                "",
                "Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu."
        );
    }
}
