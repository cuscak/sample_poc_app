package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.service.FolderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
