package s2a.inference.mgp;

import s2a.predicates.api.AbstractPredicateFactory;
import s2a.predicates.api.PredicateObject;

import java.util.*;

/**
 * Объект хранит таблицу соответствия между кванторами и объектами предикатов
 * User: Mikhail Glukhikh
 * Date: 13.01.13
 * Time: 22:34
 */
class QuantifierValueMap implements Cloneable {

    static private final AbstractPredicateFactory factory = AbstractPredicateFactory.getInstance();

    private final Map<StackedQuantifier, PredicateObject> valueMap =
            new HashMap<StackedQuantifier, PredicateObject>();

    void merge(QuantifierValueMap other) {
        final Set<StackedQuantifier> mergedKeys = valueMap.keySet();
        mergedKeys.retainAll(other.valueMap.keySet());
        for (StackedQuantifier key: mergedKeys) {
            final PredicateObject prev = valueMap.get(key);
            final PredicateObject modifier = other.valueMap.get(key);
            final PredicateObject res;
            if (prev.equals(modifier)) {
                res = prev;
            } else {
                res = factory.createOneOfObject(prev, modifier);
            }
            assert(!key.equals(res));
            valueMap.put(key, res);
        }
    }

    void put(final StackedQuantifier key, final PredicateObject value) {
        assert(!key.equals(value));
        final PredicateObject old = valueMap.get(key);
        if (old instanceof StackedQuantifier) {
            assert(!old.equals(value));
            put((StackedQuantifier)old, value);
            //valueMap.put((StackedQuantifier)old, value);
        }
        valueMap.put(key, value);
    }

    boolean contains(final StackedQuantifier key) {
        return get(key) != null;
    }

    PredicateObject get(final StackedQuantifier key) {
        final PredicateObject obj = valueMap.get(key);
        if (obj instanceof StackedQuantifier) {
            assert(!obj.equals(key));
            return get((StackedQuantifier)obj);
        } else {
            return obj;
        }
    }

    void putAll(final QuantifierValueMap toPut) {
        valueMap.putAll(toPut.valueMap);
    }

    void removeOlder(final int level) {
        final Iterator<StackedQuantifier> keyIt = valueMap.keySet().iterator();
        while (keyIt.hasNext()) {
            if (keyIt.next().getLevel() > level)
                keyIt.remove();
        }
    }

    public String toString() {
        return valueMap.toString();
    }

    public QuantifierValueMap clone() {
        QuantifierValueMap result = new QuantifierValueMap();
        result.valueMap.putAll(valueMap);
        return result;
    }
}

