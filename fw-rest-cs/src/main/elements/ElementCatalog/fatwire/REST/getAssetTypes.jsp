<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="string" uri="futuretense_cs/string.tld"
%><%@ taglib prefix="assettype" uri="futuretense_cs/assettype.tld"
%><%@ taglib prefix="publication" uri="futuretense_cs/publication.tld"
%><%@ page import="COM.FutureTense.Interfaces.FTValList"
%><%@ page import="COM.FutureTense.Interfaces.ICS"
%><%@ page import="COM.FutureTense.Interfaces.IList"
%><%@ page import="COM.FutureTense.Interfaces.Utilities"
%><%@ page import="COM.FutureTense.Util.ftErrors"
%><%@ page import="COM.FutureTense.Util.ftMessage"
%><%@ page import="com.fatwire.assetapi.util.*"
%><cs:ftcs><?xml version="1.0" encoding="UTF-8"?>
<%
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
    }else {
        %><publication:get name="site" field="id" output="pubid"/><%
    }
}
if (!hasError){
    FTValList list = new FTValList();
    list.put("NAME","site");
    list.put("LIST","AssetType");
    list.put("OBJECTTYPE","AssetType");
    list.put("ORDER","description");
    ics.runTag("PUBLICATION.CHILDREN",list);

    if (ics.GetErrno()==0){
        %><assettypes><%
            %><ics:listloop listname="AssetType">
<%          %><assettype><ics:listget listname="AssetType" fieldname="assettype" output="assettype"/><%
            %><name><string:stream list="AssetType" column='assettype' /></name><%
            %><description><string:stream list="AssetType" column='description' /></description><%

            java.util.List<String> subTypes = AssetUtil.getSubtypes(ics, ics.GetVar("assettype"), ics.GetVar("pubid"));
            for (String subType: subTypes){
                %><subtype><string:stream value='<%= subType %>'/></subtype><%
            }
            %></assettype><%
            %></ics:listloop>
<%      %></assettypes><%
    } else if (ics.GetErrno() == -101) {
        %><assettypes></assettypes><%
    } else {
        hasError = ics.GetErrno() <0;
        if (hasError){
            errorCode=ics.GetErrno();
            errorMessage="Problem getting assettypes for "+ ics.GetVar("pubid");
        }

    }
}
ics.ClearErrno();
if (hasError) {
    %><message><fault code='<%= Integer.toString(errorCode) %>' string='<%= errorMessage %>' actor='<%= ics.GetVar("pagename") %>' /></message><%
}
%></cs:ftcs>