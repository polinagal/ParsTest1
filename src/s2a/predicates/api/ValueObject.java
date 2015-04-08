/*
 * $Id:$
 */

package s2a.predicates.api;

/**
 * Объект предиката, имеющий конкретное (примитивное) значение.
 * Подразделяется на константы, примитивные переменные и
 * части составных объектов, а также временные объекты,
 * не связанные с переменными
 * @author Mikhail Glukhikh
 */
public interface ValueObject extends PredicateObject {
    /**
     * Размер хранимого значения в байтах
     * @return размер хранимого значения в байтах
     */
    public int getValueSize();

}
