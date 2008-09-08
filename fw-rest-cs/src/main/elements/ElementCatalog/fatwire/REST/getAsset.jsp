<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="asset" uri="futuretense_cs/asset.tld"
%><%@ page import="COM.FutureTense.Interfaces.Utilities"
%><%@ page import="java.util.*"
%><cs:ftcs><%
String assettype = ics.GetVar("assettype");
String id = ics.GetVar("id");
boolean hasError=false;
int errorCode=0;
String errorMessage="";

ics.ClearErrno();
if((assettype == null) || (id == null)) {
	hasError=true;
	errorCode=-22225;
	errorMessage="No assettype and/or id specified";
}
if (!hasError){
	%><asset:load name="asset" type='<%= assettype %>' objectid='<%= id %>' editable="true" /><%
	hasError = ics.GetErrno() <0;
	if (hasError){
		errorCode=ics.GetErrno();
		errorMessage="Problem loading asset "+ assettype +"-"+ id;
	}
}
if (!hasError) {
	%><asset:scatter name="asset" prefix="as" fieldlist="PubList" /><%
	hasError = ics.GetErrno() <0;
	if (hasError){
		errorCode=ics.GetErrno();
		errorMessage="Problem scattering asset "+ assettype +"-"+ id + " for PubList";
	}

}
if(!hasError){
	%><asset:scatter name="asset" prefix="as" exclude="true" /><%
	hasError = ics.GetErrno() <0;
	if (hasError){
		errorCode=ics.GetErrno();
		errorMessage="Problem scattering asset "+ assettype +"-"+ id + " for data";
	}

}
if (!hasError){
	%><asset:export name="asset" prefix="as" output="export" /><%
	hasError = ics.GetErrno() <0;
	errorCode=ics.GetErrno();
	if (hasError){
		errorCode=ics.GetErrno();
		errorMessage="Problem exporting scattered asset "+ assettype +"-"+ id;
	}

}
if(!hasError){
	%><ics:getvar name="export" encoding="default"/><%
}
if (hasError) {
	%><?xml version="1.0" encoding="UTF-8"?>
<message>
<fault code='<%= Integer.toString(errorCode) %>' string='<%= errorMessage %>' actor='<%= ics.GetVar("pagename") %>' />
</message><%
}
%></cs:ftcs>