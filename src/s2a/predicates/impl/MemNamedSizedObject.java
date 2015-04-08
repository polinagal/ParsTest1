/*
 * $Id: MemStaticObject.java 19854 2011-01-12 12:17:40Z glukhikh $
 */

package s2a.predicates.impl;

import s2a.predicates.api.ComplexVariableObject;

/**
 * Реализация статического объекта -- переменной любого типа
 *
 * @author Вадим Цесько &lt;vadim.tsesko@gmail.com&gt;
 */
public class MemNamedSizedObject implements ComplexVariableObject {
    private final String name;
    private final int size;

    /**
     * Конструктор статического объекта.
     *
     */
    protected MemNamedSizedObject(
            final String name, final int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public int getObjectSize() {
        return size;
    }

    @Override
    public String getUniqueName() {
        return toString();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        //long version = VersionStorage.getInstance().getVersion(variable);
        String varName = this.name;
        varName = varName.replaceAll("[@:\\*><]", "");
        if (varName.lastIndexOf("#") != -1) {
            int index = varName.lastIndexOf("#");
            varName = varName.substring(0, index);
        }
        varName = varName.replaceAll("\\'", "_");
        varName = varName.replaceAll("\\.", "0");
        varName = varName.replaceAll("#", "");
        sb.append('V');
        sb.append(varName);
        return sb.toString();
    }

    @Override
    public String getShortName() {
        return name;
    }
}
