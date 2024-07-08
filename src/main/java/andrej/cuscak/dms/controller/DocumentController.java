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
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public Page<Document> getAllDocuments(Pageable pageable){
        return documentService.getAllDocuments(pageable);
    }

    @GetMapping("/{id}")
    public Document findDocumentById(@PathVariable("id") Long id){
        Optional<Document> doc = documentService.findDocumentById(id);
        if(doc.isPresent()){
            return doc.get();
        } else {
            throw new NoSuchElementException("Document not Found");
        }

        //return ResponseEntity.of(documentService.findDocumentById(id));
    }

    @GetMapping("/owner/{id}")
    public Page<Document> findByOwner(@PathVariable("id") Long id, Pageable pageable){
        return documentService.findByOwner(id, pageable);
    }

    @GetMapping("/folder/{id}")
    public Page<Document> findAllByFolder(@PathVariable("id") Long id, Pageable pageable){
        return documentService.findByFolder(id, pageable);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("id") Long id) {
        boolean isDeleted = documentService.deleteDocument(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
