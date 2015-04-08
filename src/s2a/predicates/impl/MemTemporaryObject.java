/*
 * $Id:$
 */

package s2a.predicates.impl;

import s2a.predicates.api.TemporaryObject;
import s2a.predicates.api.ValueObject;

/**
 * Реализация временного объекта предиката
 * @author Mikhail Glukhikh
 */
public class MemTemporaryObject implements TemporaryObject {
    
    static private int count = 0;

    private final String name;

    private final int size;

    MemTemporaryObject(ValueObject vo) {
        count++;
        final StringBuilder sb = new StringBuilder();
        sb.append(vo);
        sb.append("~");
        sb.append(count);
        name = sb.toString();
        size = vo.getValueSize();
    }

    public int getValueSize() {
        return size;
    }

    public String getUniqueName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
  
}
