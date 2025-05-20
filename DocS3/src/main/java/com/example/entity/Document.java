package com.example.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int documentId;

    private String documentName;
    private String author;
    private LocalDateTime uploadDate;
    private LocalDateTime updateDate;

    @Lob
    private String content;

    @Transient
    private String formattedUploadDate;

    @Transient
    private String formattedUpdateDate;

    // Геттеры и сеттеры
    public int getDocumentId() { return documentId; }
    public void setDocumentId(int documentId) { this.documentId = documentId; }

    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFormattedUploadDate() { return formattedUploadDate; }
    public void setFormattedUploadDate(String formattedUploadDate) {
        this.formattedUploadDate = formattedUploadDate;
    }

    public String getFormattedUpdateDate() { return formattedUpdateDate; }
    public void setFormattedUpdateDate(String formattedUpdateDate) {
        this.formattedUpdateDate = formattedUpdateDate;
    }
}