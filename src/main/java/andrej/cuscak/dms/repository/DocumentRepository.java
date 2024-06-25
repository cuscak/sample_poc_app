package andrej.cuscak.dms.repository;

import andrej.cuscak.dms.model.Document;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends ListCrudRepository<Document, Long> {

    Optional<Document> findDocumentByDocumentId(Long id);

    @Query("SELECT * FROM document WHERE title = :title")
    List<Document> findByTitle(@Param("title") String title);

    List<Document> findAllByOwner(Long id);

    List<Document> findAllByFolder(Long id);
}