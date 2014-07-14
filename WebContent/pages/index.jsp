<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="env.jsp" %>
<%
String contextPath=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<br><br><br><br><br><br><br><br><br><br>
<form method="post" action="server.jsp">
	<table align="center">
		<tr>
			<td>
				<input id="username" type="text" name="code" style="width:300px;height:30px;font-size:20px;color:#D5DBDB;">
			</td>
			<td>
				<input type="submit" value="Submit" class="button" style="height:40px;">
			</td>
		</tr>
	</table>
</form>
<!-- <h2><a target="_blank" href="https://oauth.taobao.com/authorize?response_type=code&client_id=<%=appkey%>&redirect_uri=<%=redirectUrlPrefix%><%=contextPath %>/server.jsp">OAuth2.0 Server-side flow Sample</a></h2> -->
<h2 align="center"><a target="_blank" href="https://oauth.taobao.com/authorize?response_type=token&client_id=<%=appkey%>">获取授权码</a></h2><!-- OAuth2.0 Client-side  -->
<!-- <h2><a target="_blank" href="https://oauth.taobao.com/authorize?response_type=code&client_id=<%=appkey%>&redirect_uri=urn:ietf:wg:oauth:2.0:oob">OAuth2.0 Native Application Sample</a></h2> -->
</body>
</html>
