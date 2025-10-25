package vn.ts.insight.web.mapper;

import org.springframework.stereotype.Component;
import vn.ts.insight.domain.equipment.Equipment;
import vn.ts.insight.web.dto.equipment.EquipmentListItemDto;
import vn.ts.insight.web.dto.equipment.EquipmentRequest;
import vn.ts.insight.web.dto.equipment.EquipmentResponse;

@Component
public class EquipmentMapper {

    public Equipment toEntity(EquipmentRequest request) {
        Equipment equipment = new Equipment();
        equipment.setCode(request.getCode());
        equipment.setName(request.getName());
        equipment.setType(request.getType());
        equipment.setSerialNumber(request.getSerialNumber());
        equipment.setStatus(request.getStatus());
        equipment.setPurchaseDate(request.getPurchaseDate());
        equipment.setPurchasePrice(request.getPurchasePrice());
        equipment.setNotes(request.getNotes());
        return equipment;
    }

    public void updateEntity(Equipment equipment, EquipmentRequest request) {
        equipment.setCode(request.getCode());
        equipment.setName(request.getName());
        equipment.setType(request.getType());
        equipment.setSerialNumber(request.getSerialNumber());
        equipment.setStatus(request.getStatus());
        equipment.setPurchaseDate(request.getPurchaseDate());
        equipment.setPurchasePrice(request.getPurchasePrice());
        equipment.setNotes(request.getNotes());
    }

    public EquipmentResponse toResponse(Equipment equipment) {
        EquipmentResponse response = new EquipmentResponse();
        response.setId(equipment.getId());
        response.setCode(equipment.getCode());
        response.setName(equipment.getName());
        response.setType(equipment.getType());
        response.setSerialNumber(equipment.getSerialNumber());
        response.setStatus(equipment.getStatus());
        response.setPurchaseDate(equipment.getPurchaseDate());
        response.setPurchasePrice(equipment.getPurchasePrice());
        response.setNotes(equipment.getNotes());
        
        if (equipment.getAssignedTo() != null) {
            response.setAssignedToEmployeeId(equipment.getAssignedTo().getId());
            response.setAssignedToEmployeeName(equipment.getAssignedTo().getFullName());
        }
        
        return response;
    }

    public EquipmentListItemDto toListItem(Equipment equipment) {
        EquipmentListItemDto dto = new EquipmentListItemDto();
        dto.setId(equipment.getId());
        dto.setCode(equipment.getCode());
        dto.setName(equipment.getName());
        dto.setType(equipment.getType());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setStatus(equipment.getStatus());
        dto.setPurchaseDate(equipment.getPurchaseDate());
        dto.setPurchasePrice(equipment.getPurchasePrice());
        
        if (equipment.getAssignedTo() != null) {
            dto.setAssignedToEmployeeId(equipment.getAssignedTo().getId());
            dto.setAssignedToEmployeeName(equipment.getAssignedTo().getFullName());
        }
        
        return dto;
    }
}
