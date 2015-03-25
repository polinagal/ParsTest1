package simpleparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, призванный изображать структуру предиката.
 * Состоит из типа предиката и его параметров.
 * В дальнейшем эти данные будут использоваться для создания Theory.
 */

class Predicate_my {
    
    String type;
    List<String> args;

    public Predicate_my(String type, List<String> args) {
        this.type = type;
        this.args = args;
    }
    
    public void print ()
    {
        System.out.println(type);
        for (String arg:args) {
            System.out.println(arg);
        }
    }
}

public class SimpleParser {
    
    /**
     * 
     * @param input строка, соержащая выражение
     * @return true - если строка распознана 
     * false - при возникновении ошибок
     */
    public static boolean parseStr (String input) {
        Predicate_my target;
        List<Predicate_my> facts = new ArrayList<>();
        
        int delimiter = input.indexOf(":-");
        
        String sTarget;
        try {
            sTarget = input.substring(0, delimiter);
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            System.out.println("Неправильный формат ввода");
            return false;
        }
        try {
            target = parse(sTarget);
        }
        catch (PredicateParseException ppe)
        {
            return false;
        }
        
        String sFacts = input.substring(delimiter+2);
        String part;
        delimiter = sFacts.indexOf(")");
        while (delimiter > 0) {
            part = sFacts.substring(0,delimiter);
            sFacts = sFacts.substring(delimiter+1);
            try {
                facts.add(parse(part));
            }
            catch (PredicateParseException ppe)
            {
               return false;
            }
            delimiter = sFacts.indexOf(",");
        }
        
        return true;
    }
        
    /**
     * 
     * @param input - строка вида ПРЕДИКАТ(АРГУМЕНТ1, АРГУМЕНТ2, ... )
     * @return экземпляр класса Predicate_my 
     * @throws PredicateParseException в случае, если что-то пошло не так
     */
    private static Predicate_my parse(String input) throws PredicateParseException {
        
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
        List<String> args = new ArrayList<>();
        for (int i = 1; i<parts.length; i++) {
            args.add(parts[i]);
        }
                
        return (new Predicate_my(parts[0],args));        
    }
    
    public static void main(String[] args) {
        
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("1try.txt"));
        } catch (FileNotFoundException ex) {
        }
        String data = null;
        try {
            data = in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SimpleParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        parseStr(data);
    }
}