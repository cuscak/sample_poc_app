package andrej.cuscak.dms.repository;

import andrej.cuscak.dms.model.Folder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends ListCrudRepository<Folder, Long> {
    List<Folder> findAllByParent(Long id);
}