<%--
  Admin login page.
  User: sboykin
  Date: 12/11/2014
  Time: 8:06 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" session="true" %>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>mySCILHS Administration Login</title>
</head>
<body>
<h2>mySCILHS Survey Manager Administration</h2>
<c:if test="${param.error != null}">
</c:if>
<form method="POST" action="j_security_check">
  <table>
    <tr>
      <td colspan="2" style="align-content: center"><h2>Login:</h2></td>
    </tr>
    <tr>
      <td>Name:</td>
      <td><input type="text" name="j_username" /></td>
    </tr>
    <tr>
      <td>Password:</td>
      <td><input type="password" name="j_password" /></td>
    </tr>
    <tr>
      <td colspan="2"><input type="submit" value="Login" /></td>
    </tr>
  </table>
</form>
</body>
</html>