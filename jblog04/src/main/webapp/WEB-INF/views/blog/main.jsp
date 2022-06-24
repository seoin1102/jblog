<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="wrapper">
			<div id="content">
				<div class="blog-content">
					<h4>${postVo.title}</h4>
					<p>${postVo.contents}
					<p>
				</div>
				<ul class="blog-list">
				<c:forEach items="${postVoList }" var="vo" varStatus="status">
					<li><a href="${pageContext.request.contextPath}/${blogVo.id}/${vo.categoryNo}/${vo.no}">${vo.title }</a> <span>2015/05/02</span>	</li>
				</c:forEach>
					
				</ul>
			</div>
		</div>

		<div id="extra">
			<div class="blog-logo">
				<img src="${pageContext.request.contextPath}${blogVo.logo}">
			</div>
		</div>

		<div id="navigation">
			<h2>카테고리</h2>
			<ul>
				<c:set var="count" value="${fn:length(list) }"/>
				<c:forEach items="${list }" var="vo" varStatus="status">
				<c:choose>
					<c:when test="${vo.countPost==0}">
						<li>${vo.name }</li>
					</c:when>
					<c:otherwise>
						<li><a href="${pageContext.request.contextPath}/${blogVo.id}/${vo.no}">${vo.name }</a></li>
					</c:otherwise>
				</c:choose>
				</c:forEach>
		
			</ul>
		</div>
		
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>