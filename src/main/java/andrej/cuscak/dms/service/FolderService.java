package andrej.cuscak.dms.service;

import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.repository.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FolderService {
    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
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
}