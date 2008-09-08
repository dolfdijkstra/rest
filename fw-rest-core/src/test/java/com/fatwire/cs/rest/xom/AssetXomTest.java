package com.fatwire.cs.rest.xom;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.Attribute;
import com.fatwire.cs.rest.model.FieldType;

public class AssetXomTest extends AbstractXomTest {

	public void testToObject() throws DocumentException, IOException,
			XOMException, ParseException {
		AssetXom xom = new AssetXom();
		Document doc = readTestDocument("asset.xml");
		Asset asset = xom.toObject(doc);

		assertEquals(1121304726674L, asset.getId());
        
		for (Attribute a : asset.getAttributes()) {
			System.out.println(a.getName() + " " + a.getType() + " "
					+ a.getData());
            if("rootelement".equals(a.getName())){
                assertEquals(FieldType.string,a.getType());
                assertEquals("/FSIILayout",a.getData());
            }
            if("category".equals(a.getName())){
                assertEquals(FieldType.string,a.getType());
                assertEquals("page",a.getData());
            }
            if("status".equals(a.getName())){
                assertEquals(FieldType.string,a.getType());
                assertEquals("ED",a.getData());
            }
            if("updatedby".equals(a.getName())){
                assertEquals(FieldType.string,a.getType());
                assertEquals("firstsite",a.getData());
            }
            if("createdby".equals(a.getName())){
                assertEquals(FieldType.string,a.getType());
                assertEquals("tony.field",a.getData());
            }
            if("name".equals(a.getName())){
                assertEquals(FieldType.string,a.getType());
                assertEquals("FSIILayout",a.getData());
            }
            if("ttype".equals(a.getName())){
                assertEquals(FieldType.string,a.getType());
                assertEquals("x",a.getData());
            }
            if("description".equals(a.getName())){
                assertEquals(FieldType.string,a.getType());
                assertEquals("Generates an overall layout of a page which includes calling the Detail template for the specified asset.",a.getData());
            }
            if("flexgrouptemplateid".equals(a.getName())){
                assertEquals(FieldType.assetreference,a.getType());
            }
            if("updateddate".equals(a.getName())){
                assertEquals(FieldType.date,a.getType());
                Calendar c = Calendar.getInstance();
                c.set(2005,7,29,14,50,38);
                c.set(Calendar.MILLISECOND, 0);
                assertEquals(c.getTime(),a.getData());
            }
            if("createddate".equals(a.getName())){
                assertEquals(FieldType.date,a.getType());
            }
             
            /*
            pagecriteria array [c, cid, context, form-to-render, p, rendermode, site, sitepfx, ft_ss]
            updateddate date Mon Aug 29 14:50:38 CEST 2005
            
            element array [{cscacheinfo=true,~0, elementname=/FSIILayout, siteentry=[{defaultarguments=[{value=FirstSiteII, name=site}, {value=FSII, name=sitepfx}, {value=live, name=rendermode}], pagename=FirstSiteII/FSIILayout}], url={file:FSIILayout,0.jsp}, sscacheinfo=true,~0, resdetails1=tid=1121304726674, csstatus=live}]
            createddate date Thu Jul 14 00:30:06 CEST 2005

            Mapping array [{key=BottomNav, value=FSIIBottomNav, type=tname}, {key=Head, value=FSIIHead, type=tname}, {key=TopNav, value=FSIITopNav, type=tname}, {key=Detail, value=FSIIDetail, type=tname}, {key=SideNav, value=FSIISideNav, type=tname}]
            flexgrouptemplateid assetreference Product_PD-1112195134216
            Publist array [1112198287026]
            */
//			if (this.getClass() == Array.class){
//				
//			}
//			if ("array".equals(a.getType())){
//				for (Object o: (Object[])a.getData()){
//					System.out.println(o);
//				}
//			}else if(a.getData() instanceof Map){
//				
//			}
		}

	}

}
