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

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final DocumentRepository documentRepository;

    public FolderService(FolderRepository folderRepository, DocumentRepository documentRepository) {
        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
    }

    public Page<Folder> findAll(Pageable pageable) {
        return folderRepository.findAll(pageable);
    }

    public List<Folder> findAllByParent(Long id){
        return folderRepository.findAllByParent(id);
    }

    public Optional<Folder> findFolderById(Long id){
        return folderRepository.findById(id);
    }

    public boolean deleteFolder(Long id) {
        if (!folderRepository.existsById(id)) {
            return false; // Folder doesn't exist
        }

        if (documentRepository.existsByFolderId(id)) {
            throw new FolderDeletionException("Cannot delete folder because it contains documents.");
        }

        if (folderRepository.isParent(id)) {
            throw new FolderDeletionException("Cannot delete folder because it contains other folders.");
        }

        folderRepository.deleteById(id);
        return true;
    }

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
}