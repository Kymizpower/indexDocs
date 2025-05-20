package com.example.dao;

import com.example.entity.Document;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DocumentDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public DocumentDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Сохранение документа
    public void save(Document document) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(document);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error saving document", e);
        }
    }

    public List<Document> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Document", Document.class).list();
        }
    }

    // Получение всех документов
    public List<Document> findAllSorted() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Document ORDER BY documentId", Document.class).list();
        }
    }

    // Поиск по ID
    public Document findById(int documentId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Document.class, documentId);
        } catch (Exception e) {
            throw new RuntimeException("Error finding document by ID", e);
        }
    }

    // Обновление документа
    public void update(Document document) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(document);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating document", e);
        }
    }

    // Удаление документа
    public void delete(int documentId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Document document = session.get(Document.class, documentId);
            if (document != null) {
                session.delete(document);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error deleting document", e);
        }
    }

    // Закрытие SessionFactory при завершении
    public void shutdown() {
        sessionFactory.close();
    }
}