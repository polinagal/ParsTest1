/*
 * $Id:$
 */

package s2a.predicates.api;

/**
 * <p>Объект (аргумент) предиката.</p>
 *
 * <p>По-видимому, существует много разных видов, среди них:</p>
 * <ul>
 * <li>примитивная константа</li>
 * <li>примитивная переменная</li>
 * <li>составная константа</li>
 * <li>часть составной переменной-объекта</li>
 * <li>временная (не существующая сейчас) переменная -
 *     может потребоваться для связи предикатов</li>
 * <li>составной объект - может потребоваться для SIZE_OF, LENGTH_OF,
 *     возможно, PART_OF</li>
 * <li>состояние ресурса</li>
 * <li>объект-ресурс</li>
 * <li>функция</li>
 * <li>другой предикат - может потребоваться для объединений</li>
 * <li>...</li>
 * </ul>
 * @author Mikhail Glukhikh
 */
public interface PredicateObject {

    /**
     * Получить уникальное имя объекта. Может понадобиться, если
     * для анализа предикатов будет использовано внешнее средство -
     * тогда будет критично, чтобы разные объекты имели разные имена
     * @return уникальное имя объекта
     */
    public String getUniqueName();

    /**
     * Сравнение на равенство.
     * Реализация обязательна для всех объектов предиката.
     * @param o сравниваемый объект
     * @return true, если объекты предиката равны
     */
    @Override
    public boolean equals(Object o);

    /**
     * Хэш-код
     * @return хэш-код
     */
    @Override
    public int hashCode();

}
