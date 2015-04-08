/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2a.predicates.impl;

import s2a.predicates.api.PredicateObject;
import s2a.predicates.api.ValueObject;
import s2a.predicates.api.VersionObject;

/**
 * Объект, имеющий конкретное значение с учётом версии
 * @author Tanya
 */
public class MemVersionObject implements VersionObject, PredicateObject {

    private ValueObject object;
    private long version;

    public MemVersionObject(ValueObject obj, long version) {
        this.object = obj;
        this.version = version;
    }

    public ValueObject getObject() {
        return object;
    }

    public long getVersion() {
        return version;
    }

    public String getUniqueName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(object.toString());
        sb.append(version);
        return sb.toString();
    }
}
