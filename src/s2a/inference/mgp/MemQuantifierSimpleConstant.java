/*
 * $Id:$
 */

package s2a.inference.mgp;

import s2a.inference.api.QuantifierSimpleConstant;
import s2a.predicates.api.PredicateObject;
import s2a.predicates.api.SimpleConstantObject;

/**
 * Реализация шаблона-константы
 * @author Mikhail Glukhikh
 */
public class MemQuantifierSimpleConstant extends AbstractQuantifierObject
        implements QuantifierSimpleConstant {

    MemQuantifierSimpleConstant(final int code, final String name) {
        super(code, name);
    }

    @Override
    public boolean isCompatible(PredicateObject obj) {
        return (obj instanceof SimpleConstantObject);
    }

    @Override
    public int getValueSize() {
        return 0;
    }

    @Override
    public long getConstantValue() {
        return 0;
    }
}
