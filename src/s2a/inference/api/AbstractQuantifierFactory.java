/*
 * $Id:$
 */

package s2a.inference.api;

import s2a.inference.mgp.QuantifierFactory;

/**
 * Фабрика шаблонов предикатов
 * @author Mikhail Glukhikh
 */
public abstract class AbstractQuantifierFactory {

    /**
     * Получить экземпляр фабрики
     * @return экземпляр фабрики
     */
    public static AbstractQuantifierFactory getInstance() {
        return QuantifierFactory.instance;
    }

    /**
     * Создать шаблон константы
     * @param code код объекта
     * @param name имя объекта
     * @return шаблон константы
     */
    abstract public QuantifierSimpleConstant createQuantifierSimpleConstant(int code, String name);

    /**
     * Создать шаблон значения
     * @param code код объекта
     * @param name имя объекта
     * @return шаблон значения
     */
    abstract public QuantifierValue createQuantifierValue(int code, String name);

    /**
     * Создать шаблон значения не константы
     * @param code код объекта
     * @param name имя объекта
     * @return шаблон значения не константы
     */
    abstract public QuantifierValue createQuantifierNonconstValue(int code, String name);

    /**
     * Создать шаблон предиката
     * @param code код объекта
     * @param name имя объекта
     * @return шаблон предиката
     */
    abstract public QuantifierPredicate createQuantifierPredicate(int code, String name);
}
