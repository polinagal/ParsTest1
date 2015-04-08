/*
 * $Id:$
 */

package s2a.predicates.api;

/**
 * Примитивная (целая) константа
 * @author Mikhail Glukhikh
 */
public interface SimpleConstantObject extends ConstantObject, ValueObject {
    /**
     * Получить значение, соответствующее объекту
     * @return значение, соответствующее объекту
     */
    public long getConstantValue();

}
