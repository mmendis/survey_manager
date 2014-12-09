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
      <th>Property</th>
      <th>Current Value</th>
      <th></th>
    </tr>
    <tr>
      <td>Redcap API Token</td>
      <td><form:input path="redcapApiToken" /></td>
      <td><form:errors path="redcapApiToken" cssStyle="color: red;" /> </td>
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
