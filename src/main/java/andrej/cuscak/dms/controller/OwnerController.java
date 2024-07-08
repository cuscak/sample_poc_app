package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Owner;
import andrej.cuscak.dms.service.OwnerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/owner")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public Page<Owner> getAllOwners(Pageable pageable){
        return ownerService.getAllOwners(pageable);
    }

}
