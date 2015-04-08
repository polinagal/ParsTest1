/*
 * $Id:$
 */

package s2a.inference.mgp;

import s2a.inference.api.AbstractQuantifierFactory;
import s2a.inference.api.QuantifierPredicate;
import s2a.inference.api.QuantifierSimpleConstant;
import s2a.inference.api.QuantifierValue;

/**
 * Реализация фабрики шаблонов
 * @author Mikhail Glukhikh
 */
public class QuantifierFactory extends AbstractQuantifierFactory {

    static public final QuantifierFactory instance = new QuantifierFactory();

    @Override
    public QuantifierSimpleConstant createQuantifierSimpleConstant(int code, String name) {
        return new MemQuantifierSimpleConstant(code, name);
    }

    @Override
    public QuantifierValue createQuantifierValue(int code, String name) {
        return new MemQuantifierValue(code, name);
    }

    @Override
    public QuantifierValue createQuantifierNonconstValue(int code, String name) {
        return new MemQuantifierNonconstValue(code, name);
    }

    @Override
    public QuantifierPredicate createQuantifierPredicate(int code, String name) {
        return new MemQuantifierPredicate(code, name);
    }
}
