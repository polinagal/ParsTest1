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
    
    private static final LogicianFactory logfactory = LogicianFactory.instance;

    private static final AbstractPredicateFactory predFactory = AbstractPredicateFactory.getInstance();

    private static final AbstractTheoryFactory thFactory = AbstractTheoryFactory.getInstance();
    
    private final Theory theory = thFactory.createTheory();
    
    private final Logician logician = logfactory.createLogician();
    
    private  Predicate target = null;
    
    /**
     * Просмотр файла по строкам и в соответствии с видом каждой строки 
     * принимается решение о принадлежности данных к фактам, правилам или 
     * предикату-цели (в начале строки цели стоит ?)
     * @param filename - имя просматриваемого файла
     * @throws IOException 
     */
    public void parseFile (String filename) throws IOException {
        
        String regexFact = "[A-Z]*\\([a-z|A-Z]+(,[a-z|A-Z]+)*\\)";       //^ - the beginning and $ - the end
        String regexRule = regexFact + ":-" + regexFact + "(," + regexFact + ")*";
        String regexTarget = "\\?" + regexFact;
        

        List<String> list = Files.readAllLines(new File(filename).toPath(), Charset.defaultCharset() );
        
        try {
            for (String line:list) {
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
        catch (PredicateCreateException pce) 
        {
            System.out.println(pce.getLocalizedMessage());
        }
        catch (PredicateParseException ppe)
        {
            System.out.println(ppe.getLocalizedMessage());
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
        
        input = input.replaceAll(" ", "");
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
        final List<PredicateObject> pos = new ArrayList<>();
        for (int i = 1; i<parts.length; i++) {
            
            pos.set(i, predFactory.createVariableObject(4, parts[i]));
        }
        
        return predFactory.createPredicate(PredicateType.valueOf(parts[0]), pos);
    }

    /**
     * Парсинг строки вида NAME (arg1,arg2,...) :- NAME2(arg1,...), NAME3(arg1,...), ...
     * @return Inference Rule
     * @throws PredicateParseException - если предикат неизвестного типа или с неверным кол-вом аргументов
     * @throws PredicateCreateException - если невозможно создать предикат
     */
    private InferenceRule parseRule(String input) throws PredicateParseException, PredicateCreateException {
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
        return logfactory.createPrologRule(facts, target);
    }

    /**
     * Парсинг строки вида ?NAME (arg1,arg2,...)
     * @param input
     * @return Predicate
     * @throws PredicateParseException - если предикат неизвестного типа или с неверным кол-вом аргументов
     * @throws PredicateCreateException - если невозможно создать предикат
     */
    private Predicate parseTarget(String input) throws PredicateParseException, PredicateCreateException {
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
}
