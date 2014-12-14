/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique.propositionnelle;

/**
 *
 * @author Gabriel
 */

import java.util.ArrayList;
import java.util.Arrays;

//Syntaxe: page 259 du livre
public interface Enonce {
    abstract String afficher();
    public interface Complexe extends Enonce{
        abstract Enonce simplifier();
        
        public abstract class A implements Enonce.Complexe{
            protected Enonce a;
            public A(Enonce a_)
            {
                a=a_;
            }
            public Enonce getA()
            {
                return a;
            }
        }
        
        public abstract class AB implements Enonce.Complexe{
            protected Enonce a;
            protected Enonce b;
            public AB(Enonce a_,Enonce b_)
            {
                a=a_;
                b=b_;
            }
            public Enonce getA()
            {
                return a;
            }
            public Enonce getB()
            {
                return b;
            }
        }
        
        public abstract class N implements Enonce.Complexe{
            protected ArrayList<Enonce> n;
            public N(Enonce... n_)
            {
                n=new ArrayList(Arrays.asList(n_));
            }
            
            public int getNbEnonces()
            {
                return n.size();
            }
            
            public Enonce getN(int pos)
            {
                return n.get(pos);
            }
        }
    }
    
    public interface Atomique extends Enonce{}
}
