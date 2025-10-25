package vn.ts.insight.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ts.insight.domain.equipment.Equipment;
import vn.ts.insight.domain.employee.Employee;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    
    List<Equipment> findByStatus(vn.ts.insight.domain.common.EquipmentStatus status);
    
    List<Equipment> findByAssignedTo(Employee employee);
    
    @Query("SELECT e FROM Equipment e WHERE " +
           "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:type IS NULL OR LOWER(e.type) LIKE LOWER(CONCAT('%', :type, '%')))")
    List<Equipment> findByFilters(@Param("name") String name, 
                                  @Param("status") vn.ts.insight.domain.common.EquipmentStatus status, 
                                  @Param("type") String type);
}
