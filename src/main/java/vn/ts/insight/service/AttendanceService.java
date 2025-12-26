package vn.ts.insight.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ts.insight.domain.attendance.AttendanceRecord;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.repository.AttendanceRecordRepository;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.web.dto.attendance.AttendanceRequest;
import vn.ts.insight.web.dto.attendance.AttendanceResponse;
import vn.ts.insight.web.mapper.AttendanceMapper;

@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceService(
        AttendanceRecordRepository attendanceRecordRepository,
        EmployeeRepository employeeRepository,
        AttendanceMapper attendanceMapper
    ) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Transactional
    public AttendanceResponse create(AttendanceRequest request) {
        AttendanceRecord record = new AttendanceRecord();
        mapRequest(record, request);
        attendanceRecordRepository.save(record);
        return attendanceMapper.toResponse(record);
    }

    public List<AttendanceResponse> findByEmployee(Long employeeId) {
        return attendanceRecordRepository.findByEmployeeId(employeeId).stream()
            .map(attendanceMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<AttendanceResponse> findByRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRecordRepository.findByWorkDateBetween(startDate, endDate).stream()
            .map(attendanceMapper::toResponse)
            .collect(Collectors.toList());
    }

    public Page<AttendanceResponse> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttendanceRecord> result = attendanceRecordRepository.findAll(pageable);
        List<AttendanceResponse> content = result.getContent().stream()
            .map(attendanceMapper::toResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    public Page<AttendanceResponse> findPageWithFilters(int page, int size, Long employeeId, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttendanceRecord> result;
        
        // Build query based on filters
        if (employeeId == null && startDate == null && endDate == null) {
            result = attendanceRecordRepository.findAll(pageable);
        } else {
            // Filter in memory - in production should use JPA Specification
            List<AttendanceRecord> allRecords = attendanceRecordRepository.findAll();
            List<AttendanceRecord> filtered = allRecords.stream()
                .filter(record -> {
                    boolean matches = true;
                    if (employeeId != null) {
                        matches = matches && (record.getEmployee() != null && 
                            record.getEmployee().getId().equals(employeeId));
                    }
                    if (startDate != null) {
                        matches = matches && (record.getWorkDate() != null && 
                            !record.getWorkDate().isBefore(startDate));
                    }
                    if (endDate != null) {
                        matches = matches && (record.getWorkDate() != null && 
                            !record.getWorkDate().isAfter(endDate));
                    }
                    return matches;
                })
                .collect(Collectors.toList());
            
            // Manual pagination
            int start = page * size;
            int end = Math.min(start + size, filtered.size());
            List<AttendanceRecord> pageContent = start < filtered.size() 
                ? filtered.subList(start, end) 
                : List.of();
            result = new PageImpl<>(pageContent, pageable, filtered.size());
        }
        
        List<AttendanceResponse> content = result.getContent().stream()
            .map(attendanceMapper::toResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    public List<AttendanceResponse> findAll() {
        return attendanceRecordRepository.findAll().stream()
            .map(attendanceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public AttendanceResponse update(Long id, AttendanceRequest request) {
        AttendanceRecord record = attendanceRecordRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Attendance record not found"));
        mapRequest(record, request);
        return attendanceMapper.toResponse(record);
    }

    public void delete(Long id) {
        attendanceRecordRepository.deleteById(id);
    }

    private void mapRequest(AttendanceRecord record, AttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        record.setEmployee(employee);
        record.setWorkDate(request.getWorkDate());
        record.setCheckIn(request.getCheckIn());
        record.setCheckOut(request.getCheckOut());
        
        // Tự động tính số giờ làm nếu có giờ vào và giờ ra
        if (request.getCheckIn() != null && request.getCheckOut() != null) {
            BigDecimal workedHours = calculateWorkedHours(request.getCheckIn(), request.getCheckOut());
            record.setWorkedHours(workedHours);
        } else {
            record.setWorkedHours(request.getWorkedHours());
        }
        
        record.setNotes(request.getNotes());
    }
    
    private BigDecimal calculateWorkedHours(LocalTime checkIn, LocalTime checkOut) {
        if (checkOut.isAfter(checkIn)) {
            long minutes = ChronoUnit.MINUTES.between(checkIn, checkOut);
            double hours = minutes / 60.0;
            return BigDecimal.valueOf(hours).setScale(2, java.math.RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
