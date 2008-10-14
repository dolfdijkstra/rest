<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="string" uri="futuretense_cs/string.tld"
%><%@ taglib prefix="publication" uri="futuretense_cs/publication.tld"
%><cs:ftcs><?xml version="1.0" encoding="UTF-8"?>
<publications>
  <publication:list list="pubs"/><ics:listloop listname="pubs"><%
   %><publication>
    <id><string:stream list="pubs" column='id' /></id>
    <name><string:stream list="pubs" column='name' /></name>
    <description><string:stream list="pubs" column='description' /></description>
    <cs_preview><string:stream list="pubs" column='cs_preview' /></cs_preview>
    <cs_prefix><string:stream list="pubs" column='cs_prefix' /></cs_prefix>
    <cs_previewasset><string:stream list="pubs" column='cs_previewasset' /></cs_previewasset>
    <pubroot><string:stream list="pubs" column='pubroot' /></pubroot>
   </publication>
   </ics:listloop><%
%></publications>
</cs:ftcs>