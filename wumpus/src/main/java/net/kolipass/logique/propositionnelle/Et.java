/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique.propositionnelle;

/**
 *
 * @author Gabriel
 */
//Et logique
public class Et extends Enonce.Complexe.AB{
        
    public Et(Enonce a_,Enonce b_)
    {
        super(a_,b_);
    }

    public Enonce commutativite()
    {
        return (Enonce)new Et(b,a);
    }

    public Enonce distributivite()
    {
        if(b instanceof Ou)
        {
            return (Enonce)new Ou((Enonce)new Et(a,((Ou)b).getA()),(Enonce)new Et(a,((Ou)b).getB()));
        }
        return (Enonce)this;
    }

    @Override
    public Enonce simplifier()
    {
        if(a instanceof ValVerite&&b instanceof ValVerite)
        {
            return (Enonce)new ValVerite("",((ValVerite)a).estVrai()&&((ValVerite)b).estVrai());
        }
        else if(a instanceof ValVerite)
        {
            if(((ValVerite)a).estVrai())
            {
                return b;
            }
            else
            {
                return (Enonce)new ValVerite("",false);
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
                return (Enonce)new ValVerite("",false);
            }
        }
        return (Enonce)this;
    }

    @Override
    public String afficher()
    {
        return "("+a.afficher()+" && "+b.afficher()+")";
    }
}
