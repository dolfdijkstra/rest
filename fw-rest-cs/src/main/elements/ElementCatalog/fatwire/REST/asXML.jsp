<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="string" uri="futuretense_cs/string.tld"
%><%@ page import="COM.FutureTense.Interfaces.ICS, COM.FutureTense.Interfaces.IList"
%><cs:ftcs><?xml version="1.0" encoding="UTF-8"?>
<table><%
StringBuffer err = new StringBuffer();
IList schema = ics.GetList("list_to_export");
if(schema != null && schema.hasData()){
    int cols= schema.numColumns();
    int rows= schema.numRows();
    String[] colNames= new String[cols];
    for (int i=0; i< cols;i++){
        colNames[i]= schema.getColumnName(i);
    }
    for (int j=1; j<=rows;j++){
        schema.moveTo(j);
        %><row><%
        for (int i=0; i< cols;i++){
            boolean upload = colNames[i].startsWith("url");
            Object val = schema.getObject(colNames[i]);
            if (val ==null){
                val = schema.getValue(colNames[i]);
                if ("".equals(val)) val=null;
            }
            if (val !=null){
            %><column name='<%= colNames[i] %>' urlcolumn='<%= upload %>'><%
                    %><colvalue><string:stream value='<%= String.valueOf(val) %>'/></colvalue><%
                if (upload && !"".equals(schema.getValue(colNames[i]) )) {
                    %><urldata><string:stream value='<%= schema.getFileString(colNames[i]) %>'/></urldata><%
                }

            %></column>
            <%
            }
        }
        %></row><%

    }
} else {
    %>No data found!<%= ics.GetErrno() %><%= err.toString() %><%
    ics.ClearErrno();
    //ics.FlushStream();

}
%>
</table>
</cs:ftcs>
