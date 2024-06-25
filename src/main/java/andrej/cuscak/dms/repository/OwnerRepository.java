package andrej.cuscak.dms.repository;

import andrej.cuscak.dms.model.Owner;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends ListCrudRepository<Owner, Long> {
}
