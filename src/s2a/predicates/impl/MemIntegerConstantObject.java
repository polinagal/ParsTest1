/*
 * $Id: MemConstantObject.java 17577 2010-06-21 12:42:05Z zakharov $
 */

package s2a.predicates.impl;

import s2a.predicates.api.SimpleConstantObject;

/**
 * Реализация объекта-константы
 *
 * @author Вадим Цесько &lt;vadim.tsesko@gmail.com&gt;
 */
public class MemIntegerConstantObject implements SimpleConstantObject {

    private final long value;
    private final int size;

    MemIntegerConstantObject(final long value, final int size) {
        assert size > 0 : "Неправильный размер объекта-константы";
        this.value = value;
        this.size = size;
    }

    @Override
    public int getValueSize() {
        return size;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public long getConstantValue() {
        return value;
    }

    @Override
    public String getUniqueName() {
        return this.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this==o)
            return true;
        if (o instanceof MemIntegerConstantObject) {
            final MemIntegerConstantObject co = (MemIntegerConstantObject)o;
            return (co.value==value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (this.value ^ (this.value >>> 32));
        return hash;
    }
 
}
