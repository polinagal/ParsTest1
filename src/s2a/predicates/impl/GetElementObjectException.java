/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2a.predicates.impl;

/**
 * Исключение, связанное с выходом за границу объекта.
 *
 * @author Tatiana Vert
 */
public class GetElementObjectException extends Exception{

    /**
     * Создать исключение
     *
     * @param msg сообщение
     */
    public GetElementObjectException(final String msg) {
        super(msg);
    }
}
