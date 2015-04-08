package s2a.inference.mgp;

import s2a.inference.api.*;
import s2a.predicates.api.*;
import s2a.util.config.DepsConfigManager;

import java.util.*;
import java.util.logging.Logger;

/**
 * Very simple Prolog-based logician
 *
 * List of predicates which are integrated into the core: OPPOS, ONEOF, EQUALS
 */
public class PrologLogician implements Logician {

    static private final Logger logger = Logger.getLogger(PrologLogician.class.getName());
    
    static private final Predicate truth = AbstractPredicateFactory.getInstance().getTruth();

    static private final Predicate falsehood = AbstractPredicateFactory.getInstance().getFalsehood();

    private final List<PrologInferenceRule> rules = new LinkedList<PrologInferenceRule>();

    /**
     * Set of predicates that were already proven
     */
    private final Set<Predicate> truePredicatesCache = new HashSet<Predicate>();

    /**
     * Set of predicates that we were not able to prove with a level given as value
     */
    private final Map<Predicate, Integer> falsePredicatesCache = new HashMap<Predicate, Integer>();

    @Override
    public void addRule(InferenceRule rule) {
        assert rule instanceof PrologInferenceRule :
                "Prolog-based logician works only with Prolog-based rules";
        rules.add((PrologInferenceRule)rule);
    }

    @Override
    public Theory simplify(Theory theory, Predicate start) {
        return theory;
    }

    private Predicate predicateWithoutLevels(final Predicate predicate) {
        final List<PredicateObject> simplerObjs = new LinkedList<PredicateObject>();
        for (PredicateObject obj: predicate.getArguments()) {
            if (obj instanceof StackedQuantifier) {
                final StackedQuantifier stackedQuantifier = (StackedQuantifier)obj;
                assert !(stackedQuantifier.getBase() instanceof StackedQuantifier) :
                        "Stacked quantifier as base of other stacked quantifier: " + obj;
                simplerObjs.add(stackedQuantifier.getBase());
            } else {
                simplerObjs.add(obj);
            }
        }
        try {
            return AbstractPredicateFactory.getInstance().createPredicate(
                    predicate.getType(), simplerObjs);
        } catch (PredicateCreateException e) {
            throw new AssertionError(e); // ???
        }
    }

    private boolean isCompatible(Predicate base, Predicate quantified,
                                 final QuantifierValueMap quantifierMap,
                                 final int level) {
        return isCompatible(base, quantified, quantifierMap, level, false);
    }

    /*
     * Check whether base predicate is compatible with quantified predicate,
     * with quantifier --> object map modification
     * <br>
     * Predicates are compatible if types and sizes are the same,
     * objects are also the same,
     * and each quantifier in quantified predicate is compatible with
     * relevant object in base predicate.
     *
     * @param base base predicate to check
     * @param quantified quantified predicate to check
     * @param quantifierMap map quantifier --> object, new pairs could be added during check
     * @param level current stack level to predicate substitution
     * @param checkReversal check also base for quantifiers
     * @return true if predicates are compatible
     */
    private boolean isCompatible(Predicate base, Predicate quantified,
                                 final QuantifierValueMap quantifierMap,
                                 final int level, boolean checkReversal) {
        final QuantifierValueMap toAdd = quantifierMap.clone();
        if (quantified.getType()==PredicateType.OPPOS) {
            quantified = (QuantifierPredicate)quantified.getArguments().get(0);
            try {
                base = AbstractPredicateFactory.getInstance().createPredicate(PredicateType.OPPOS, base);
            } catch(PredicateCreateException ex) {
                return false;
            }
        }
        if (quantified instanceof QuantifierPredicate) {
            final StackedQuantifier quanObj = StackedQuantifier.create((QuantifierObject)quantified, level);
            if (!quantifierMap.contains(quanObj)) {
                if (!quanObj.isCompatible(base))
                    return false;
                toAdd.put(quanObj, base);
            }
            quantifierMap.putAll(toAdd);
            return true;
        }
        // What if base predicate is of ONEOF type?
        if (base.getType()==quantified.getType() && base.getArguments().size()==quantified.getArguments().size()) {
            Iterator<PredicateObject> baseIt = base.getArguments().iterator();
            Iterator<PredicateObject> quanIt = quantified.getArguments().iterator();
            while (baseIt.hasNext()) {
                final PredicateObject baseObj = baseIt.next();
                final PredicateObject cmpObj = quanIt.next();
                if (cmpObj instanceof QuantifierObject) {
                    final StackedQuantifier quanObj = StackedQuantifier.create((QuantifierObject)cmpObj, level);
                    if (quantifierMap.contains(quanObj))
                        continue;
                    if (baseObj instanceof StackedQuantifier) {
                        final StackedQuantifier baseStacked = (StackedQuantifier)baseObj;
                        if (!quanObj.isCompatible(baseStacked.getBase()))
                            return false;
                    } else {
                        if (!quanObj.isCompatible(baseObj))
                            return false;
                    }
                    toAdd.put(quanObj, baseObj);
                } else if (checkReversal && baseObj instanceof StackedQuantifier) {
                    // What level should be used here??? May be no level???
                    final StackedQuantifier baseStacked = (StackedQuantifier)baseObj;
                    // Only constants are considered
                    //if (!(cmpObj instanceof SimpleConstantObject))
                    //    return false;
                    if (!baseStacked.isCompatible(cmpObj))
                        return false;
                    logger.finer("Reversal compatibility case: base quantifier = " + baseStacked +
                            " goal predicate " + cmpObj + " map BEFORE resolving is ...");
                    logger.finer(toAdd.toString());
                    toAdd.put(baseStacked, cmpObj);
                    logger.finer("Reversal compatibility case: base quantifier = " + baseStacked +
                            " goal predicate " + cmpObj + " map AFTER resolving is ...");
                    logger.finer(toAdd.toString());
                } else {
                    if (!baseObj.equals(cmpObj))
                        return false;
                }
            }
            quantifierMap.putAll(toAdd);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check whether all predicates from the rule's left part
     * beginning from the given index can be found in the theory,
     * according to quantifier --> object map and
     * existing goal stack
     *
     * @param theory the current theory
     * @param rule the rule which left part is checked
     * @param quantifierMap map quantifier --> object
     * @param goalStack stack of previous goals (if goal is repeated, it never can be found)
     * @param fromIndex index of the first predicate from the rule's left part to check
     * @return true if all predicates can be found, false otherwise
     */
    private boolean findLeftPartFrom(Theory theory, InferenceRule rule,
                                 final QuantifierValueMap quantifierMap,
                                 final Set<Predicate> goalStack, final int fromIndex) {
        int level = goalStack.size();
        // If the end of rule is reached, then everything is already found
        if (fromIndex >= rule.getLeftPart().size()) {
            return true;
        }
        // Take the first predicate to find
        final Predicate left = rule.getLeftPart().get(fromIndex);
        // It's better to call find directly here,
        // but the code is a bit different because of exist quantifiers
        logger.info("Trying to find rule's left part " + left + " at level " + level);

        final Predicate resolved = resolve(left, quantifierMap, level);
        if (resolved==null)
            return false;
        // First check the goal stack
        final Predicate simpler = predicateWithoutLevels(resolved);
        if (goalStack.contains(simpler)) {
            // The goal cannot appear the second time,
            // or we will get infinite recursion
            logger.info("Rule's left part " + left + " is not found, it is already in stack at level " + level);
            return false;
        }
        logger.info("Trying to find resolved rule's left part " + resolved + " at level " + level);
        // May be, we should check here caches first, it's faster

        // Let's deals with exists quantifiers
        // (this part is still complex)
        // First, let's find exists quantifiers
        final List<StackedQuantifier> existQuantifiers = findExists(resolved, quantifierMap, level+1);
        if (!existQuantifiers.isEmpty()) {
            // If there are, try to find compatible facts from the theory
            // First, direct solution only for predicates like q = const
            if (resolved.getType()==PredicateType.EQUALS) {
                final PredicateObject eqLeft = resolved.getArguments().get(0);
                final PredicateObject eqRight = resolved.getArguments().get(1);
                // May be we could process here not only constants
                if (eqLeft instanceof StackedQuantifier &&
                        eqRight instanceof SimpleConstantObject) {
                    final StackedQuantifier quantifierLeft = (StackedQuantifier)eqLeft;
                    if (quantifierLeft.isCompatible(eqRight)) {
                        logger.info("Rule's left part " + resolved +
                                " can be directly solved at level " + level);
                        final QuantifierValueMap factMap = quantifierMap.clone();
                        factMap.put(quantifierLeft, eqRight);
                        if (findLeftPartFrom(theory, rule, factMap, goalStack, fromIndex+1)) {
                            logger.info("Full rule " + rule + " is matched at level " + level);
                            quantifierMap.putAll(factMap);
                            return true;
                        }
                    }
                }
            }
            // We can use here some backtracking...
            for (Predicate fact: theory.getPredicates()) {
                if (fact.getType()==PredicateType.ONEOF)
                    continue;
                final QuantifierValueMap factMap = quantifierMap.clone();
                if (isCompatible(fact, resolved, factMap, level)) {
                    // If some predicate is compatible,
                    // then remember all resolved quantifiers,
                    // and try to find the remaining left part
                    logger.info("Rule's left part " + resolved +
                            " matches the known fact " + fact + " at level " + level);
                    if (findLeftPartFrom(theory, rule, factMap, goalStack, fromIndex+1)) {
                        logger.info("Full rule " + rule + " is matched at level " + level +
                                " for a fact " + fact);
                        quantifierMap.putAll(factMap);
                        return true;
                    } else {
                        logger.info("Cannot match full rule " + rule +
                                " according to the known fact " + fact + " at level " + level);
                    }
                }
            }
            // ONEOF facts are considered in this branch
            oneofLoop: for (Predicate oneofFact: theory.getPredicates()) {
                if (oneofFact.getType()!=PredicateType.ONEOF)
                    continue;
                // The goal should be true for any oneofFact from ONEOF
                final ListObject lo = (ListObject)oneofFact.getArguments().iterator().next();
                QuantifierValueMap mergedMap = null;
                for (PredicateObject obj: lo) {
                    final Predicate fact = (Predicate)obj;
                    final QuantifierValueMap factMap = quantifierMap.clone();
                    if (isCompatible(fact, resolved, factMap, level)) {
                        // If some predicate is compatible,
                        // then remember all resolved quantifiers,
                        // and try to find the remaining left part
                        logger.info("Rule's left part " + resolved +
                                " matches the one-of fact " + fact + " at level " + level);
                        if (findLeftPartFrom(theory, rule, factMap, goalStack, fromIndex+1)) {
                            logger.info("Full rule " + rule + " is matched at level " + level +
                                        " for a one-of fact " + fact);
                            // It's unclear what to do with quantifiers here
                            if (mergedMap == null) {
                                mergedMap = factMap;
                            } else {
                                mergedMap.merge(factMap);
                            }
                        } else {
                            logger.info("Cannot match full rule " + rule +
                                    " according to the one-of fact " + fact + " at level " + level);
                            continue oneofLoop;
                        }
                    } else {
                        continue oneofLoop;
                    }
                }
                // Only if goal is true for all one-of facts, we are satisfied
                logger.info("Full rule " + rule + " is matched at level " + level +
                        " for all one-of facts: " + oneofFact);
                quantifierMap.putAll(mergedMap);
                return true;
            }
        }
        final Set<Predicate> newStack = new HashSet<Predicate>(goalStack);
        newStack.add(predicateWithoutLevels(resolved));
        // Recursion: we should begin with the first predicate,
        // and then find all predicates from the remaining left part
        final QuantifierValueMap ruleMap = quantifierMap.clone();
        if (find(theory, resolved, ruleMap, newStack)) {
            ruleMap.removeOlder(level);
            if (findLeftPartFrom(theory, rule, ruleMap, goalStack, fromIndex+1)) {
                quantifierMap.putAll(ruleMap);
                if (fromIndex==rule.getLeftPart().size()-1) {
                    logger.info("Full rule " + rule + " is matched at level " + level);
                    logger.fine(ruleMap.toString());
                }
                return true;
            }
        }
        if (fromIndex==0) {
            logger.info("Full rule " + rule + " is not matched at level " + level);
        }
        return false;
    }

    /**
     * Check whether all predicates from the rule's left part
     * can be found in the theory,
     * according to quantifier --> object map and
     * existing goal stack
     *
     * @param theory the current theory
     * @param rule the rule which left part is checked
     * @param quantifierMap map quantifier --> object
     * @param goalStack stack of previous goals (if goal is repeated, it never can be found)
     * @return true if all predicates can be found, false otherwise
     */
    private boolean findLeftPart(Theory theory, InferenceRule rule,
                                 final QuantifierValueMap quantifierMap,
                                 final Set<Predicate> goalStack) {
        return findLeftPartFrom(theory, rule, quantifierMap, goalStack, 0);
    }

    /**
     * Find all unresolved quantifiers (exist quantifiers)
     * from the goal
     * @param goal the goal to find quantifiers
     * @param quantifierMap resolved quantifiers map: quantifier --> object
     * @param level current stack level to predicate substitution
     * @return list of unresolved quantifiers
     */
    private List<StackedQuantifier> findExists(Predicate goal,
                                               final QuantifierValueMap quantifierMap,
                                               final int level) {
        final List<StackedQuantifier> existObjects = new ArrayList<StackedQuantifier>(goal.getArguments().size());
        for (PredicateObject goalObj: goal.getArguments()) {
            if (goalObj instanceof QuantifierObject) {
                final StackedQuantifier goalQuantifier = StackedQuantifier.create((QuantifierObject) goalObj, level);
                if (!quantifierMap.contains(goalQuantifier)) {
                    existObjects.add(goalQuantifier);
                }
            }
        }
        return existObjects;
    }

    /**
     * Resolve all known quantifiers inside a given goal
     * @param goal the given goal
     * @param quantifierMap map of quantifier values
     * @param level current stack level for predicate substitution
     * @return resolved goal
     */
    private Predicate resolve(final Predicate goal, final QuantifierValueMap quantifierMap, final int level) {
        final List<PredicateObject> resolvedObjects = new ArrayList<PredicateObject>(goal.getArguments().size());
        // First, exchange all goal quantifiers with their real values
        for (PredicateObject goalObj: goal.getArguments()) {
            if (goalObj instanceof QuantifierObject) {
                final StackedQuantifier goalQuantifier =
                        StackedQuantifier.create((QuantifierObject) goalObj, level);
                if (quantifierMap.contains(goalQuantifier)) {
                    final PredicateObject resolvedObject = quantifierMap.get(goalQuantifier);
                    logger.fine("Quantifier " + goalQuantifier + " resolved to " + resolvedObject);
                    resolvedObjects.add(resolvedObject);
                } else {
                    resolvedObjects.add(goalQuantifier);
                }
            } else {
                resolvedObjects.add(goalObj);
            }
        }
        try {
            if (goal instanceof QuantifierPredicate) {
                final StackedQuantifier goalQuantifier = StackedQuantifier.create((QuantifierObject) goal, level);
                if (quantifierMap.contains(goalQuantifier)) {
                    return (Predicate)quantifierMap.get(goalQuantifier);
                } else {
                    return goalQuantifier;
                }
            } else {
                for (PredicateObject resolvedObj: resolvedObjects) {
                    if (resolvedObj instanceof Predicate) {
                        final Predicate resolvedPredicate = (Predicate)resolvedObj;
                        // It's prohibited to have recursive higher order predicate
                        if (resolvedPredicate.getType().higherOrder())
                            return null;
                    }
                }
                return AbstractPredicateFactory.getInstance().createPredicate(
                        goal.getType(), resolvedObjects);
            }
        } catch (PredicateCreateException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Check whether the goal can be found in the theory,
     * according to quantifier --> object map and
     * existing goal stack
     *
     * @param theory the current theory
     * @param goal the goal to find
     * @param quantifierMap map quantifier --> object
     * @param goalStack stack of previous goals (if goal is repeated, it never can be found)
     * @return true if goal can be found, false otherwise
     */
    private boolean find(Theory theory, final Predicate goal,
                         final QuantifierValueMap quantifierMap,
                         final Set<Predicate> goalStack) {
        int level = goalStack.size();
        int maxDeep = DepsConfigManager.getInstance().getTheoryDeep();
        if (maxDeep > 0 && level > maxDeep) {
            logger.warning("Maximum deep of " + maxDeep + " is exceeded, exiting");
            return false;
        }
        final Predicate resolvedGoal = resolve(goal, quantifierMap, level);
        logger.warning("Trying to find goal " + goal + "( " + resolvedGoal + " ) at level " + level);
        // Check existing predicates (and also TRUTH, FALSEHOOD)
        if (truePredicatesCache.contains(resolvedGoal)) {
            logger.warning("Goal " + goal + "( " + resolvedGoal + " ) is found because it is known true fact");
            return true;
        } else {
            final Integer unprovenLevel = falsePredicatesCache.get(resolvedGoal);
            if (unprovenLevel != null && unprovenLevel <= level) {
                logger.warning("Goal " + goal + "( " + resolvedGoal +
                        " ) is NOT found because it is known false fact at level " + unprovenLevel);
                return false;
            }
        }
        // Facts: y=z, z<=w
        // Goal: y<=w
        // Way of search: y<=w <== y<=z, z<=w (!) <= y=z
        // y<=w <== y<=EX, (X<=w)
        // y<=EX <== y=EX

        // Try to find goal according to existing rules
        // May be, we should add here some backtracking...
        // At this moment, if we find here some rule which matches our goal,
        // it's not allowed in future to try some other rule
        final List<PrologInferenceRule> tautologyRules = new LinkedList<PrologInferenceRule>();
        for (PrologInferenceRule rule: rules) {
            Predicate right = rule.getRight();
            final QuantifierValueMap ruleMap = quantifierMap.clone();
            // Here both predicates can have quantifiers: either rule predicate or goal predicate
            // - example for both
            //   facts: x=y-z, y=z
            //   goal: x<1
            //   rules: u<w <== u=v, v<w; r=0 <== r=p-q, p=q.
            //   prove: u(x)<w(1) <=1= u(x)=v, v<w(1) <=2= r(u(x))=0(v), v(0)<w(1) <=3=
            //                         r(u(x))=p(y)-q(z), p(y)=q(z), v(0)<w(1)
            // On step 2, 0(v) is an example of goal quantifier
            // On step 1, u(x) and w(1) are examples of rule quantifier

            if (isCompatible(resolvedGoal, right, ruleMap, level, true)) {
                logger.info("Trying to find goal " + resolvedGoal +
                        " according to the rule " + rule + " at level " + level );
                // Rule goal is compatible with toSolve predicate
                if (findLeftPart(theory, rule, ruleMap, goalStack)) {
                    // Resolve additional quantifier after rule application
                    final Predicate addResolvedGoal = resolve(resolvedGoal, ruleMap, level);
                    // Questionable place: if we get "truth" here, it will gives us nothing,
                    // so we must continue with different rule
                    if (truth.equals(addResolvedGoal)) {
                        logger.info("Goal " + goal + " was reduced to tautology by rule " + rule +
                                " at level " + level + ", miss this rule");
                        tautologyRules.add(rule);
                        continue;
                    }
                    // If we can find all predicates from left part,
                    // then we can also find goal
                    logger.warning("Goal " + goal + "( " + addResolvedGoal + " ) is found because it matches the rule " +
                            rule + " at level " + level);
                    quantifierMap.putAll(ruleMap);
                    // Storing in cache to omit prove repetition later
                    if (addResolvedGoal.isQuantifierFree())
                        truePredicatesCache.add(addResolvedGoal);
                    return true;
                }
            }
        }
        // Additional loop for tautology rules
        for (PrologInferenceRule rule: tautologyRules) {
            Predicate right = rule.getRight();
            final QuantifierValueMap ruleMap = quantifierMap.clone();
            if (isCompatible(resolvedGoal, right, ruleMap, level, true)) {
                logger.info("Trying to find goal " + resolvedGoal +
                        " according to the rule " + rule + " at level " + level );
                // Rule goal is compatible with toSolve predicate
                if (findLeftPart(theory, rule, ruleMap, goalStack)) {
                    // Resolve additional quantifier after rule application
                    final Predicate addResolvedGoal = resolve(resolvedGoal, ruleMap, level);
                    // If we can find all predicates from left part,
                    // then we can also find goal
                    logger.warning("Goal " + goal + "( " + addResolvedGoal + " ) is found because it matches the tautology rule " +
                            rule + " at level " + level);
                    quantifierMap.putAll(ruleMap);
                    // Storing in cache to omit prove repetition later
                    if (addResolvedGoal.isQuantifierFree())
                        truePredicatesCache.add(addResolvedGoal);
                    return true;
                }
            }
        }
        // Storing in cache to omit prove repetition later
        // NB: dangerous operation, because at lower level we can try to prove this fact again
        // So we must record also level at which the fact was not proven
        if (resolvedGoal.isQuantifierFree()) {
            final Integer unprovenLevel = falsePredicatesCache.get(resolvedGoal);
            if (unprovenLevel == null || unprovenLevel > level)
                falsePredicatesCache.put(resolvedGoal, level);
        }
        logger.warning("Goal " + goal + "( " + resolvedGoal + " ) is NOT found because " +
                "it does not match any rule at level " + level);
        return false;
    }

    @Override
    public boolean proveTrue(Theory theory, Predicate toSolve) {
        truePredicatesCache.clear();
        truePredicatesCache.add(truth);
        truePredicatesCache.addAll(theory.getPredicates());
        falsePredicatesCache.clear();
        falsePredicatesCache.put(falsehood, 0);
        return find(theory, toSolve, new QuantifierValueMap(), Collections.singleton(toSolve));
    }

    @Override
    public boolean proveFalse(Theory theory, Predicate toSolve) {
        try {
            return proveTrue(theory, AbstractPredicateFactory.getInstance().createPredicate(PredicateType.OPPOS, toSolve));
        } catch (PredicateCreateException e) {
            throw new AssertionError(e); // ???
        }
    }

    @Override
    public boolean solveTrue(Theory theory, Predicate toProve) {
        return !proveFalse(theory, toProve);
    }

    @Override
    public boolean solveFalse(Theory theory, Predicate toProve) {
        return !proveTrue(theory, toProve);
    }
}
