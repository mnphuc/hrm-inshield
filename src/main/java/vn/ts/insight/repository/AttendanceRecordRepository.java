package vn.ts.insight.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ts.insight.domain.attendance.AttendanceRecord;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByEmployeeIdAndWorkDate(Long employeeId, LocalDate workDate);
    List<AttendanceRecord> findByEmployeeId(Long employeeId);
    List<AttendanceRecord> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);
}
