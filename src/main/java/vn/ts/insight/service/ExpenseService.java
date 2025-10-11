package vn.ts.insight.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ts.insight.domain.common.ExpenseStatus;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.domain.expense.Expense;
import vn.ts.insight.domain.project.Project;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.repository.ExpenseRepository;
import vn.ts.insight.repository.ProjectRepository;
import vn.ts.insight.web.dto.expense.ExpenseRequest;
import vn.ts.insight.web.dto.expense.ExpenseResponse;
import vn.ts.insight.web.mapper.ExpenseMapper;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseService(
        ExpenseRepository expenseRepository,
        EmployeeRepository employeeRepository,
        ProjectRepository projectRepository,
        ExpenseMapper expenseMapper
    ) {
        this.expenseRepository = expenseRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.expenseMapper = expenseMapper;
    }

    @Transactional
    public ExpenseResponse submit(ExpenseRequest request) {
        Expense expense = new Expense();
        mapExpenseDetails(expense, request);
        expense.setStatus(ExpenseStatus.SUBMITTED);
        expenseRepository.save(expense);
        return expenseMapper.toResponse(expense);
    }

    public List<ExpenseResponse> findAll() {
        return expenseRepository.findAll().stream()
            .map(expenseMapper::toResponse)
            .collect(Collectors.toList());
    }

    public Page<ExpenseResponse> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Expense> result = expenseRepository.findAll(pageable);
        List<ExpenseResponse> content = result.getContent().stream()
            .map(expenseMapper::toResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    public ExpenseResponse getById(Long id) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        return expenseMapper.toResponse(expense);
    }

    public List<ExpenseResponse> findByEmployee(Long employeeId) {
        return expenseRepository.findByEmployeeId(employeeId).stream()
            .map(expenseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseResponse updateStatus(Long id, ExpenseStatus status) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        expense.setStatus(status);
        return expenseMapper.toResponse(expense);
    }

    @Transactional
    public ExpenseResponse update(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        mapExpenseDetails(expense, request);
        return expenseMapper.toResponse(expense);
    }

    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }

    private void mapExpenseDetails(Expense expense, ExpenseRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        expense.setEmployee(employee);

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            expense.setProject(project);
        } else {
            expense.setProject(null);
        }

        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setIncurredDate(request.getIncurredDate());
        expense.setDescription(request.getDescription());
    }
}
