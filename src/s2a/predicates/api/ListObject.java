/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2a.predicates.api;

/**
 * Интерфейс, представляющий список предикатов в Прологе
 *
 * @author Крикун Татьяна
 */
public interface ListObject extends PredicateObject, Iterable<PredicateObject> {

    public void append(PredicateObject po);

    public int size();

    public PredicateObject get(int index);
}
