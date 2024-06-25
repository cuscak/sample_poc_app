package andrej.cuscak.dms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public record Folder(
        @Id Long id,
        String name,
        AggregateReference<Folder, Long> parent
) {
}