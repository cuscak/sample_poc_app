package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.service.FolderService;
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
    public List<Folder> findAll(){
        return folderService.findAll();
    }

    @GetMapping("/parent/{id}")
    public List<Folder> findAllByParent(@PathVariable("id") Long id){
        return folderService.findAllByParent(id);
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
