<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>错误页面</title>
</head>
<body>
 <H1>错误：</H1><%=exception%>  
      <H2>错误内容：</H2>  
    <%   
          exception.printStackTrace(response.getWriter());  
      %>  
</body>
</html>