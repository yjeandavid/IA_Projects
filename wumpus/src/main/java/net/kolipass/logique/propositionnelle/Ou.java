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
import java.util.ArrayList;

public class Ou extends Enonce.Complexe.N{
    
    public Ou(Enonce ... n_)
    {
        super(n_);
    }

    public Enonce distributivite(int pos)
    {
        if(n.get(pos) instanceof Et)
        {
            Et temp=((Et)n.get(pos));
            ArrayList<Enonce> listOu=new ArrayList();
            ArrayList<Enonce> listEt=new ArrayList();
            
            for(int i=0;i<temp.getNbEnonces();i++)
            {
                listOu.add(temp.getN(i));
                for(int j=0;j<n.size();j++)
                {
                    if(j!=pos)
                    {
                        listOu.add(n.get(j));
                    }
                }
                Enonce[] temp2=new Enonce[listOu.size()];
                for(int j=0;j<listOu.size();j++)
                {
                    temp2[j]=listOu.get(j);
                }
                listEt.add(new Ou(temp2));
                listOu.clear();
            }
            Enonce[] temp2=new Enonce[listEt.size()];
            for(int j=0;j<listEt.size();j++)
            {
                temp2[j]=listEt.get(j);
            }
            return (Enonce)new Et(temp2);
        }
        return (Enonce)this;
    }

    @Override
    public Enonce simplifier()
    {
        Enonce simplification=n.get(0);
        for(int i=1;i<n.size();i++)
        {
            if(simplification instanceof ValVerite&&n.get(i) instanceof ValVerite)
            {
                simplification=(Enonce)new ValVerite("",((ValVerite)simplification).estVrai()||((ValVerite)n.get(i)).estVrai());
            }
            else if(simplification instanceof ValVerite)
            {
                if(((ValVerite)simplification).estVrai())
                {
                    simplification=(Enonce)new ValVerite("",true);
                }
                else
                {
                    simplification=n.get(i);
                }
            }
            else if(n.get(i) instanceof ValVerite)
            {
                if(((ValVerite)n.get(i)).estVrai())
                {
                    simplification=(Enonce)new ValVerite("",true);
                }
            }
            else
            {
                simplification=(Enonce)new Ou(simplification,n.get(i));
            }
        }
        return simplification;
    }
    
    @Override
    public String afficher()
    {
        String sortie="(";
        for(int i=0;i<n.size();i++)
        {
            sortie+=n.get(i).afficher();
            if(i+1<n.size())
            {
                sortie+=" || ";
            }
        }
        sortie+=")";
        return sortie;
    }
    
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Ou))
        {
            return false;
        }
        else
        {
            if(n.size()==((Ou)obj).n.size())
            {
                boolean egal=true;
                for(int i=0;i<n.size()&&egal;i++)
                {
                    egal&=n.contains(((Ou)obj).n.get(i));
                    egal&=((Ou)obj).n.contains(n.get(i));
                }
                return egal;
            }
            return false;
        }
    }
}
