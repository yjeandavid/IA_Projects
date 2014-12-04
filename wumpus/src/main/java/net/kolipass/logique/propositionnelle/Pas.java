/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique.propositionnelle;

/**
 *
 * @author Gabriel
 */
//Négation logique
public class Pas extends Enonce.Complexe.A{
    
    public Pas(Enonce a_)
    {
        super(a_);
    }
    public Enonce deMorgan()
    {
        if(a instanceof Et)
        {
            return (Enonce)new Ou((Enonce)new Pas(((Et)a).getA()),(Enonce)new Pas(((Et)a).getB()));
        }
        else if(a instanceof Ou)
        {
            return (Enonce)new Et((Enonce)new Pas(((Et)a).getA()),(Enonce)new Pas(((Et)a).getB()));
        }
        return (Enonce)this;
    }

    public Enonce dblNega()
    {
        if(a instanceof Pas)
        {
            return ((Pas)a).getA();
        }
        return (Enonce)this;
    }

    @Override
    public Enonce simplifier()
    {
        if(a instanceof ValVerite)
        {
            return (Enonce)new ValVerite(((ValVerite)a).getNom(),!((ValVerite)a).estVrai());
        }
        return (Enonce)this;
    }
    
    @Override
    public String afficher()
    {
        return "(!"+a.afficher()+")";
    }
}
