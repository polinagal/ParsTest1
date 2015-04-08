package s2a.inference.mgp;

import s2a.inference.api.AbstractLogicianFactory;
import s2a.inference.api.AbstractTheory;
import s2a.inference.api.Logician;
import s2a.inference.api.Theory;
import s2a.predicates.api.Predicate;
import s2a.predicates.api.PredicateCreateException;
import s2a.predicates.api.PredicateType;

/**
 * Реализация теории на основе встроенного логика
 */
public class LogicianTheory extends AbstractTheory implements Theory {

    static private final LogicianFactory factory =
            (LogicianFactory)AbstractLogicianFactory.getInstance();

    private Logician logician = factory.createLogician(); {
        try {
            factory.addAllRules(logician);
            //factory.addComparisonRules(logician);
        } catch (PredicateCreateException e) {
            throw new AssertionError(e);
        }
    }

    protected LogicianTheory() {
        super();
    }

    private LogicianTheory(Theory t) {
        super(t);
    }

    @Override
    public void addPredicate(Predicate p) {
        if (p.getType()== PredicateType.URETRACT) {
            final Predicate toRemove = (Predicate)p.getArguments().get(0);
            removePredicate(toRemove);
        } else {
            super.addPredicate(p);
        }
    }

    /**
     * Проверка правильности теории
     *
     * @return true если цель теории разрешима в рамках теории
     */
    public boolean checkValid() {
        if (predicates.isEmpty()) {
            return true;
        }
        assert goal.size()<=1:
                "checkValid does not yet work with more than one goal" +
                goal;
        if (goal.isEmpty())
            return true;
        final Predicate toSolve = goal.iterator().next();
        return logician.solveTrue(this, toSolve);
    }

    public LogicianTheory clone() {
        return new LogicianTheory(this);
    }
}
