package vn.ts.insight.web.dto.equipment;

import java.math.BigDecimal;
import java.time.LocalDate;
import vn.ts.insight.domain.common.EquipmentStatus;

public class EquipmentResponse {

    private Long id;
    private String code;
    private String name;
    private String type;
    private String serialNumber;
    private EquipmentStatus status;
    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    private String notes;
    private Long assignedToEmployeeId;
    private String assignedToEmployeeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getAssignedToEmployeeId() {
        return assignedToEmployeeId;
    }

    public void setAssignedToEmployeeId(Long assignedToEmployeeId) {
        this.assignedToEmployeeId = assignedToEmployeeId;
    }

    public String getAssignedToEmployeeName() {
        return assignedToEmployeeName;
    }

    public void setAssignedToEmployeeName(String assignedToEmployeeName) {
        this.assignedToEmployeeName = assignedToEmployeeName;
    }
}
