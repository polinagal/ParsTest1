/*
 * $Id:$
 */

package s2a.inference.api;

import s2a.predicates.api.Predicate;

/**
 * Это наш простенький логик
 * @author Mikhail Glukhikh
 */
public interface Logician {

    /**
     * Добавить правило вывода
     * @param rule правило
     */
    public void addRule(InferenceRule rule);

    /**
     * Упростить теорию
     * @param theory исходная теория
     * @param start предикат, с которого следует начать упрощение
     * @return упрощенное состояние
     */
    public Theory simplify(Theory theory, Predicate start);

    /**
     * Попытаться доказать истинность предиката
     * @param theory исходная теория
     * @param toProve предикат, который доказываем
     * @return true, если в данном состоянии предикат всегда истинен,
     * false в противном случае
     */
    public boolean proveTrue(Theory theory, Predicate toProve);

    /**
     * Попытаться доказать ложность предиката
     * @param theory исходная теория
     * @param toProve предикат, который доказываем
     * @return true, если в данном состоянии предикат всегда ложен,
     * false в противном случае
     */
    public boolean proveFalse(Theory theory, Predicate toProve);

    /**
     * Попытаться определить, разрешим ли предикат
     * @param theory исходная теория
     * @param toSolve предикат, который пытаемся разрешить
     * @return true, если в данном состоянии предикат разрешим (может быть истинен),
     * false в противном случае (предикат всегда ложен)
     */
    public boolean solveTrue(Theory theory, Predicate toSolve);

    /**
     * Попытаться определить, опровержим ли предикат
     * @param theory исходная теория
     * @param toSolve предикат, который пытаемся опровергнуть
     * @return true, если в данном состоянии предикат опровержим (может быть ложен)
     * false в противном случае (предикат всегда истинен)
     */
    public boolean solveFalse(Theory theory, Predicate toSolve);
}
