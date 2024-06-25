package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Document;
import andrej.cuscak.dms.model.dto.DocumentCreationDto;
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
    public ResponseEntity<Document> createDocument(@RequestBody @Valid DocumentCreationDto documentcreationDto){
        return new ResponseEntity<>(
                documentService.createDocument(documentcreationDto),
                HttpStatus.CREATED
        );
    }
}
