<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>智能推荐</title>
<style>
body {
	font: 12px/20px 宋体, Arial;
	margin: 0;
}

.otherNews {
	width: 646px;
	margin: 0 auto;
}

.otherNews ul li {
	height: 24px;
	line-height: 24px;
	position: relative;
	overflow: hidden
}

.otherNews ul li a {
	width: 550px;
	height: 24px;
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden
}

a {
	color: #f56c08;
	text-decoration: none
}

.otherNews ul li span {
	position: absolute;
	right: 0px;
	top: 0px;
	color: #666666
}

ul, ol {
	list-style: outside none
}

div, span, iframe, tt, p, a, img, dt, ul, li, fieldset, form, label,
	legend, caption, dl, dt, dd {
	padding: 0;
}
</style>
</head>
<body>
<div  class="otherNews">

	<c:if test="${msg.sig}">
		<div>
			相关标签:<span>${msg.msg.keyWord}</span>
		</div>
		<div>
			<ul>
				<c:forEach items="${msg.msg.list}" var="article">
					<li><a href="${article.url}" target="_blank" title="${article.title}">${article.title}</a><span> <fmt:formatDate
								value="${article.date}" pattern="yyyy-MM-dd" /></span></li>
				</c:forEach>

			</ul>
		</div>
	</c:if>
	<c:if test="${!msg.sig}">
		<div>
			相关标签:<span></span>
		</div>
		<div>
			<ul>
				<li><span>无结果</span></li>

			</ul>
		</div>
	</c:if>
	</div>
</body>
</html>