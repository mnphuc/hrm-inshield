package vn.ts.insight.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ts.insight.domain.partner.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    
    List<Partner> findByStatus(String status);
    
    List<Partner> findByPartnerType(vn.ts.insight.domain.common.PartnerType partnerType);
    
    @Query("SELECT p FROM Partner p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:partnerType IS NULL OR p.partnerType = :partnerType)")
    List<Partner> findByFilters(@Param("name") String name, 
                               @Param("status") String status, 
                               @Param("partnerType") vn.ts.insight.domain.common.PartnerType partnerType);
}
