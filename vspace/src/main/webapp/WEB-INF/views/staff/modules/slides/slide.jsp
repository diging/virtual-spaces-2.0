<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
    <link href="https://cdn.quilljs.com/1.3.4/quill.snow.css" rel="stylesheet"> 
    <script src="https://cdn.quilljs.com/1.3.4/quill.js"></script>
    <script src="/node_modules/quill-image-resize-module/image-resize.min.js"></script>
  </head>
<body>
<h1>slide creation</h1>
    <div id="editor"></div>
    <h1>Module: ${slide.name}</h1>
<h3>Description: ${slide.description}</h3>
</body>


     
    
