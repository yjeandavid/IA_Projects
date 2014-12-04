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


public class KBPropositionnelle {
    
    private ArrayList<Enonce> KB;
    
    public KBPropositionnelle()
    {
        KB=new ArrayList();
    }
    
    public boolean demander(ArrayList<Enonce> a)
    {
        return TTEntails(KB,a);
    }
    
    public void raconter(Enonce a)
    {
        KB.add(a);
    }
    
    public String afficherKB()
    {
        String resultat="\n";
        for(int i=0;i<KB.size();i++)
        {
            resultat+=KB.get(i).afficher()+"\n";
        }
        return resultat;
    }
    
    private void getSymboles(Enonce enonce,ArrayList<Symbole> symboles)
    {
        if(enonce instanceof Enonce.Complexe.AB)
        {
            getSymboles(((Enonce.Complexe.AB)enonce).getA(),symboles);
            getSymboles(((Enonce.Complexe.AB)enonce).getB(),symboles);
        }
        else if(enonce instanceof Enonce.Complexe.A)
        {
            getSymboles(((Enonce.Complexe.A)enonce).getA(),symboles);
        }
        else if(enonce instanceof Symbole)
        {
            if(!symboles.contains((Symbole)enonce))
            {
                symboles.add((Symbole)enonce);
            }
        }
    }
    
    //À compléter
    //But du programme:
    //Remplacer tout les symboles dans les énoncés par les valeurs de vérité du modèle
    //Puis, simplifier les nouveaux énoncés jusqu'à obtenir une valeur de vérité
    //Finalement, faire un Et logique entre chacunes des valeurs de vérités obtenus et retourner le résultat
    private boolean modeleValide(ArrayList<Enonce> enonces,ArrayList<ValVerite> modele)
    {
        boolean valide=true;
        for(int i=0;i<enonces.size()&&valide;i++)
        {
            Enonce cour=enonces.get(i);
            cour=revelerValVerite(cour,modele);
            valide=valide&&((ValVerite)cour).estVrai();
        }
        return valide;
    }
    
    private Enonce revelerValVerite(Enonce cour,ArrayList<ValVerite> modele)
    {
        if(cour instanceof Enonce.Complexe)
        {
            Enonce tempEnonce=cour;
            if(cour instanceof Enonce.Complexe.AB)
            {
                if(cour instanceof DblImpl)
                {
                    tempEnonce=(Enonce)new DblImpl(revelerValVerite(((Enonce.Complexe.AB)cour).getA(),modele),
                        revelerValVerite(((Enonce.Complexe.AB)cour).getB(),modele));
                }
                else if(cour instanceof Impl)
                {
                    tempEnonce=(Enonce)new Impl(revelerValVerite(((Enonce.Complexe.AB)cour).getA(),modele),
                        revelerValVerite(((Enonce.Complexe.AB)cour).getB(),modele));
                }
                else if(cour instanceof Et)
                {
                    tempEnonce=(Enonce)new Et(revelerValVerite(((Enonce.Complexe.AB)cour).getA(),modele),
                        revelerValVerite(((Enonce.Complexe.AB)cour).getB(),modele));
                }
                else if(cour instanceof Ou)
                {
                    tempEnonce=(Enonce)new Ou(revelerValVerite(((Enonce.Complexe.AB)cour).getA(),modele),
                        revelerValVerite(((Enonce.Complexe.AB)cour).getB(),modele));
                }
            }
            else if(cour instanceof Enonce.Complexe.A)
            {
                tempEnonce=(Enonce)new Pas(revelerValVerite(((Enonce.Complexe.A)cour).getA(),modele));
            }
            else
            {
                return tempEnonce;
            }
            return((Enonce.Complexe)tempEnonce).simplifier();
        }
        else if(cour instanceof Symbole)
        {
            for(int i=0;i<modele.size();i++)
            {
                if(modele.get(i).getNom().equals(((Symbole)cour).getNom()))
                {
                    return (Enonce)((Symbole)cour).reveler(modele.get(i).estVrai());
                }
            }
        }
        return cour;
    }
    
    //page 263 du livre
    private boolean TTEntails(ArrayList<Enonce> KB,ArrayList<Enonce> a)
    {
        ArrayList<Symbole> symboles=new ArrayList();
        ArrayList<ValVerite> modele=new ArrayList();
        for(int i=0;i<KB.size();i++)
        {
            getSymboles(KB.get(i),symboles);
        }
        getSymboles(a.get(0),symboles);
        return TTCheckAll(KB,a,symboles,modele);
    }
    
    //page 263 du livre
    private boolean TTCheckAll(ArrayList<Enonce> KB,ArrayList<Enonce> a,ArrayList<Symbole> symboles,ArrayList<ValVerite> modele)
    {
        if(symboles.isEmpty())
        {
            if(modeleValide(KB,modele))
            {
                return modeleValide(a,modele);
            }
            else
            {
                return true;
            }        
        }
        else
        {
            Symbole symbole=symboles.get(0);
            ArrayList<Symbole> reste=(ArrayList<Symbole>)symboles.clone();
            reste.remove(0);
            ArrayList<ValVerite> modeleVrai=(ArrayList<ValVerite>)modele.clone();
            modeleVrai.add(new ValVerite(symbole.getNom(),true));
            ArrayList<ValVerite> modeleFaux=(ArrayList<ValVerite>)modele.clone();
            modeleFaux.add(new ValVerite(symbole.getNom(),false));
            
            return TTCheckAll(KB,a,reste,modeleFaux)&&
                TTCheckAll(KB,a,reste,modeleVrai);
        }
    }
}






