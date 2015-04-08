/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package s2a.predicates.api;

import s2a.predicates.impl.MemNumberElement;

/**
 * Конкретное (примитивное) значение
 * @author Tatiana Vert
 */
public interface ValueElement {
    /**
     * Операция сложения
     * @param val слагаемое
     * @return результат сложения
     */
    ValueElement add(MemNumberElement val);
    /**
     * Операция меньше
     * @param val операнд
     * @return true, если значение меньше val
     */
    boolean less(ValueElement val);
    /**
     * Операция меньше или равно
     * @param val операнд
     * @return true, если значение меньше или равно val
     */
    boolean lessOrEqual(ValueElement val);
    /**
     * Операция больше
     * @param val операнд
     * @return true, если значение больше val
     */
    boolean greater(ValueElement val);
    /**
     * Операция больше или равно
     * @param val операнд
     * @return true, если значение больше или равно val
     */
    boolean greaterOrEqual(ValueElement val);
    /**
     * Операция сравнение на равенство
     * @param val операнд
     * @return true, если значение равно val
     */
    boolean equal(ValueElement val);
    /**
     * Операция сравнения на неравенство
     * @param val операнд
     * @return true, если значение не равно val
     */
    boolean notEqual(ValueElement val);
    /**
     * Операция логическое И
     * @param val операнд
     * @return true, если оба операнда имеют значение true
     */
    boolean and(ValueElement val);
    /**
     * Операция логическое ИЛИ
     * @param val операнд
     * @return true, если хотя бы один из операндов имеет значение true
     */
    boolean or(ValueElement val);
}
