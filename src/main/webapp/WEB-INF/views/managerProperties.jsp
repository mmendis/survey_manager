<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: sboykin
  Date: 12/9/2014
  Time: 11:07 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<html>
<head>
    <title>Survey Manager Application properties</title>
</head>
<body>
<form:form commandName="wrapperConfig" method="post">
  <h2>Survey Manager Application Properties</h2>
  <table>
    <tr>
      <td colspan="3">${message}</td>
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
</form:form>
</body>
</html>
