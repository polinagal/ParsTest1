package s2a.inference.mgp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.*;
import parserthing.*;
import s2a.inference.api.AbstractQuantifierFactory;
import s2a.inference.api.AbstractTheoryFactory;
import s2a.inference.api.InferenceRule;
import s2a.inference.api.Logician;
import s2a.inference.api.QuantifierValue;
import s2a.inference.api.Theory;
import s2a.predicates.api.AbstractPredicateFactory;
import s2a.predicates.api.Predicate;
import s2a.predicates.api.PredicateCreateException;
import s2a.predicates.api.PredicateObject;
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

    static private final AbstractQuantifierFactory quantifierFactory =
            AbstractQuantifierFactory.getInstance();
    private static final LogicianFactory logFactory = LogicianFactory.instance;

    private static final AbstractPredicateFactory predFactory = AbstractPredicateFactory.getInstance();
    
    private static Parser parser = null;

    private final AbstractTheoryFactory thFactory = AbstractTheoryFactory.getInstance();

    @BeforeClass
    static public void setUp() {
        DepsConfigManager.getInstance().setTheoryName("mgp");
    }

    @AfterClass
    static public void tearDown() {
        DepsConfigManager.getInstance().setTheoryName("smt");
    }

    @Ignore
    @Test 
    public void testContains () {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        HashMap<String, PredicateObject> hm = new HashMap<>();
   
        ArrayList<PredicateObject> local = new ArrayList<>();
        
        PredicateObject po1 = predFactory.createIntegerConstantObject(15, 1);
        PredicateObject po2 = predFactory.createStringConstantObject("SCO");        
        PredicateObject po3 = predFactory.createVariableObject(4, "vo");       
        PredicateObject po4 = quantifierFactory.createQuantifierValue(1, "qv");
        PredicateObject po5 = quantifierFactory.createQuantifierSimpleConstant(2, "QSC");
        PredicateObject po51 = quantifierFactory.createQuantifierSimpleConstant(3, "QSC");
        PredicateObject po52 = quantifierFactory.createQuantifierSimpleConstant(2, "SSQSC");
        PredicateObject po6 = quantifierFactory.createQuantifierNonconstValue(3, "_qnv");
        
        hm.put(po1.getUniqueName(), po1);
        hm.put(po2.getUniqueName(), po2);
        hm.put(po3.getUniqueName(), po3);
        hm.put(po4.getUniqueName(), po4);
        hm.put(po5.getUniqueName(), po5);
        hm.put(po6.getUniqueName(), po6);

        System.out.println(po1.getUniqueName());
        System.out.println(po2.getUniqueName());
        System.out.println(po3.getUniqueName());
        System.out.println(po4.getUniqueName());
        System.out.println(po5.getUniqueName());
        System.out.println(po6.getUniqueName());
                
        PredicateObject po_7 = predFactory.createVariableObject(4, "vo");
        PredicateObject po_8 = quantifierFactory.createQuantifierSimpleConstant(6, "SSQSC");
        PredicateObject po_9 = quantifierFactory.createQuantifierSimpleConstant(3, "SSQSC");
        
        System.out.println(hm.containsKey(po_7.getUniqueName()));
        hm.put(po_7.getUniqueName(), po_7);
        
        System.out.println("spo5&po51 " + po5.equals(po51));
        System.out.println("spo5&po52 " + po5.equals(po52));
        System.out.println("spo6&po51 " + po6.equals(po51));
        
        System.out.println("hashmap has value createQuantifierSimpleConstant(6, \"SSQSC\")" + hm.containsValue(po_8));
        System.out.println("hashmap has value createQuantifierSimpleConstant(3, \"SSQSC\")" + hm.containsValue(po_9));
        String inp = "CORRECT_PTR(v) :- PTR(u,v,w), GREATER_EQUALS(w,0),  SIZEOF(u,s), LESS(w,s)";
        System.out.println(inp.hashCode());

        
        System.out.println("??????????????????????????????????");
     
    }
   
    @Test
    public void testRegexComplex () {
        String input = "ONEOF(EQUALS(a,2),EQUALS(b,5),EQUALS(A),3,a)";
        String input_sim = "EQUALS(a,b)";
        String regexArg = "((_{0,1}[a-z]+)|(\\-{0,1}[0-9]+)|([A-Z]+))";
        String regexPred = "([A-Z]+(_[A-Z]+)*\\(" + regexArg + "(," + regexArg + ")*\\))";                                  //^ - the beginning and $ - the end
        String regexArgComplex = "((_{0,1}[a-z]+)|(\\-{0,1}[0-9]+)|([A-Z]+)|"+regexPred+")";
        String regexPredComplex =  "[A-Z]+(_[A-Z]+)*\\(" + regexArgComplex + "(," + regexArgComplex + ")*\\)";
        String regexArgVeryComplex = "((_{0,1}[a-z]+)|(\\-{0,1}[0-9]+)|([A-Z]+)|"+regexPredComplex+")";
        String regexPredVeryComplex = "[A-Z]+(_[A-Z]+)*\\(" + regexArgVeryComplex + "(," + regexArgVeryComplex + ")*\\)";
        System.out.println(regexPredVeryComplex);
        System.out.println(regexPred);
        System.out.println(input.matches(regexPredVeryComplex));
        System.out.println(input_sim.matches(regexPredComplex));
    }
    
    //ok
    @Ignore
    @Test
    public void testVerySimple() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testVerySimple.txt");
        parser.parseFile();
        //ok
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore
    @Test
    public void testNegateVerySimple() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testNegativeVerySimple.txt");
        parser.parseFile();
        //ok
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertFalse(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore
    @Test
    public void testLessToLessEquals() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testLessToLessEquals.txt");
        parser.parseFile();
        parser.addRules("Comparison");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
//        logFactory.addComparisonRules(logician);
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore
    @Test
    public void testZeroDefinition() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testZeroDefinition.txt");
        parser.parseFile();
        parser.addRules("ZeroNonzero");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
               
        Assert.assertTrue(logician.proveTrue(theory,
                target));
        
        
    }
    
    //ok
    @Ignore
    @Test
    public void testZeroNegate() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testZeroNegate.txt");
        parser.parseFile();
        parser.addRules("ZeroNonzero");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertFalse(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore
    @Test
    public void testNonZeroDefinition() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testNonZeroDefinition.txt");
        parser.parseFile();
        parser.addRules("ZeroNonzero");
        
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore
    @Test
    public void testNonZeroNegate() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testNonZeroNegate.txt");
        parser.parseFile();
        parser.addRules("ZeroNonzero");
        
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertFalse(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore 
    @Test
    public void testNonZeroChain() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testNonZeroChain.txt");
        parser.parseFile();
        
        parser.addAllRules();

        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore
    @Test
    public void testNotNonZero() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testNotNonZero.txt");
        parser.parseFile();
        parser.addAllRules();
        
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        
                
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore
    @Test
    public void testOrFalse1() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testOrFalse1.txt");
        parser.parseFile();
        
        parser.addRules("ZeroNonzero");
        
                
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));
    }
    
    //ok
    @Ignore
    @Test
    public void testOrFalse2() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testOrFalse2.txt");
        parser.parseFile();
        parser.addRules("ZeroNonzero");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
//        logFactory.addZeroNonzeroRules(logician);

        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }
    
    //ok
    @Ignore
    @Test
    public void testOrTrue1() throws PredicateCreateException, IOException {
         parser = new Parser("test_inp/testOrTrue1.txt");
        parser.parseFile();
        
        parser.addRules("ZeroNonzero");
                        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
                
//        logFactory.addZeroNonzeroRules(logician);
        
        Assert.assertFalse(logician.proveTrue(theory,
                 target));
    }
    
    //ok
    @Ignore
    @Test
    public void testOrTrue2() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testOrTrue2.txt");
        parser.parseFile();
        
        parser.addRules("ZeroNonzero");
                        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
                
        Assert.assertTrue(logician.proveTrue(theory,
                 target));
    }
       
    //ok
    @Ignore
    @Test
    public void testAndTrue1() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testAndTrue1.txt");
        parser.parseFile();                
        parser.addAllRules();
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));
    }
    
    //ok
    @Ignore
    @Test
    public void testAndTrue2() throws PredicateCreateException, IOException {
    parser = new Parser("test_inp/testAndTrue2.txt");
    parser.parseFile();                
    parser.addAllRules();

    final Logician logician = parser.getLogician();

    final Theory theory = parser.getTheory();

    final Predicate target = parser.getTarget();


    Assert.assertTrue(logician.proveTrue(theory,
             target));
    }

    //ok
    @Ignore
    @Test
    public void testCommutativity() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testCommutativity.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();

        Assert.assertTrue(logician.proveTrue(theory,
                 target));
        
    }

    //ok
    @Ignore
    @Test
    public void testCommutativityNegate() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testCommutativityNegate.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();


        Assert.assertFalse(logician.proveTrue(theory,
                 target));
        
    }
    
    //ok
    @Ignore
    @Test
    public void testAssociativity() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testAssociativity.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));
    }
    
    //ok
    @Ignore
    @Test
    public void testAssociativityNegate() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testAssociativityNegate.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();


        Assert.assertFalse(logician.proveTrue(theory,
                 target));
        
    }

    //ok
    @Ignore
    @Test
    public void testAssociativityExtra() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testAssociativityExtra.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));
        
    }

    //ok
    @Ignore
    @Test
    public void testAssociativityChain() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testAssociativityChain.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));
    }

    //ok
    @Ignore
    @Test
    public void testCommAndAssoc() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testCommAndAssoc.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));
    }
   
    //ok
    @Ignore
    @Test
    public void testAssociativityNumbers() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testAssociativityNumbers.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));        
    }
    
    //ok
    @Ignore
    @Test
    public void testAssociativityChainNumbers() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testAssociativityChainNumbers.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));       
    }
   
    //ok
    @Ignore
    @Test
    public void testVariableShortComparisons() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testVariableShortComparisons.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));  
       
    }

    //ok
    @Ignore
    @Test
    public void testVariableReverseComparisons() throws PredicateCreateException, IOException {
        // y<=w <== y<=z, z<=w (!) <= y=z
        parser = new Parser("test_inp/testVariableReverseComparisons.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));  
       
    }

    //ok
    @Ignore
    @Test
    public void testVariableChainComparisons() throws PredicateCreateException, IOException {
        // x<w <== x<y (!), y<=w <== y<=z, z<=w (!) <= y=z
        parser = new Parser("test_inp/testVariableChainComparisons.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                 target));  
        
    }

    //ok
    @Ignore
    @Test
    public void testNumberShortComparisons() throws PredicateCreateException, IOException {
        // y<=7 <== y<=EX, (EX<=7)
        // y<=EX <= y=EX
        // y<=7 <== y<=4 (?), 4<=7 (!) <= y=4
        parser = new Parser("test_inp/testNumberShortComparisons.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();

        Assert.assertTrue(logician.proveTrue(theory,
                 target));  
    }

    //ok
    @Ignore 
    @Test
    public void testNumberShortComparisonsNegate() throws PredicateCreateException, IOException {
        // I should think more about numbers here
        // y<=7 <== y<=EX, (EX<=7)
        // y<=EX <= y=EX
        // y<=7 <== y<=4 (?), 4<=7 (!) <= y=4
        parser = new Parser("test_inp/testNumberShortComparisonsNegate.txt");
        parser.parseFile();                
        parser.addRules("Comparison");

        final Logician logician = parser.getLogician();

        final Theory theory = parser.getTheory();

        final Predicate target = parser.getTarget();

        Assert.assertFalse(logician.proveTrue(theory,
                 target));  
        
    }

    //ok
    @Ignore 
    @Test
    public void testNumberChainComparisons() throws PredicateCreateException, IOException {
        
        parser = new Parser("test_inp/testNumberChainComparisons.txt");
        parser.parseFile();
        parser.addRules("Comparison");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
        
    }

    //ok
    @Ignore 
    @Test
    public void testNumberChainComparisonsNegate() throws PredicateCreateException, IOException {
        //         x<7 <== x<y (!), y<=7 <= y<=4 (?), 4<=7 (!) <= y=4
        parser = new Parser("test_inp/testNumberChainComparisonsNegate.txt");
        parser.parseFile();
        parser.addRules("Comparison");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertFalse(logician.proveTrue(theory,
                target));
    }

    //ok
    @Ignore
    @Test
    public void testLessAndGreater() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testLessAndGreater.txt");
        parser.parseFile();
        parser.addRules("Comparison");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }

    //ok
    @Ignore
    @Test
    public void testLessAndNotEquals() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testLessAndNotEquals.txt");
        parser.parseFile();
        parser.addRules("Comparison");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
        
    }

    //ok
    @Ignore
    @Test
    public void testNegEquals() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testNegEquals.txt");
        parser.parseFile();
        parser.addRules("Arithmetic");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
        
        Assert.assertTrue(logician.proveTrue(theory,
                target));
        
    }

    //ok
    @Ignore
    @Test
    public void testDoubleMinus() throws PredicateCreateException , IOException{
        parser = new Parser("test_inp/testDoubleMinus.txt");
        parser.parseFile();
        parser.addRules("Arithmetic");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
                
        Assert.assertTrue(logician.proveTrue(theory,
                target));
    }
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
    //ok
    @Ignore
    @Test
    public void testOneofParts1() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testOneofParts1.txt");
        parser.parseFile();
        parser.addRules("Comparison");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
                
        Assert.assertTrue(logician.proveTrue(theory,
                target));
        
        
    }
    
    //ok
    @Ignore
    @Test
    public void testOneofParts2() throws PredicateCreateException, IOException {
        parser = new Parser("test_inp/testOneofParts2.txt");
        parser.parseFile();
        parser.addRules("Comparison");
        
        final Logician logician = parser.getLogician();
        
        final Theory theory = parser.getTheory();
        
        final Predicate target = parser.getTarget();
                
        Assert.assertTrue(logician.proveTrue(theory,
                target));
        
        
    }

    
    
    
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
