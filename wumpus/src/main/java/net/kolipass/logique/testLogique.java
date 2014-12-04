/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique;

import main.java.net.kolipass.logique.propositionnelle.*;
import java.util.ArrayList;

/**
 *
 * @author Gabriel
 */
public class testLogique {
    
    static KBPropositionnelle test=new KBPropositionnelle();
    
    public static void main(String[] args) {
        
        test.raconter((Enonce)new Pas(new Symbole("P11")));
        test.raconter((Enonce)new DblImpl(new Symbole("B11"),new Ou(new Symbole("P12"),new Symbole("P21"))));
        test.raconter((Enonce)new DblImpl(new Symbole("B21"),new Ou(new Symbole("P11"),new Ou(new Symbole("P22"),new Symbole("P31")))));
        test.raconter((Enonce)new Pas(new Symbole("B11")));
        test.raconter((Enonce)new Symbole("B21"));
        
        System.out.println(test.afficherKB());
        
        test1();
        test2();
        test3();
    }
    private static void test1()
    {
        ArrayList<Enonce> enonces=new ArrayList();
        enonces.add((Enonce)new Pas(new Symbole("P12")));
        if(test.demander(enonces))
        {
            System.out.println("test1:Réussi");
        }
        else
        {
            System.out.println("test1:Échoué");
        }
    }
    private static void test2()
    {
        ArrayList<Enonce> enonces=new ArrayList();
        enonces.add((Enonce)new Pas(new Symbole("P22")));
        if(!test.demander(enonces))
        {
            System.out.println("test2:Réussi");
        }
        else
        {
            System.out.println("test2:Échoué");
        }
    }
    private static void test3()
    {
        ArrayList<Enonce> enonces=new ArrayList();
        enonces.add((Enonce)new Symbole("P11"));
        if(!test.demander(enonces))
        {
            System.out.println("test3:Réussi");
        }
        else
        {
            System.out.println("test3:Échoué");
        }
    }
}
