<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
    <title>Create an account</title>
<%--    <link rel="stylesheet" href="${contextPath}/resources/css/bootstrap.min.css">--%>
    <link rel="stylesheet" href="${contextPath}/resources/css/main.css"/>
</head>
<body>

<noscript>
    <h2>Включи Javascript</h2>
</noscript>

<div class="logout" >
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Welcome ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()">Logout</a></h2>
    </c:if>
</div>

<div id="username-page">
    <div class="username-page-container">
        <h1 class="title">Введи имя чата</h1>
        <form id="usernameForm" name="usernameForm">
            <div class="form-group">
                <input type="hidden" value="anon" type="text" id="name" placeholder="Ник" autocomplete="off" class="form-control"/>
            </div>
            <div class="form-group">
                <input type="text" id="room-id" value="lobby" placeholder="Имя чата" autocomplete="off"
                       class="form-control"/>
            </div>
            <div class="form-group">
                <button type="submit" class="accent username-submit">Початимся</button>
            </div>
        </form>
    </div>
</div>

<div id="chat-page" class="hidden">
    <div class="chat-container">
        <div class="chat-header">
            <h2>Имя чата [<span id="room-id-display"></span>]</h2>
        </div>
        <div class="connecting">
            Connecting...
        </div>
        <ul id="messageArea">

        </ul>
        <div class="forms">
            <form id="messageForm" name="messageForm" nameForm="messageForm">
                <div class="form-group">
                    <div class="input-group clearfix">
                        <input type="text" id="message"
                               placeholder="Введи сообщение... или /join [room-id] для нового чата."
                               autocomplete="off" class="form-control"/>
                        <button type="submit" class="primary">Послать</button>
                    </div>
                </div>

            </form>
            <form id="roomForm" name="roomForm" nameForm="roomForm">
                <div class="form-group">
                    <div class="input-group clearfix">
                        <input type="text" id="room"
                               placeholder="Имя чата"
                               autocomplete="off" class="form-control"/>
                        <button type="submit" class="primary">Новый чат</button>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>

<div id="chanels"></div>



<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>
<script src="/webjars/js-cookie/js.cookie.js"></script>
<script src="${contextPath}/resources/js/main.js"></script>
<%--<script src="${contextPath}/resources/js/bootstrap.min.js"></script>--%>
<%--<script src="/webjars/jquery/jquery.min.js"></script>--%>
</body>
</html>