package andrej.cuscak.dms.repository;

import andrej.cuscak.dms.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, Long>, CrudRepository<Document, Long> {

    Optional<Document> findDocumentByDocumentId(Long id);

    Document findByTitle(@Param("title") String title);

    Page<Document> findByOwner(Long id, Pageable pageable);

    Page<Document> findByFolder(Long id, Pageable pageable);

    @Query("SELECT COUNT(*) > 0 FROM document WHERE folder = :folderId")
    boolean existsByFolderId(Long folderId);
}