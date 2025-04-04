package com.example.demo.service.impl;

import com.example.demo.config.SecurityUtils;
import com.example.demo.domain.*;

import com.example.demo.repos.DocumentRepo;
import com.example.demo.repos.UserDocumentRepo;
import com.example.demo.rest.dto.DocumentDtos.ContentRequestDto;
import com.example.demo.rest.dto.DocumentDtos.DocumentListDTO;
import com.example.demo.rest.dto.DocumentDtos.DocumentResponse;
import com.example.demo.rest.dto.DocumentDtos.NewDocumentRequest;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.DocumentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.print.Doc;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {


    DocumentRepo documentRepo;
    UserService userService;
    UserDocumentRepo userDocumentRepo;
    private  final SecurityUtils securityUtils;

    @Autowired
    public DocumentServiceImpl(DocumentRepo documentRepo, UserService userService, UserDocumentRepo userDocumentRepo,SecurityUtils securityUtils) {
        this.documentRepo = documentRepo;
        this.userService = userService;
        this.userDocumentRepo = userDocumentRepo;
        this.securityUtils = securityUtils;

    }

    public DocumentResponse convertDocumentToResponse(Document document )
    {
        return  new DocumentResponse(document.getId(), document.getTitle(), document.getContent(), document.getOwnerUser().getUsername(),document.getCreatedAt(),document.getUpdatedAt());
    }


    @Override
    public void addNewUserToDocument(Long userId, Long documentId) {
        // Найти документ по ID
        Optional<Document> element = documentRepo.findById(documentId);
        Document document = element.get();

        // Найти пользователя по ID
        User user = userService.findById(userId);

        // Установить связь пользователя с документом

        // Сохранить обновленного пользователя
        userService.save(user);
    }
    @Override
    public ContentRequestDto findById(Long id) {

        Optional<Document> element = documentRepo.findById(id);
        if (element.isPresent()) {
            Document document =  element.get();
            return new ContentRequestDto(document.getContent());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Документ не найден");
        }
    }


    @Override
    public List<DocumentListDTO> getDocumentsForCurrentUser(Authentication authentication) {


        Long userId = null;

        // Сначала проверяем обычную аутентификацию
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userId = userDetails.getId();
        }
        // Если обычной аутентификации нет, пытаемся получить ID из SecurityUtils
        else {
            userId = securityUtils.getCurrentUserId();
        }

        List<Document> documents =documentRepo.findAllDocumentsByUserId(userId);
        List<DocumentListDTO> result = new ArrayList<>();
        for (Document doc : documents) {
            DocumentListDTO dto = new DocumentListDTO(doc.getTitle(),doc.getId());
            result.add(dto);
        }

        return  result;

    }


    @Override
    public Long createDocument(NewDocumentRequest request, Authentication authentication) {


        Long userId = null;

        // Сначала проверяем обычную аутентификацию
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userId = userDetails.getId();
        }
        // Если обычной аутентификации нет, пытаемся получить ID из SecurityUtils
        else {
            userId = securityUtils.getCurrentUserId();
        }





        User author = userService.findById(userId);


        // Создаем новый документ и устанавливаем заголовок и владельца
        Document document = new Document();
        document.setTitle(request.getTitle());
        document.setOwnerUser(author); // Обязательно устанавливаем владельца!
        document.setContent(request.getContent());

        // Сохраняем документ, чтобы получить его ID
        Document savedDocument = documentRepo.save(document);







        // Создаем запись в таблице user_documents для установления связи "пользователь-документ"
        UserDocument userDocument = new UserDocument();

        // Предполагается, что у вас есть класс-ключ UserDocumentId, состоящий из userId и documentId
        userDocument.setId(new UserDocumentId(author.getId(), savedDocument.getId()));
        userDocument.setUser(author);
        userDocument.setDocument(savedDocument);
        userDocument.setPermissionLevel("write");
        userDocument.setAddedAt(Instant.now());




        // Сохраняем связь через репозиторий user_documents
        userDocumentRepo.save(userDocument);


        return savedDocument.getId();


    }

    @Override
    public DocumentResponse updateDocument(Long id, ContentRequestDto documentContentDto)
    {
        Optional<Document> element = documentRepo.findById(id);
        Document document = element.get();

        document.setContent(documentContentDto.getContent());
        document.setUpdatedAt(Instant.now());

        documentRepo.save(document);

        return convertDocumentToResponse(document);

    }




}
