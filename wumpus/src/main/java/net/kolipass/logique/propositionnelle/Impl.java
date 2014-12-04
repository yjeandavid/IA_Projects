/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique.propositionnelle;

/**
 *
 * @author Gabriel
 */
//Implication logique
public class Impl extends Enonce.Complexe.AB{
        
    public Impl(Enonce a_,Enonce b_)
    {
        super(a_,b_);
    }

    public Enonce elimineImpl()
    {
        return (Enonce)new Ou((Enonce)new Pas(a),b);
    }

    //À compléter
    /*public Enonce contraposition()
    {

    }*/

    @Override
    public Enonce simplifier()
    {
        if(a instanceof ValVerite&&b instanceof ValVerite)
        {
            if(((ValVerite)a).estVrai())
            {
                return (Enonce)new ValVerite("",((ValVerite)b).estVrai());
            }
            else
            {
                return (Enonce)new ValVerite("",true);
            }
        }
        else if(a instanceof ValVerite)
        {
            if(((ValVerite)a).estVrai())
            {
                return b;
            }
            else
            {
                return (Enonce)new ValVerite("",true);
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
                return (Enonce)new Pas(a);
            }
        }
        return (Enonce)this;
    }

    @Override
    public String afficher()
    {
        return "("+a.afficher()+" => "+b.afficher()+")";
    }
}
