package vn.ts.insight.web.mapper;

import org.springframework.stereotype.Component;
import vn.ts.insight.domain.attendance.AttendanceRecord;
import vn.ts.insight.web.dto.attendance.AttendanceResponse;

@Component
public class AttendanceMapper {

    public AttendanceResponse toResponse(AttendanceRecord record) {
        AttendanceResponse response = new AttendanceResponse();
        response.setId(record.getId());
        response.setEmployeeId(record.getEmployee() != null ? record.getEmployee().getId() : null);
        response.setWorkDate(record.getWorkDate());
        response.setCheckIn(record.getCheckIn());
        response.setCheckOut(record.getCheckOut());
        response.setWorkedHours(record.getWorkedHours());
        response.setNotes(record.getNotes());
        return response;
    }
}
