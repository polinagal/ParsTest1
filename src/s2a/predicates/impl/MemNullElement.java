/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package s2a.predicates.impl;

import s2a.predicates.api.NullElement;
import s2a.predicates.api.ValueElement;

/**
 * Нулевое значение (числовой нуль или нулевой указатель)
 * @author Tanya
 */
public class MemNullElement implements NullElement{
    private long value = 0;
    public ValueElement add(MemNumberElement val) {
        return new MemNumberElement( val.getValue());
    }

    public boolean less(ValueElement val) {
        assert (val instanceof MemNumberElement):"Операнды разных типов";
        MemNumberElement rval = (MemNumberElement)val;
        if (this.value < rval.getValue())
            return true;
        else
            return false;
    }

    public boolean lessOrEqual(ValueElement val) {
        assert (val instanceof MemNumberElement):"Операнды разных типов";
        MemNumberElement rval = (MemNumberElement)val;
        if (this.value <= rval.getValue())
            return true;
        else
            return false;
    }

    public boolean greater(ValueElement val) {
        assert (val instanceof MemNumberElement):"Операнды разных типов";
        MemNumberElement rval = (MemNumberElement)val;
        if (this.value > rval.getValue())
            return true;
        else
            return false;
    }

    public boolean greaterOrEqual(ValueElement val) {
        assert (val instanceof MemNumberElement):"Операнды разных типов";
        MemNumberElement rval = (MemNumberElement)val;
        if (this.value >= rval.getValue())
            return true;
        else
            return false;
    }

    public boolean equal(ValueElement val) {
        if (val instanceof NullElement)
            return true;
        else if (val instanceof MemNumberElement){
            MemNumberElement rval = (MemNumberElement)val;
            if (this.value == rval.getValue())
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public boolean notEqual(ValueElement val) {
        if (val instanceof NullElement)
            return false;
        else if (val instanceof MemNumberElement){
            MemNumberElement rval = (MemNumberElement)val;
            if (this.value != rval.getValue())
                return true;
            else
                return false;
        }
        else
            return true;
    }

    public long getValue() {
        return value;
    }

    public boolean and(ValueElement val) {
        return false;
    }

    public boolean or(ValueElement val) {
        return val.or(this);
    }

}
