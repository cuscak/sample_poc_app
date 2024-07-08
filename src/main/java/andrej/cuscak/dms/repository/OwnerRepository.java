package andrej.cuscak.dms.repository;

import andrej.cuscak.dms.model.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends PagingAndSortingRepository<Owner, Long>, CrudRepository<Owner, Long> {
}
