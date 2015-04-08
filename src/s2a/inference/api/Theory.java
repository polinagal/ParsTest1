package s2a.inference.api;

import s2a.predicates.api.Predicate;
import s2a.predicates.api.VersionObject;

import java.util.List;

/**
 * Interface for a theory and its goal as a set of predicates
 */
public interface Theory extends Cloneable {

    /**
     * Добавить предикат к теории
     * @param p добавляемый предикат
     */
    public void addPredicate(Predicate p);

    /**
     * Убрать предикат из теории
     * @param p убираемый предикат
     *
     */
    public void removePredicate(Predicate p);

    /**
     * Установить целевой предикат
     * @param goal новый целевой предикат
     */
    public void setGoal(Predicate goal);

    /**
     * Получить список предикатов теории
     * @return список предикатов теории
     */
    public List<Predicate> getPredicates();

    /**
     * Получить список целевых предикатов
     * @return список целевых предикатов
     */
    public List<Predicate> getGoal();

    /**
     * Проверка правильности теории
     *
     * @return true если теория правильна (непротиворечива?)
     */
    public boolean checkValid();

    /**
     * Метод слияния двух теорий.
     * В результате слияния в текущей теории
     * остаются только предикаты, содержащиеся
     * в обеих теориях.
     *
     * @param t теория, с которой сливаем текущую
     */
    public void add(Theory t);

    /**
     * Поменять версию одного из задействованных объектов
     * @param oldver меняемый объект
     * @param newver новый объект
     */
    public void changeVersion(VersionObject oldver, VersionObject newver);

    /**
     * Клонирование
     * @return копия данной теории
     */
    public Theory clone();
}
