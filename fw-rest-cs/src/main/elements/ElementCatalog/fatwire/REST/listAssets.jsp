<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="asset" uri="futuretense_cs/asset.tld"
%><cs:ftcs><%--
      [pubid="siteId"]
      <asset:argument name="fieldName" value="fieldValue"/>
--%><asset:list type="Template" list="list_to_export" order="id"  excludevoided="true"></asset:list><%--
--%><ics:callelement element="fatwire/REST/asXML"/></cs:ftcs>