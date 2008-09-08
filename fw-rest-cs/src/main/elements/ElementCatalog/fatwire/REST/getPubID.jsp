<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" 
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" 
%><%@ taglib prefix="publication" uri="futuretense_cs/publication.tld" 
%><%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" 
%><%@ page import="COM.FutureTense.Interfaces.ICS" 
%><cs:ftcs><?xml version="1.0" encoding="UTF-8"?>
<message>
<%
String pubName = ics.GetVar("pubName");
if(pubName == null) { 
	%><fault code="-10005" string="Received empty publication name" actor="<%= ics.GetVar("pagename") %>"/><%
} else { 
	%><publication:load name="publication" field="name" value="<%= pubName %>"/><%
			%><publication:get name="publication" field="id" output="pubid"/><%
	if(ics.GetErrno() == 0) { 
		%><id><ics:getvar name="pubid" encoding="default"/></id><%
	} else { 
		%><fault code="<%= Integer.toString(ics.GetErrno()) %>" string="Publication not found"	actor="<%= ics.GetVar("pagename") %>"/><%
	} 
 }
%>
</message>
</cs:ftcs>