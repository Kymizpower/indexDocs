package com.example.service;

import com.example.dao.DocumentDAO;
import com.example.entity.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentDAO documentDAO;
    private final LuceneIndexer luceneIndexer;

    @Autowired
    public DocumentService(DocumentDAO documentDAO, LuceneIndexer luceneIndexer) {
        this.documentDAO = documentDAO;
        this.luceneIndexer = luceneIndexer;
    }

    public void createDocument(Document document) {
        document.setUploadDate(LocalDateTime.now());
        documentDAO.save(document);
        try {
            luceneIndexer.indexDocument(document);
        } catch (Exception e) {
            throw new RuntimeException("Indexing failed", e);
        }
    }

    public List<Document> getAllDocuments() {
        return documentDAO.findAll();
    }

    public List<Document> getAllDocumentsSorted() {
        return documentDAO.findAllSorted();
    }

    public Document getDocumentById(int id) {
        return documentDAO.findById(id);
    }

    public List<Document> getDocumentsByIds(List<Integer> ids) {
        return ids.stream()
                .map(this::getDocumentById)
                .collect(Collectors.toList());
    }

    public void updateDocument(Document document) {
        document.setUpdateDate(LocalDateTime.now());
        documentDAO.update(document);
    }

    public void deleteDocument(int id) {
        documentDAO.delete(id);
        try {
            luceneIndexer.deleteDocument(id);
        } catch (Exception e) {
            throw new RuntimeException("Index deletion failed", e);
        }
    }
}