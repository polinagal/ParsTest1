/*
 * $Id:$
 */

package s2a.predicates.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import s2a.predicates.api.*;

/**
 * Реализация фабрики предикатов
 * @author Mikhail Glukhikh
 */
public class PredicateFactory extends AbstractPredicateFactory {

    static private final PredicateFactory instance = new PredicateFactory();

    static public PredicateFactory getInstance() {
        return instance;
    }
    @Override
    public ComplexVariableObject createVariableObject(final int size, final String name) {
        return new MemNamedSizedObject(name, size);
    }

    @Override
    public SimpleConstantObject createIntegerConstantObject(long value, int size) {
        return new MemIntegerConstantObject(value, size);
    }

    @Override
    public ValueObject createElementObject(ComplexObject parent, int shift, int size) {
        return new MemElementObject(parent, shift, size);
    }

    @Override
    public Predicate createPredicate(PredicateType pt, PredicateObject... objs)
            throws PredicateCreateException {
        return createPredicate(pt, Arrays.asList(objs));
    }

    @Override
    public Predicate createPredicate(PredicateType pt, List<PredicateObject> objs)
            throws PredicateCreateException {
        int index = 0;
        for (PredicateObject obj: objs) {
            if (obj instanceof OneOfPredicateObject) {
                final OneOfPredicateObject oneOfObj = (OneOfPredicateObject)obj;
                final List<PredicateObject> oneOfArgs = new LinkedList<PredicateObject>();
                for (PredicateObject variant: oneOfObj.getVariants()) {
                    final List<PredicateObject> variantArgs = new LinkedList<PredicateObject>();
                    if (index > 0)
                        variantArgs.addAll(objs.subList(0, index));
                    variantArgs.add(variant);
                    if (index < objs.size())
                        variantArgs.addAll(objs.subList(index+1, objs.size()));
                    final Predicate variantPredicate = createPredicate(pt, variantArgs);
                    oneOfArgs.add(variantPredicate);
                }
                return createPredicate(PredicateType.ONEOF, oneOfArgs);
            }
            index++;
        }
        final Predicate simplPred = pt.simplify(objs);
        return simplPred != null ? simplPred : new MemPredicate(pt, objs);
    }

    @Override
    public Predicate createChangedPredicate(Predicate p,
            ValueObject vo, TemporaryObject to) throws PredicateCreateException {
        final List<PredicateObject> args = new LinkedList<PredicateObject>(p.getArguments());
        for (int i = 0; i < args.size(); i++) {
            if (vo.equals(args.get(i))) {
                args.set(i, to);
            } else if (args.get(i) instanceof Predicate) {
                final Predicate child = (Predicate)args.get(i);
                if (child.depends(vo)) {
                    args.set(i, createChangedPredicate(child, vo, to));
                }
            }
        }
        return createPredicate(p.getType(), args);
    }

    @Override
    public ComplexConstantObject createStringConstantObject(String value) {
        return new MemStringConstantObject(value);
    }

    @Override
    public TemporaryObject createTemporaryObject(ValueObject vo) {
        return new MemTemporaryObject(vo);
    }

    @Override
    public Predicate getTruth() {
        return PredicateType.TRUE_PRED;
    }

    @Override
    public Predicate getFalsehood() {
        return PredicateType.FALSE_PRED;
    }

    @Override
    public ListObject createListObject(PredicateObject... po)
            throws PredicateCreateException {
        return createListObject(Arrays.asList(po));
    }

    @Override
    public ListObject createListObject(List<PredicateObject> list)
            throws PredicateCreateException {
        final ListObject res = new MemListObject();
        for (PredicateObject obj: list) {
            res.append(obj);
        }
        return res;
    }

    @Override
    public PredicateObject createOneOfObject(PredicateObject first, PredicateObject second) {
        return new OneOfPredicateObject(first, second);
    }

}
