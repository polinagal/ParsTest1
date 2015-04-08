/*
 * $Id:$
 */

package s2a.inference.mgp;

import s2a.predicates.api.ConstantObject;
import s2a.predicates.api.PredicateObject;
import s2a.predicates.api.ValueObject;

/**
 * Шаблон-значение НЕ КОНСТАНТА
 * @author Mikhail Glukhikh
 */
public class MemQuantifierNonconstValue extends MemQuantifierValue {

    MemQuantifierNonconstValue(final int code, final String name) {
        super(code, name);
    }

    @Override
    public boolean isCompatible(PredicateObject obj) {
        return (obj instanceof ValueObject &&
                !(obj instanceof ConstantObject));
    }
}
