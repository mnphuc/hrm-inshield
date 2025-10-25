package vn.ts.insight.domain.common;

public enum PartnerType {
    SUPPLIER("Nhà cung cấp"),
    CLIENT("Khách hàng"),
    VENDOR("Nhà phân phối"),
    OTHER("Khác");

    private final String displayName;

    PartnerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
