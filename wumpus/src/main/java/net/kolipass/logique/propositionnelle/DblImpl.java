/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique.propositionnelle;

/**
 *
 * @author Gabriel
 */
//Double implication logique
public class DblImpl extends Enonce.Complexe.AB{

    public DblImpl(Enonce a_,Enonce b_)
    {
        super(a_,b_);
    }
    
    public Enonce biconditionnel()
    {
        return (Enonce)new Et((Enonce)new Impl(a,b),(Enonce)new Impl(a,b));
    }

    @Override
    public Enonce simplifier()
    {
        if(a instanceof ValVerite&&b instanceof ValVerite)
        {
            return (Enonce)new ValVerite("",((ValVerite)a).estVrai()&&((ValVerite)b).estVrai()||!((ValVerite)a).estVrai()&&!((ValVerite)b).estVrai());
        }
        else if(a instanceof ValVerite)
        {
            if(((ValVerite)a).estVrai())
            {
                return b;
            }
            else
            {
                return (Enonce)new Pas(b);
            }
        }
        else if(b instanceof ValVerite)
        {
            if(((ValVerite)b).estVrai())
            {
                return a;
            }
            else
            {
                return (Enonce)new Pas(a);
            }
        }
        return (Enonce)this;
    }
    
    @Override
    public String afficher()
    {
        return "("+a.afficher()+" <=> "+b.afficher()+")";
    }
}
