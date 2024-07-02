package andrej.cuscak.dms.repository;

import andrej.cuscak.dms.model.Folder;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends ListCrudRepository<Folder, Long> {
    List<Folder> findAllByParent(Long id);

    @Query("SELECT COUNT(*) > 0 FROM folder WHERE parent =:folderId")
    boolean isParent(@Param("folderId") Long folderId);
}