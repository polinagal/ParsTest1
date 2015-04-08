/*
 * $Id:$
 */

package s2a.predicates.api;

/**
 * Объект предиката, соответствующие переменной (любой)
 * @author Mikhail Glukhikh
 */
public interface VariableObject extends RuntimeObject {

    /**
     * Получить имя переменной.
     * Имя не обязано быть уникальным.
     * 
     * @return имя переменной 
     */
    public String getShortName();
    
}
