<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String root = request.getContextPath();
    String host = request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
    <script type="text/javascript" src="<%=root%>/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=root%>/js/echarts.common.min.js"></script>
</head>

<body>
<h2>JVM内存监控DEMO</h2>

<div id="memory_main" style="width: 1500px;height:750px;"></div>
<input type="hidden" id="hid_host" value="<%=host%>" />
<script type="text/javascript" src="<%=root%>/js/jvm-echart.js"></script>
</body>
</html>
