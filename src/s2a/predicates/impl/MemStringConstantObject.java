/*
 * $Id:$
 */
package s2a.predicates.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import s2a.predicates.api.ComplexConstantObject;
import s2a.predicates.api.ElementObject;
import s2a.predicates.api.RuntimeObject;

/**
 * Реализация сложной (строковой) константы
 * @author Mikhail Glukhikh
 */
public class MemStringConstantObject implements ComplexConstantObject, RuntimeObject {

    final String value;
    private Map<Integer, ElementObject> elements = new HashMap<Integer, ElementObject>();

    MemStringConstantObject(String value) {
        this.value = value;
    }

    /**
     * Создать простые элементы для строковой константы
     * @param type тип элементов
     */
    public void createElements(int size) {
        for (int i = 0; i < value.length(); i++) {
            int shift = i * size;
            ElementObject el = (ElementObject) PredicateFactory.getInstance().createElementObject(
                    this, shift, size);
            elements.put(shift, el);
        }
    }

    /**
     * Получить все простые элементы
     * @return список простых элементов
     */
    public List<ElementObject> getElements() {
        List<ElementObject> res = new ArrayList<ElementObject>();
        Iterator it = elements.entrySet().iterator();
        Entry entry;
        while (it.hasNext()) {
            entry = (Entry) it.next();
            res.add((ElementObject) entry.getValue());
        }
        return res;
    }

    /**
     * Получить простой элемент по смещению
     * @param offset смещение в байтах
     * @return простой элемент
     */
    public ElementObject getElement(int offset) {
        return elements.get(offset);
    }

    /**
     * Получить простые элементы
     * @param shift начальное смещение
     * @param size размер в байтах
     * @return список элементов
     * @throws GetElementObjectException
     */
    public List<ElementObject> getElements(int shift, long size) throws GetElementObjectException{
        List<ElementObject> res = new ArrayList<ElementObject>();
        if (shift+size > getObjectSize()){
            throw new GetElementObjectException ("Выход за границу строки");
        }else {
            SortedSet<Integer> offsets =new TreeSet<Integer>();
            offsets.addAll(elements.keySet());
            for (Integer offset: offsets){
                if (offset >= shift && offset < shift + size){
                    res.add(elements.get(offset));
                }
            }
        }
        return res;
    }

    /**
     * Получить значение, соответствующее объекту с заданным смещением
     * @param shift смещение от начала объекта в байтах
     * @return значение, соответствующее объекту
     */
    public long getConstantValue(int shift) {
        assert (shift >= 0) && (shift < value.length());
        long result;
        int c = value.charAt(shift);
        result = c;
        return result;
    }

    public int getObjectSize() {
        return value.length();
    }

    public String getUniqueName() {
        return this.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Sconst_");
        String name = value;
        int index = name.lastIndexOf('\0');
		if(index != -1)
			name = name.substring(0,index);
        sb.append(name);
        sb.append("_");
        //sb.append(value);
        return sb.toString();
    }
}
