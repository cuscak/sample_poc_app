package andrej.cuscak.dms.service;

import andrej.cuscak.dms.model.Owner;
import andrej.cuscak.dms.repository.OwnerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Page<Owner> getAllOwners(Pageable pageable){
        return ownerRepository.findAll(pageable);
    }

    public Optional<Owner> findOwnerById(Long id){
        return ownerRepository.findById(id);
    }
}