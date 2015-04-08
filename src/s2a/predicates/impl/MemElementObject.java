/*
 * $Id: MemObjectElement.java 17794 2010-07-12 15:52:18Z mm $
 */

package s2a.predicates.impl;

import s2a.predicates.api.ComplexObject;
import s2a.predicates.api.ElementObject;

/**
 * Элемент, представляющий собой пару (объект, смещение)
 *
 * @author Вадим Цесько &lt;vadim.tsesko@gmail.com&gt;
 */
public class MemElementObject implements ElementObject {

    private final ComplexObject object;
    private final int shift;
    private final int size;

    /**
     * Создать пару (объект, смещение)
     *
     * @param object объект
     * @param shift  смещение
     * @param size   размер
     */
    MemElementObject(
            final ComplexObject object,
            final int shift,
            final int size) {
        assert object != null : "Неверные параметры пары (объект, смещение)";

        this.object = object;
        this.shift = shift;
        this.size = size;
    }

    @Override
    public int getShift() {
        return shift;
    }

    @Override
    public String toString() {
        return object.toString() +"e"+ shift;
    }

    @Override
    public ComplexObject getParent() {
        return object;
    }

    @Override
    public int getValueSize() {
        return size;
    }

    @Override
    public String getUniqueName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Object o) {
        if (this==o)
            return true;
        if (o instanceof MemElementObject) {
            final MemElementObject eo = (MemElementObject)o;
            return object.equals(eo.object) && shift==eo.shift && size==eo.size;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.object != null ? this.object.hashCode() : 0);
        hash = 83 * hash + this.shift;
        hash = 83 * hash + this.size;
        return hash;
    }
}
