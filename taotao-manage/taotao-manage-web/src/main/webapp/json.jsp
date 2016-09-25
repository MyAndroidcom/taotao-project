<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <!-- 后台页面的jsp模拟跨域问题 -->
<%
	String callback = request.getParameter("callback");
	if(callback != null){
	    out.print(callback+"({\"abc\":123})");
	}else{
	    out.print("{\"abc\":123}");
	}
	/*  out.print(fun"({\"abc\":123})"); */
%>