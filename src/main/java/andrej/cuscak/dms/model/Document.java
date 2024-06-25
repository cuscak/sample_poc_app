package andrej.cuscak.dms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;


public record Document(
        @Id Long documentId,
        String title,
        DocumentMetaData metaData,
        AggregateReference<Owner, Long> owner,
        AggregateReference<Folder, Long> folder
) {
}