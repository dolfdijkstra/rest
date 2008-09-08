<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="string" uri="futuretense_cs/string.tld"
%><%@ taglib prefix="publication" uri="futuretense_cs/publication.tld"
%><%@ taglib prefix="asset" uri="futuretense_cs/asset.tld"
%><%@ page import="COM.FutureTense.Interfaces.FTValList"
%><%@ page import="COM.FutureTense.Interfaces.ICS"
%><%@ page import="COM.FutureTense.Interfaces.IList"
%><%@ page import="COM.FutureTense.Interfaces.Utilities"
%><%@ page import="COM.FutureTense.Util.ftErrors"
%><%@ page import="COM.FutureTense.Util.ftMessage"
%><%@ page import="com.openmarket.xcelerate.asset.AssetType"
%><%@ page import="java.text.SimpleDateFormat"
%><%@ page import="java.util.*"
%><%!
private String[] fields = new String[]{"id", "updateddate","name","status","createdby","createddate","updatedby","description"};
%><cs:ftcs><?xml version="1.0" encoding="UTF-8"?>
<%

String publication = ics.GetVar("publication");
String assettype = ics.GetVar("assettype");

boolean hasError=false;
int errorCode=0;
String errorMessage="";


ics.ClearErrno();
if(ics.GetVar("publication") == null) {
    hasError=true;
    errorCode=-22225;
    errorMessage="No publication specified";
}
if (!hasError){
    %><publication:load name="site" field="name" value='<%= ics.GetVar("publication") %>'/><%
    hasError = ics.GetErrno() <0;
    if (hasError){
        errorCode=ics.GetErrno();
        errorMessage="Problem loading publication "+ ics.GetVar("publication");
    }

}

if (!hasError){
    %><publication:get name="site" field="id" output="pubid"/><%
}
if (!hasError && assettype == null) {
    hasError=true;
    errorCode=-22226;
    errorMessage="No assettype specified";
}
if (!hasError ) {
    AssetType at = AssetType.Load(ics, assettype);
    hasError = (at ==null);
    errorCode= -103;
    errorMessage="Assettype not found";
}

if (!hasError) {
         String sql = "SELECT t.id as id,t.updateddate,t.name,t.status,t.createdby,t.createddate,t.updatedby,t.description FROM "+assettype+" t, AssetPublication WHERE (AssetPublication.pubid=" + ics.GetVar("pubid") +" OR AssetPublication.pubid = 0 ) AND t.id=AssetPublication.assetid AND t.status !='VO' ORDER BY id";
         %><ics:sql sql='<%=sql %>'listname="assets" table='<%= assettype %>' /><%
        hasError = (ics.GetErrno() <0) && (ics.GetErrno() != -101);
        if (hasError){
            errorCode=ics.GetErrno();
            errorMessage="Error getting assets";
        }
}

if (!hasError){
          %><assets><%
                %><ics:listloop listname="assets">
<%                 %>   <assetdata><assettype><%= assettype %></assettype><%
                    for (String field:fields){
                      %><<%= field %>><string:stream list="assets" column='<%= field %>' /></<%= field %>><%
                    }
                    %></assetdata><%
                %></ics:listloop>
<%        %></assets><%

} else {
    %><message><fault code='<%= errorCode %>' string='<%= errorMessage %>' actor="<%= ics.GetVar("pagename") %>" /></message><%
}
%></cs:ftcs>