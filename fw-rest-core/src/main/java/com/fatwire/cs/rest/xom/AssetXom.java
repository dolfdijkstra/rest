package com.fatwire.cs.rest.xom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.AssetReferenceField;
import com.fatwire.cs.rest.model.Attribute;
import com.fatwire.cs.rest.model.FieldType;
import com.fatwire.cs.rest.model.FileField;
import com.fatwire.cs.rest.util.Base64;

public class AssetXom implements XOM<Asset> {


	private final XPath xpathSelector = DocumentHelper
			.createXPath("/document/asset");

	public AssetXom() {
		super();
	}

	public Asset toObject(final Document doc) throws XOMException {
		final List nodes = xpathSelector.selectNodes(doc);
		try {
			final Element p = (Element) nodes.get(0);
			final Asset asset = new Asset();

			asset.setAssetType(p.attributeValue("type"));
			asset.setSubType(p.attributeValue("subtype"));
			asset.setId(Long.parseLong(p.attributeValue("id")));
			for (final Iterator i = p.elementIterator("attribute"); i.hasNext();) {
				final Element e = (Element) i.next();
				asset.addAttribute(doAttributeElement(e));
			}
			return asset;
		} catch (final Throwable e) {
			throw new XOMException(e.getMessage(), e);
		}

	}

	Attribute doAttributeElement(final Element e) throws Exception {
		final Attribute a = new Attribute();
		//System.out.println(e.getName());
		a.setName(e.attributeValue("name"));

		for (final Iterator i2 = e.elementIterator(); i2.hasNext();) {
			final Element eav = (Element) i2.next();
			final FieldType type = FieldType.parse(eav.getName());
			a.setType(type);
			a.setData(doData(eav, type));

		}
		return a;
	}

	Object doData(final Element eav, final FieldType type) throws Exception {
		//System.out.println(eav.getName());
		//System.out.println(eav.getUniquePath());
		switch (type) {
		case _int:
			return Long.parseLong(eav.attributeValue("value"));
		case string:
			return eav.attributeValue("value");
		case _float:
			return Float.parseFloat(eav.attributeValue("value"));
		case date:
			final SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return formatter.parse(eav.attributeValue("value"));
		case assetreference:
			//<assetreference type="Product_PD" value="1112195134216"/>
			final AssetReferenceField arf = new AssetReferenceField(Long.parseLong(eav.attributeValue("value")),eav.attributeValue("type"));
			return arf;
		case binary:
			return Base64.decode(eav.getText());
		case file:
			final FileField ff = new FileField(eav.attributeValue("name"),Base64.decode(eav.getText()));
			return ff;
		case ruleset:
			throw new java.lang.UnsupportedOperationException(type.toString());

		case array: {
			final List<Object> array = new ArrayList<Object>();
			for (final Iterator i2 = eav.elementIterator(); i2.hasNext();) {
				final Element ae = (Element) i2.next();
				final FieldType type2 = FieldType.parse(ae.getName());
				array.add(doData(ae, type2));

			}
			return array;
		}
		case list: {
			final List<Object> ary = new ArrayList<Object>();
			for (final Iterator i2 = eav.elementIterator("row"); i2.hasNext();) {
				final Element ae = (Element) i2.next();
				final FieldType type2 = FieldType.parse(ae.getName());
				ary.add(doData(ae, type2));

			}
			return ary;
		}
		case struct: {
			// a list of fields
			final Map<String,Object> map = new HashMap<String,Object>();
			for (final Iterator i2 = eav.elementIterator("field"); i2.hasNext();) {
				final Element field = (Element) i2.next();
				final Element child = (Element) field.elements().get(0);
				final FieldType type2 = FieldType.parse(child.getName());
				map.put(field.attributeValue("name"), doData(child, type2));

			}
			return map;
		}
		case row:{
			final Map<String,Object> map = new HashMap<String,Object>();
			for (final Iterator i2 = eav.elementIterator("column"); i2.hasNext();) {
				final Element field = (Element) i2.next();
				final Element child = (Element) field.elements().get(0);
				final FieldType type2 = FieldType.parse(child.getName());
				map.put(field.attributeValue("name"), doData(child, type2));

			}
			return map;
			
		}
			
		case oneof:
			throw new java.lang.UnsupportedOperationException(type.toString());

		}
		
		throw new java.lang.IllegalStateException(type.toString());
	}
}
