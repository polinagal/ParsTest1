/*
 * $Id:$
 */

package s2a.predicates.api;

import java.util.List;

/**
 * <p>Предикат - состоит из объектов и типа.</p>
 *
 * <p>По-видимому, сам может быть объектом другого предиката.</p>
 * 
 * @author Mikhail Glukhikh
 */
public interface Predicate extends PredicateObject {

    /**
     * Получить список аргументов предиката
     * @return список аргументов предиката
     */
    public List<PredicateObject> getArguments();
    
    /**
     * Получить тип предиката
     * @return тип предиката
     */
    public PredicateType getType();

    /**
     * Зависит ли предикат от данного объекта
     * @param obj объект предиката
     * @return true, если зависит
     */
    public boolean depends(PredicateObject obj);

    /**
     * Determine whether predicate contains no quantifiers
     *
     * @return true if predicate contains no quantifiers, otherwise false
     */
    public boolean isQuantifierFree();

}
