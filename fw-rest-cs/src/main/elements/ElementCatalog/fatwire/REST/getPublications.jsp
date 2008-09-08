<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="string" uri="futuretense_cs/string.tld"
%><%@ taglib prefix="publication" uri="futuretense_cs/publication.tld"
%><%
// FatWire/IDE/WS/getPublications
//
// INPUT
//
// OUTPUT
%><%@ page import="COM.FutureTense.Interfaces.FTValList"
%><%@ page import="COM.FutureTense.Interfaces.ICS"
%><%@ page import="COM.FutureTense.Interfaces.IList"
%><%@ page import="COM.FutureTense.Interfaces.Utilities"
%><%@ page import="COM.FutureTense.Util.ftErrors"
%><%@ page import="COM.FutureTense.Util.ftMessage"
%><cs:ftcs><?xml version="1.0" encoding="UTF-8"?>
<publications>
  <publication:list list="pubs"/><ics:listloop listname="pubs"><%
   %><publication>
    <id><string:stream list="pubs" column='id' /></id>
    <name><string:stream list="pubs" column='name' /></name>
    <description><string:stream list="pubs" column='description' /></description>
   </publication>
   </ics:listloop><%
%></publications>
</cs:ftcs>