package vn.ts.insight.web.mapper;

import org.springframework.stereotype.Component;
import vn.ts.insight.domain.partner.Partner;
import vn.ts.insight.web.dto.partner.PartnerListItemDto;
import vn.ts.insight.web.dto.partner.PartnerRequest;
import vn.ts.insight.web.dto.partner.PartnerResponse;

@Component
public class PartnerMapper {

    public Partner toEntity(PartnerRequest request) {
        Partner partner = new Partner();
        partner.setCode(request.getCode());
        partner.setName(request.getName());
        partner.setEmail(request.getEmail());
        partner.setPhone(request.getPhone());
        partner.setAddress(request.getAddress());
        partner.setPartnerType(request.getPartnerType());
        partner.setStatus(request.getStatus());
        partner.setStartDate(request.getStartDate());
        partner.setNotes(request.getNotes());
        return partner;
    }

    public void updateEntity(Partner partner, PartnerRequest request) {
        partner.setCode(request.getCode());
        partner.setName(request.getName());
        partner.setEmail(request.getEmail());
        partner.setPhone(request.getPhone());
        partner.setAddress(request.getAddress());
        partner.setPartnerType(request.getPartnerType());
        partner.setStatus(request.getStatus());
        partner.setStartDate(request.getStartDate());
        partner.setNotes(request.getNotes());
    }

    public PartnerResponse toResponse(Partner partner) {
        PartnerResponse response = new PartnerResponse();
        response.setId(partner.getId());
        response.setCode(partner.getCode());
        response.setName(partner.getName());
        response.setEmail(partner.getEmail());
        response.setPhone(partner.getPhone());
        response.setAddress(partner.getAddress());
        response.setPartnerType(partner.getPartnerType());
        response.setStatus(partner.getStatus());
        response.setStartDate(partner.getStartDate());
        response.setNotes(partner.getNotes());
        return response;
    }

    public PartnerListItemDto toListItem(Partner partner) {
        PartnerListItemDto dto = new PartnerListItemDto();
        dto.setId(partner.getId());
        dto.setCode(partner.getCode());
        dto.setName(partner.getName());
        dto.setEmail(partner.getEmail());
        dto.setPhone(partner.getPhone());
        dto.setPartnerType(partner.getPartnerType());
        dto.setStatus(partner.getStatus());
        dto.setStartDate(partner.getStartDate());
        return dto;
    }
}
