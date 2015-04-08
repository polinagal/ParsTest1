/*
 * $Id:$
 */

package s2a.predicates.api;

/**
 * Исключение "объект предиката не существует"
 * @author Mikhail Glukhikh
 */
public class NonexistentException extends Exception {

    public NonexistentException(final String msg) {
        super(msg);
    }

}
