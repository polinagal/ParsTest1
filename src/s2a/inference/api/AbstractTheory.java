package s2a.inference.api;

import s2a.predicates.api.*;
import s2a.predicates.impl.PredicateFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Абстрактный класс, содержащий обобщенную реализацию теории,
 * без каких-либо доказательств
 * @author Tatiana Krikun, Mikhail Glukhikh
 */
public abstract class AbstractTheory implements Theory {
    /**
     * Предикаты, на основе которых делаются выводы
     */
    protected final List<Predicate> predicates;
    /**
     * Предикаты, определяющие гипотезу(цель)
     */
    protected final List<Predicate> goal;

    protected AbstractTheory() {
        this(new ArrayList<Predicate>(), new ArrayList<Predicate>());
    }

    protected AbstractTheory(List<Predicate> predicates) {
        this(predicates, new ArrayList<Predicate>());
    }

    protected AbstractTheory(Theory t) {
        this(new ArrayList<Predicate>(t.getPredicates()), new ArrayList<Predicate>());
    }

    protected AbstractTheory(List<Predicate> predicates, List<Predicate> goal) {
        this.predicates = predicates;
        this.goal = goal;
    }

    public void addPredicate(Predicate p) {
        predicates.add(p);
        Logger.getLogger(AbstractTheory.class.getName()).log(Level.INFO, p.toString());
        //System.out.println(p);
    }

    @Override
    public void removePredicate(Predicate p) {
        predicates.remove(p);
        Logger.getLogger(AbstractTheory.class.getName()).log(Level.INFO, "REMOVED: " + p.toString());
    }

    public void setGoal(Predicate goalp) {
        this.goal.clear();
        if(goalp != null)
            goal.add(goalp);
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public List<Predicate> getGoal() {
        return goal;
    }

    /**
     * Метод слияния двух теорий.
     * В результате слияния в текущей теории
     * остаются только предикаты, содержащиеся
     * в обеих теориях.
     *
     * @param t теория с которой сливаем
     */
    public void add(Theory t) {
        for (Predicate p : t.getPredicates()) {
            if (!predicates.contains(p))
                addPredicate(p);
        }

        for (Predicate p : t.getGoal()) {
            if (!goal.contains(p))
                goal.add(p);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // добавляем обычные предикаты
        for (Predicate p : predicates) {
            if (!sb.toString().isEmpty()) {
                sb.append(',');
            }
            if (p.getType().isFact()) {
                sb.append("uassert(");
            }
            sb.append(p.toString());
            if (p.getType().isFact()) {
                sb.append(")");
            }
        }
        // добавляем предикаты цели
        for (Predicate p : goal) {
            sb.append(',');
            sb.append(p.toString());
        }
        return sb.toString();
    }
    // Заменить версию объекта в одном предикате
    private Predicate changeVersion(Predicate p, VersionObject oldver,VersionObject newver){
        PredicateFactory factory = PredicateFactory.getInstance();
        if ( newver == null) {
            return null;
        }
        final Object old;
        if(oldver == null){
            return null;
        }
        else{
            old = oldver;
        }
        if (p.getType().equals(PredicateType.EQUIV)) {
            List<PredicateObject> args = p.getArguments();
            List<PredicateObject> newargs = new ArrayList<PredicateObject>();
            boolean changed = false;
            for(PredicateObject arg: args){
                if(arg instanceof Predicate){
                    Predicate parg = (Predicate)arg;
                    Predicate newparg = changeVersion(parg, oldver, newver);
                    if(newparg != null){
                        newargs.add(newparg);
                        changed = true;
                    }
                    else{
                        newargs.add(parg);
                    }
                }
            }
            if(changed){
                try {
                    return factory.createPredicate(PredicateType.EQUIV, newargs);
                } catch (PredicateCreateException ex) {
                    Logger.getLogger(AbstractTheory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (p.getType().equals(PredicateType.PTR)) {
            List<PredicateObject> args = p.getArguments();
            PredicateObject target = args.get(0);
            if (target.toString().equals(old.toString())) {
                try {
                    return factory.createPredicate(p.getType(), (PredicateObject) newver, args.get(1), args.get(2));
                } catch (PredicateCreateException ex) {
                }
            }
        } else if (p.getType().equals(PredicateType.ARRAYSET) && oldver.getObject() instanceof ElementObject) {
            List<PredicateObject> args = p.getArguments();
            PredicateObject element = args.get(1);
            if (element.toString().equals(old.toString())) {
                try {
                    return factory.createPredicate(p.getType(), args.get(0), (PredicateObject) newver, args.get(2));
                } catch (PredicateCreateException ex) {
                }
            }
        }
        return null;
    }
    // Заменить версию объекта во всей теории
    public void changeVersion(VersionObject oldver, VersionObject newver) {
        PredicateFactory factory = PredicateFactory.getInstance();
        List<Predicate> newPredicates = new ArrayList<Predicate>();
        // NB: по-хорошему, тут надо не создавать URETRACT-ы,
        // а просто удалять старые предикаты (см. закомментированный код),
        // однако в режиме с SMT это приводит к появлению кучи ошибок
        // вида "обе цели провалились". В других режимах проблем вроде не возникает
        List<Predicate> delPredicates = new ArrayList<Predicate>();
        for (Predicate p : predicates) {
            Predicate p2 = changeVersion(p, oldver, newver);
            if(p2 != null){
                try {
                    newPredicates.add(factory.createPredicate(PredicateType.URETRACT, p));
                    delPredicates.add(p);
                    newPredicates.add(changeVersion(p, oldver, newver));
                } catch (PredicateCreateException ex) {
                    Logger.getLogger(AbstractTheory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
        }
        for(Predicate p : delPredicates){
            removePredicate(p);
        }
        for(Predicate p : newPredicates){
            addPredicate(p);
        }
    }

    public abstract Theory clone();
}
