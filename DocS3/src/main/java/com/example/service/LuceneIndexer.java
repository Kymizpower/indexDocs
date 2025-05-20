package com.example.service;

import com.example.entity.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class LuceneIndexer {
    private static final String INDEX_DIR = "lucene-index";

    public void indexDocument(Document doc) throws Exception {
        try (FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
             IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {

            org.apache.lucene.document.Document luceneDoc = new org.apache.lucene.document.Document();
            // Убедитесь, что ID документа существует
            if (doc.getDocumentId() == 0) {
                throw new IllegalStateException("Document ID is not set!");
            }

            luceneDoc.add(new StringField("ID", String.valueOf(doc.getDocumentId()), Field.Store.YES));
            luceneDoc.add(new TextField("title", doc.getDocumentName(), Field.Store.NO));
            luceneDoc.add(new TextField("author", doc.getAuthor(), Field.Store.NO));
            luceneDoc.add(new TextField("content", doc.getContent(), Field.Store.NO));

            writer.addDocument(luceneDoc);
        }
    }

    public void deleteDocument(int documentId) throws Exception {
        try (FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
             IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {

            writer.deleteDocuments(new Term("id", String.valueOf(documentId)));
            writer.commit();
        }
    }

    public List<Integer> searchDocuments(String queryString) throws Exception {
        try (FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
             DirectoryReader reader = DirectoryReader.open(directory)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            String[] fields = {"title", "author", "content"};
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
            Query query = parser.parse(queryString);
            TopDocs topDocs = searcher.search(query, 100);

            List<Integer> documentIds = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                org.apache.lucene.document.Document luceneDoc = searcher.doc(scoreDoc.doc);
                String idStr = luceneDoc.get("id");
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        documentIds.add(Integer.parseInt(idStr));
                    } catch (NumberFormatException e) {
                        // Логируем ошибку, но не прерываем выполнение
                        System.err.println("Invalid document ID: " + idStr);
                    }
                }
            }
            return documentIds;
        }
    }

    public void reindexAll(List<Document> documents) throws Exception {
        try (FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
             IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {

            // Только обновляем существующие документы
            for (Document doc : documents) {
                Term term = new Term("id", String.valueOf(doc.getDocumentId()));

                // Создаем новый документ
                org.apache.lucene.document.Document luceneDoc = new org.apache.lucene.document.Document();
                luceneDoc.add(new StringField("id", String.valueOf(doc.getDocumentId()), Field.Store.YES));
                luceneDoc.add(new TextField("title", doc.getDocumentName(), Field.Store.NO));
                luceneDoc.add(new TextField("author", doc.getAuthor(), Field.Store.NO));
                luceneDoc.add(new TextField("content", doc.getContent(), Field.Store.NO));

                // Обновляем индекс
                writer.updateDocument(term, luceneDoc);
            }
            writer.commit();
        }
    }
}