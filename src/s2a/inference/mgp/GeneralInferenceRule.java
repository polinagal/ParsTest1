/*
 * $Id:$
 */

package s2a.inference.mgp;

import java.util.Collections;
import java.util.List;
import s2a.inference.api.InferenceRule;
import s2a.predicates.api.Predicate;

/**
 * Обобщенная реализация правила вывода
 * @author Mikhail Glukhikh
 */
public class GeneralInferenceRule implements InferenceRule {

    private final boolean precise;

    private final boolean equivalent;

    private final List<Predicate> leftPart;

    private final List<Predicate> rightPart;

    /**
     * Конструктор базового правила
     * @param precise является ли правило точным (не теряющим информацию)
     * @param equivalent является ли правило эквивалентным
     * (разрешается ли переставить левую и правую часть)
     * @param leftPart левая часть правила (причина)
     * @param rightPart правая часть правила (следствие)
     */
    GeneralInferenceRule(final boolean precise, final boolean equivalent,
                         final List<Predicate> leftPart, final List<Predicate> rightPart) {
        if (equivalent && !precise)
            throw new AssertionError("Правило не может быть эквивалентным и не точным");
        this.precise = precise;
        this.equivalent = equivalent;
        this.leftPart = Collections.unmodifiableList(leftPart);
        this.rightPart = Collections.unmodifiableList(rightPart);
    }

    @Override
    public final boolean isPrecise() {
        return precise;
    }

    @Override
    public final boolean isEquivalent() {
        return equivalent;
    }

    @Override
    public final List<Predicate> getLeftPart() {
        return leftPart;
    }

    @Override
    public final List<Predicate> getRightPart() {
        return rightPart;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Predicate right: rightPart) {
            if (first)
                first = false;
            else
                sb.append(", ");
            sb.append(right);
        }
        if (equivalent)
            sb.append(" <==> ");
        else if (precise)
            sb.append(" <== ");
        else
            sb.append(" <-- ");
        first = true;
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
