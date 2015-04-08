/*
 * $Id:$
 */

package s2a.inference.api;

import s2a.predicates.api.PredicateObject;

/**
 * Шаблон объекта предиката, необходимый для описания правил.
 * Любой объект может соответствовать шаблону либо нет.
 * Шаблон имеет идентификатор (код) и имя
 * @author Mikhail Glukhikh
 */
public interface QuantifierObject extends PredicateObject {

    /**
     * Получить код шаблона
     * @return код шаблона
     */
    public int getCode();

    /**
     * Совместим ли данный объект с данным шаблоном
     * @param obj объект, проверяемый на совместимость
     * @return true, если совместим
     */
    public boolean isCompatible(PredicateObject obj);
}
