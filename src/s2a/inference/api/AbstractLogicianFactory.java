/*
 * $Id:$
 */

package s2a.inference.api;

import s2a.inference.mgp.LogicianFactory;
import s2a.predicates.api.Predicate;

import java.util.List;

/**
 * Фабрика логика
 * @author Mikhail Glukhikh
 */
public abstract class AbstractLogicianFactory {

    /**
     * Получить экземпляр фабрики
     * @return экземпляр фабрики
     */
    public static AbstractLogicianFactory getInstance() {
        return LogicianFactory.instance;
    }

    /**
     * Создать логика
     * @return логик
     */
    abstract public Logician createLogician();

    /**
     * Создать правило вывода в общем виде
     * @param isPrecise true если правило точное
     * @param isEquivalent true если правило эквивалентное: левая часть &lt;==&gt; правая часть
     * @param leftPart левая часть правила (условие)
     * @param rightPart правая часть правила (следствие)
     * @return правило вывода в общем виде
     */
    abstract public InferenceRule createGeneralRule(final boolean isPrecise, final boolean isEquivalent,
                                                    final List<Predicate> leftPart, final List<Predicate> rightPart);

    abstract public InferenceRule createPrologRule(final List<Predicate> leftPart, final Predicate right);

}
