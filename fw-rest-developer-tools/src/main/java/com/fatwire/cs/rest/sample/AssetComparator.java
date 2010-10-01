package com.fatwire.cs.rest.sample;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.Attribute;
import com.fatwire.cs.rest.model.FileField;

/**
 * Comparator for {@link Assets}. It only compares assets with the same id.
 * </p>
 * It does not implement the Comparator interface to the full spec. Especially a.compare(b) == -sgn(b.compare(a) is not fully implemented.
 * 
 * @author Dolf Dijkstra
 *
 */

public class AssetComparator implements Comparator<Asset> {

    
    /**
     * List of attributes that we do not care about if they are different
     * 
     */
    private static final List<String> exclude = Arrays.asList("updateddate", "updatedby", "createddate", "createdby");

    public int compare(Asset o1, Asset o2) {
        if (o1.getId() != o2.getId())
            throw new IllegalArgumentException("This is special Comparator, it only accepts assets with the same id");
        int equal = 0;
        if (o2.getAttributes().size() != o1.getAttributes().size())
            return o2.getAttributes().size() < o1.getAttributes().size()? -1:1; // different number of attributes, the asset is different

        for (Attribute a : o1.getAttributes()) {
            String name = a.getName();

            if (!exclude.contains(name.toLowerCase())) {
                // System.out.println("comparing attr " + name);
                Attribute a2 = o2.getAttributeValue(name);
                equal = compare(a, a2);
            }
            if (equal!=0) {
                System.out.println("not equal on " + name);

                break;
            }

        }
        return equal;
    }

    private int compare(Attribute a1, Attribute a2) {
        if (a1 == a2)
            return 0; // same or both null
        if (a1 == null || a2 == null)
            return a1==null?-1:1;
        if (a1.getType() != a2.getType())
            return a1.getType().compareTo(a2.getType());
        Object o1 = a1.getData();
        Object o2 = a2.getData();
        if (o1 == o2) {// same or both null
            return 0;
        }
        if (o1 == null || o2 == null) {
            return o1==null?-1:1;
        }

        // o1 and o2 are both not null
        int equal = 0;
        // System.out.println(o1 + " " + o2);

        switch (a1.getType()) {
            case _int:
            case string:
            case _float:
            case date:
            case assetreference:
                equal = String.valueOf(o1).compareTo(String.valueOf(o2));
                break;
            case binary:
                equal = Arrays.equals((byte[]) o1, (byte[]) o2)?0:-1;
                break;
            case file:
                final FileField ff = (FileField) o1;
                final FileField ff2 = (FileField) o2;
                //ff.getName()
                equal = Arrays.equals(ff.getContent(), ff2.getContent())?0:-1;
                break;
            case array:
            case list: {
                final List<?> array = (List<?>) o1;
                final List<?> array2 = (List<?>) o2;
                equal = array.equals(array2)?0:-1;
                break;
            }
            case struct:
            case row: {
                // a list of fields
                final Map<?, ?> array = (Map<?, ?>) o1;
                final Map<?, ?> array2 = (Map<?, ?>) o2;
                equal = array.equals(array2)?0:-1;
                break;
            }

            case ruleset:
            case oneof:
                throw new IllegalStateException(a1.getName());

        }
        return equal;

    }
}
