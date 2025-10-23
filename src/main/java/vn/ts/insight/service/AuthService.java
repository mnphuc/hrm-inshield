package vn.ts.insight.service;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import vn.ts.insight.web.mapper.AccountMapper;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final String ACCOUNT_NOT_FOUND = "Account not found";

    public AuthService(
        UserAccountRepository userAccountRepository,
        RoleRepository roleRepository,
        EmployeeRepository employeeRepository,
        PasswordEncoder passwordEncoder,
        AccountMapper accountMapper) {
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
    }
    public Page<RegisterUserResponse> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserAccount> result = userAccountRepository.findAll(pageable);
        List<RegisterUserResponse> content = result.getContent().stream()
                .map(accountMapper::toResponse)
                .toList();
        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    @Transactional
    public RegisterUserResponse update (Long id, RegisterUserRequest request) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ACCOUNT_NOT_FOUND));
        accountMapper.updateAccount(userAccount, request);
        return accountMapper.toResponse(userAccount);
    }

    @Transactional
    public void delete(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại."));
        boolean isAdmin = userAccount.getRoles().stream()
                .anyMatch(role -> role.getName() == SystemRoleName.ADMIN);
        if (isAdmin) {
            throw new IllegalStateException("Không thể vô hiệu hóa tài khoản có quyền ADMIN.");
        }
        if (!userAccount.isEnabled()) {
            throw new IllegalStateException("Tài khoản này đã bị vô hiệu hoá trước đó.");
        }
        userAccount.setEnabled(false);
        userAccount.setAccountNonLocked(false);
        userAccountRepository.save(userAccount);
    }


}
