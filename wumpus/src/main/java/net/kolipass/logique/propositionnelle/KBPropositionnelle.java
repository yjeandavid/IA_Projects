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
        //return TTEntails(KB,a);
        return PL_Resolution(KB,a.get(0));
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
    
    private boolean PL_Resolution(ArrayList<Enonce> KB,Enonce a)
    {
        ArrayList<Enonce> clauses=new ArrayList();
        ArrayList<Enonce> nouveau=new ArrayList();
        
        for(int i=0;i<KB.size();i++)
        {
            nouveau=CNF(KB.get(i));
            for(int j=0;j<nouveau.size();j++)
            {
                if(!clauses.contains(nouveau.get(j)))
                {
                    clauses.add(nouveau.get(j));
                }
            }
        }
        clauses.add((new Pas(a)).dblNega());
        nouveau.clear();
        int limite=2;
        int increm=0;
        while(increm<limite)
        {
            for(int i=0;i<clauses.size();i++)
            {
                for(int j=i+1;j<clauses.size();j++)
                {
                    ArrayList<Enonce> resolvants=resolution(clauses.get(i),clauses.get(j));
                    for(int k=0;k<resolvants.size();k++)
                    {
                        if(resolvants.get(k)instanceof ValVerite)
                        {
                            if(((ValVerite)resolvants.get(k)).estVrai())
                            {
                                resolvants.remove(k);
                                k--;
                            }
                            else
                            {
                                return true;
                            }
                        }
                    }
                    nouveau.addAll(resolvants);
                    resolvants.clear();
                }
            }
            boolean fini=true;
            for(int i=0;i<nouveau.size();i++)
            {
                if(!clauses.contains(nouveau.get(i)))
                {
                    clauses.add(nouveau.get(i));
                    fini=false;
                }
            } 
            if(fini)
            {
                return false;
            }
            increm++;
        }
        return false;
    }
    
    private ArrayList<Enonce> CNF(Enonce cour)
    {
        ArrayList<Enonce> enonceCNF=new ArrayList();
        
        while(!estCNF(cour))
        {
            cour=conversion(cour,0);
        }
        
        if(cour instanceof Et)
        {
            for(int i=0;i<((Et)cour).getNbEnonces();i++)
            {
                enonceCNF.add(((Et)cour).getN(i));
            }
        }
        else
        {
            enonceCNF.add(cour);
        }
        
        return enonceCNF;
    }
    
    private Enonce conversion(Enonce cour,int niveau)
    {
        Enonce resultat=cour;
        
        if(cour instanceof DblImpl)
        {
            resultat=((DblImpl)cour).biconditionnel();
        }
        else if(cour instanceof Impl)
        {
            resultat=((Impl)cour).elimineImpl();
        }
        else if(cour instanceof Et)
        {
            ArrayList<Enonce> temp=new ArrayList();
            
            boolean distribuable=false;
            int pos=-1;
            
            for(int i=0;i<((Et)cour).getNbEnonces()&&!distribuable;i++)
            {
                if(((Et)cour).getN(i) instanceof Ou)
                {
                    pos=i;
                    distribuable=true;
                }
            }
            
            if(distribuable&&niveau!=0)
            {
                resultat=((Et)cour).distributivite(pos);
            }
            else
            {
                for(int i=0;i<((Et)cour).getNbEnonces();i++)
                {
                    if(((Et)cour).getN(i) instanceof Et)
                    {
                        Enonce temp2=((Et)cour).getN(i);
                        for(int j=0;j<((Et)temp2).getNbEnonces();j++)
                        {
                            temp.add(((Et)temp2).getN(j));
                        }
                    }
                    else if(!(((Et)cour).getN(i) instanceof Enonce.Atomique))
                    {
                        temp.add(conversion(((Et)cour).getN(i),niveau+1));
                    }
                    else
                    {
                        temp.add(((Et)cour).getN(i));
                    }
                }
                Enonce[] temp2=new Enonce[temp.size()];
                for(int i=0;i<temp.size();i++)
                {
                    temp2[i]=temp.get(i);
                }
                resultat=new Et(temp2);
            }
        }
        else if(cour instanceof Ou)
        {
            ArrayList<Enonce> temp=new ArrayList();
            
            boolean distribuable=false;
            int pos=-1;
            
            for(int i=0;i<((Ou)cour).getNbEnonces()&&!distribuable;i++)
            {
                if(((Ou)cour).getN(i) instanceof Et)
                {
                    pos=i;
                    distribuable=true;
                }
            }
            
            if(distribuable)
            {
                resultat=((Ou)cour).distributivite(pos);
            }
            else
            {
                for(int i=0;i<((Ou)cour).getNbEnonces();i++)
                {
                    if(((Ou)cour).getN(i) instanceof Ou)
                    {
                        Enonce temp2=((Ou)cour).getN(i);
                        for(int j=0;j<((Ou)temp2).getNbEnonces();j++)
                        {
                            temp.add(((Ou)temp2).getN(j));
                        }
                    }
                    else if(!(((Ou)cour).getN(i) instanceof Enonce.Atomique))
                    {
                        temp.add(conversion(((Ou)cour).getN(i),niveau+1));
                    }
                    else
                    {
                        temp.add(((Ou)cour).getN(i));
                    }
                }
                Enonce[] temp2=new Enonce[temp.size()];
                for(int i=0;i<temp.size();i++)
                {
                    temp2[i]=temp.get(i);
                }
                resultat=new Ou(temp2);
            }
            
        }
        else if(cour instanceof Pas)
        {
            if(((Pas)cour).getA() instanceof Pas)
            {
                resultat=((Pas)cour).dblNega();
            }
            else if(!(((Pas)cour).getA() instanceof Enonce.Atomique))
            {
                resultat=((Pas)cour).deMorgan();
            }
            else
            {
                resultat=cour;
            }
        }
        return resultat;
    }
    
    private boolean estCNF(Enonce cour)
    {
        if(cour instanceof Et)
        {
            for(int i=0;i<((Et)cour).getNbEnonces();i++)
            {
                Enonce temp=((Et)cour).getN(i);
                if(temp instanceof Ou)
                {
                    for(int j=0;j<((Ou)temp).getNbEnonces();j++)
                    {
                        Enonce temp2=((Ou)temp).getN(j);
                        if(!(temp2 instanceof Symbole||temp2 instanceof Pas))
                        {
                            return false;
                        }
                        else if(temp2 instanceof Pas)
                        {
                            if(!(((Pas)temp2).getA() instanceof Symbole))
                            {
                                return false;
                            }
                        }
                    }
                }
                else
                {
                    return false;
                }
            }
            return true;
        }
        else if(cour instanceof Pas)
        {
            if(((Pas)cour).getA() instanceof Symbole)
            {
                return true;
            }
        }
        else if(cour instanceof Symbole)
        {
            return true;
        }
        
        return false;
    }
    
    private ArrayList<Enonce> resolution(Enonce clauseA,Enonce clauseB)
    {
        ArrayList<Enonce> enonceResolu=new ArrayList();
        
        if(clauseA instanceof Ou&&clauseB instanceof Ou)
        {
            for(int i=0;i<((Ou)clauseA).getNbEnonces();i++)
            {
                for(int j=0;j<((Ou)clauseB).getNbEnonces();j++)
                {
                    Enonce a=((Ou)clauseA).getN(i);
                    Enonce b=((Ou)clauseB).getN(j);
                    
                    if(complementaire(a,b))
                    {
                        ArrayList<Enonce> temp=new ArrayList();
                        Enonce resolutions;
                        for(int k=0;k<((Ou)clauseA).getNbEnonces();k++)
                        {
                            if(k!=i&&!temp.contains(((Ou)clauseA).getN(k)))
                            {
                                temp.add(((Ou)clauseA).getN(k));
                            }
                        }
                        for(int k=0;k<((Ou)clauseB).getNbEnonces();k++)
                        {
                            if(k!=j&&!temp.contains(((Ou)clauseB).getN(k)))
                            {
                                temp.add(((Ou)clauseB).getN(k));
                            }
                        }
                        if(temp.size()==1)
                        {
                            resolutions=temp.get(0);
                        }
                        else if(temp.isEmpty())
                        {
                            resolutions=new ValVerite("",false);
                        }
                        else
                        {
                            Enonce[] temp2=new Enonce[temp.size()];
                            for(int k=0;k<temp.size();k++)
                            {
                                temp2[k]=temp.get(k);
                            }
                            resolutions=new Ou(temp2);
                        }
                        
                        enonceResolu.add(resolutions);
                    }
                }
            }
            
        }
        else if(clauseA instanceof Ou&&(clauseB instanceof Symbole||clauseB instanceof Pas))
        {
            for(int i=0;i<((Ou)clauseA).getNbEnonces();i++)
            {
                Enonce a=((Ou)clauseA).getN(i);
                Enonce b=clauseB;

                if(complementaire(a,b))
                {
                    ArrayList<Enonce> temp=new ArrayList();
                    Enonce resolutions;
                    for(int j=0;j<((Ou)clauseA).getNbEnonces();j++)
                    {
                        if(j!=i&&!temp.contains(((Ou)clauseA).getN(j)))
                        {
                            temp.add(((Ou)clauseA).getN(j));
                        }
                    }
                    if(temp.size()==1)
                    {
                        resolutions=temp.get(0);
                    }
                    else if(temp.isEmpty())
                    {
                        resolutions=new ValVerite("",false);
                    }
                    else
                    {
                        Enonce[] temp2=new Enonce[temp.size()];
                        for(int j=0;j<temp.size();j++)
                        {
                            temp2[j]=temp.get(j);
                        }
                        resolutions=new Ou(temp2);
                    }

                    enonceResolu.add(resolutions);
                }
            }
        }
        else if((clauseA instanceof Symbole||clauseA instanceof Pas)&&clauseB instanceof Ou)
        {
            for(int i=0;i<((Ou)clauseB).getNbEnonces();i++)
            {
                Enonce a=clauseA;
                Enonce b=((Ou)clauseB).getN(i);

                if(complementaire(a,b))
                {
                    ArrayList<Enonce> temp=new ArrayList();
                    Enonce resolutions;
                    for(int j=0;j<((Ou)clauseB).getNbEnonces();j++)
                    {
                        if(j!=i&&!temp.contains(((Ou)clauseB).getN(j)))
                        {
                            temp.add(((Ou)clauseB).getN(j));
                        }
                    }
                    if(temp.size()==1)
                    {
                        resolutions=temp.get(0);
                    }
                    else if(temp.isEmpty())
                    {
                        resolutions=new ValVerite("",false);
                    }
                    else
                    {
                        Enonce[] temp2=new Enonce[temp.size()];
                        for(int j=0;j<temp.size();j++)
                        {
                            temp2[j]=temp.get(j);
                        }
                        resolutions=new Ou(temp2);
                    }

                    enonceResolu.add(resolutions);
                }
            }
        }
        else if((clauseA instanceof Symbole||clauseA instanceof Pas)&&(clauseB instanceof Symbole||clauseB instanceof Pas))
        {
            Enonce a=clauseA;
            Enonce b=clauseB;

            if(complementaire(a,b))
            {
                Enonce resolutions=new ValVerite("",false);

                enonceResolu.add(resolutions);
            }
        }
        return enonceResolu;
    }
    
    private boolean complementaire(Enonce a,Enonce b)
    {
        if(a instanceof Symbole&&b instanceof Pas)
        {
            if(a.equals(((Pas)b).getA()))
            {
                return true;
            }
        }
        else if(a instanceof Pas&&b instanceof Symbole)
        {
            if(b.equals(((Pas)a).getA()))
            {
                return true;
            }
        }
        return false;
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
    private Enonce revelerValVerite(Enonce cour,ArrayList<ValVerite> modele)
    {
        if(cour instanceof Enonce.Complexe)
        {
            Enonce tempEnonce=cour;
            if(cour instanceof Enonce.Complexe.N)
            {
                int nbEnonces=((Enonce.Complexe.N)cour).getNbEnonces();
                Enonce[] enonces=new Enonce[nbEnonces];
                for(int i=0;i<nbEnonces;i++)
                {
                    enonces[i]=revelerValVerite(((Enonce.Complexe.N)cour).getN(i),modele);
                }
                if(cour instanceof Et)
                {
                    tempEnonce=(Enonce)new Et(enonces);
                }
                else if(cour instanceof Ou)
                {
                    tempEnonce=(Enonce)new Ou(enonces);
                }
            }
            else if(cour instanceof Enonce.Complexe.AB)
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
    
    private void getSymboles(Enonce enonce,ArrayList<Symbole> symboles)
    {
        if(enonce instanceof Enonce.Complexe.N)
        {
            int nbEnonces=((Enonce.Complexe.N)enonce).getNbEnonces();
            for(int i=0;i<nbEnonces;i++)
            {
                getSymboles(((Enonce.Complexe.N)enonce).getN(i),symboles);
            }
        }
        else if(enonce instanceof Enonce.Complexe.AB)
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
}






