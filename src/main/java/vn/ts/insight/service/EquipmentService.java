package vn.ts.insight.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ts.insight.domain.equipment.Equipment;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.repository.EquipmentRepository;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.web.dto.equipment.AssignEquipmentRequest;
import vn.ts.insight.web.dto.equipment.EquipmentListItemDto;
import vn.ts.insight.web.dto.equipment.EquipmentRequest;
import vn.ts.insight.web.dto.equipment.EquipmentResponse;
import vn.ts.insight.web.mapper.EquipmentMapper;

@Service
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EquipmentMapper equipmentMapper;

    public EquipmentService(EquipmentRepository equipmentRepository, 
                           EmployeeRepository employeeRepository, 
                           EquipmentMapper equipmentMapper) {
        this.equipmentRepository = equipmentRepository;
        this.employeeRepository = employeeRepository;
        this.equipmentMapper = equipmentMapper;
    }

    @Transactional(readOnly = true)
    public List<EquipmentListItemDto> findAll() {
        return equipmentRepository.findAll().stream()
                .map(equipmentMapper::toListItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EquipmentListItemDto> findByFilters(String name, vn.ts.insight.domain.common.EquipmentStatus status, String type) {
        return equipmentRepository.findByFilters(name, status, type).stream()
                .map(equipmentMapper::toListItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<EquipmentResponse> findById(Long id) {
        return equipmentRepository.findById(id)
                .map(equipmentMapper::toResponse);
    }

    public EquipmentResponse create(EquipmentRequest request) {
        Equipment equipment = equipmentMapper.toEntity(request);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toResponse(savedEquipment);
    }

    public Optional<EquipmentResponse> update(Long id, EquipmentRequest request) {
        return equipmentRepository.findById(id)
                .map(equipment -> {
                    equipmentMapper.updateEntity(equipment, request);
                    Equipment savedEquipment = equipmentRepository.save(equipment);
                    return equipmentMapper.toResponse(savedEquipment);
                });
    }

    public boolean delete(Long id) {
        if (equipmentRepository.existsById(id)) {
            equipmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean assignToEmployee(Long equipmentId, AssignEquipmentRequest request) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        Optional<Employee> employeeOpt = employeeRepository.findById(request.getEmployeeId());
        
        if (equipmentOpt.isPresent() && employeeOpt.isPresent()) {
            Equipment equipment = equipmentOpt.get();
            Employee employee = employeeOpt.get();
            
            equipment.setAssignedTo(employee);
            equipment.setStatus(vn.ts.insight.domain.common.EquipmentStatus.ASSIGNED);
            equipmentRepository.save(equipment);
            return true;
        }
        return false;
    }

    public boolean unassign(Long equipmentId) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        
        if (equipmentOpt.isPresent()) {
            Equipment equipment = equipmentOpt.get();
            equipment.setAssignedTo(null);
            equipment.setStatus(vn.ts.insight.domain.common.EquipmentStatus.AVAILABLE);
            equipmentRepository.save(equipment);
            return true;
        }
        return false;
    }
}
