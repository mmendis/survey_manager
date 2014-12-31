<%--
  ~ Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
  ~
  ~ Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
  ~ For more information, see http://chip.org/ihlab and https://github.com/chb
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