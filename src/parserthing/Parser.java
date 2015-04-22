package parserthing;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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
    
    private  final LogicianFactory logFactory = LogicianFactory.instance;

    private  final AbstractPredicateFactory predFactory = AbstractPredicateFactory.getInstance();

    private  final AbstractTheoryFactory thFactory = AbstractTheoryFactory.getInstance();
    
    private   final Theory theory = thFactory.createTheory();
    
    private final Logician logician = logFactory.createLogician();
    
    private  Predicate target = null;
    
    private List<PredicateObject> args = new ArrayList<>();

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
        
//        args.add(predFactory.createVariableObject(4, "x"));
//        args.add(predFactory.createVariableObject(4, "y"));
//        for (PredicateObject po : args) {
//            System.out.println(po.getUniqueName());
//        }
        
        String regexFact = "[A-Z]*\\([a-z|A-Z]+(,[a-z|A-Z]+)*\\)";       //^ - the beginning and $ - the end
        String regexRule = regexFact + ":-" + regexFact + "(," + regexFact + ")*";
        String regexTarget = "\\?" + regexFact;
        

        List<String> list = Files.readAllLines(new File(filename).toPath(), Charset.defaultCharset() );
        
        try {
            for (String line:list) {
                line = line.replaceAll(" ", "");
                if (line.matches(regexFact))
                    theory.addPredicate(parseFact(line));
                else if (line.matches(regexRule))
                    logician.addRule(parseRule(line));
                else if (line.matches(regexTarget)) 
                {
                    target = parseTarget(line);
                    break;
                }
                else    
                {
                    System.out.println("somethings gone wrong on line "
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
     * Парсинг строки вида NAME (arg1,arg2,...)
     * @param input входная строка
     * @return Predicate
     * @throws PredicateParseException - если предикат неизвестного типа или с неверным кол-вом аргументов
     * @throws PredicateCreateException - если невозможно создать предикат
     */
    private Predicate parseFact(String input) throws PredicateParseException, PredicateCreateException {
        
        List<PredicateObject> argsLoc = new ArrayList<>();
        String[] parts = input.split("\\(|\\)|,"); 
        
        try {
            PredicateType.valueOf(parts[0]);
        }
        catch (IllegalArgumentException iae) {
            
            throw new PredicateParseException("Неверное имя предикатa");

        }
        if ((parts.length - 1)  != PredicateType.valueOf(parts[0]).getArgsNumber()) {
            throw new PredicateParseException("Неверное количество аргументов");
        }
        
        //this weird construction is about adding all variables of predicate to local list
        //and further checking if rhey exist in the global list
        for (int i = 1; i<parts.length; i++) {
            PredicateObject po = predFactory.createVariableObject(4, parts[i]);
                argsLoc.add(po);
        }
        
        
        if (args.isEmpty())
        {
                for (PredicateObject po : argsLoc) {
                    
                        args.add(po);
                    }          
        }
           
        else {
            int lngth = args.size();  
            for (PredicateObject po : argsLoc) {
                for (int i = 0; i<lngth; i++) {
    //                String uname1 = pog.getUniqueName();
    //                String uname2 = pog.getUniqueName();
    //                System.out.println(uname1);
    //                System.out.println(uname2);
                    if (!args.get(i).getUniqueName().equals(po.getUniqueName()))
    //                if (!args.contains(po)) 
                        args.add(po);
                    else
                        argsLoc.set(argsLoc.indexOf(po),args.get(i));
                }
            }
        }
        return predFactory.createPredicate(PredicateType.valueOf(parts[0]), argsLoc);
    }

    /**
     * Парсинг строки вида NAME (arg1,arg2,...) :- NAME2(arg1,...), NAME3(arg1,...), ...
     * @param input
     * @return Inference Rule
     * @throws PredicateParseException - если предикат неизвестного типа или с неверным кол-вом аргументов
     * @throws PredicateCreateException - если невозможно создать предикат
     */
    private  InferenceRule parseRule(String input) throws PredicateParseException, PredicateCreateException {
        //this thing looks like this 
        //predtarg(arg1, arg2,...) :- pred1(arg1,arg2,...), pred2(arg1,arg2,...)
        
        Predicate target ;
        List<Predicate> facts = new ArrayList<>();
        
        int delimiter = input.indexOf(":-");
        
        String sTarget;
        try {
            sTarget = input.substring(0, delimiter);
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            System.out.println("Неправильный формат ввода");
            return null;
        }
        
        target = parseFact(sTarget);
        
        String sFacts = input.substring(delimiter+2);
        String part;
        delimiter = sFacts.indexOf(")");
        while (delimiter > 0) {
            part = sFacts.substring(0,delimiter);
            sFacts = sFacts.substring(delimiter+1);
            try {
                facts.add(parseFact(part));
            }
            catch (PredicateParseException ppe)
            {
               return null;
            }
            delimiter = sFacts.indexOf(",");
        }
        return logFactory.createPrologRule(facts, target);
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

    public Logician getLogician() {
        return logician;
    }

    public Predicate getTarget() {
        return target;
    }

    public Theory getTheory() {
        return theory;
    }

    private int indexOf(PredicateObject po) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
