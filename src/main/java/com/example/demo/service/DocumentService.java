package com.example.demo.service;

import com.example.demo.domain.Document;
import com.example.demo.rest.dto.DocumentDtos.ContentRequestDto;
import com.example.demo.rest.dto.DocumentDtos.DocumentListDTO;
import com.example.demo.rest.dto.DocumentDtos.DocumentResponse;
import com.example.demo.rest.dto.DocumentDtos.NewDocumentRequest;
import com.example.demo.rest.dto.UserDtos.UserDto;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    Long createDocument(NewDocumentRequest request, Authentication authentication) throws GitAPIException, IOException;

    Document findById(Long id);
    List<DocumentListDTO> getDocumentsForCurrentUser(Authentication authentication);

    DocumentResponse findById(Long id, boolean includeContent) throws IOException;

    DocumentResponse updateDocument(Long id, NewDocumentRequest updateDocumentRequest, Authentication authentication) throws GitAPIException, IOException;


    Long deleteDocument(Long id, Authentication authentication) throws IOException;
    void deleteAllDocuments() throws IOException;
    Long leaveDocument(Long documentId, Authentication authentication);

     UserDto inviteUserToDocument(Long documentId, String username, Authentication authentication);


}
