package s2a.predicates.impl;

import s2a.predicates.api.PredicateObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for variant predicate object
 */
public class OneOfPredicateObject implements PredicateObject {
    private final List<PredicateObject> oneOfList = new LinkedList<PredicateObject>();

    OneOfPredicateObject(List<PredicateObject> list) {
        for (PredicateObject po: list) {
            add(po);
        }
    }

    OneOfPredicateObject(PredicateObject... arr) {
        this(Arrays.asList(arr));
    }

    public void add(PredicateObject po) {
        if (po instanceof OneOfPredicateObject) {
            final OneOfPredicateObject other = (OneOfPredicateObject)po;
            oneOfList.addAll(other.oneOfList);
        } else {
            oneOfList.add(po);
        }
    }

    public List<PredicateObject> getVariants() {
        return Collections.unmodifiableList(oneOfList);
    }

    @Override
    public String getUniqueName() {
        return toString();
    }

    @Override
    public int hashCode() {
        return oneOfList.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this==o)
            return true;
        if (o instanceof OneOfPredicateObject) {
            final OneOfPredicateObject other = (OneOfPredicateObject)o;
            return oneOfList.equals(other.oneOfList);
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append("$");
        for (PredicateObject po: oneOfList) {
            if (!first)
                sb.append(" or ");
            first = false;
            sb.append(po);
        }
        sb.append("$");
        return sb.toString();
    }
}
