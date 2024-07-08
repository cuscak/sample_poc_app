package andrej.cuscak.dms.model;

import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("document_meta")
public record DocumentMetaData(
        LocalDateTime createdOn,
        LocalDateTime updatedOn,
        String description,
        Integer size,
        DocumentTypes documentType
) {
    public DocumentMetaData withUpdatedOn(LocalDateTime updatedOn) {
        return new DocumentMetaData(createdOn, updatedOn, description, size, documentType);
    }
}