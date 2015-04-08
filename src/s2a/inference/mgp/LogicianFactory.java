/*
 * $Id:$
 */

package s2a.inference.mgp;

import java.util.*;

import s2a.inference.api.*;
import s2a.predicates.api.*;

/**
 * Реализация фабрики логика.
 * Создает логика и заполняет его правилами.
 * В перспективе, сюда должны переехать все правила
 * из InferenceRule.java.
 * @author Mikhail Glukhikh
 */
public class LogicianFactory extends AbstractLogicianFactory {

    static private final AbstractPredicateFactory predFactory =
            AbstractPredicateFactory.getInstance();

    static private final AbstractQuantifierFactory quantifierFactory =
            AbstractQuantifierFactory.getInstance();

    static public final LogicianFactory instance = new LogicianFactory();

    /**
     * Создать множество из набора предикатов
     * @param ps набор предикатов
     * @return множество предикатов
     */
    private List<Predicate> createPredicateList(final Predicate... ps) {
        if (ps.length==1)
            return Collections.singletonList(ps[0]);
        final List<Predicate> result = new LinkedList<Predicate>();
        result.addAll(Arrays.asList(ps));
        return result;
    }

    /**
     * Classic "Modus Ponens" rule.
     * May be, it's better to integrate it somehow in the logician core.
     * Also, it's quite possible that in reality it's second order rule
     *
     * @return EQUIV(p, q), p ==> q
     * @throws PredicateCreateException
     */
    InferenceRule modusPonensRule() throws PredicateCreateException {
        final QuantifierPredicate p = quantifierFactory.createQuantifierPredicate(1, "p");
        final QuantifierPredicate q = quantifierFactory.createQuantifierPredicate(2, "q");
        final Predicate l1 = predFactory.createPredicate(PredicateType.EQUIV, p, q);
        return createPrologRule(createPredicateList(l1, p), q);
    }

    /**
     * Classic "Modus Ponens" rule in inverse form.
     * May be, it's better to integrate it somehow in the logician core.
     * Also, it's quite possible that in reality it's second order rule
     *
     * @return EQUIV(p, q), OPPOS(p) ==> OPPOS(q)
     * @throws PredicateCreateException
     */
    InferenceRule modusPonensReverseRule() throws PredicateCreateException {
        final QuantifierPredicate p = quantifierFactory.createQuantifierPredicate(1, "p");
        final QuantifierPredicate q = quantifierFactory.createQuantifierPredicate(2, "q");
        final Predicate l1 = predFactory.createPredicate(PredicateType.EQUIV, p, q);
        final Predicate l2 = predFactory.createPredicate(PredicateType.OPPOS, p);
        final Predicate r = predFactory.createPredicate(PredicateType.OPPOS, q);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    Logician addModusPonensRules(final Logician logician) throws PredicateCreateException {
        logician.addRule(modusPonensRule());
        logician.addRule(modusPonensReverseRule());
        return logician;
    }

    /**
     *
     * @return ZERO(a) <==> EQUALS(a, 0)
     * @throws PredicateCreateException
     */
    InferenceRule equalsToZeroRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final Predicate l = predFactory.createPredicate(PredicateType.EQUALS,
                a, predFactory.createIntegerConstantObject(0, 1));
        final Predicate r = predFactory.createPredicate(PredicateType.ZERO, a);
        return createPrologRule(createPredicateList(l), r);
    }

    InferenceRule zeroToEqualsRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS,
                a, predFactory.createIntegerConstantObject(0, 1));
        final Predicate l = predFactory.createPredicate(PredicateType.ZERO, a);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return NONZERO(a) <==> NOT_EQUALS(a, 0)
     * @throws PredicateCreateException
     */
    InferenceRule notEqualsToNonzeroRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final Predicate l = predFactory.createPredicate(PredicateType.NOT_EQUALS,
                a, predFactory.createIntegerConstantObject(0, 1));
        final Predicate r = predFactory.createPredicate(PredicateType.NONZERO, a);
        return createPrologRule(createPredicateList(l), r);
    }

    InferenceRule nonzeroToNotEqualsRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final Predicate r = predFactory.createPredicate(PredicateType.NOT_EQUALS,
                a, predFactory.createIntegerConstantObject(0, 1));
        final Predicate l = predFactory.createPredicate(PredicateType.NONZERO, a);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return NOT(a, b) <==> NOT(b, a)
     * @throws PredicateCreateException
     */
    InferenceRule notSymmetryRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final Predicate r = predFactory.createPredicate(PredicateType.NOT, a, b);
        final Predicate l = predFactory.createPredicate(PredicateType.NOT, b, a);
        return createPrologRule(Collections.singletonList(l), r);
    }

    /**
     *
     * @return NOT(a, b), NONZERO(a) ==> ZERO(b)
     * @throws PredicateCreateException
     */
    InferenceRule zeroNotRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final Predicate r = predFactory.createPredicate(PredicateType.ZERO, b);
        final Predicate l1 = predFactory.createPredicate(PredicateType.NOT, a, b);
        final Predicate l2 = predFactory.createPredicate(PredicateType.NONZERO, a);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return NOT(a, b), ZERO(a) ==> NONZERO(b)
     * @throws PredicateCreateException
     */
    InferenceRule nonzeroNotRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final Predicate r = predFactory.createPredicate(PredicateType.NONZERO, b);
        final Predicate l1 = predFactory.createPredicate(PredicateType.NOT, a, b);
        final Predicate l2 = predFactory.createPredicate(PredicateType.ZERO, a);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return OR(a, c, b) ==> OR(a, b, c)
     * @throws PredicateCreateException
     */
    InferenceRule orCommutativityRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.OR, a, c, b);
        final Predicate l = predFactory.createPredicate(PredicateType.OR, a, b, c);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return OR(a, b, c), ZERO(a) ==> ZERO(b)
     * @throws PredicateCreateException
     */
    InferenceRule zeroOrRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.ZERO, b);
        final Predicate l1 = predFactory.createPredicate(PredicateType.OR, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.ZERO, a);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return OR(a, b, c), NONZERO(a), ZERO(c) ==> NONZERO(b)
     * @throws PredicateCreateException
     */
    InferenceRule nonZeroOrRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.NONZERO, b);
        final Predicate l1 = predFactory.createPredicate(PredicateType.OR, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.NONZERO, a);
        final Predicate l3 = predFactory.createPredicate(PredicateType.ZERO, c);
        return createPrologRule(createPredicateList(l1, l2, l3), r);
    }

    /**
     *
     * @return AND(a, c, b) ==> AND(a, b, c)
     * @throws PredicateCreateException
     */
    InferenceRule andCommutativityRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.AND, a, c, b);
        final Predicate l = predFactory.createPredicate(PredicateType.AND, a, b, c);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return AND(a, b, c), NONZERO(a) ==> NONZERO(b)
     * @throws PredicateCreateException
     */
    InferenceRule nonZeroAndRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.NONZERO, b);
        final Predicate l1 = predFactory.createPredicate(PredicateType.AND, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.NONZERO, a);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return AND(a, b, c), ZERO(a), NONZERO(c) ==> ZERO(b)
     * @throws PredicateCreateException
     */
    InferenceRule zeroAndRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.ZERO, b);
        final Predicate l1 = predFactory.createPredicate(PredicateType.AND, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.ZERO, a);
        final Predicate l3 = predFactory.createPredicate(PredicateType.NONZERO, c);
        return createPrologRule(createPredicateList(l1, l2, l3), r);
    }

    Logician addZeroNonzeroRules(final Logician logician) throws PredicateCreateException {
        logician.addRule(zeroToEqualsRule());
        logician.addRule(nonzeroToNotEqualsRule());
        logician.addRule(equalsToZeroRule());
        logician.addRule(notEqualsToNonzeroRule());
        logician.addRule(notSymmetryRule());
        logician.addRule(zeroNotRule());
        logician.addRule(nonzeroNotRule());
        logician.addRule(orCommutativityRule());
        logician.addRule(zeroOrRule());
        logician.addRule(nonZeroOrRule());
        logician.addRule(andCommutativityRule());
        logician.addRule(zeroAndRule());
        logician.addRule(nonZeroAndRule());
        return logician;
    }

    /**
     *
     * @return SUM(v, w, c1), LESS_EQ(w, c2) ==> LESS_EQ(v, c3=c1+c2)
     * @throws PredicateCreateException
     */
    private InferenceRule sumLessEqRule() throws PredicateCreateException {
        final QuantifierValue v = quantifierFactory.createQuantifierNonconstValue(1, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierNonconstValue(2, "w");
        final QuantifierSimpleConstant c1 = quantifierFactory.createQuantifierSimpleConstant(3, "c1");
        final QuantifierSimpleConstant c2 = quantifierFactory.createQuantifierSimpleConstant(4, "c2");
        final QuantifierSimpleConstant c3 = quantifierFactory.createQuantifierSimpleConstant(5, "c3");
        final Predicate l1 = predFactory.createPredicate(PredicateType.SUM, v, w, c1);
        final Predicate l2 = predFactory.createPredicate(PredicateType.LESS_EQUALS, w, c2);
        final Predicate r = predFactory.createPredicate(PredicateType.LESS_EQUALS, v, c3);
        return createGeneralRule(false, false, createPredicateList(l1, l2), createPredicateList(r));
    }

    /**
     *
     * @return SUM(a,b,c)  ==> SUM(a,c,b)
     * @throws PredicateCreateException
     */
    InferenceRule sumCommutativityRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.SUM, a, c, b);
        final Predicate l = predFactory.createPredicate(PredicateType.SUM, a, b, c);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return SUM(a,b,c)  ==> DIFF(b,a,c)
     * @throws PredicateCreateException
     */
    InferenceRule sumToDiffRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.DIFF, b, a, c);
        final Predicate l = predFactory.createPredicate(PredicateType.SUM, a, b, c);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return SUM(a,b,c),ZERO(c) ==> EQUALS(a,b)
     * @throws PredicateCreateException
     */
    InferenceRule sumWithZeroRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS, a, b);
        final Predicate l1 = predFactory.createPredicate(PredicateType.SUM, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.ZERO, c);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return NEG(a,b) ==> NEG(b,a)
     * @throws PredicateCreateException
     */
    InferenceRule negSymmetryRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final Predicate r = predFactory.createPredicate(PredicateType.NEG, a, b);
        final Predicate l = predFactory.createPredicate(PredicateType.NEG, b, a);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return NEG(a,b), NEG(b,c) ==> EQUALS(a,c)
     * @throws PredicateCreateException
     */
    InferenceRule negAntiTransitivityRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS, a, c);
        final Predicate l1 = predFactory.createPredicate(PredicateType.NEG, a, b);
        final Predicate l2 = predFactory.createPredicate(PredicateType.NEG, b, c);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return DIFF(a,b,c), NEG(c,d) ==> SUM(a,b,d)
     * @throws PredicateCreateException
     */
    InferenceRule sumAndDiffNegRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final QuantifierValue d = quantifierFactory.createQuantifierValue(4, "d");
        final Predicate r = predFactory.createPredicate(PredicateType.SUM, a, b, d);
        final Predicate l1 = predFactory.createPredicate(PredicateType.DIFF, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.NEG, c, d);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return DIFF(a,b,c), EQUALS(b,c) ==> EQUALS(a,0)
     * @throws PredicateCreateException
     */
    InferenceRule diffSameIsZeroRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate l1 = predFactory.createPredicate(PredicateType.DIFF, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.EQUALS, b, c);
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS, a,
                predFactory.createIntegerConstantObject(0, 1));
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return DIFF(a,b,c), GREATER_EQUALS(b,c) ==> GREATER_EQUALS(a,0)
     * @throws PredicateCreateException
     */
    InferenceRule diffGreaterEqZeroRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final PredicateObject z = predFactory.createIntegerConstantObject(0, 1);
        final Predicate l1 = predFactory.createPredicate(PredicateType.DIFF, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.GREATER_EQUALS, b, c);
        final Predicate r = predFactory.createPredicate(PredicateType.GREATER_EQUALS, a, z);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return DIFF(a,b,c), SUM(b,c,d) ==> EQUALS(a,d)
     * @throws PredicateCreateException
     */
    InferenceRule diffSimplifyRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final QuantifierValue d = quantifierFactory.createQuantifierValue(4, "d");
        final Predicate l1 = predFactory.createPredicate(PredicateType.DIFF, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.SUM, b, c, d);
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS, a, d);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return PROD(a,b,c)  ==> PROD(a,c,b)
     * @throws PredicateCreateException
     */
    InferenceRule prodCommutativityRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.PROD, a, c, b);
        final Predicate l = predFactory.createPredicate(PredicateType.PROD, a, b, c);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return PROD(a,b,c)  ==> QUOT(b,a,c)
     * @throws PredicateCreateException
     */
    InferenceRule prodToQuotRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.QUOT, b, a, c);
        final Predicate l = predFactory.createPredicate(PredicateType.PROD, a, b, c);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return QUOT(a,b,c)  ==> PROD(b,a,c)
     * @throws PredicateCreateException
     */
    InferenceRule quotToProdRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final Predicate r = predFactory.createPredicate(PredicateType.PROD, b, a, c);
        final Predicate l = predFactory.createPredicate(PredicateType.QUOT, a, b, c);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return PROD(a,b,c), GREATER_EQUALS(b,0), GREATER_EQUALS(c,0) ==> GREATER_EQUALS(a,0)
     * @throws PredicateCreateException
     */
    InferenceRule prodGreaterEqZeroRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final PredicateObject z = predFactory.createIntegerConstantObject(0, 1);
        final Predicate l1 = predFactory.createPredicate(PredicateType.PROD, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.GREATER_EQUALS, b, z);
        final Predicate l3 = predFactory.createPredicate(PredicateType.GREATER_EQUALS, c, z);
        final Predicate r = predFactory.createPredicate(PredicateType.GREATER_EQUALS, a, z);
        return createPrologRule(createPredicateList(l1, l2, l3), r);
    }

    /**
     *
     * @return PROD(a,b,c), PROD(s,c,d), LESS(b,d) ==> LESS(a,s)
     * @throws PredicateCreateException
     */
    InferenceRule prodLessRule() throws PredicateCreateException {
        final QuantifierValue a = quantifierFactory.createQuantifierValue(1, "a");
        final QuantifierValue b = quantifierFactory.createQuantifierValue(2, "b");
        final QuantifierValue c = quantifierFactory.createQuantifierValue(3, "c");
        final QuantifierSimpleConstant d = quantifierFactory.createQuantifierSimpleConstant(4, "d");
        final QuantifierValue s = quantifierFactory.createQuantifierValue(5, "s");
        final Predicate l1 = predFactory.createPredicate(PredicateType.PROD, a, b, c);
        final Predicate l2 = predFactory.createPredicate(PredicateType.PROD, s, c, d);
        final Predicate l3 = predFactory.createPredicate(PredicateType.LESS, b, d);
        final Predicate r = predFactory.createPredicate(PredicateType.LESS, a, s);
        return createPrologRule(createPredicateList(l1, l2, l3), r);
    }

    Logician addArithmeticRules(final Logician logician) throws PredicateCreateException {
        logician.addRule(negSymmetryRule());
        logician.addRule(negAntiTransitivityRule());
        logician.addRule(sumCommutativityRule());
        logician.addRule(sumToDiffRule());
        logician.addRule(sumWithZeroRule());
        logician.addRule(sumAndDiffNegRule());
        logician.addRule(diffSameIsZeroRule());
        logician.addRule(diffGreaterEqZeroRule());
        logician.addRule(diffSimplifyRule());
        logician.addRule(prodCommutativityRule());
        logician.addRule(prodToQuotRule());
        logician.addRule(quotToProdRule());
        logician.addRule(prodGreaterEqZeroRule());
        logician.addRule(prodLessRule());
        return logician;
    }

    /**
     *
     * @return EQUALS(cnst, v) ==> EQUALS(v, cnst)
     * @throws PredicateCreateException
     */
    InferenceRule equalsSymmetryRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final Predicate l = predFactory.createPredicate(PredicateType.EQUALS,
                u, v);
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS,
                v, u);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return EQUALS(u, v), EQUALS(v, w) ==> EQUALS(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule equalsTransitivityRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS, u, w);
        final Predicate l1 = predFactory.createPredicate(PredicateType.EQUALS, u, v);
        final Predicate l2 = predFactory.createPredicate(PredicateType.EQUALS, v, w);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return EQUALS(u, v), NOT_EQUALS(v, w) ==> NOT_EQUALS(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule equalsNotEqualsTransitivityRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate r = predFactory.createPredicate(PredicateType.NOT_EQUALS, u, w);
        final Predicate l1 = predFactory.createPredicate(PredicateType.EQUALS, u, v);
        final Predicate l2 = predFactory.createPredicate(PredicateType.NOT_EQUALS, v, w);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return LESS(x, y) ==> LESS_EQUALS(x, y)
     * @throws PredicateCreateException
     */
    InferenceRule lessToLessEqualsRule() throws PredicateCreateException {
        final QuantifierValue x = quantifierFactory.createQuantifierValue(1, "X");
        final QuantifierValue y = quantifierFactory.createQuantifierValue(2, "Y");
        final Predicate l = predFactory.createPredicate(PredicateType.LESS, x, y);
        final Predicate r = predFactory.createPredicate(PredicateType.LESS_EQUALS, x, y);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return EQUALS(x, y) ==> LESS_EQUALS(x, y)
     * @throws PredicateCreateException
     */
    InferenceRule equalsToLessEqualsRule() throws PredicateCreateException {
        final QuantifierValue x = quantifierFactory.createQuantifierValue(1, "X");
        final QuantifierValue y = quantifierFactory.createQuantifierValue(2, "Y");
        final Predicate l = predFactory.createPredicate(PredicateType.EQUALS, x, y);
        final Predicate r = predFactory.createPredicate(PredicateType.LESS_EQUALS, x, y);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return LESS(u, v) ==> GREATER(v, u)
     * @throws PredicateCreateException
     */
    InferenceRule lessToGreaterRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final Predicate l = predFactory.createPredicate(PredicateType.LESS,
                u, v);
        final Predicate r = predFactory.createPredicate(PredicateType.GREATER,
                v, u);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return LESS_EQ(u, v) ==> GREATER_EQ(v, u)
     * @throws PredicateCreateException
     */
    InferenceRule lessEqToGreaterEqRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final Predicate l = predFactory.createPredicate(PredicateType.LESS_EQUALS,
                u, v);
        final Predicate r = predFactory.createPredicate(PredicateType.GREATER_EQUALS,
                v, u);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return GREATER(u, v) ==> LESS(v, u)
     * @throws PredicateCreateException
     */
    InferenceRule greaterToLessRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final Predicate l = predFactory.createPredicate(PredicateType.GREATER,
                u, v);
        final Predicate r = predFactory.createPredicate(PredicateType.LESS,
                v, u);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return GREATER_EQ(u, v) ==> LESS_EQ(v, u)
     * @throws PredicateCreateException
     */
    InferenceRule greaterEqToLessEqRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final Predicate l = predFactory.createPredicate(PredicateType.GREATER_EQUALS,
                u, v);
        final Predicate r = predFactory.createPredicate(PredicateType.LESS_EQUALS,
                v, u);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return LESS(u, v), LESS_EQ(v, w) ==> LESS(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule lessTransitivityRule01() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate r = predFactory.createPredicate(PredicateType.LESS, u, w);
        final Predicate l1 = predFactory.createPredicate(PredicateType.LESS, u, v);
        final Predicate l2 = predFactory.createPredicate(PredicateType.LESS_EQUALS, v, w);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return LESS_EQ(u, v), LESS(v, w) ==> LESS(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule lessTransitivityRule02() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate r = predFactory.createPredicate(PredicateType.LESS, u, w);
        final Predicate l1 = predFactory.createPredicate(PredicateType.LESS_EQUALS, u, v);
        final Predicate l2 = predFactory.createPredicate(PredicateType.LESS, v, w);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return LESS_EQ(u, v), LESS_EQ(v, w) ==> LESS_EQ(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule lessEqualsAssociativityRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate r = predFactory.createPredicate(PredicateType.LESS_EQUALS, u, w);
        final Predicate l1 = predFactory.createPredicate(PredicateType.LESS_EQUALS, u, v);
        final Predicate l2 = predFactory.createPredicate(PredicateType.LESS_EQUALS, v, w);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     *
     * @return LESS(u, v) ==> NOT_EQUALS(v, u)
     * @throws PredicateCreateException
     */
    InferenceRule lessToNotEqualsRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final Predicate l = predFactory.createPredicate(PredicateType.LESS,
                u, v);
        final Predicate r = predFactory.createPredicate(PredicateType.NOT_EQUALS,
                v, u);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     *
     * @return GREATER(u, v) ==> NOT_EQUALS(v, u)
     * @throws PredicateCreateException
     */
    InferenceRule greaterToNotEqualsRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final Predicate l = predFactory.createPredicate(PredicateType.GREATER,
                u, v);
        final Predicate r = predFactory.createPredicate(PredicateType.NOT_EQUALS,
                v, u);
        return createPrologRule(createPredicateList(l), r);
    }

    Logician addComparisonRules(final Logician logician) throws PredicateCreateException {
        logician.addRule(equalsSymmetryRule());
        logician.addRule(equalsTransitivityRule());
        logician.addRule(equalsNotEqualsTransitivityRule());
        logician.addRule(lessTransitivityRule01());
        logician.addRule(lessTransitivityRule02());
        logician.addRule(lessEqualsAssociativityRule());
        logician.addRule(lessToGreaterRule());
        logician.addRule(greaterToLessRule());
        logician.addRule(lessEqToGreaterEqRule());
        logician.addRule(greaterEqToLessEqRule());
        logician.addRule(equalsToLessEqualsRule());
        logician.addRule(lessToLessEqualsRule());
        logician.addRule(lessToNotEqualsRule());
        logician.addRule(greaterToNotEqualsRule());
        return logician;
    }

    /**
     * v = &u + 0, w = *v ==> u = w
     *
     * @return PTR(u, v, 0), DEREF(w, v) ==> EQUALS(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule simpleRefDereferenceRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate l1 = predFactory.createPredicate(PredicateType.PTR,
                u, v, predFactory.createIntegerConstantObject(0, 1)); // v = &u + 0
        final Predicate l2 = predFactory.createPredicate(PredicateType.DEREFFROM,
                w, v); // w = *v
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS,
                u, w); // Conclusion: u = w
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     * v = &x + s, w = *v, x[s] = u ==> u = w (x is assumed as a byte array)
     *
     * @return PTR(x, v, s), DEREF(w, v), ARRAY(x, s, u) ==> EQUALS(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule complexRefDereferenceRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final QuantifierValue s = quantifierFactory.createQuantifierValue(4, "s");
        final QuantifierValue x = quantifierFactory.createQuantifierValue(5, "x");
        final Predicate l1 = predFactory.createPredicate(PredicateType.PTR,
                x, v, s); // v = &x+s
        final Predicate l2 = predFactory.createPredicate(PredicateType.DEREFFROM,
                w, v); // w = *v
        final Predicate l3 = predFactory.createPredicate(PredicateType.ARRAYSET,
                x, s, u); // x[s] = u
        final Predicate r = predFactory.createPredicate(PredicateType.EQUALS,
                u, w); // Conclusion: u = v
        return createPrologRule(createPredicateList(l1, l2, l3), r);
    }

    /**
     * v = &u + w, w >= 0, w < s, s = sizeof(u) ==> *v is correct
     *
     * @return PTR(u, v, w), GREATER_EQ(w, 0), LESS(w, s), SIZEOF(u, s) ==> CORRECT(v)
     * @throws PredicateCreateException
     */
    InferenceRule correctPtrRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final QuantifierValue s = quantifierFactory.createQuantifierValue(4, "s");
        final Predicate l1 = predFactory.createPredicate(PredicateType.PTR, u, v, w);
        final Predicate l2 = predFactory.createPredicate(PredicateType.GREATER_EQUALS, w,
                predFactory.createIntegerConstantObject(0, 1));
        final Predicate l3 = predFactory.createPredicate(PredicateType.SIZEOF, u, s);
        final Predicate l4 = predFactory.createPredicate(PredicateType.LESS, w, s);
        final Predicate r = predFactory.createPredicate(PredicateType.CORRECT_PTR, v);
        return createPrologRule(createPredicateList(l1, l2, l3, l4), r);
    }

    /**
     * w = &u + 0, v = w + m ==> v = &u + m
     *
     * @return SUM(v, w, m), PTR(u, w, 0) ==> PTR(u, v, m)
     * @throws PredicateCreateException
     */
    InferenceRule sumZeroPtrRule() throws PredicateCreateException {
        // PTR (u, v, m) :- SUM (v, w, m), PTR(u, w, 0)
        // v = &u+m <== v=w+t, w=&u+n, m=n+t
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final QuantifierValue m = quantifierFactory.createQuantifierValue(4, "m");
        final Predicate l1 = predFactory.createPredicate(PredicateType.SUM, v, w, m);
        final Predicate l2 = predFactory.createPredicate(PredicateType.PTR, u, w,
                predFactory.createIntegerConstantObject(0, 1));
        final Predicate r = predFactory.createPredicate(PredicateType.PTR, u, v, m);
        return createPrologRule(createPredicateList(l2, l1), r);
    }

    /**
     * v = w + t, w = &u + n, m = n + t ==> v = &u + m
     *
     * @return SUM(v, w, t), PTR(u, w, n), SUM(m, n, t) ==> PTR(u, v, m)
     * @throws PredicateCreateException
     */
    InferenceRule sumPtrRule() throws PredicateCreateException {
        // PTR (u, v, m) :- SUM (v, w, t), PTR(u, w, n), SUM (m, n, t)
        // v = &u+m <== v=w+t, w=&u+n, m=n+t
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final QuantifierValue t = quantifierFactory.createQuantifierValue(4, "t");
        final QuantifierValue m = quantifierFactory.createQuantifierValue(5, "m");
        final QuantifierValue n = quantifierFactory.createQuantifierValue(6, "n");
        final Predicate l1 = predFactory.createPredicate(PredicateType.SUM, v, w, t);
        final Predicate l2 = predFactory.createPredicate(PredicateType.PTR, u, w, n);
        final Predicate l3 = predFactory.createPredicate(PredicateType.SUM, m, n, t);
        final Predicate r = predFactory.createPredicate(PredicateType.PTR, u, v, m);
        return createPrologRule(createPredicateList(l2, l1, l3), r);
    }

    /**
     * v = 0 ==> *v is incorrect
     *
     * @return ZERO(v) ==> INCORRECT(v)
     * @throws PredicateCreateException
     */
    InferenceRule incorrectPtrRule() throws PredicateCreateException {
        final QuantifierValue v = quantifierFactory.createQuantifierValue(1, "v");
        final Predicate l = predFactory.createPredicate(PredicateType.ZERO, v);
        final Predicate r = predFactory.createPredicate(PredicateType.INCORRECT_PTR, v);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     * v = &u + w ==> v != 0
     *
     * @return PTR(u, v, w) ==> NONZERO(v)
     * @throws PredicateCreateException
     */
    InferenceRule nonZeroPtrRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate l = predFactory.createPredicate(PredicateType.PTR, u, v, w);
        final Predicate r = predFactory.createPredicate(PredicateType.NONZERO, v);
        return createPrologRule(createPredicateList(l), r);
    }

    /**
     * v = sizeof(u), v<w ==> sizeof(u) < w
     *
     * @return SIZEOF(u, v), LESS(v, w) ==> SIZEOF_LESS(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule sizeOfLessRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate l1 = predFactory.createPredicate(PredicateType.SIZEOF, u, v);
        final Predicate l2 = predFactory.createPredicate(PredicateType.LESS, v, w);
        final Predicate r = predFactory.createPredicate(PredicateType.SIZEOF_LESS, u, w);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    /**
     * v = sizeof(u), v >= w ==> sizeof(u) >= w
     *
     * @return SIZEOF(u, v), GREATER_EQ(v, w) ==> SIZEOF_GREATER_EQ(u, w)
     * @throws PredicateCreateException
     */
    InferenceRule sizeOfGreaterEqRule() throws PredicateCreateException {
        final QuantifierValue u = quantifierFactory.createQuantifierValue(1, "u");
        final QuantifierValue v = quantifierFactory.createQuantifierValue(2, "v");
        final QuantifierValue w = quantifierFactory.createQuantifierValue(3, "w");
        final Predicate l1 = predFactory.createPredicate(PredicateType.SIZEOF, u, v);
        final Predicate l2 = predFactory.createPredicate(PredicateType.GREATER_EQUALS, v, w);
        final Predicate r = predFactory.createPredicate(PredicateType.SIZEOF_GREATER_EQUALS, u, w);
        return createPrologRule(createPredicateList(l1, l2), r);
    }

    Logician addPointerRules(final Logician logician) throws PredicateCreateException {
        logician.addRule(simpleRefDereferenceRule());
        logician.addRule(complexRefDereferenceRule());
        logician.addRule(correctPtrRule());
        logician.addRule(sumZeroPtrRule());
        logician.addRule(incorrectPtrRule());
        logician.addRule(nonZeroPtrRule());
        logician.addRule(sizeOfLessRule());
        logician.addRule(sizeOfGreaterEqRule());
        return logician;
    }

    Logician addAllRules(final Logician logician) throws PredicateCreateException {
        addModusPonensRules(logician);
        addComparisonRules(logician);
        addZeroNonzeroRules(logician);
        addArithmeticRules(logician);
        addPointerRules(logician);
        return logician;
    }

    public InferenceRule createGeneralRule(final boolean isPrecise, final boolean isEquivalent,
                                           final List<Predicate> leftPart, final List<Predicate> rightPart) {
        if (!isPrecise && !isEquivalent && rightPart.size()==1)
            return createPrologRule(leftPart, rightPart.iterator().next());
        return new GeneralInferenceRule(isPrecise, isEquivalent, leftPart, rightPart);
    }

    public InferenceRule createPrologRule(final List<Predicate> leftPart, final Predicate right) {
        return new PrologInferenceRule(leftPart, right);
    }

    @Override
    public Logician createLogician() {
        return new PrologLogician();
    }
}
