/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2a.predicates.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import s2a.predicates.api.ListObject;
import s2a.predicates.api.PredicateObject;

/**
 * Реализация списка предикатов в Прологе.
 *
 * @author Крикун Татьяна
 */
public class MemListObject implements ListObject {

    List<PredicateObject> predicates;

    public MemListObject() {
        predicates = new ArrayList<PredicateObject>();
    }

    public void append(PredicateObject po) {
        predicates.add(po);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (PredicateObject po : predicates) {
            if (sb.length() != 1)
                sb.append(',');
            sb.append(po.toString());
        }
        sb.append(']');
        return sb.toString();
    }

    public String getUniqueName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int size() {
        return predicates.size();
    }

    public PredicateObject get(int index) {
        return predicates.get(index);
    }

    public Iterator<PredicateObject> iterator() {
        return predicates.iterator();
    }
}
