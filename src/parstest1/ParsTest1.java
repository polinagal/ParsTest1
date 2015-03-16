/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parstest1;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParsTest1 {

    static Map <String, Integer> vocabulary = new HashMap<String, Integer>() {{
        put ("OR",3);
        put ("FALSE",1);   
        put ("ZERO",1);
        put ("NONZERO",1);
        
    }};
   
    //в дальнейшем будет возвращать Theory
    public static void ParseFile (String filename) throws IOException
    {
        List<String> list = Files.readAllLines(new File
        (filename).toPath(),
                Charset.defaultCharset() );
        for (String list1 : list) {
            Parse(list1);
        }        
    }
    
    
    public static void Parse (String input)
    {
        String t = input;
        String[] parts = t.split("\\(|\\)|,"); //divide by '(' OR ')' OR ','
        //если такого правила нет с "словаре", то это ошибка
        if (!vocabulary.containsKey(parts[0])){
            System.out.println("Unknow predicate type " + input);
        }
        //если количество аргументов для этого предиката 
        //указано неправильно, то это ошибка
        else if (parts.length != (vocabulary.get(parts[0]) + 1))
            
                System.out.println("FAIL: " + input );
        else 
            System.out.println("OK: "+ input);
    }
    
    //чтение набора правил для словаря из файла 
    public static void FillVocab(String filename) throws IOException
    {
        List<String> list = null;
        list = Files.readAllLines(new File(filename).toPath(),
                Charset.defaultCharset() );
        for (String list1: list) {
            String[] parts = list1.split("\\(|\\)");
            vocabulary.put(parts[0], Integer.parseInt(parts[1]));
            
        }
//        for (Map.Entry<String, Integer> e : vocabulary.entrySet()) {
//            System.out.print(e.getKey() + " ");
//            System.out.println( e.getValue());
//        }
    }
    
    public static void main(String[] args) throws IOException {
        
        FillVocab("lib.txt");
        
        ParseFile("1try.txt");       
    }
}