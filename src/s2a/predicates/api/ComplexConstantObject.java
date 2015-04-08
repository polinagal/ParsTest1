/*
 * $Id:$
 */

package s2a.predicates.api;

/**
 * Составная (видимо строковая) константа
 * @author Mikhail Glukhikh
 */
public interface ComplexConstantObject extends ComplexObject, ConstantObject {
    /**
     * Получить значение, соответствующее объекту с заданным смещением
     * @param shift смещение от начала объекта в байтах
     * @return значение, соответствующее объекту
     */
    public long getConstantValue(int shift);

}
