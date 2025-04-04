package com.example.demo.service;

import com.example.demo.rest.dto.DocumentDtos.ContentRequestDto;
import com.example.demo.rest.dto.DocumentDtos.DocumentListDTO;
import com.example.demo.rest.dto.DocumentDtos.DocumentResponse;
import com.example.demo.rest.dto.DocumentDtos.NewDocumentRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DocumentService {
    Long createDocument(NewDocumentRequest request, Authentication authentication);
    void addNewUserToDocument(Long userId, Long documentId);
    List<DocumentListDTO> getDocumentsForCurrentUser(Authentication authentication);
    ContentRequestDto findById(Long id);
    DocumentResponse updateDocument(Long id, ContentRequestDto contentRequestDto);
}
