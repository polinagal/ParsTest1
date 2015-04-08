/*
 * $Id:$
 */

package s2a.inference.mgp;

import s2a.inference.api.QuantifierObject;

/**
 * Реализация шаблона предиката
 * @author Mikhail Glukhikh
 */
public abstract class AbstractQuantifierObject implements QuantifierObject {

    private final int code;

    private final String name;

    protected AbstractQuantifierObject(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getUniqueName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Шаблоны предиката равны, если равны их коды
     * @param o сравниваемый объект
     * @return true, если равны
     */
    @Override
    public boolean equals(Object o) {
        if (this==o)
            return true;
        if (o instanceof AbstractQuantifierObject) {
            final AbstractQuantifierObject atpo = (AbstractQuantifierObject)o;
            return code==atpo.code;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.code;
        return hash;
    }
}
