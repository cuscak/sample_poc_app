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

/**
 * Controller for managing folder-related operations in the document management system.
 * Provides REST endpoints for creating, retrieving, and deleting folders, as well as
 * listing all folders or specifically those by parent ID.
 *
 * <p>This controller handles HTTP requests and delegates business logic to the {@link FolderService}.
 * It is responsible for input validation, constructing response entities, and defining the URL
 * mappings for folder-related actions.</p>
 *
 * @see FolderService for service layer operations
 */
@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    /**
     * Retrieves all folders, supporting pagination.
     *
     * @param pageable the pagination information
     * @return a page of folders
     */
    @GetMapping
    public Page<Folder> findAll(Pageable pageable){
        return folderService.findAll(pageable);
    }

    /**
     * Retrieves all child folders of a given parent folder, supporting pagination.
     *
     * @param parentId the ID of the parent folder
     * @param pageable the pagination information
     * @return a page of child folders
     */
    @GetMapping("/parent/{parentId}")
    public Page<Folder> findAllByParent(@PathVariable("parentId") Long parentId, Pageable pageable){
        return folderService.findAllFoldersByParentId(parentId, pageable);
    }

    /**
     * Creates a new folder with the given details.
     *
     * @param newFolder the details of the new folder
     * @return a response entity containing the created folder and HTTP status
     */
    @PostMapping
    public ResponseEntity<Folder> createFolder(@RequestBody @Valid FolderCreateDto newFolder) {
        return new ResponseEntity<>(
                folderService.createFolder(newFolder),
                HttpStatus.CREATED
        );
    }

    /**
     * Deletes a folder by its ID.
     *
     * @param folderId the ID of the folder to delete
     * @return a response entity with HTTP status indicating the result of the operation
     */
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Boolean> deleteFolder(@PathVariable("folderId") Long folderId){
        return new ResponseEntity<>(
                folderService.deleteFolder(folderId),
                HttpStatus.NO_CONTENT
        );
    }
}
