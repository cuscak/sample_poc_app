package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Document;
import andrej.cuscak.dms.model.dto.DocumentCreateDto;
import andrej.cuscak.dms.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public List<Document> getAllDocuments(){
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> findDocumentById(@PathVariable("id") Long id){
        return ResponseEntity.of(documentService.findDocumentById(id));
    }

    @GetMapping("/owner/{id}")
    public List<Document> findAllByOwner(@PathVariable("id") Long id){
        return documentService.findAllByOwner(id);
    }

    @GetMapping("/folder/{id}")
    public List<Document> findAllByFolder(@PathVariable("id") Long id){
        return documentService.findAllByFolder(id);
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
