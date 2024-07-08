package andrej.cuscak.dms.service;

import andrej.cuscak.dms.model.Document;
import andrej.cuscak.dms.model.DocumentMetaData;
import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.model.Owner;
import andrej.cuscak.dms.model.dto.DocumentCreateDto;
import andrej.cuscak.dms.repository.DocumentRepository;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final OwnerService ownerService;
    private final FolderService folderService;

    public DocumentService(DocumentRepository documentRepository, OwnerService ownerService, FolderService folderService) {
        this.documentRepository = documentRepository;
        this.ownerService = ownerService;
        this.folderService = folderService;
    }

    public Optional<Document> findDocumentById(Long id){
        return documentRepository.findDocumentByDocumentId(id);
    }

    public List<Document> findDocumentByTitle(String title){
        return documentRepository.findByTitle(title);
    }

    public List<Document> getAllDocuments(){
        return documentRepository.findAll();
    }

    public List<Document> findAllByOwner(Long id) {
        return documentRepository.findAllByOwner(id);
    }

    public List<Document> findAllByFolder(Long id) {
        return documentRepository.findAllByFolder(id);
    }

    public Document createDocument(DocumentCreateDto documentcreationDto){
        DocumentMetaData meta = new DocumentMetaData(
                LocalDateTime.now(),
                null,
                documentcreationDto.getDescription(),
                documentcreationDto.getSize(),
                documentcreationDto.getDocumentType()
        );

        Optional<Owner> owner_entity = ownerService.findOwnerById(documentcreationDto.getOwnerId());
        if (owner_entity.isEmpty()){
            throw new NoSuchElementException("Owner with id " + documentcreationDto.getOwnerId() +  " is not present");
        }

        Optional<Folder> folder_entity = folderService.findFolderById(documentcreationDto.getFolderId());
        if (folder_entity.isEmpty()){
            throw new NoSuchElementException("Folder with id " + documentcreationDto.getFolderId() +  " is not present");
        }

        AggregateReference<Owner, Long> owner = AggregateReference.to(owner_entity.get().id());
        AggregateReference<Folder, Long> folder = AggregateReference.to(folder_entity.get().id());

        Document doc = new Document(null,
                documentcreationDto.getTitle(),
                meta,
                owner,
                folder);

        return documentRepository.save(doc);
    }

    public boolean deleteDocument(Long id){
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
