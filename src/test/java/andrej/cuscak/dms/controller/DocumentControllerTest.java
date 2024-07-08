package andrej.cuscak.dms.controller;

import andrej.cuscak.dms.model.Document;
import andrej.cuscak.dms.model.DocumentMetaData;
import andrej.cuscak.dms.model.DocumentTypes;
import andrej.cuscak.dms.model.dto.DocumentCreateDto;
import andrej.cuscak.dms.service.DocumentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DocumentService documentService;

    private static List<Document> mockDocuments;

    @BeforeAll
    public static void setUp() {
        DocumentMetaData meta1 = new DocumentMetaData(
                LocalDateTime.now(),
                null,
                "This is the description for Sample Document 1.",
                151,
                DocumentTypes.TEXT
        );

        DocumentMetaData meta2 = new DocumentMetaData(
                LocalDateTime.now(),
                null,
                "This is the description for Sample Document 2.",
                152,
                DocumentTypes.ZIP
        );

        mockDocuments = Arrays.asList(
                new Document(
                        1L,
                        "Document 1",
                        meta1,
                        AggregateReference.to(1L),
                        AggregateReference.to(1L)),
                new Document(
                        2L,
                        "Document 2",
                        meta2,
                        AggregateReference.to(1L),
                        AggregateReference.to(1L))
        );
    }

    @Test
    public void should_get_all_documents() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Document> mockPage = new PageImpl<>(mockDocuments, pageable, mockDocuments.size());

        when(documentService.getAllDocuments(pageable)).thenReturn(mockPage);

        ResultActions result = mvc.perform(get("/api/v1/document")
                .param("page", "0")
                .param("size", "10"));

        verify(documentService).getAllDocuments(pageable);

        // Assert the response status and content
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].documentId").value(1))
                .andExpect(jsonPath("$.content[1].documentId").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Document 1"))
                .andExpect(jsonPath("$.content[1].title").value("Document 2"))
                .andExpect(jsonPath("$.content[0].metaData.documentType").value(DocumentTypes.TEXT.name()))
                .andExpect(jsonPath("$.content[1].metaData.documentType").value(DocumentTypes.ZIP.name()));

    }

    @Test
    public void should_get_document_by_id() throws Exception {
        when(documentService.findDocumentById(1L)).thenReturn(Optional.ofNullable(mockDocuments.getFirst()));

        mvc.perform(get("/api/v1/document/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.documentId").value(1))
                .andExpect(jsonPath("$..title").value("Document 1"));

        verify(documentService).findDocumentById(1L);
    }

    @Test
    @DisplayName("Should return not found for non-existent document ID")
    public void should_return_not_found_for_non_existent_doc_id() throws Exception {
        Long nonExistentId = 111L;
        when(documentService.findDocumentById(nonExistentId))
                .thenThrow(new NoSuchElementException("Document not found"));

        mvc.perform(get("/api/v1/document/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Document not found"));

        verify(documentService).findDocumentById(nonExistentId);
    }

    @Test
    public void should_return_all_documents_by_owner() throws Exception {
        Long ownerId = 1L;
        when(documentService.findAllByOwner(ownerId)).thenReturn(mockDocuments);

        mvc.perform(get("/api/v1/document/owner/{id}", ownerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].documentId").value(1))
                .andExpect(jsonPath("$[1].documentId").value(2));

        verify(documentService).findAllByOwner(ownerId);
    }

    @Test
    public void should_return_all_documents_by_folder() throws Exception {
        Long folderId = 1L;
        when(documentService.findAllByFolder(folderId)).thenReturn(mockDocuments);

        mvc.perform(get("/api/v1/document/folder/{id}", folderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].documentId").value(1))
                .andExpect(jsonPath("$[1].documentId").value(2));

        verify(documentService).findAllByFolder(folderId);
    }

    @Test
    public void should_create_document_successfully() throws Exception {
        DocumentCreateDto newDocumentDto = new DocumentCreateDto(
                "New Document",
                "This is the description for New Document.",
                150,
                1L,
                1L,
                DocumentTypes.MEDIA
        );

        Document createdDocument = new Document(
                3L,
                newDocumentDto.getTitle(),
                new DocumentMetaData(
                        LocalDateTime.now(),
                        null,
                        newDocumentDto.getDescription(),
                        newDocumentDto.getSize(),
                        newDocumentDto.getDocumentType()
                ),
                AggregateReference.to(1L),
                AggregateReference.to(1L)
        );

        when(documentService.createDocument(any(DocumentCreateDto.class))).thenReturn(createdDocument);

        mvc.perform(post("/api/v1/document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"title\": \"New Document\",\n" +
                                "    \"description\": \"This is the description for New Document.\",\n" +
                                "    \"size\": 150,\n" +
                                "    \"ownerId\": 1,\n" +
                                "    \"folderId\": 1,\n" +
                                "    \"documentType\": \"MEDIA\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.documentId").exists());

        verify(documentService).createDocument(any(DocumentCreateDto.class));
    }

    @Test
    @DisplayName("Should return bad request for invalid document creation")
    public void should_return_bad_request_for_invalid_document_creation() throws Exception {
        String invalidDocumentJson = "{\n" +
                "    \"title\": \"\",\n" + // Invalid due to empty title
                "    \"description\": \"This is a description.\",\n" +
                "    \"size\": 150,\n" +
                "    \"ownerId\": 1,\n" +
                "    \"folderId\": 1,\n" +
                "    \"documentType\": \"MEDIA\"\n" +
                "}";

        mvc.perform(post("/api/v1/document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDocumentJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_document_successfully() throws Exception {
        Long documentId = 1L;
        Document updatedDocument = new Document(
                documentId,
                "Updated Document",
                new DocumentMetaData(
                        null,
                        LocalDateTime.now(),
                        "This is the updated description for Updated Document.",
                        150,
                        DocumentTypes.TEXT
                ),
                AggregateReference.to(2L),
                AggregateReference.to(2L)
        );

        when(documentService.updateDocument(any(Document.class))).thenReturn(updatedDocument);

        mvc.perform(put("/api/v1/document/{documentId}", documentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\n" +
                                        "    \"documentId\": 1,\n" +
                                        "    \"title\": \"Updated Document\",\n" +
                                        "    \"metaData\": {\n" +
                                        "        \"createdOn\": null,\n" +
                                        "        \"updatedOn\": null,\n" +
                                        "        \"description\": \"This is the updated description for Updated Document.\",\n" +
                                        "        \"size\": 150,\n" +
                                        "        \"documentType\": \"TEXT\"\n" +
                                        "    },\n" +
                                        "    \"owner\": {\n" +
                                        "        \"id\": 2\n" +
                                        "    },\n" +
                                        "    \"folder\": {\n" +
                                        "        \"id\": 2\n" +
                                        "    }\n" +
                                        "}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.documentId").value(documentId));

        verify(documentService).updateDocument(any(Document.class));
    }

    @Test
    void should_delete_document_with_with_id() throws Exception {
        Long documentId = 2L;
        when(documentService.deleteDocument(documentId)).thenReturn(true);

        mvc.perform(delete("/api/v1/document/2"))
                .andExpect(status().isNoContent());

        verify(documentService).deleteDocument(documentId);

    }

    @Test
    public void should_return_not_found_when_deleting_doc_with_non_existent_id() throws Exception {
        Long nonExistentId = 33L; // Non-existent document ID
        when(documentService.deleteDocument(nonExistentId)).thenReturn(false);

        mvc.perform(delete("/api/v1/document/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        verify(documentService).deleteDocument(nonExistentId);
    }
}
