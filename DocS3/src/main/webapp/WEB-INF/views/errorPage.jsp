<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Error</title>
  <style>
    .error { color: red; margin: 20px; }
    .back-link { margin-top: 20px; }
  </style>
</head>
<body>
<h1 class="error">Error occurred!</h1>
<p class="error">${error}</p>
<a href="/documents" class="back-link">Back to documents list</a>
</body>
</html>