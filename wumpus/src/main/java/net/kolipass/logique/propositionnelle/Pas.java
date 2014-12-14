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
            int nbEnonces=((Et)a).getNbEnonces();
            Enonce[] n=new Enonce[nbEnonces];
            for(int i=0;i<nbEnonces;i++)
            {
                n[i]=new Pas(((Et)a).getN(i));
            }
            return (Enonce)new Ou(n);
        }
        else if(a instanceof Ou)
        {
            int nbEnonces=((Ou)a).getNbEnonces();
            Enonce[] n=new Enonce[nbEnonces];
            for(int i=0;i<nbEnonces;i++)
            {
                n[i]=new Pas(((Ou)a).getN(i));
            }
            return (Enonce)new Et(n);
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
    
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Pas))
        {
            return false;
        }
        else
        {
            return a.equals(((Pas)obj).a);
        }
    }
}
