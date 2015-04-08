/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package s2a.predicates.api;

/**
 * Объект, имеющий конкретное значение с учётом версии
 * @author Tatiana Vert
 */
public interface VersionObject {
    /**
     * Получить объект 
     * @return объект
     */
    public ValueObject getObject();
    /**
     * Получить номер версии значения объекта
     * @return номер версии
     */
    public long getVersion();
}
