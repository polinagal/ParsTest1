/*
 * $Id:$
 */

package s2a.predicates.api;

/**
 * Составной объект предиката, включающий в себя несколько значений.
 * Соответствует составной переменной или динамическому сегменту
 * @author Mikhail Glukhikh
 */
public interface ComplexObject extends PredicateObject {

    /**
     * Размер составного объекта в байтах
     * @return размер составного объекта в байтах,
     * или -1, если он неизвестен точно
     */
    public int getObjectSize();
}
