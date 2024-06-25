package andrej.cuscak.dms.service;

import andrej.cuscak.dms.model.Owner;
import andrej.cuscak.dms.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public List<Owner> getAllOwners(){
        return ownerRepository.findAll();
    }

    public Optional<Owner> findOwnerById(Long id){
        return ownerRepository.findById(id);
    }
}