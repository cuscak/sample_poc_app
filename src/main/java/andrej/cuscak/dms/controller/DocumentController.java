package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Document;
import andrej.cuscak.dms.model.dto.DocumentCreateDto;
import andrej.cuscak.dms.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public Page<Document> getAllDocuments(Pageable pageable){
        return documentService.getAllDocuments(pageable);
    }

    @GetMapping("/{documentId}")
    public Document findDocumentById(@PathVariable("documentId") Long documentId){
        Optional<Document> doc = documentService.findDocumentById(documentId);
        if(doc.isPresent()){
            return doc.get();
        } else {
            throw new NoSuchElementException("Document not Found");
        }

        //return ResponseEntity.of(documentService.findDocumentById(id));
    }

    @GetMapping("/owner/{ownerId}")
    public Page<Document> findByOwner(@PathVariable("ownerId") Long ownerId, Pageable pageable){
        return documentService.findByOwner(ownerId, pageable);
    }

    @GetMapping("/folder/{folderId}")
    public Page<Document> findAllByFolder(@PathVariable("folderId") Long folderId, Pageable pageable){
        return documentService.findByFolder(folderId, pageable);
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody @Valid DocumentCreateDto newDocument){
        return new ResponseEntity<>(
                documentService.createDocument(newDocument),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long documentId, @RequestBody Document updatedDocument) {
        return new ResponseEntity<>(
                documentService.updateDocument(updatedDocument),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("documentId") Long documentId) {
        boolean isDeleted = documentService.deleteDocument(documentId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
