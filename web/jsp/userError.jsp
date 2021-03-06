<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright: Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: The user error page for the online review application.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="userError.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- JS from wireframes -->
    <script language="javascript" type="text/javascript" src="/js/or/popup.js"></script>
    <script language="javascript" type="text/javascript" src="/js/or/expand_collapse.js"></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css">
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"></script>
</head>

<body>
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
        <jsp:include page="/includes/project/project_tabs.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">

                    <div style="margin: 11px 6px 9px 0px; ">
                        <span class="bodyTitle"><or:text key="userError.Attention" /></span><br />
                        <b><or:text key="userError.ErrorPrefix" />
                        <span style="color: red; font-weight: bold;">${errorTitle}</span></b>
                    </div>
                    <div style="margin-top: 12px; margin-bottom: 32px;">
                        <b><or:text key="userError.Reason" /></b>
                        ${errorMessage}
                    </div>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
