<%--
  ~ Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
  ~
  ~ Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
  ~ For more information, see http://chip.org/ihlab and https://github.com/chb
  --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         session="true" %>

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
    table {
      padding-bottom: 2em;
      padding-top: 2em;
      text-align: left;
    }
    td {
      padding: 0.25em;
      padding-right: 1em;;
    }
    td.variable {
      background-color: lightgray;
    }
    td.message {
      padding-bottom: 2em;
      padding-top: 0.5em;
    }
  </style>
</head>
<body>
<h2>Survey Manager Application Properties</h2>
<c:if test="${pageContext.request.remoteUser != null}">
  <h3>
  Logged in as: ${pageContext.request.remoteUser}
  </h3>
  </c:if>
<c:if test="${pageContext.request.userPrincipal.name != null}">
  <h3>
    Logged in as: ${pageContext.request.userPrincipal.name}
    |<a href="<c:url value="/logout" />"> Logout</a>
  </h3>
</c:if>
<h3><a href="<c:url value="/logout" />"> Logout</a></h3>
<form:form commandName="wrapperConfig" method="post">
  <table style="table-layout: fixed;">
    <tr>
      <td class="message" colspan="3" style="text-align: center">${message}</td>
    </tr>
    <tr>
      <th class="variable">Property</th>
      <th>Current Value</th>
      <th></th>
    </tr>
    <tr>
      <td class="variable">REDCap API Token</td>
      <td><form:input path="redcapApiToken" size="40"/></td>
      <td><form:errors path="redcapApiToken" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">REDCap Private Forms (Separate each form with comma)</td>
      <td><form:input path="redcapPrivateForms" size="40"/></td>
      <td><form:errors path="redcapPrivateForms" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker URL</td>
      <td><form:input path="messagingUrl" size="40" /></td>
      <td><form:errors path="messagingUrl" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker Queue</td>
      <td><form:input path="messagingQueue" size="40" /></td>
      <td><form:errors path="messagingQueue" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker Username</td>
      <td><form:input path="messagingUsername" size="40" /></td>
      <td><form:errors path="messagingUsername" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker Password</td>
      <td><form:password showPassword="true" path="messagingPassword" size="40" /></td>
      <td><form:errors path="messagingPassword" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td class="variable">Message Broker Send Timeout (ms)</td>
      <td><form:input path="messagingSendTimeout"  size="40"/></td>
      <td><form:errors path="messagingSendTimeout" cssStyle="color: red;" /> </td>
    </tr>
    <tr>
      <td colspan="3" style="text-align: center">
        <input type="submit" value="Save Properties" />
        <input type="button" value="Reset" onclick="location.href='<c:url value="/admin/config"/>'" />
      </td>
    </tr>
</table>
  <!-- include CSRF token for security purposes -->
  <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">--%>
</form:form>
</body>
</html>
