package andrej.cuscak.dms.service;

import andrej.cuscak.dms.model.Document;
import andrej.cuscak.dms.model.DocumentMetaData;
import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.model.Owner;
import andrej.cuscak.dms.model.dto.DocumentCreateDto;
import andrej.cuscak.dms.repository.DocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Document findDocumentByTitle(String title){
        return documentRepository.findByTitle(title);
    }

    public Page<Document> getAllDocuments(Pageable pageable){
        return documentRepository.findAll(pageable);
    }

    public Page<Document> findByOwner(Long id, Pageable pageable) {
        return documentRepository.findByOwner(id, pageable);
    }

    public Page<Document> findByFolder(Long id, Pageable pageable) {
        return documentRepository.findByFolder(id, pageable);
    }

    public Document createDocument(DocumentCreateDto documentCreateDto){
        DocumentMetaData meta = new DocumentMetaData(
                LocalDateTime.now(),
                null,
                documentCreateDto.getDescription(),
                documentCreateDto.getSize(),
                documentCreateDto.getDocumentType()
        );

        Optional<Owner> ownerEntity = ownerService.findOwnerById(documentCreateDto.getOwnerId());
        if (ownerEntity.isEmpty()){
            throw new NoSuchElementException("Owner with id " + documentCreateDto.getOwnerId() +  " is not present");
        }

        Optional<Folder> folderEntity = folderService.findFolderById(documentCreateDto.getFolderId());
        if (folderEntity.isEmpty()){
            throw new NoSuchElementException("Folder with id " + documentCreateDto.getFolderId() +  " is not present");
        }

        AggregateReference<Owner, Long> owner = AggregateReference.to(ownerEntity.get().id());
        AggregateReference<Folder, Long> folder = AggregateReference.to(folderEntity.get().id());

        Document doc = new Document(null,
                documentCreateDto.getTitle(),
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

    public Document updateDocument(Document updatedDocument) {
        //check if references exist
        Optional<Owner> ownerEntity = ownerService.findOwnerById(updatedDocument.owner().getId());
        if (ownerEntity.isEmpty()){
            throw new NoSuchElementException("Owner with id " + updatedDocument.owner().getId() +  " is not present");
        }

        Optional<Folder> folderEntity = folderService.findFolderById(updatedDocument.folder().getId());
        if (folderEntity.isEmpty()){
            throw new NoSuchElementException("Folder with id " + updatedDocument.folder().getId() +  " is not present");
        }

        //todo meta validations
        //update updatedOn
        DocumentMetaData updatedMeta = updatedDocument.metaData().withUpdatedOn(LocalDateTime.now());

        Document updatedDocMeta = updatedDocument.withMetaData(updatedMeta);


        return documentRepository.save(updatedDocMeta);
    }
}
