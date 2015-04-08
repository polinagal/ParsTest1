package s2a.inference.mgp;

import s2a.inference.api.QuantifierObject;
import s2a.inference.api.QuantifierPredicate;
import s2a.predicates.api.PredicateObject;

/**
 * Quantifier which include stack level where it is used
 * User: mike
 * Date: 2/15/13
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
class StackedQuantifier extends MemQuantifierPredicate implements QuantifierPredicate {

    private final QuantifierObject base;

    private final int level;

    static StackedQuantifier create(final QuantifierObject base, final int level) {
        if (base instanceof StackedQuantifier)
            return (StackedQuantifier)base;
        return new StackedQuantifier(base, level);
    }

    private StackedQuantifier(final QuantifierObject base, final int level) {
        super(base.getCode(), base.getUniqueName());
        this.base = base;
        this.level = level;
    }

    int getLevel() {
        return level;
    }

    QuantifierObject getBase() {
        return base;
    }

    @Override
    public int getCode() {
        return base.getCode();
    }

    @Override
    public boolean isCompatible(PredicateObject obj) {
        return base.isCompatible(obj);
    }

    @Override
    public String getUniqueName() {
        return base.getUniqueName();
    }

    @Override
    public int hashCode() {
        int result = base.hashCode();
        result = 31 * result + level;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj)
            return true;
        if (obj instanceof StackedQuantifier) {
            final StackedQuantifier other = (StackedQuantifier)obj;
            return level==other.level && base.equals(other.base);
        }
        return false;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        return sb.append(base).append("#").append(level).toString();
    }
}
