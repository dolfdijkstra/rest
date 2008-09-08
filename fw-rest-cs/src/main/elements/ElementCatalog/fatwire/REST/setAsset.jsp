<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="asset" uri="futuretense_cs/asset.tld"
%><%@ page import="COM.FutureTense.Interfaces.Utilities"
%><%@ page import="COM.FutureTense.Interfaces.FTVAL"
%><%@ page import="com.openmarket.basic.util.Base64"
%><%@ page import="java.util.*"
%><cs:ftcs><?xml version="1.0" encoding="UTF-8"?>
<message><%
String assettype = ics.GetVar("assettype");
String assetid = ics.GetVar("id");
FTVAL assetdata = ics.GetCgi("assetdata");
String publication = ics.GetVar("publication");
boolean hasError=false;
int errorCode=0;
String errorMessage="";
boolean doScatter = false;

ics.ClearErrno();

if(!Utilities.goodString(assettype)){
    hasError=true;
    errorCode=-22225;
    errorMessage="No assettype specified";

}
if(assetdata==null){
    hasError=true;
    errorCode=-22225;
    errorMessage="No assetdata specified";

}
if(!Utilities.goodString(publication)){
    hasError=true;
    errorCode=-22225;
    errorMessage="No publication specified";

}
if(!Utilities.goodString(assetid)){
    hasError=true;
    errorCode=-22225;
    errorMessage="No assetid specified";
}
boolean created =false;
if (!hasError){
    //ics.LogMsg("loading asset with id  " + assetid);
    if(assetid.equals("0")) {
        %><asset:create name="asset" type='<%= assettype %>'/><%
        hasError= ics.GetErrno()<0;
        if (hasError){
            errorCode=ics.GetErrno();
            errorMessage="Problem creating asset "+ assettype +"-"+ assetid;
        } else {
            created=true;
        }
    } else {
        %><asset:load name="asset" type='<%= assettype %>' objectid='<%= assetid %>' editable="true"/><%
        //ics.LogMsg("Load errno: " + ics.GetErrno());
        hasError= ics.GetErrno()<0;
        if (hasError){
            //ics.LogMsg("Load failed with " + ics.GetErrno());
            %><asset:create name="asset" type='<%= assettype %>'/><%
            hasError= ics.GetErrno()<0;
            if (hasError){
                errorCode=ics.GetErrno();
                errorMessage="Problem creating asset "+ assettype +"-"+ assetid;
            } else {
                created=true;

            }
        }

    }
}
if (!hasError){
    %><asset:get name="asset" field="id" output="myId"/><%
    //ics.LogMsg("assetid is "+ ics.GetVar("myId"));
    if(!assetid.equals(ics.GetVar("myId"))){
        if(!assetid.equals("0")){
            ics.LogMsg("setting assetid to "+ assetid);
            %><asset:set name="asset" field="id" value='<%= assetid%>' /><%
        }
    }

}
if (!hasError && doScatter){
    %><asset:scatter name="asset" prefix="as" fieldlist="Publist"/><%
    hasError= ics.GetErrno()<0;
    if (hasError){
        errorCode=ics.GetErrno();
        errorMessage="Problem scattering asset "+ assettype +"-"+ assetid + " for PubList";
    }
}
if (!hasError && doScatter){
    %><asset:scatter name="asset" prefix="as" exclude="true"/><%
    hasError= ics.GetErrno()<0;
    if (hasError){
        errorCode=ics.GetErrno();
        errorMessage="Problem scattering asset "+ assettype +"-"+ assetid;
    }
}
if (!hasError){
    String myAsset = new String(assetdata.getBlob(),"UTF-8");
    %><asset:import name="asset" prefix="as" xml='<%= myAsset%>' /><%
    hasError= ics.GetErrno()<0;
    if (hasError){
        errorCode=ics.GetErrno();
        errorMessage="Problem importing asset "+ assettype +"-"+ assetid;
    }
}
if (!hasError && (!assetid.equals("0"))){
    %><ics:setvar name="as:id" value='<%=assetid%>' /><%

}
if (!hasError){
    //removing derived attributes as they will be done on save
    List<String> toRemove = new ArrayList<String>();
    for (Enumeration e=ics.GetVars(); e.hasMoreElements() ;){
        String v = (String)e.nextElement();
        if(v.startsWith("as:DAttribute_")){
            toRemove.add(v);
        }

    }
    for(String v:toRemove){
        ics.RemoveVar(v);
    }

}
if (!hasError){
    %><asset:gather name="asset" prefix="as" fieldlist="Publist"/><%
    hasError= ics.GetErrno()<0;
    if (hasError){
        errorCode=ics.GetErrno();
        errorMessage="Problem gathering asset "+ assettype +"-"+ assetid + " for PubList";
    }
}
if (!hasError){
    %><asset:gather name="asset" prefix="as" exclude="true"/><%
    hasError= ics.GetErrno()<0;
    if (hasError){
        errorCode=ics.GetErrno();
        errorMessage="Problem gathering asset "+ assettype +"-"+ assetid;
    }
}
if (!hasError){
    if (!created){
        %><asset:set name="asset" field="status" value="ED" /><%
    }
    if(!assetid.equals("0")){
        ics.LogMsg("setting assetid to "+ assetid);
        %><asset:set name="asset" field="id" value='<%= assetid%>' /><%
    }
    %><asset:save name="asset"/><%
    hasError= ics.GetErrno()<0;
    if (hasError){
        errorCode=ics.GetErrno();
        errorMessage="Problem saving asset "+ assettype +"-"+ assetid;
    }
}
if (!hasError){
    %><response>Asset saved</response><%
} else {
    %><fault code="<%= Integer.toString(errorCode) %>" string="<%= errorMessage %>" actor="<%= ics.GetVar("pagename") %>" /><%
}
%></message>
</cs:ftcs>