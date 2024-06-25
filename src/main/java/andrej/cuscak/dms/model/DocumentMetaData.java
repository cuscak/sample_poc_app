package andrej.cuscak.dms.model;

import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("document_meta")
public record DocumentMetaData(
        LocalDateTime createdOn,
        LocalDateTime UpdatedOn,
        String description,
        Integer size,
        DocumentTypes documentType
) {
}