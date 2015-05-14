package parserthing;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import s2a.inference.api.AbstractQuantifierFactory;
import s2a.inference.api.AbstractTheoryFactory;
import s2a.inference.api.InferenceRule;
import s2a.inference.api.Logician;
import s2a.inference.api.Theory;
import s2a.inference.mgp.LogicianFactory;
import s2a.predicates.api.AbstractPredicateFactory;
import s2a.predicates.api.Predicate;
import s2a.predicates.api.PredicateCreateException;
import s2a.predicates.api.PredicateObject;
import s2a.predicates.api.PredicateType;


public  class Parser {
    
    private final String filename;
    
    private  final LogicianFactory logFactory = 
            LogicianFactory.instance;

    private  final AbstractPredicateFactory predFactory = 
            AbstractPredicateFactory.getInstance();

    private  final AbstractTheoryFactory thFactory = 
            AbstractTheoryFactory.getInstance();
    
    static private final AbstractQuantifierFactory quantifierFactory = 
            AbstractQuantifierFactory.getInstance();
    
    private   final Theory theory = 
            thFactory.createTheory();
    
    private final Logician logician = 
            logFactory.createLogician();
    
    private  Predicate target = 
            null;
    
//    private List<PredicateObject> args = new ArrayList<>();
    
    private final HashMap<String, PredicateObject> args = new HashMap<>();
    

    public Parser(String filename) {
        
        this.filename = filename;
    }
    
    


    
    
    /**
     * Просмотр файла по строкам и в соответствии с видом каждой строки 
     * принимается решение о принадлежности данных к фактам, правилам или 
     * предикату-цели (в начале строки цели стоит ?)
     * @param filename - имя просматриваемого файла
     * @throws IOException 
     */
       
    public  void parseFile () throws IOException {
                
        String regexFact = "[A-Z]+(_[A-Z]+)*\\([a-z|A-Z|0-9]+(,[a-z|A-Z|0-9]+)*\\)";       //^ - the beginning and $ - the end
        String regexRule = regexFact + ":-" + regexFact + "(," + regexFact + ")*";
        String regexTarget = "\\?" + regexFact;
        

        List<String> list = Files.readAllLines(new File(filename).toPath(), Charset.defaultCharset() );
        
        try {
            for (String line:list) {
                System.out.println("Parsing line: " + line);
                line = line.replaceAll(" ", "");
                if (line.matches(regexFact))
                    theory.addPredicate(parseFact(line));
                else if (line.matches(regexTarget)) 
                {
                    target = parseTarget(line);
                    break;
                }
                else if (line.equals("\n"))
                    break;
                else    
                {
                    System.out.println("something's wrong w/line "
                            + line);
                }
            }
        }
        catch (PredicateCreateException | PredicateParseException pce) 
        {
            System.out.println(pce.getLocalizedMessage());
        }
    }
    
    /**
     * 
     * @param name - Comparison, Pointer, ZeroNonzero, Arithmetic expected 
     * @throws java.io.IOException 
     */
    public void addRules(String name) throws IOException {
        
        System.out.println("!!!ADDING RULES " + name);
        String regexFact = "[A-Z]+(_[A-Z]+)*\\(_*[a-z|A-Z|0-9]+(,_*[a-z|A-Z|0-9]+)*\\)";       //^ - the beginning and $ - the end
        String regexRule = regexFact + ":-" + regexFact + "(," + regexFact + ")*";
        
        String fileWRules = "rules/_" + name + ".txt";
        List<String> list;
        list = Files.readAllLines(new File(fileWRules).toPath(), Charset.defaultCharset() );
        
        try {
            for (String line:list) {
                line = line.replaceAll(" ", "");
                if (line.matches(regexRule))
                    logician.addRule(parseRule(line));
                else if (line.equals(""))
                    break;
                else    
                {
                    System.out.println("The line doesn't match the regex: "
                            + line);
                }
            }
        }
        catch (PredicateCreateException | PredicateParseException pce) 
        {
            System.out.println(pce.getLocalizedMessage());
        }        
    }
    
    public void addAllRules () throws IOException {
        this.addRules("Arithmetic");
        this.addRules("Comparison");        
        this.addRules("ZeroNonzero");
        this.addRules("Pointer");
    }
    
    public Logician getLogician() {
        return logician;
    }

    public Predicate getTarget() {
        return target;
    }

    public Theory getTheory() {
        return theory;
    }
    
        /**
     * Парсинг строки вида NAME (arg1,arg2,...)
     * @param input входная строка
     * @return Predicate
     * @throws PredicateParseException - если предикат неизвестного типа или с неверным кол-вом аргументов
     * @throws PredicateCreateException - если невозможно создать предикат
     */
    private Predicate parseFact(String input) throws PredicateParseException, PredicateCreateException {
        
        List<PredicateObject> argsLoc = new ArrayList<>();
        String[] parts = input.split("\\(|\\)|,");
        
        String intConstantRegex ="\\d+";
        String strConstantRegex = "[A-Z]+";
        String varRegex = "[a-z]+";
        
        try {
            PredicateType.valueOf(parts[0]);
        }
        catch (IllegalArgumentException iae) {
            
            throw new PredicateParseException("Неверное имя предикатa "
                    + input);

        }
        if ((parts.length - 1)  != PredicateType.valueOf(parts[0]).getArgsNumber()) {
            throw new PredicateParseException("Неверное количество аргументов");
        }
        
        for (int i = 1; i<parts.length; i++) {
            PredicateObject po;
            if (parts[i].matches(varRegex))
                po = predFactory.createVariableObject(i, parts[i]); 
            else if (parts[i].matches(intConstantRegex))
                po = predFactory.createIntegerConstantObject(Long.decode(parts[i]), 1);
            else
                throw new PredicateParseException("Неправильное имя у переменной "
                        + parts[i]);
            if (args.containsKey(po.getUniqueName()))
                argsLoc.add(args.get(po.getUniqueName()));
            else {
                argsLoc.add(po);
                args.put(po.getUniqueName(), po);
            }
            
        }
        Predicate p = predFactory.createPredicate(PredicateType.valueOf(parts[0]), (argsLoc));
        System.out.println("GOT "
                + p.getType() + "  " + p.getArguments());
        return predFactory.createPredicate(PredicateType.valueOf(parts[0]), (argsLoc));
    }
    
    private Predicate parseItemQ (String input) throws PredicateParseException, PredicateCreateException {
        
        System.out.println("Parsing item " + input);
        ArrayList<PredicateObject> argsLoc = new ArrayList<>();
        input = input.replaceAll(" ", "");
        String[] parts = input.split("\\(|\\)|,"); 
        String intConstantRegex ="-{0,1}\\d+";
        String simConstantRegex = "[A-Z]+";
        String valueRegex = "[a-z]+";
        String nonconstRegex = "_[a-z]+";
        
        try {
            PredicateType.valueOf(parts[0]);
            System.out.println(parts[0]);
        }
        catch (IllegalArgumentException iae) {
            
            throw new PredicateParseException("Неверное имя предикатa "
                    + input);

        }
        if ((parts.length - 1)  != PredicateType.valueOf(parts[0]).getArgsNumber()) {
            throw new PredicateParseException("Неверное количество аргументов");
        }
        
        PredicateObject qo ;
        for (int i = 1; i<parts.length; i++) {
            if (parts[i].matches(simConstantRegex))
                qo = quantifierFactory.createQuantifierSimpleConstant(i, parts[i]);
            else if (parts[i].matches(valueRegex))
                qo = quantifierFactory.createQuantifierValue(i, parts[i]);    
            else if (parts[i].matches(intConstantRegex))
                qo = predFactory.createIntegerConstantObject(Long.decode(parts[i]), 1);
            else if (parts[i].matches(nonconstRegex))
                qo = quantifierFactory.createQuantifierNonconstValue(i, input);
            else
                throw new PredicateParseException("Неправильное имя у переменной "
                        + parts[i]);
            
            if (args.containsKey(qo.getUniqueName()))
                argsLoc.add(args.get(qo.getUniqueName()));
            else {
                argsLoc.add(qo);
                args.put(qo.getUniqueName(), qo);
            }
        }
        System.out.println(argsLoc);
        
        PredicateType pt = PredicateType.valueOf(parts[0]);
        Predicate p = predFactory.createPredicate(pt, (argsLoc));
        System.out.println("GOTCHA "
                + p.getType() + "  " + p.getArguments());
        return predFactory.createPredicate(PredicateType.valueOf(parts[0]), (argsLoc));
    }
               
    /**
     * Парсинг строки вида NAME (arg1,arg2,...) :- NAME2(arg1,...), NAME3(arg1,...), ...
     * @param input
     * @return Inference Rule
     * @throws PredicateParseException - если предикат неизвестного типа или с неверным кол-вом аргументов
     * @throws PredicateCreateException - если невозможно создать предикат
     */
    private InferenceRule parseRule(String input) throws PredicateParseException, PredicateCreateException {
        
        System.out.println("=====PARSING LINE " + input);
        
        String regexFact = "[A-Z]+(_[A-Z]+)*\\(_*[a-z|A-Z|0-9]+(,_*[a-z|A-Z|0-9]+)*\\)";       //^ - the beginning and $ - the end
        String regexRule = regexFact + ":-" + regexFact + "(," + regexFact + ")*";
        String regexTarget = "\\?" + regexFact;
        
        Predicate right ;
        List<Predicate> left = new ArrayList<>();
        String sFacts, sTarget, parts[];
                
        
        int delimiter = input.indexOf(":-");
        
        try {
            sTarget = input.substring(0, delimiter);
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            throw new PredicateParseException("Неправильный формат ввода: "
                    + input);
        }
        
        right = parseItemQ(sTarget);
        
        sFacts = input.substring(delimiter+2);
        
        parts = sFacts.split("\\)");
        
        for (String it : parts)
        {
            if (it.startsWith(","))
                    it = it.substring(1);
            try {
                left.add(parseItemQ(it));
            }
            catch (PredicateParseException ppe)
            {
               throw new PredicateParseException(ppe.getMessage());
            }
        }        
        
        
        return logFactory.createPrologRule(left, right);
    }

    /**
     * Парсинг строки вида ?NAME (arg1,arg2,...)
     * @param input
     * @return Predicate
     * @throws PredicateParseException - если предикат неизвестного типа или с неверным кол-вом аргументов
     * @throws PredicateCreateException - если невозможно создать предикат
     */
    private  Predicate parseTarget(String input) throws PredicateParseException, PredicateCreateException {
        input = input.replaceAll("\\?", "");
        return parseFact(input);
    }
    
    private List<PredicateObject> someFunction (List<PredicateObject> local) {
        return null;
        
    }
    
    
//    
//    public List<PredicateObject> mixNMatch ( List<PredicateObject> local) {
//        //this weird construction meant to perform 
//        //adding all variables of predicate to local list
//        //and further checking if they exist in the global list
//        boolean found = false;
//         if (args.isEmpty())
//        {
//                for (PredicateObject po : local) {
//                        args.add(po);
//                }          
//        }
//        else {
//            int lngth = args.size();  
//            for (PredicateObject po : local) {                
//                for (int i = 0; i<lngth; i++) {
//                    if (args.get(i).getUniqueName().equals(po.getUniqueName())) { //если такая переменная не существует, то добавить
//                        found = true;
//                        local.set(local.indexOf(po),args.get(i));
//                        break;
//                    }
//                }
//                if (!found) {
//                    args.add(po);
//                }
//                found = false;
//            }
//        }
//        return local;
//    }
//    
//     public List<PredicateObject> mixNMatch ( List<PredicateObject> global, List<PredicateObject> local) {
//        //this weird construction meant to perform 
//        //adding all variables of predicate to local list
//        //and further checking if they exist in the global list
//        boolean found = false;
//         if (global.isEmpty())
//        {
//                for (PredicateObject po : local) {
//                        global.add(po);
//                }          
//        }
//        else {
//            int lngth = global.size();  
//            for (PredicateObject po : local) {                
//                for (int i = 0; i<lngth; i++) {
//                    if (global.get(i).getUniqueName().equals(po.getUniqueName())) { 
//                        found = true;
//                        local.set(local.indexOf(po),global.get(i));
//                        break;
//                    }
//                }
//                if (!found) {
//                    global.add(po);
//                    lngth = global.size();
//                }
//                found = false;
//            }
//        }
//        return local;
//    }
//    
}