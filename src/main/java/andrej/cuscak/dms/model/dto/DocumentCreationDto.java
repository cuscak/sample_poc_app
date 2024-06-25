package andrej.cuscak.dms.model.dto;

import andrej.cuscak.dms.model.DocumentTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DocumentCreationDto {
    @NotBlank(message = "Document must have a title")
    private String title;
    private String description;
    @Positive
    private Integer size;
    @NotNull(message = "Document must have an owner. Please provide ownerId")
    private Long ownerId;
    @NotNull(message = "Document must be in a folder. Please provide folderId")
    private Long folderId;
    private DocumentTypes documentType;

    public DocumentCreationDto(String title, String description, Integer size, Long ownerId, Long folderId, DocumentTypes documentType) {
        this.title = title;
        this.description = description;
        this.size = size;
        this.ownerId = ownerId;
        this.folderId = folderId;
        this.documentType = documentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public DocumentTypes getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypes documentType) {
        this.documentType = documentType;
    }
}
