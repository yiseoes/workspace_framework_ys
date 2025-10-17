<%-- welcome.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome</title>
    <link rel="stylesheet" href="${cPath}/css/welcome.css?v=1" />
</head>
<body>

    <main class="split-container">
        <div class="column left-column">
            <div class="image-wrapper">
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1542291026-7eec264c27ab?q=80&w=2070&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?q=80&w=1887&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1552346154-21d32810aba3?q=80&w=2070&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1560769629-975ec94e6a86?q=80&w=1964&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1542291026-7eec264c27ab?q=80&w=2070&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?q=80&w=1887&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1552346154-21d32810aba3?q=80&w=2070&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1560769629-975ec94e6a86?q=80&w=1964&auto=format&fit=crop');"></div>
            </div>
        </div>
        <div class="column right-column">
            <div class="image-wrapper">
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1515955656352-a1fa3ffcd111?q=80&w=2070&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1579338908476-3a3a1d71a706?q=80&w=1964&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?q=80&w=1898&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1491553895911-0055eca6402d?q=80&w=1780&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1515955656352-a1fa3ffcd111?q=80&w=2070&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1579338908476-3a3a1d71a706?q=80&w=1964&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?q=80&w=1898&auto=format&fit=crop');"></div>
                <div class="image-item" style="background-image: url('https://images.unsplash.com/photo-1491553895911-0055eca6402d?q=80&w=1780&auto=format&fit=crop');"></div>
            </div>
        </div>
    </main>

</body>
</html>