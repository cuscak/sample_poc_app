package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.advice.FolderDeletionException;
import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.model.dto.FolderCreateDto;
import andrej.cuscak.dms.service.FolderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(FolderController.class)
public class FolderControllerTest {

    private static final String FOLDERS_API_URL = "/api/v1/folders";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FolderService folderService;

    private static Pageable pageable;
    private static Page<Folder> mockPage;

    @BeforeAll
    public static void setUp() {
        Folder root = new Folder(1L, "Root", null);
        Folder child1 = new Folder(2L, "Child 1", AggregateReference.to(1L));
        Folder child2 = new Folder(3L, "Child 2", AggregateReference.to(1L));
        Folder child3 = new Folder(4L, "Child in Child 1", AggregateReference.to(2L));

        List<Folder> mockFolders = Arrays.asList(root, child1, child2, child3);

        pageable = PageRequest.of(0, 10);
        mockPage = new PageImpl<>(mockFolders, pageable, mockFolders.size());
    }


    @Test
    void should_return_all_folders() throws Exception {
        when(folderService.findAll(pageable)).thenReturn(mockPage);

        mockMvc.perform(get(FOLDERS_API_URL)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(mockPage.getContent().size())))
                .andExpect(jsonPath("$.content[0].name").value("Root"))
                .andExpect(jsonPath("$.content[1].name").value("Child 1"));

        verify(folderService).findAll(pageable);
    }

    @Test
    void should_return_page_with_2_folders() throws Exception {
        Folder root = new Folder(1L, "Root", null);
        Folder child1 = new Folder(2L, "Child 1", AggregateReference.to(1L));
        List<Folder> mockFolders = Arrays.asList(root, child1);

        pageable = PageRequest.of(0, 2);
        mockPage = new PageImpl<>(mockFolders, pageable, mockFolders.size());

        when(folderService.findAll(pageable)).thenReturn(mockPage);

        mockMvc.perform(get(FOLDERS_API_URL)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Root"))
                .andExpect(jsonPath("$.content[1].name").value("Child 1"));

        verify(folderService).findAll(pageable);
    }

    @Test
    void should_return_all_child_folders() throws Exception {
        Long parentId = 1L; // Assuming 1L is the ID of the parent folder
        Folder child1 = new Folder(2L, "Child 1", AggregateReference.to(parentId));
        Folder child2 = new Folder(3L, "Child 2", AggregateReference.to(parentId));
        List<Folder> childFolders = Arrays.asList(child1, child2);
        Page<Folder> mockChildPage = new PageImpl<>(childFolders, pageable, childFolders.size());

        when(folderService.findAllFoldersByParentId(parentId, pageable)).thenReturn(mockChildPage);

        mockMvc.perform(get(FOLDERS_API_URL + "/parent/" + parentId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(mockChildPage.getContent().size())));
    }

    @Test
    void should_create_new_folder() throws Exception {

        Folder createdFolder = new Folder(5L, "New Folder", AggregateReference.to(1L));

        when(folderService.createFolder(any(FolderCreateDto.class))).thenReturn(createdFolder);

        mockMvc.perform(post(FOLDERS_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"New Folder\", \"parentId\": 1 }")) // Adjust JSON structure based on FolderCreateDto fields
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdFolder.id()))
                .andExpect(jsonPath("$.name").value(createdFolder.name()));

        verify(folderService).createFolder(any(FolderCreateDto.class));
    }

    @Test
    void should_not_create_new_folder_with_invalid_name() throws Exception {
        mockMvc.perform(post(FOLDERS_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"\", \"parentId\": 1 }")) // Adjust JSON structure based on FolderCreateDto fields
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.field").value("Validation Error"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("Folder must have a name"));
    }

    @Test
    void should_delete_folder() throws Exception {
        Long folderId = 3L;

        when(folderService.deleteFolder(folderId)).thenReturn(true);

        mockMvc.perform(delete(FOLDERS_API_URL + "/{folderId}", folderId))
                .andExpect(status().isNoContent());

        verify(folderService).deleteFolder(folderId);
    }

    @Test
    void should_not_delete_folder_if_contains_other_folders() throws Exception {
        Long folderId = 1L;

        when(folderService.deleteFolder(folderId)).thenThrow(new FolderDeletionException("Cannot delete folder because it contains other folders"));

        mockMvc.perform(delete(FOLDERS_API_URL + "/{folderId}", folderId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.field").value("Folder Error"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("Cannot delete folder because it contains other folders"));
    }
}