<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ page import="COM.FutureTense.Interfaces.FTValList"
%><cs:ftcs><%
String resource = ics.GetVar("resource");
if (resource ==null){
    ics.CallElement("fatwire/REST/getPublications",null);
} else {
    String[] parts = resource.split("/");
    FTValList list = new FTValList();

    if (parts.length==1) {
        list.put("publication",parts[0]);
        ics.CallElement("fatwire/REST/getAssetTypes",list);
    } else if (parts.length==2) {
        list.put("publication",parts[0]);
        list.put("assettype",parts[1]);
        ics.CallElement("fatwire/REST/getAssetData",list);
    } else if (parts.length==3) {
        list.put("publication",parts[0]);
        list.put("assettype",parts[1]);
        list.put("id",parts[2]);
        if ("GET".equals(ics.GetVar("op"))){
            ics.CallElement("fatwire/REST/getAsset",list);
        } else if ("PUT".equals(ics.GetVar("op"))){
            ics.CallElement("fatwire/REST/setAsset",list);
        }
        //if (method is POST) {setAsset} else {getAsset}
    }
}
%></cs:ftcs>