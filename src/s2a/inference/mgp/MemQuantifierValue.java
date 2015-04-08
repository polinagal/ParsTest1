/*
 * $Id:$
 */

package s2a.inference.mgp;

import s2a.inference.api.QuantifierValue;
import s2a.predicates.api.ComplexObject;
import s2a.predicates.api.PredicateObject;
import s2a.predicates.api.ValueObject;
import s2a.predicates.api.VersionObject;

/**
 * Реализация шаблона-значения
 * @author Mikhail Glukhikh
 */
public class MemQuantifierValue extends AbstractQuantifierObject
        implements QuantifierValue {

    MemQuantifierValue(final int code, final String name) {
        super(code, name);
    }

    @Override
    public boolean isCompatible(PredicateObject obj) {
        return (obj instanceof ValueObject) || (obj instanceof VersionObject) ||
                // Temporary
                (obj instanceof ComplexObject);
    }

    @Override
    public int getValueSize() {
        return 0;
    }
}
