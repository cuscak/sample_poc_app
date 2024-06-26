package andrej.cuscak.dms.service;

import andrej.cuscak.dms.advice.FolderDeletionException;
import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.repository.DocumentRepository;
import andrej.cuscak.dms.repository.FolderRepository;
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

    public List<Folder> findAll(){
        return folderRepository.findAll();
    }

    public List<Folder> findAllByParent(Long id){
        return folderRepository.findAllByParent(id);
    }

    public Optional<Folder> findFolderById(Long id){
        return folderRepository.findById(id);
    }

    public boolean deleteFolder(Long id) {
        if (documentRepository.existsByFolderId(id)) {
            throw new FolderDeletionException("Cannot delete folder because it contains documents.");
        }

        if (folderRepository.existsById(id)) {
            folderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}