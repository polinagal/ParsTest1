package s2a.inference.mgp;

import s2a.inference.api.InferenceRule;
import s2a.predicates.api.Predicate;

import java.util.Collections;
import java.util.List;

/**
 * Правило вывода в стиле Пролога
 */
public class PrologInferenceRule implements InferenceRule {

    private final Predicate right;

    private final List<Predicate> leftPart;

    protected PrologInferenceRule(final List<Predicate> leftPart, final Predicate right) {
        this.leftPart = leftPart;
        this.right = right;
    }

    @Override
    public boolean isPrecise() {
        return false;
    }

    @Override
    public boolean isEquivalent() {
        return false;
    }

    @Override
    public List<Predicate> getLeftPart() {
        return leftPart;
    }

    @Override
    public List<Predicate> getRightPart() {
        return Collections.singletonList(right);
    }

    Predicate getRight() {
        return right;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(right);
        sb.append(" :- ");
        boolean first = true;
        for (Predicate left: leftPart) {
            if (first)
                first = false;
            else
                sb.append(", ");
            sb.append(left);
        }
        return sb.toString();
    }
}
