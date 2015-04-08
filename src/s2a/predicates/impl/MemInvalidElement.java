/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package s2a.predicates.impl;

import s2a.predicates.api.InvalidElement;
import s2a.predicates.api.ValueElement;

/**
 * Некорректное значение (указатель или числовое значение)
 * @author Tanya
 */
public class MemInvalidElement implements InvalidElement {

    @Override
    public ValueElement add(MemNumberElement val) {
        return new MemInvalidElement();
    }

    @Override
    public boolean less(ValueElement val) {
        throw new UnsupportedOperationException("операция с некорректным значением");
    }

    @Override
    public boolean lessOrEqual(ValueElement val) {
        throw new UnsupportedOperationException("операция с некорректным значением");
    }

    @Override
    public boolean greater(ValueElement val) {
        throw new UnsupportedOperationException("операция с некорректным значением");
    }

    @Override
    public boolean greaterOrEqual(ValueElement val) {
        throw new UnsupportedOperationException("операция с некорректным значением");
    }

    @Override
    public boolean equal(ValueElement val) {
        if (val instanceof InvalidElement)
            return true;
        else
            return false;
    }

    @Override
    public boolean notEqual(ValueElement val) {
        if (val instanceof InvalidElement)
            return false;
        else
            return true;
    }

    @Override
    public boolean and(ValueElement val) {
        throw new UnsupportedOperationException("операция с некорректным значением");
    }

    @Override
    public boolean or(ValueElement val) {
        throw new UnsupportedOperationException("операция с некорректным значением");
    }

}
