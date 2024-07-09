package andrej.cuscak.dms.service;

import andrej.cuscak.dms.advice.FolderDeletionException;
import andrej.cuscak.dms.advice.ParentFolderNotFoundException;
import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.model.dto.FolderCreateDto;
import andrej.cuscak.dms.repository.DocumentRepository;
import andrej.cuscak.dms.repository.FolderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing folders within the document management system.
 * Provides functionality for creating, retrieving and deleting folders,
 * as well as checking folder existence and content.
 */
@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final DocumentRepository documentRepository;

    /**
     * Constructs a FolderService with the necessary repository dependencies.
     *
     * @param folderRepository   the repository for folder operations
     * @param documentRepository the repository for document operations
     */
    public FolderService(FolderRepository folderRepository, DocumentRepository documentRepository) {
        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
    }

    /**
     * Retrieves all folders with pagination support.
     *
     * @param pageable the pagination information
     * @return a page of folders
     */
    public Page<Folder> findAll(Pageable pageable) {
        return folderRepository.findAll(pageable);
    }

    /**
     * Retrieves all child folders of a given parent folder with pagination support.
     *
     * @param id       the ID of the parent folder
     * @param pageable the pagination information
     * @return a page of child folders
     */
    public Page<Folder> findAllFoldersByParentId(Long id, Pageable pageable){
        return folderRepository.findAllByParent(id, pageable);
    }


    /**
     * Retrieves a folder by its ID.
     *
     * @param id the ID of the folder to retrieve
     * @return an Optional containing the found folder or empty if not found
     */
    public Optional<Folder> findFolderById(Long id){
        return folderRepository.findById(id);
    }

    /**
     * Deletes a folder by its ID after checking for existence and ensuring it is empty.
     *
     * @param id the ID of the folder to delete
     * @return true if the folder was successfully deleted
     * @throws FolderDeletionException if the folder does not exist, contains documents, or contains other folders
     */
    public boolean deleteFolder(Long id) {
        checkFolderExists(id);
        checkFolderIsEmpty(id);
        folderRepository.deleteById(id);
        return true;
    }

    /**
     * Creates a new folder with the specified details. If a parent folder is specified,
     * it validates the existence of the parent folder.
     *
     * @param newFolder the details of the new folder to create
     * @return the created folder
     * @throws ParentFolderNotFoundException if the specified parent folder does not exist
     */
    public Folder createFolder(FolderCreateDto newFolder) {
        AggregateReference<Folder, Long> parentRef = null;
        if(newFolder.parent().isPresent()) {
            Long parentId = newFolder.parent().get();
            Folder parentFolder = folderRepository.findById(parentId).orElseThrow(
                    () -> new ParentFolderNotFoundException("No such parent folder")
            );
            parentRef = AggregateReference.to(parentFolder.id());
        }

        Folder folder = new Folder(null, newFolder.name(), parentRef);
        return folderRepository.save(folder);
    }

    /**
     * Checks if a folder exists by its ID.
     *
     * @param id the ID of the folder to check
     * @throws FolderDeletionException if the folder does not exist
     */
    private void checkFolderExists(Long id) {
        if (!folderRepository.existsById(id)) {
            throw new FolderDeletionException("Folder with id " + id + " does not exist.");
        }
    }

    /**
     * Checks if a folder is empty by its ID.
     *
     * @param id the ID of the folder to check
     * @throws FolderDeletionException if the folder contains documents or other folders
     */
    private void checkFolderIsEmpty(Long id) {
        if (documentRepository.existsByFolderId(id)) {
            throw new FolderDeletionException("Cannot delete folder because it contains documents.");
        }
        if (folderRepository.isParent(id)) {
            throw new FolderDeletionException("Cannot delete folder because it contains other folders.");
        }
    }

}