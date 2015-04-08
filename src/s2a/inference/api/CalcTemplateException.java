/*
 * $Id:$
 */

package s2a.inference.api;

/**
 * Исключение, связанное с невозможностью расчета констант в правиле.
 * @author Mikhail Glukhikh
 */
public class CalcTemplateException extends Exception {
    /**
     * Создать исключение
     * @param msg сообщение
     */
    public CalcTemplateException(final String msg) {
        super(msg);
    }
}
