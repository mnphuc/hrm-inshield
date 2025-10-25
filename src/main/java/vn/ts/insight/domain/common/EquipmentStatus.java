package vn.ts.insight.domain.common;

public enum EquipmentStatus {
    AVAILABLE("Có sẵn"),
    ASSIGNED("Đã phân bổ"),
    MAINTENANCE("Bảo trì"),
    BROKEN("Hỏng"),
    RETIRED("Đã nghỉ hưu");

    private final String displayName;

    EquipmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
