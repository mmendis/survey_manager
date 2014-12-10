<%--
  Created by IntelliJ IDEA.
  User: sboykin
  Date: 12/9/2014
  Time: 11:07 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" session="true" %>

<html>
<head>
    <title>Survey Manager Application properties</title>

  <style type="text/css">
    .passMessage {
        color: blue;
    }
    .failMessage {
        color: red;
    }
    .message {
      font-weight: bold;
      word-wrap: break-word;
    }
  </style>
</head>
<body>
<h2>Survey Manager Application Properties</h2>
<c:if test="${pageContext.request.userPrincipal.name != null}">
  <h3>
    Logged in as: ${pageContext.request.userPrincipal.name}
    |<a href="<c:url value="/logout" />"> Logout</a>
    <%--<form action="logout">--%>
      <%--<!-- include CSRF token for security purposes -->--%>
      <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">--%>
      <%--<input type="submit" value="Logout" />--%>
    <%--</form>--%>
  </h3>
</c:if>
<form:form commandName="wrapperConfig" method="post">
  <table style="table-layout: fixed;">
    <tr>
      <td class="message" colspan="3">${message}</td>
    </tr>
    <tr>
      <th class="variable">Property</th>
      <th>Current Value</th>
      <th></th>
    </tr>
    <tr>
      <td class="variable">Redcap API Token</td>
      <td><form:input path="redcapApiToken" /></td>
      <td><form:errors path="redcapApiToken" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker URL</td>
      <td><form:input path="messagingUrl" /></td>
      <td><form:errors path="messagingUrl" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker Queue</td>
      <td><form:input path="messagingQueue" /></td>
      <td><form:errors path="messagingQueue" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker Username</td>
      <td><form:input path="messagingUsername" /></td>
      <td><form:errors path="messagingUsername" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker Password</td>
      <td><form:password showPassword="true" path="messagingPassword" /></td>
      <td><form:errors path="messagingPassword" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker Send Timeout (ms)</td>
      <td><form:input path="messagingSendTimeout" /></td>
      <td><form:errors path="messagingSendTimeout" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td colspan="3">
        <input type="submit" value="Save Properties" />
      </td>
    </tr>
</table>
  <!-- include CSRF token for security purposes -->
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form:form>
</body>
</html>
