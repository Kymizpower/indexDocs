package com.example.controller;

import com.example.entity.Document;
import com.example.service.DocumentService;
import com.example.service.LuceneIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final LuceneIndexer luceneIndexer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public DocumentController(DocumentService documentService, LuceneIndexer luceneIndexer) {
        this.documentService = documentService;
        this.luceneIndexer = luceneIndexer;
    }

    @GetMapping
    public String showDocuments(Model model) {
        List<Document> documents = documentService.getAllDocuments();

        // Форматируем даты для каждого документа
        documents.forEach(doc -> {
            doc.setFormattedUploadDate(
                    doc.getUploadDate().format(formatter)
            );

            if(doc.getUpdateDate() != null) {
                doc.setFormattedUpdateDate(
                        doc.getUpdateDate().format(formatter)
                );
            }
        });

        model.addAttribute("documents", documents);
        return "documents";
    }

    @PostMapping("/upload")
    public String uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentName") String name,
            @RequestParam("author") String author,
            Model model) {

        Document document = new Document();
        document.setDocumentName(name);
        document.setAuthor(author);
        document.setUploadDate(LocalDateTime.now());

        try {
            document.setContent(new String(file.getBytes(), StandardCharsets.UTF_8));
            documentService.createDocument(document);

            // Проверка ID после сохранения
            System.out.println("Created document ID: " + document.getDocumentId());

        } catch (IOException e) {
            model.addAttribute("error", "Ошибка загрузки файла: " + e.getMessage());
            return "errorPage";
        }
        return "redirect:/documents";
    }

    @GetMapping("/view/{documentId}")
    public String viewDocument(
            @PathVariable int documentId,
            Model model) {

        Document document = documentService.getDocumentById(documentId);

        // Форматируем даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        document.setFormattedUploadDate(
                document.getUploadDate().format(formatter)
        );

        if (document.getUpdateDate() != null) {
            document.setFormattedUpdateDate(
                    document.getUpdateDate().format(formatter)
            );
        }

        model.addAttribute("document", document);
        return "viewDocument";
    }

    @GetMapping("/download/{documentId}")
    public void downloadDocument(@PathVariable int documentId, HttpServletResponse response) throws IOException {
        Document document = documentService.getDocumentById(documentId);
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + document.getDocumentName() + ".txt\"");
        response.getWriter().write(document.getContent());
    }

    @PostMapping("/delete/{documentId}")
    public String deleteDocument(@PathVariable int documentId) {
        documentService.deleteDocument(documentId);
        return "redirect:/documents";
    }

    @GetMapping("/update/{documentId}")
    public String showUpdateForm(@PathVariable int documentId, Model model) {
        model.addAttribute("document", documentService.getDocumentById(documentId));
        return "updateDocument";
    }

    @PostMapping("/reindex")
    public String reindexAll(Model model) {
        try {
            List<Document> documents = documentService.getAllDocumentsSorted();
            luceneIndexer.reindexAll(documents);
            model.addAttribute("message", "Индексы успешно обновлены");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка реиндексации: " + e.getMessage());
        }
        return "redirect:/documents";
    }

    @PostMapping("/update/{documentId}")
    public String updateDocument(
            @PathVariable int documentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentName") String name,
            @RequestParam("author") String author,
            Model model) throws IOException {

        Document document = documentService.getDocumentById(documentId);

        // Удаляем старую версию из индекса
        try {
            luceneIndexer.deleteDocument(documentId);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления индекса: " + e.getMessage());
            return "errorPage";
        }

        // Обновляем данные
        document.setDocumentName(name);
        document.setAuthor(author);
        document.setContent(new String(file.getBytes()));
        document.setUpdateDate(LocalDateTime.now());

        // Сохраняем и индексируем новую версию
        documentService.updateDocument(document);
        try {
            luceneIndexer.indexDocument(document);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка индексации: " + e.getMessage());
            return "errorPage";
        }

        return "redirect:/documents";
    }

    @GetMapping("/search")
    public String searchDocuments(@RequestParam String query, Model model) {
        try {
            List<Integer> documentIds = luceneIndexer.searchDocuments(query);
            List<Document> documents = documentService.getDocumentsByIds(documentIds);

            // Форматирование дат
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            documents.forEach(doc -> {
                doc.setFormattedUploadDate(doc.getUploadDate().format(formatter));
                if(doc.getUpdateDate() != null) {
                    doc.setFormattedUpdateDate(doc.getUpdateDate().format(formatter));
                }
            });

            model.addAttribute("documents", documents);
        } catch (Exception e) {
            model.addAttribute("error", "Search error: " + e.getMessage());
        }
        return "documents";
    }
}