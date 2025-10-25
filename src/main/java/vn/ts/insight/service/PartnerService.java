package vn.ts.insight.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ts.insight.domain.partner.Partner;
import vn.ts.insight.repository.PartnerRepository;
import vn.ts.insight.web.dto.partner.PartnerListItemDto;
import vn.ts.insight.web.dto.partner.PartnerRequest;
import vn.ts.insight.web.dto.partner.PartnerResponse;
import vn.ts.insight.web.mapper.PartnerMapper;

@Service
@Transactional
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;

    public PartnerService(PartnerRepository partnerRepository, PartnerMapper partnerMapper) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
    }

    @Transactional(readOnly = true)
    public List<PartnerListItemDto> findAll() {
        return partnerRepository.findAll().stream()
                .map(partnerMapper::toListItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PartnerListItemDto> findByFilters(String name, String status, vn.ts.insight.domain.common.PartnerType partnerType) {
        return partnerRepository.findByFilters(name, status, partnerType).stream()
                .map(partnerMapper::toListItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<PartnerResponse> findById(Long id) {
        return partnerRepository.findById(id)
                .map(partnerMapper::toResponse);
    }

    public PartnerResponse create(PartnerRequest request) {
        Partner partner = partnerMapper.toEntity(request);
        Partner savedPartner = partnerRepository.save(partner);
        return partnerMapper.toResponse(savedPartner);
    }

    public Optional<PartnerResponse> update(Long id, PartnerRequest request) {
        return partnerRepository.findById(id)
                .map(partner -> {
                    partnerMapper.updateEntity(partner, request);
                    Partner savedPartner = partnerRepository.save(partner);
                    return partnerMapper.toResponse(savedPartner);
                });
    }

    public boolean delete(Long id) {
        if (partnerRepository.existsById(id)) {
            partnerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
