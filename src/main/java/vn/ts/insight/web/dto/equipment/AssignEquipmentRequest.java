package vn.ts.insight.web.dto.equipment;

import jakarta.validation.constraints.NotNull;

public class AssignEquipmentRequest {

    @NotNull(message = "ID nhân viên không được để trống")
    private Long employeeId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
