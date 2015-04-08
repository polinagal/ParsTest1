/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2a.predicates.impl;

import s2a.predicates.api.InvalidElement;
import s2a.predicates.api.NullElement;
import s2a.predicates.api.NumberElement;
import s2a.predicates.api.PointerElement;
import s2a.predicates.api.RuntimeObject;
import s2a.predicates.api.ValueElement;

/**
 * Пара - (объект, смещение)
 * @author Tatiana Vert
 */
public class MemPointerElement implements PointerElement {

    private final RuntimeObject obj;
    private final int shift;

    public MemPointerElement(RuntimeObject o, int sh) {
        this.obj = o;
        this.shift = sh;
    }

    /**
     * Сложение с константой
     * @param val константа
     * @return результат сложения
     */
    public ValueElement add(MemNumberElement val) {
        int sh = (int) (this.shift + val.getValue());
        return new MemPointerElement(this.obj, sh);
    }

    /**
     * Разность двух указателей
     * @param p указатель
     * @return разность смещений (без деления на размер типа)
     */
    public ValueElement diff(ValueElement p) {
        if (p instanceof NullElement) {
            return new MemPointerElement(this.obj, this.shift);
        }
        MemPointerElement pointer = (MemPointerElement) p;
        assert this.obj.equals(pointer.getObject()) : "указатели на разные объекты";
        return new MemNumberElement(this.shift - pointer.getShift());
    }

    public int getShift() {
        return shift;
    }

    @Override
    public String toString() {
        return "(" + obj + ", " + shift + ")";
    }

    public RuntimeObject getObject() {
        return obj;
    }

    @Override
    public boolean less(ValueElement val) {
        assert (val instanceof MemPointerElement) : "Операнды разных типов";
        MemPointerElement rval = (MemPointerElement) val;
        assert this.obj.equals(rval.getObject()) : "указатели на разные объекты";
        return this.shift < rval.getShift();
    }

    @Override
    public boolean lessOrEqual(ValueElement val) {
        assert (val instanceof MemPointerElement) : "Операнды разных типов";
        MemPointerElement rval = (MemPointerElement) val;
        assert this.obj.equals(rval.getObject()) : "указатели на разные объекты";
        return this.shift <= rval.getShift();
    }

    @Override
    public boolean greater(ValueElement val) {
        assert (val instanceof MemPointerElement) : "Операнды разных типов";
        MemPointerElement rval = (MemPointerElement) val;
        assert this.obj.equals(rval.getObject()) : "указатели на разные объекты";
        return this.shift > rval.getShift();
    }

    @Override
    public boolean greaterOrEqual(ValueElement val) {
        assert (val instanceof MemPointerElement) : "Операнды разных типов";
        MemPointerElement rval = (MemPointerElement) val;
        assert this.obj.equals(rval.getObject()) : "указатели на разные объекты";
        return this.shift >= rval.getShift();
    }

    @Override
    public boolean equal(ValueElement val) {
        if (val instanceof NumberElement || val instanceof InvalidElement)
            return false;
        assert (val instanceof MemPointerElement) : "Операнды разных типов";
        MemPointerElement rval = (MemPointerElement) val;
        return this.obj.equals(rval.getObject()) && (this.shift == rval.getShift());
    }

    @Override
    public boolean notEqual(ValueElement val) {
        if (val instanceof NumberElement || val instanceof InvalidElement)
            return true;
        assert (val instanceof MemPointerElement) : "Операнды разных типов";
        MemPointerElement rval = (MemPointerElement) val;
        return !this.obj.equals(rval.getObject()) || (this.shift != rval.getShift());
    }

    @Override
    public boolean and(ValueElement val) {
        if (val instanceof NullElement) {
            return false;
        } else if (val instanceof MemNumberElement) {
            MemNumberElement rval = (MemNumberElement) val;
            return rval.getValue() != 0;
        } else {
            return true;
        }
    }

    @Override
    public boolean or(ValueElement val) {
        return true;
    }
}
