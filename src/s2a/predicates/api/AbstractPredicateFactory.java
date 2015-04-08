/*
 * $Id:$
 */
package s2a.predicates.api;

import java.util.List;
import s2a.predicates.impl.PredicateFactory;

/**
 * Фабрика предикатов, объектов предикатов
 * и состояния на основе множества предикатов
 * @author Mikhail Glukhikh
 */
public abstract class AbstractPredicateFactory {

    /**
     * Получить экземпляр фабрики
     * @return экземпляр фабрики
     */
    static public AbstractPredicateFactory getInstance() {
        return PredicateFactory.getInstance();
    }

    /**
     * Создать объект предиката, соответствующий переменной
     * @param size размер переменной
     * @param name имя переменной
     * @return объект предиката
     */
    public abstract ComplexVariableObject createVariableObject(
            final int size, final String name);

    /**
     * Создать объект предиката, соответствующий целой константе
     * @param value значение константы
     * @param size размер значения в байтах
     * @return объект предиката
     */
    public abstract SimpleConstantObject createIntegerConstantObject(long value, int size);

    /**
     * Создать объект предиката, соответствующий строковой константе
     * @param value значение константы
     * @return объект предиката
     */
    public abstract ComplexConstantObject createStringConstantObject(String value);

    /**
     * Создать объект предиката, соответствующий элементу составного объекта
     * @param parent составной объект предиката
     * @param shift смещение в байтах
     * @param size размер элемента в байтах
     * @return объект предиката (может вернуть не только объект-элемент,
     * но и объект-переменную)
     */
    public abstract ValueObject createElementObject(ComplexObject parent, int shift, int size);

    /**
     * Создать временный объект предиката, соответствующий существующему
     * @param vo существующий объект
     * @return временный объект
     */
    public abstract TemporaryObject createTemporaryObject(ValueObject vo);

    /**
     * СОздать предикат по типу и объектам
     * @param pt тип предиката
     * @param objs объекты предиката (доп. параметры)
     * @return предикат
     * @throws PredicateCreateException если объекты предиката не соответствуют типу
     */
    public abstract Predicate createPredicate(PredicateType pt, PredicateObject... objs)
            throws PredicateCreateException;

    /**
     * СОздать предикат по типу и объектам
     * @param pt тип предиката
     * @param objs объекты предиката (список)
     * @return предикат
     * @throws PredicateCreateException если объекты предиката не соответствуют типу
     */
    public abstract Predicate createPredicate(PredicateType pt, List<PredicateObject> objs)
            throws PredicateCreateException;

    /**
     *
     * @return заведомо истинный предикат
     */
    public abstract Predicate getTruth();

    /**
     *
     * @return заведомо ложный предикат
     */
    public abstract Predicate getFalsehood();

    /**
     * Заменить в предикате значение на временный объект
     * @param p старый предикат
     * @param vo объект-значение
     * @param to временный объект
     * @return новый предикат
     * @throws PredicateCreateException если объекты предиката не соответствуют типу
     */
    public abstract Predicate createChangedPredicate(Predicate p,
            ValueObject vo, TemporaryObject to) throws PredicateCreateException;

    /**
     * Создание списка
     * @param po элементы списка
     * @return список объектов предиката
     * @throws PredicateCreateException
     */
    public abstract ListObject createListObject(PredicateObject... po)
        throws PredicateCreateException;

    /**
     * Создание списка
     * @param list элементы списка
     * @return список объектов предиката
     * @throws PredicateCreateException
     */
    public abstract ListObject createListObject(List<PredicateObject> list)
            throws PredicateCreateException;

    /**
     * Создание объекта типа first-или-second
     * @param first первая альтернатива
     * @param second вторая альтернатива
     * @return объект типа или-или
     */
    public abstract PredicateObject createOneOfObject(PredicateObject first, PredicateObject second);

}
