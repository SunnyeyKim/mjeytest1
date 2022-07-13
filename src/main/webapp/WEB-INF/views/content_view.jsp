<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="resources/css/nstyle.css" />
</head>
<body>
<h3>content_view</h3>
<form action="content_update" method="post">
<input type="hidden" name="bid" value="${content_view.bid }" />
<table>
	<tr>
		<td class="left">번호</td>
		<td>${content_view.bid }</td>
	</tr>
	<tr>
		<td class="left">히트</td>
		<td>${content_view.bhit }</td>
	</tr>
	<tr>
		<td class="left">이름</td>
		<td>${content_view.bname }</td>
	</tr>
	<tr>
		<td class="left">제목</td>
		<td>${content_view.btitle }</td>
	</tr>
	<tr>
		<td class="left">내용</td>
		<td>${content_view.bcontent }</td>
	</tr>
	<tr>
		<td class="left">첨부</td>
		<td> 
			<a href="download?p=resources/upload/&f=${content_view.filesrc }
			&bid=${content_view.bid }">${content_view.filesrc }</a>	<!-- 클릭하면 컨트롤러로 감 -->
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="submit" value="수정" /> &nbsp;&nbsp;
			<a href="list">목록보기</a> &nbsp;&nbsp;
			<a href="delete?bid=${content_view.bid }">삭제</a>  &nbsp;&nbsp;
			<a href="reply_view?bid=${content_view.bid }">답변</a>  &nbsp;&nbsp;		
		</td>
	</tr>
	
</table>
</form>
<hr />
<c:set value="${content_view.filesrc }" var="filename" />
<c:set var="fileNm" value="${fn:toLowerCase(filename) }" />
<c:forTokens var="token" items="${fileNm }" delims="." varStatus="status">
	<c:if test="${status.last }">
		<c:choose>
			<c:when test="${token eq 'jpg' || token eq 'png' || toaken eq 'bmp' }">
				${filename }:::그림파일 jpg png bmp <br />
				${status.last }
				<div>
					<img border="1" width="100" height="100" 
					src="resources/upload/${content_view.filesrc }" alt="" />	
				</div>
			</c:when> 
		</c:choose>
	</c:if>
</c:forTokens>


<hr />
<%-- <c:if test="${content_view.filesrc ne null }">
	<div>
		<img border="1" width="100" height="100" 
		src="resources/upload/${content_view.filesrc }" alt="" />	
	</div>
</c:if> --%>
</body>
</html>