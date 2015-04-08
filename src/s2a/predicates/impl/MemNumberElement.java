/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2a.predicates.impl;

import s2a.predicates.api.InvalidElement;
import s2a.predicates.api.NullElement;
import s2a.predicates.api.NumberElement;
import s2a.predicates.api.ValueElement;

/**
 * Числовое значение переменной
 * @author Tatiana Vert
 */
public class MemNumberElement implements NumberElement {

    private long value;

    public MemNumberElement(int val) {
        this.value = val;
    }

    public MemNumberElement(long val) {
        this.value = val;
    }

    /**
     * Операция сложения
     * @param val слагаемое
     * @return сумма
     */
    public ValueElement add(MemNumberElement val) {
        return new MemNumberElement(this.value + val.getValue());
    }

    /**
     * Операция вычитания
     * @param val вычитаемое
     * @return разность
     */
    public ValueElement diff(MemNumberElement val) {
        return new MemNumberElement(this.value - val.getValue());
    }

    /**
     * Операция умножения
     * @param val множитель
     * @return произведение
     */
    public ValueElement mul(MemNumberElement val) {
        return new MemNumberElement(this.value * val.getValue());
    }

    /**
     * Операция деления
     * @param val делитель
     * @return частное
     */
    public ValueElement div(MemNumberElement val) {
        return new MemNumberElement(this.value / val.getValue());
    }

   /**
     * Операция остаток от деления
     * @param val делитель
     * @return остаток
     */
    public ValueElement mod(MemNumberElement val) {
        return new MemNumberElement(this.value % val.getValue());
    }

    /**
     * Получить значение
     * @return значение
     */
    public long getValue() {
        return value;
    }

    public boolean less(ValueElement val) {
        assert (val instanceof MemNumberElement) : "Операнды разных типов";
        MemNumberElement rval = (MemNumberElement) val;
        if (this.value < rval.getValue()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean lessOrEqual(ValueElement val) {
        assert (val instanceof MemNumberElement) : "Операнды разных типов";
        MemNumberElement rval = (MemNumberElement) val;
        if (this.value <= rval.getValue()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean greater(ValueElement val) {
        assert (val instanceof MemNumberElement) : "Операнды разных типов";
        MemNumberElement rval = (MemNumberElement) val;
        if (this.value > rval.getValue()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean greaterOrEqual(ValueElement val) {
        assert (val instanceof MemNumberElement) : "Операнды разных типов";
        MemNumberElement rval = (MemNumberElement) val;
        if (this.value >= rval.getValue()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equal(ValueElement val) {
        if (val instanceof InvalidElement)
            return false;
        else if (val instanceof NullElement){
            if (this.value == 0)
                return true;
            else return false;
        }
        assert (val instanceof MemNumberElement) : "Операнды разных типов";
        MemNumberElement rval = (MemNumberElement) val;
        if (this.value == rval.getValue()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean notEqual(ValueElement val) {
        if (val instanceof InvalidElement)
            return true;
        else if (val instanceof NullElement){
            if (this.value != 0)
                return true;
            else return false;
        }
        assert (val instanceof MemNumberElement) : "Операнды разных типов";
        MemNumberElement rval = (MemNumberElement) val;
        if (this.value != rval.getValue()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    public boolean and(ValueElement val) {
        if (this.value == 0) {
            return false;
        } else if (val instanceof NullElement) {
            return false;
        } else if (val instanceof MemNumberElement) {
            MemNumberElement rval = (MemNumberElement) val;
            if (rval.getValue() == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean or(ValueElement val) {
        if (this.value != 0) {
            return true;
        } else if (val instanceof NullElement) {
            return false;
        } else if (val instanceof MemNumberElement) {
            MemNumberElement rval = (MemNumberElement) val;
            if (rval.getValue() == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
