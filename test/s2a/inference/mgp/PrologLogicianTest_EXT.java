package s2a.inference.mgp;

import java.io.IOException;
import org.junit.*;
import parserthing.*;
import s2a.inference.api.AbstractTheoryFactory;
import s2a.inference.api.Logician;
import s2a.inference.api.Theory;
import s2a.predicates.api.AbstractPredicateFactory;
import s2a.predicates.api.Predicate;
import s2a.predicates.api.PredicateCreateException;
import s2a.predicates.api.PredicateType;
import s2a.predicates.api.VariableObject;
import s2a.util.config.DepsConfigManager;

/**
 * Простые тесты для доказателя Пролог
 * User: Mike
 * Date: 09.01.13
 * Time: 18:39
 */
public class PrologLogicianTest_EXT {

    private static final LogicianFactory logFactory = LogicianFactory.instance;

    private static final AbstractPredicateFactory predFactory = AbstractPredicateFactory.getInstance();
    
    private static Parser parser = null;

    private final AbstractTheoryFactory thFactory = AbstractTheoryFactory.getInstance();

//    final VariableObject x = predFactory.createVariableObject(4, "x");
//    final VariableObject y = predFactory.createVariableObject(4, "y");
//    final VariableObject z = predFactory.createVariableObject(4, "z");
//    final VariableObject w = predFactory.createVariableObject(4, "w");
//    final VariableObject s = predFactory.createVariableObject(4, "s");
//    final VariableObject p = predFactory.createVariableObject(4, "p");

    final VariableObject arr[] = new VariableObject[20]; {
        for (int i=0; i<20; i++)
            arr[i] = predFactory.createVariableObject(4, "abcdefghijklmnopqrst".substring(i,i+1));
    }

    @BeforeClass
    static public void setUp() {
        DepsConfigManager.getInstance().setTheoryName("mgp");
    }

    @AfterClass
    static public void tearDown() {
        DepsConfigManager.getInstance().setTheoryName("smt");
    }


    @Test
    public void testVerySimple() throws PredicateCreateException, IOException {
        parser = new Parser("testVerySimple.txt");
        parser.parseFile();
        //ok
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }
//
//    @Test
//    public void testNegateVerySimple() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS, x, y));
//        final Logician logician = logFactory.createLogician();
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS, y, x)));
//    }
//
//    @Test
//    public void testLessToLessEquals() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS, x, y));
//        final Logician logician = logFactory.createLogician();
//        logician.addRule(logFactory.lessToLessEqualsRule());
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS, x, y)));
//    }
//
//    @Test
//    public void testZeroDefinition() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x,
//                predFactory.createIntegerConstantObject(0, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addZeroNonzeroRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.ZERO, x)));
//    }
//
//    @Test
//    public void testZeroNegate() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x,
//                predFactory.createIntegerConstantObject(1, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addZeroNonzeroRules(logician);
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.ZERO, x)));
//    }
//
//    @Test
//    public void testNonZeroDefinition() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.NOT_EQUALS, x,
//                predFactory.createIntegerConstantObject(0, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addZeroNonzeroRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.NONZERO, x)));
//    }
//
//    @Test
//    public void testNonZeroNegate() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.NONZERO, x));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addZeroNonzeroRules(logician);
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, x,
//                        predFactory.createIntegerConstantObject(1, 1))));
//    }
//
//    @Test
//    public void testNonZeroChain() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x,
//                predFactory.createIntegerConstantObject(3, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.NONZERO, x)));
//    }
//
//    @Test
//    public void testNotNonZero() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                x, predFactory.createIntegerConstantObject(-3, 1)));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.NOT,
//                x, y));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.ZERO, y)));
//    }
//
//    @Test
//    public void testOrFalse() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.OR, x, y, z));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.ZERO, x));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addZeroNonzeroRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.ZERO, y)));
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, z,
//                        predFactory.createIntegerConstantObject(0, 1))));
//    }
//
//    @Test
//    public void testOrTrue() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.OR,
//                x, y, z));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.NONZERO, x));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addZeroNonzeroRules(logician);
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.NONZERO, y)));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.ZERO, y));
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.NONZERO, z)));
//    }
//
//    @Test
//    public void testAndTrue() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.AND, x, y, z));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                x, predFactory.createIntegerConstantObject(2, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.NONZERO, y)));
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.NOT_EQUALS,
//                        z, predFactory.createIntegerConstantObject(0, 1))));
//    }
//
//    @Test
//    public void testCommutativity() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x, y));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, y, x)));
//    }
//
//    @Test
//    public void testCommutativityNegate() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x, y));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, y, z)));
//    }
//
//    @Test
//    public void testAssociativity() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, x, z)));
//    }
//
//    @Test
//    public void testAssociativityNegate() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, w));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, x, z)));
//    }
//
//    @Test
//    public void testAssociativityExtra() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x, w));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, x, z)));
//    }
//
//    @Test
//    public void testAssociativityChain() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, z, w));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, x, w)));
//    }
//
//    @Test
//    public void testCommAndAssoc() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, w));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, w, x)));
//    }
//
//    @Test
//    public void testAssociativityNumbers() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS,
//                x, predFactory.createIntegerConstantObject(3, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS,
//                        x, predFactory.createIntegerConstantObject(5, 1))));
//    }
//
//    @Test
//    public void testAssociativityChainNumbers() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS,
//                y, predFactory.createIntegerConstantObject(3, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS,
//                        x, predFactory.createIntegerConstantObject(5, 1))));
//    }
//
//    @Test
//    public void testVariableShortComparisons() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        // x<z <== x<y (!), y<=z <== <= y=z
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS, x, z)));
//    }
//
//    @Test
//    public void testVariableReverseComparisons() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS_EQUALS, z, w));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        // y<=w <== y<=z, z<=w (!) <= y=z
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS, y, w)));
//    }
//
//    @Test
//    public void testVariableChainComparisons() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS_EQUALS, z, w));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        // x<w <== x<y (!), y<=w <== y<=z, z<=w (!) <= y=z
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS, x, w)));
//    }
//
//    @Test
//    public void testNumberShortComparisons() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                y, predFactory.createIntegerConstantObject(4, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        // I should think more about numbers here
//        // y<=7 <== y<=EX, (EX<=7)
//        // y<=EX <= y=EX
//        // y<=7 <== y<=4 (?), 4<=7 (!) <= y=4
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS,
//                        y, predFactory.createIntegerConstantObject(7, 1))));
//    }
//
//    @Test
//    public void testNumberShortComparisonsNegate() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                y, predFactory.createIntegerConstantObject(4, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        // I should think more about numbers here
//        // y<=7 <== y<=EX, (EX<=7)
//        // y<=EX <= y=EX
//        // y<=7 <== y<=4 (?), 4<=7 (!) <= y=4
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS,
//                        y, predFactory.createIntegerConstantObject(2, 1))));
//    }
//
//    @Test
//    public void testNumberChainComparisons() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                y, predFactory.createIntegerConstantObject(4, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        // I should think more about numbers here
//        // x<7 <== x<y (!), y<=7 <= y<=4 (?), 4<=7 (!) <= y=4
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS,
//                        x, predFactory.createIntegerConstantObject(7, 1))));
//    }
//
//    @Test
//    public void testNumberChainComparisonsNegate() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS, x, y));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                y, predFactory.createIntegerConstantObject(4, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        // I should think more about numbers here
//        // x<7 <== x<y (!), y<=7 <= y<=4 (?), 4<=7 (!) <= y=4
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS,
//                        x, predFactory.createIntegerConstantObject(1, 1))));
//    }
//
//    @Test
//    public void testLessAndGreater() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS,
//                x, predFactory.createIntegerConstantObject(4, 1)));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.GREATER,
//                y, predFactory.createIntegerConstantObject(4, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS, x, y)));
//    }
//
//    @Test
//    public void testLessAndNotEquals() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS_EQUALS, y, x));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS,
//                x, predFactory.createIntegerConstantObject(4, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.NOT_EQUALS,
//                        y, predFactory.createIntegerConstantObject(4, 1))));
//    }
//
//    @Test
//    public void testNegEquals() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.NEG, y, x));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.NEG, y, z));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addArithmeticRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, x, z)));
//    }
//
//    @Test
//    public void testDoubleMinus() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        theory.addPredicate(predFactory.createPredicate(PredicateType.DIFF,
//                y, x, predFactory.createIntegerConstantObject(-5, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addArithmeticRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.SUM,
//                        y, x, predFactory.createIntegerConstantObject(5, 1))));
//    }
//
//    @Test
//    public void testModusPonens() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        final Predicate p = predFactory.createPredicate(PredicateType.NONZERO, x);
//        final Predicate q = predFactory.createPredicate(PredicateType.GREATER,
//                y, predFactory.createIntegerConstantObject(2, 1));
//        theory.addPredicate(p);
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUIV, p, q));
//        final Logician logician = logFactory.createLogician();
//        logician.addRule(logFactory.modusPonensRule());
//        Assert.assertTrue(logician.proveTrue(theory, q));
//    }
//
//    @Test
//    public void testModusPonensNegate() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        final Predicate p = predFactory.createPredicate(PredicateType.NONZERO, x);
//        final Predicate q = predFactory.createPredicate(PredicateType.GREATER,
//                y, predFactory.createIntegerConstantObject(2, 1));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.OPPOS, p));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUIV, p, q));
//        final Logician logician = logFactory.createLogician();
//        logician.addRule(logFactory.modusPonensRule());
//        Assert.assertFalse(logician.proveTrue(theory, q));
//    }
//
//    @Test
//    public void testModusPonensReverse() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        final Predicate p = predFactory.createPredicate(PredicateType.ZERO, x);
//        final Predicate q = predFactory.createPredicate(PredicateType.GREATER,
//                y, predFactory.createIntegerConstantObject(2, 1));
//        theory.addPredicate(p);
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUIV,
//                predFactory.createPredicate(PredicateType.NONZERO, x), q));
//        final Logician logician = logFactory.createLogician();
//        logician.addRule(logFactory.modusPonensReverseRule());
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS,
//                        y, predFactory.createIntegerConstantObject(2, 1))));
//    }
//
//    @Test
//    public void testModusPonensReverseNegate() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        final Predicate p = predFactory.createPredicate(PredicateType.NONZERO, x);
//        final Predicate q = predFactory.createPredicate(PredicateType.GREATER,
//                y, predFactory.createIntegerConstantObject(2, 1));
//        theory.addPredicate(p);
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUIV,
//                p, q));
//        final Logician logician = logFactory.createLogician();
//        logician.addRule(logFactory.modusPonensReverseRule());
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS,
//                        y, predFactory.createIntegerConstantObject(2, 1))));
//    }
//
//    @Test
//    public void testOneofParts() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        final Predicate p1 = predFactory.createPredicate(PredicateType.EQUALS,
//                x, predFactory.createIntegerConstantObject(1, 1));
//        final Predicate p2 = predFactory.createPredicate(PredicateType.EQUALS,
//                y, predFactory.createIntegerConstantObject(2, 1));
//        theory.addPredicate(p1);
//        theory.addPredicate(p2);
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS,
//                        x, predFactory.createIntegerConstantObject(2, 1))));
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS,
//                        y, predFactory.createIntegerConstantObject(2, 1))));
//    }
//
//    @Test
//    public void testSimpleOneof() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        final Predicate p1 = predFactory.createPredicate(PredicateType.EQUALS,
//                x, predFactory.createIntegerConstantObject(1, 1));
//        final Predicate p2 = predFactory.createPredicate(PredicateType.EQUALS,
//                x, predFactory.createIntegerConstantObject(2, 1));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.ONEOF,
//                p1, p2));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS_EQUALS,
//                        x, predFactory.createIntegerConstantObject(2, 1))));
//    }
//
//    @Test
//    public void testLongEqualChainFalse() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        for (int i=0; i<18; i++)
//            theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                    arr[i], arr[i+1]));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        final int prevDeep = DepsConfigManager.getInstance().getTheoryDeep();
//        try {
//            DepsConfigManager.getInstance().setTheoryDeep(20);
//            Assert.assertFalse(logician.proveTrue(theory,
//                    predFactory.createPredicate(PredicateType.EQUALS, arr[0], arr[19])));
//        } finally {
//            DepsConfigManager.getInstance().setTheoryDeep(prevDeep);
//        }
//    }
//
//    @Test
//    public void testLongEqualChainTrue() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        for (int i=0; i<19; i++)
//            theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                    arr[i], arr[i+1]));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addComparisonRules(logician);
//        final int prevDeep = DepsConfigManager.getInstance().getTheoryDeep();
//        try {
//            // 20 is the least needed depth
//            DepsConfigManager.getInstance().setTheoryDeep(20);
//            Assert.assertTrue(logician.proveTrue(theory,
//                    predFactory.createPredicate(PredicateType.EQUALS, arr[0], arr[19])));
//        } finally {
//            DepsConfigManager.getInstance().setTheoryDeep(prevDeep);
//        }
//    }
//
//    @Test
//    public void testRefDereference() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // y = &x + 0
//        theory.addPredicate(predFactory.createPredicate(PredicateType.PTR, x, y,
//                predFactory.createIntegerConstantObject(0, 1)));
//        // z = *y
//        theory.addPredicate(predFactory.createPredicate(PredicateType.DEREFFROM, z, y));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, z, x)));
//    }
//
//    @Test
//    public void testOneOfDereference() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x = 1
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS,
//                x, predFactory.createIntegerConstantObject(1, 1)));
//        // x = 0 <==> y = 0
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUIV,
//                predFactory.createPredicate(PredicateType.EQUALS,
//                        x, predFactory.createIntegerConstantObject(0, 1)),
//                predFactory.createPredicate(PredicateType.ZERO, y)));
//        // x = 1 <==> y = &z + 0
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUIV,
//                predFactory.createPredicate(PredicateType.EQUALS,
//                        x, predFactory.createIntegerConstantObject(1, 1)),
//                predFactory.createPredicate(PredicateType.PTR,
//                        z, y, predFactory.createIntegerConstantObject(0, 1))));
//        // arr0 = 0
//        theory.addPredicate(predFactory.createPredicate(PredicateType.ZERO, arr[0]));
//        // arr1 = &z + 0
//        theory.addPredicate(predFactory.createPredicate(PredicateType.PTR, z, arr[1],
//                predFactory.createIntegerConstantObject(0, 1)));
//        // ONEOF(y=arr0,y=arr1)
//        theory.addPredicate(predFactory.createPredicate(PredicateType.ONEOF,
//                predFactory.createPredicate(PredicateType.EQUALS, 
//                        y, arr[0],
//                predFactory.createPredicate(PredicateType.EQUALS,
//                        y, arr[1]))));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.NONZERO, y)));
//    }
//
//    @Test
//    public void testProdLessRule() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x=y*4
//        theory.addPredicate(predFactory.createPredicate(PredicateType.PROD,
//                x, y, predFactory.createIntegerConstantObject(4, 1)));
//        // y<2
//        theory.addPredicate(predFactory.createPredicate(PredicateType.LESS,
//                y, predFactory.createIntegerConstantObject(2, 1)));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        // x<8 ?
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS,
//                        x, predFactory.createIntegerConstantObject(8, 1))));
//    }
//
//    @Test
//    public void testSomethingMinusSomething() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x=y-y
//        theory.addPredicate(predFactory.createPredicate(PredicateType.DIFF, x, y, y));
//        final Logician logician = logFactory.createLogician();
//        logician.addRule(logFactory.diffSameIsZeroRule());
//        //factory.addAllRules(logician);
//        // x=0 ?
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, x, 
//                        predFactory.createIntegerConstantObject(0, 1))));
//    }
//
//    @Test
//    public void testEqualsToLess() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x=0
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, x,
//                predFactory.createIntegerConstantObject(0, 1)));
//        final Logician logician = logFactory.createLogician();
//        logician.addRule(logFactory.equalsToLessEqualsRule());
//        logician.addRule(logFactory.lessTransitivityRule02());
//        //factory.addAllRules(logician);
//        // x<1 ?
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS,
//                        x, predFactory.createIntegerConstantObject(1, 1))));
//    }
//
//    @Test
//    public void testSomethingMinusSomethingLess() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x=y-z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.DIFF, x, y, z));
//        // y=z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        // We should go here through intermediate x=0,
//        // and probably logician cannot understand it
//        // x<=1 ? YES!
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS,
//                        x, predFactory.createIntegerConstantObject(1, 1))));
//        Assert.assertFalse(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS,
//                        x, predFactory.createIntegerConstantObject(-1, 1))));
//    }
//
//    @Test
//    public void testSomethingMinusSomethingOneOfLess() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x=y-z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.DIFF, x, y, z));
//        // arr0=z, arr1=z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, arr[0], z));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, arr[1], z));
//        // oneof(y=arr0,y=arr1)
//        theory.addPredicate(predFactory.createPredicate(PredicateType.ONEOF,
//                predFactory.createPredicate(PredicateType.EQUALS, y, arr[0]),
//                predFactory.createPredicate(PredicateType.EQUALS, y, arr[1])));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        // x=0 ? YES!
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.EQUALS, x,
//                        predFactory.createIntegerConstantObject(0, 1))));
//        // x<1 ? YES!
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS, x, 
//                        predFactory.createIntegerConstantObject(1, 1))));
//    }
//
//    @Test
//    public void testSomethingMinusSomethingMultiplySomething() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x=y-z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.DIFF, x, y, z));
//        // w=x*4
//        theory.addPredicate(predFactory.createPredicate(PredicateType.PROD, w, x,
//                predFactory.createIntegerConstantObject(4, 1)));
//        // y=z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS, w, 
//                        predFactory.createIntegerConstantObject(4, 1))));
//    }
//
//    @Test
//    public void testSomethingMinusSomethingOneOfMultiplySomething() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x=y-z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.DIFF, x, y, z));
//        // w=x*4
//        theory.addPredicate(predFactory.createPredicate(PredicateType.PROD, w, x,
//                predFactory.createIntegerConstantObject(4, 1)));
//        // arr0(a)=z, arr1(b)=z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, arr[0], z));
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, arr[1], z));
//        // oneof(y=arr0(a),y=arr1(b))
//        theory.addPredicate(predFactory.createPredicate(PredicateType.ONEOF,
//                predFactory.createPredicate(PredicateType.EQUALS, y, arr[0]),
//                predFactory.createPredicate(PredicateType.EQUALS, y, arr[1])));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        // w<4?
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS, w,
//                        predFactory.createIntegerConstantObject(4, 1))));
//        // w<8?
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.LESS, w,
//                        predFactory.createIntegerConstantObject(8, 1))));
//    }
//
//    @Test
//    public void testPointerToArray() throws PredicateCreateException {
//        final Theory theory = thFactory.createTheory();
//        // x=y-z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.DIFF, x, y, z));
//        // w=x*4
//        theory.addPredicate(predFactory.createPredicate(PredicateType.PROD, w, x,
//                predFactory.createIntegerConstantObject(4, 1)));
//        // sizeof(s)=8
//        theory.addPredicate(predFactory.createPredicate(PredicateType.SIZEOF, s,
//                predFactory.createIntegerConstantObject(8, 1)));
//        // p=&s+w
//        theory.addPredicate(predFactory.createPredicate(PredicateType.PTR, s, p, x));
//        // y=z
//        theory.addPredicate(predFactory.createPredicate(PredicateType.EQUALS, y, z));
//        final Logician logician = logFactory.createLogician();
//        logFactory.addAllRules(logician);
//        Assert.assertTrue(logician.proveTrue(theory,
//                predFactory.createPredicate(PredicateType.CORRECT_PTR, p)));
//    }
}
