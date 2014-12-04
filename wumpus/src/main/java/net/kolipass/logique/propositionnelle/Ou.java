/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique.propositionnelle;

/**
 *
 * @author Gabriel
 */
//Ou logique
public class Ou extends Enonce.Complexe.AB{
    
    public Ou(Enonce a_,Enonce b_)
    {
        super(a_,b_);
    }

    public Enonce commutativite()
    {
        return (Enonce)new Ou(b,a);
    }

    public Enonce distributivite()
    {
        if(b instanceof Et)
        {
            return (Enonce)new Et((Enonce)new Ou(a,((Et)b).getA()),(Enonce)new Ou(a,((Et)b).getB()));
        }
        return (Enonce)this;
    }

    @Override
    public Enonce simplifier()
    {
        if(a instanceof ValVerite&&b instanceof ValVerite)
        {
            return (Enonce)new ValVerite("",((ValVerite)a).estVrai()||((ValVerite)b).estVrai());
        }
        else if(a instanceof ValVerite)
        {
            if(((ValVerite)a).estVrai())
            {
                return (Enonce)new ValVerite("",true);
            }
            else
            {
                return b;
            }
        }
        else if(b instanceof ValVerite)
        {
            if(((ValVerite)b).estVrai())
            {
                return (Enonce)new ValVerite("",true);
            }
            else
            {
                return a;
            }
        }
        return (Enonce)this;
    }
    
    @Override
    public String afficher()
    {
        return "("+a.afficher()+" || "+b.afficher()+")";
    }
}
