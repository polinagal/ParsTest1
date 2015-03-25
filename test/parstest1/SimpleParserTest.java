/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parstest1;

import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import simpleparser.SimpleParser;

/**
 *
 * @author Dante
 */
public class SimpleParserTest {
   
    @Test
    public void testParseStrOK() throws Exception {
        System.out.println("ok");
        String input = "OR(a,b,c) :- FALSE(z), EQUALS(x,y)";
        Assert.assertTrue(SimpleParser.parseStr(input));
    }    
    
    @Test
    public void testParseStrFail1() throws Exception {
        System.out.println("wrong delimiter :");
        String input = "OR(a,b,c) : FALSE(z), EQUALS(x,y)";
        Assert.assertFalse(SimpleParser.parseStr(input));
    } 
    
    @Test
    public void testParseStrFail2() throws Exception {
        System.out.println("Missing delimiter");
        String input = "OR(a,b,c), FALSE(z), EQUALS(x,y)";
        Assert.assertFalse(SimpleParser.parseStr(input));
    }  
    
    @Test
    public void testParseStrFail3() throws Exception {
        System.out.println("Wrong predicate name");
        String input = "OR(a,b,c) :- FALS(z), EQUALS(x,y)";
        Assert.assertFalse(SimpleParser.parseStr(input));
    }  
    
    @Test
    public void testParseStrFail4() throws Exception {
        System.out.println("Missing args");
        String input = "OR(a,b) :- FALSE(z), EQUALS(x,y)";
        Assert.assertFalse(SimpleParser.parseStr(input));
    }  
}
