<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Document Details</title>
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

    .document-info {
      background: #f8f9fa;
      padding: 20px;
      border-radius: 5px;
      margin: 20px 0;
      border-left: 4px solid var(--primary-color);
    }

    .document-info p {
      margin: 12px 0;
      font-size: 1.1em;
      color: #444;
    }

    .document-info strong {
      color: var(--primary-color);
      min-width: 120px;
      display: inline-block;
    }

    .actions-container {
      margin-top: 30px;
      display: flex;
      gap: 15px;
    }

    .btn {
      background-color: var(--primary-color);
      color: white;
      padding: 12px 25px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: background 0.3s;
      text-decoration: none;
      display: inline-block;
      font-size: 0.95em;
    }

    .btn:hover {
      background-color: var(--hover-color);
    }

    @media (max-width: 768px) {
      .actions-container {
        flex-direction: column;
      }

      .btn {
        width: 100%;
        text-align: center;
      }
    }
  </style>
</head>
<body>
<div class="container">
  <h1>${document.documentName}</h1>

  <div class="document-info">
    <p><strong>ID:</strong> ${document.documentId}</p>
    <p><strong>Author:</strong> ${document.author}</p>
    <p><strong>Upload Date:</strong> ${document.formattedUploadDate}</p>
    <p><strong>Last Updated:</strong> ${document.formattedUpdateDate}</p>
  </div>

  <div class="actions-container">
    <a href="/documents" class="btn">Back to List</a>
    <a href="/documents/download/${document.documentId}" class="btn">Download Document</a>
  </div>
</div>
</body>
</html>