/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package s2a.predicates.api;

/**
 * Интерфейс числового значения переменной
 * @author Tanya
 */
public interface NumberElement extends ValueElement{
    /**
     * Получить значение
     * @return значение
     */
    long getValue();
}
