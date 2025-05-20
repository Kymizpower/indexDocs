<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Document Manager</title>
    <style>
        :root {
            --primary-color: #4a90e2;
            --hover-color: #357abd;
            --background: #f5f7fa;
            --text-light: #666;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: var(--background);
        }

        .container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        h1 {
            color: #333;
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 10px;
        }

        .search-instructions {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin: 15px 0;
            font-size: 0.9em;
            color: var(--text-light);
            border-left: 4px solid var(--primary-color);
        }

        input[type="text"], input[type="file"] {
            width: 100%;
            padding: 8px;
            margin: 5px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        button, .btn {
            background-color: var(--primary-color);
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background 0.3s;
        }

        button:hover, .btn:hover {
            background-color: var(--hover-color);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: var(--primary-color);
            color: white;
        }

        tr:hover {
            background-color: #f6f6f6;
        }

        .success {
            color: #28a745;
            padding: 10px;
            background: #e8f5e9;
            border-radius: 4px;
            margin: 10px 0;
        }

        .error {
            color: #dc3545;
            padding: 10px;
            background: #f8d7da;
            border-radius: 4px;
            margin: 10px 0;
        }

        .form-section {
            margin-bottom: 30px;
        }

        .actions-form {
            display: inline-block;
            margin: 0 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Document Manager</h1>

    <c:if test="${not empty message}">
        <div class="success">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <div class="form-section">
        <h2>Upload New Document</h2>
        <form method="post" action="/documents/upload" enctype="multipart/form-data">
            <input type="file" name="file" required><br>
            <input type="text" name="documentName" placeholder="Document Name" required><br>
            <input type="text" name="author" placeholder="Author" required><br>
            <button type="submit">Upload Document</button>
        </form>
    </div>

    <div class="form-section">
        <h2>Search Documents</h2>
        <div class="search-instructions">
            Для поиска по названию напишите <b>title:"название документа"</b> или по автору <b>author:"автор документа"</b>.<br>
            Также можно использовать комбинированный поиск: <b>title:"" AND author:""</b><br>
            Или поиск по id: <b>id:"номер"</b>
        </div>
        <form method="get" action="/documents/search">
            <input type="text" name="query" placeholder="Enter search query..." required>
            <button type="submit">Search</button>
        </form>

        <form method="post" action="/documents/reindex" style="margin-top: 15px;">
            <button type="submit" class="btn">Rebuild Search Index</button>
        </form>
    </div>

    <h2>All Documents</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Author</th>
            <th>Upload Date</th>
            <th>Actions</th>
        </tr>
        <c:forEach items="${documents}" var="doc">
            <tr>
                <td>${doc.documentId}</td>
                <td>${doc.documentName}</td>
                <td>${doc.author}</td>
                <td>${doc.formattedUploadDate}</td>
                <td>
                    <a href="/documents/view/${doc.documentId}" class="btn">View</a>
                    <a href="/documents/update/${doc.documentId}" class="btn">Update</a>
                    <form method="post" action="/documents/delete/${doc.documentId}" class="actions-form">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>