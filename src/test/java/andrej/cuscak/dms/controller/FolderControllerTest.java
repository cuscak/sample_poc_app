package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Folder;
import andrej.cuscak.dms.service.FolderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FolderController.class)
class FolderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FolderService folderService;

    @BeforeAll
    static void setUp() {
        Folder root = new Folder(1L, "Root", null);
        Folder child1 = new Folder(2L, "Child 1", AggregateReference.to(1L));
        Folder child2 = new Folder(3L, "Child 2", AggregateReference.to(1L));
        Folder child3 = new Folder(4L, "Child in Child 1", AggregateReference.to(2L));
    }


    @Test
    void should_return_all_folders () throws Exception {

    }

    @Test
    void findAllByParent() {
    }

    @Test
    void createFolder() {
    }

    @Test
    void deleteFolder() {
    }
}