package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.model.dto.FolderCreateDto;
import andrej.cuscak.dms.service.FolderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/folder")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping
    public Page<Folder> findAll(Pageable pageable){
        return folderService.findAll(pageable);
    }

    @GetMapping("/parent/{id}")
    public List<Folder> findAllByParent(@PathVariable("id") Long id){
        return folderService.findAllByParent(id);
    }

    @PostMapping
    public ResponseEntity<Folder> createFolder(@RequestBody @Valid FolderCreateDto newFolder) {
        return new ResponseEntity<>(
                folderService.createFolder(newFolder),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable("id") Long id){
        boolean isDeleted = folderService.deleteFolder(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
