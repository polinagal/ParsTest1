/*
 * $Id:$
 */

package s2a.predicates.api;

/**
 * Исключение при создании предиката
 * @author Mikhail Glukhikh
 */
public class PredicateCreateException extends Exception {
    /**
     * Создать исключение
     * @param msg сообщение
     */
    public PredicateCreateException(final String msg) {
        super(msg);
    }
}
