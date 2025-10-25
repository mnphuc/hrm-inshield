package vn.ts.insight.web.dto.equipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import vn.ts.insight.domain.common.EquipmentStatus;

public class EquipmentRequest {

    @NotBlank(message = "Mã thiết bị không được để trống")
    @Size(max = 50, message = "Mã thiết bị không được vượt quá 50 ký tự")
    private String code;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(max = 150, message = "Tên thiết bị không được vượt quá 150 ký tự")
    private String name;

    @Size(max = 100, message = "Loại thiết bị không được vượt quá 100 ký tự")
    private String type;

    @Size(max = 100, message = "Số serial không được vượt quá 100 ký tự")
    private String serialNumber;

    @NotNull(message = "Trạng thái không được để trống")
    private EquipmentStatus status;

    private LocalDate purchaseDate;

    private BigDecimal purchasePrice;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String notes;

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
}
