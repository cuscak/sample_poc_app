package andrej.cuscak.dms.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record FolderCreateDto(
        @NotBlank(message = "Folder must have a name")
        String name,
        Optional<Long> parent
) {
}
