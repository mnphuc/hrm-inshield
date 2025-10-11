package vn.ts.insight.web.dto.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PayrollResponse {
    private Long id;
    private Long employeeId;
    private int payrollYear;
    private int payrollMonth;
    private BigDecimal baseSalary;
    private BigDecimal overtimeHours;
    private BigDecimal overtimeAmount;
    private BigDecimal bonus;
    private BigDecimal deductions;
    private BigDecimal netPay;
    private LocalDate generatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public int getPayrollYear() {
        return payrollYear;
    }

    public void setPayrollYear(int payrollYear) {
        this.payrollYear = payrollYear;
    }

    public int getPayrollMonth() {
        return payrollMonth;
    }

    public void setPayrollMonth(int payrollMonth) {
        this.payrollMonth = payrollMonth;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public BigDecimal getOvertimeAmount() {
        return overtimeAmount;
    }

    public void setOvertimeAmount(BigDecimal overtimeAmount) {
        this.overtimeAmount = overtimeAmount;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }

    public LocalDate getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDate generatedAt) {
        this.generatedAt = generatedAt;
    }
}
