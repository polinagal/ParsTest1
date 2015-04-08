package s2a.inference.mgp;

import s2a.inference.api.QuantifierPredicate;
import s2a.predicates.api.Predicate;
import s2a.predicates.api.PredicateObject;
import s2a.predicates.api.PredicateType;

import java.util.Collections;
import java.util.List;

/**
 * Реализация квантора для предиката
 *
 * @author Mikhail Glukhikh
 */
public class MemQuantifierPredicate extends AbstractQuantifierObject implements QuantifierPredicate {

    protected MemQuantifierPredicate(final int code, final String name) {
        super(code, name);
    }

    @Override
    public List<PredicateObject> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public PredicateType getType() {
        return PredicateType.UNKNOWN;
    }

    @Override
    public boolean depends(PredicateObject obj) {
        return false;
    }

    @Override
    public boolean isQuantifierFree() {
        return true;
    }

    @Override
    public boolean isCompatible(PredicateObject obj) {
        return obj instanceof Predicate;
    }
}
