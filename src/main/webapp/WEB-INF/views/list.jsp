<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>list</h3>
<table border="1">
	<tr>
		<td>번호</td>
		<td>이름</td>
		<td>제목</td>
		<td>날자</td>
		<td>히트</td>
	</tr>
	<c:forEach items="${list }" var="dto">
		<tr>
			<td>${dto.bid }</td>
			<td>${dto.bname }</td>
			<td>
				<c:set value="${dto.bindent }" var="endindent" />
				<c:forEach begin="1" end="${dto.bindent }" var="cnt">
					&nbsp;
					<c:if test="${cnt eq endindent }">
						<img src="resources/img/reply.gif" alt="" />
						[re]
					</c:if>
					
				</c:forEach>
				<a href="content_view?bid=${dto.bid }">${dto.btitle }</a>
			</td>
			<td>${dto.bdate }</td>
			<td>${dto.bhit }</td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="5"><a href="write_view">글쓰기</a></td>
	</tr>
</table>
totCnt : ${totRowcnt }
현재페이지/토탈페이지 : ${searchVO.page } / ${searchVO.totPage }
<hr />

<form action="list" method="post">
	<c:if test="${searchVO.totPage>1 }"> <!-- 토탈 페이지가 1보다 크면 -->
		<c:if test="${searchVO.page>1 }">
			<a href="list?page=1">[처음]</a>
			<a href="list?page=${searchVO.page-1 }">[이전]</a>
		</c:if>
	
	<c:forEach begin="${searchVO.pageStart }" end="${searchVO.pageEnd }" var="i">
		<c:choose>
			<c:when test="${i eq searchVO.page }">  <!-- i eq searchVO.page 내가 클릭한 페이지가 같을 때 -->
				<span style="color: red; font-weight: bold;">${i }&nbsp;</span>
			</c:when>
			<c:otherwise>
				<a href="list?page=${i }$sk=${resk}&btitle=${btitle==true?'btitle':''}&bcontent=${bcontent==true?'bcontent':''}" style="text-decoration: none">${i }</a>&nbsp;
			</c:otherwise>
		
		</c:choose>
	</c:forEach>
		<c:if test="${searchVO.totPage>searchVO.page }"> <!-- 마지막 페이지가 아닐때 -->
			<a href="list?page=${searchVO.page+1 }">[다음]</a>
			<a href="list?page=${searchVO.totPage }">[마지막]</a>
		</c:if>
	</c:if>
	<div>
		<c:choose>
			<c:when test="${btitle }">	
				<input type="checkbox" name="searchType" value="btitle" checked />			
			</c:when>
			<c:otherwise>	
				<input type="checkbox" name="searchType" value="btitle" />			
			</c:otherwise>
		</c:choose>
		제목
		<c:choose>
			<c:when test="${bcontent }">	
				<input type="checkbox" name="searchType" value="bcontent" checked />			
			</c:when>
			<c:otherwise>	
				<input type="checkbox" name="searchType" value="bcontent" />			
			</c:otherwise>
		</c:choose>
		내용
		<input type="text" name="sk" style="width:150px;" maxlength="50" value="" />
		<input type="submit" value="검색" />
	
	</div>
	<!-- 검색을 가져감 -->
</form>
</body>
</html>