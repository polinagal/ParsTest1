/*
 * $Id:$
 */

package s2a.predicates.impl;

import java.util.Collections;
import java.util.List;

import s2a.inference.api.QuantifierObject;
import s2a.predicates.api.*;

/**
 * Реализация предиката
 * @author Mikhail Glukhikh
 */
public class MemPredicate implements Predicate {
    
    private final PredicateType pt;
    
    private final List<PredicateObject> objs;

    MemPredicate(final PredicateType pt, final List<PredicateObject> objs)
            throws PredicateCreateException {
        assert !objs.contains(null) :
                "Null inside predicate object list: " + objs;
        this.pt = pt;
        this.objs = Collections.unmodifiableList(objs);
        pt.checkArguments(this.objs);
    }

    @Override
    public List<PredicateObject> getArguments() {
        return objs;
    }

    @Override
    public PredicateType getType() {
        return pt;
    }

    @Override
    public String getUniqueName() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public boolean depends(PredicateObject obj) {
        // Прямая зависимость
        if (objs.contains(obj))
            return true;
        for (PredicateObject child: objs) {
            if (child instanceof Predicate &&
                ((Predicate)child).depends(obj))
                // Вложенная зависимость
                return true;
        }
        return false;
    }

    @Override
    public boolean isQuantifierFree() {
        for (PredicateObject obj: objs) {
            if (obj instanceof QuantifierObject) {
                return false;
            }
            if (obj instanceof Predicate) {
                final Predicate inner = (Predicate)obj;
                if (!inner.isQuantifierFree())
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(pt);
        sb.append("(");
        int i = 0;
        for (PredicateObject obj : objs) {
            sb.append(obj.toString());
            if (i < objs.size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this==o)
            return true;
        if (o instanceof MemPredicate) {
            final MemPredicate mp = (MemPredicate)o;
            return (mp.pt==pt && mp.objs.equals(objs));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.pt != null ? this.pt.hashCode() : 0);
        hash = 29 * hash + (this.objs != null ? this.objs.hashCode() : 0);
        return hash;
    }
}
