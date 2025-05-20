<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Update Document</title>
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
      margin-bottom: 25px;
    }

    .error {
      color: #dc3545;
      padding: 10px;
      background: #f8d7da;
      border-radius: 4px;
      margin: 10px 0;
    }

    .form-section {
      max-width: 600px;
      margin: 20px 0;
    }

    .form-group {
      margin-bottom: 20px;
    }

    label {
      display: block;
      margin-bottom: 8px;
      color: #444;
      font-weight: 500;
    }

    input[type="text"],
    input[type="file"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-sizing: border-box;
      margin-top: 5px;
    }

    input[type="text"]:focus,
    input[type="file"]:focus {
      border-color: var(--primary-color);
      outline: none;
      box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
    }

    .btn {
      background-color: var(--primary-color);
      color: white;
      padding: 12px 25px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: background 0.3s;
      font-size: 1em;
    }

    .btn:hover {
      background-color: var(--hover-color);
    }

    .file-hint {
      font-size: 0.9em;
      color: var(--text-light);
      margin-top: 5px;
    }
  </style>
</head>
<body>
<div class="container">
  <h1>Update Document #${document.documentId}</h1>

  <c:if test="${not empty error}">
    <div class="error">${error}</div>
  </c:if>

  <div class="form-section">
    <form method="post" action="/documents/update/${document.documentId}" enctype="multipart/form-data">
      <div class="form-group">
        <label>Document Name:
          <input type="text" name="documentName" value="${document.documentName}" required>
        </label>
      </div>

      <div class="form-group">
        <label>Author:
          <input type="text" name="author" value="${document.author}" required>
        </label>
      </div>

      <div class="form-group">
        <label>New File:
          <input type="file" name="file" required>
        </label>
        <div class="file-hint">Select new file to replace the existing document</div>
      </div>

      <button type="submit" class="btn">Update Document</button>
    </form>
  </div>
</div>
</body>
</html>