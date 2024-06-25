package andrej.cuscak.dms.model;

import org.springframework.data.annotation.Id;

public record Owner(
        @Id
        Long id,
        String username,
        String firstName,
        String lastName,
        String email
) {}
