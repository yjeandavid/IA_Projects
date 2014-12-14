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

import java.util.ArrayList;

public class Et extends Enonce.Complexe.N{
        
    public Et(Enonce... n_)
    {
        super(n_);
    }

    public Enonce distributivite(int pos)
    {
        if(n.get(pos) instanceof Ou)
        {
            Ou temp=((Ou)n.get(pos));
            ArrayList<Enonce> listEt=new ArrayList();
            ArrayList<Enonce> listOu=new ArrayList();
            
            for(int i=0;i<temp.getNbEnonces();i++)
            {
                listEt.add(temp.getN(i));
                for(int j=0;j<n.size();j++)
                {
                    if(j!=pos)
                    {
                        listEt.add(n.get(j));
                    }
                }
                Enonce[] temp2=new Enonce[listEt.size()];
                for(int j=0;j<listEt.size();j++)
                {
                    temp2[j]=listEt.get(j);
                }
                listOu.add(new Et(temp2));
                listEt.clear();
            }
            Enonce[] temp2=new Enonce[listOu.size()];
            for(int j=0;j<listOu.size();j++)
            {
                temp2[j]=listOu.get(j);
            }
            return (Enonce)new Ou(temp2);
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
                simplification=(Enonce)new ValVerite("",((ValVerite)simplification).estVrai()&&((ValVerite)n.get(i)).estVrai());
            }
            else if(simplification instanceof ValVerite)
            {
                if(((ValVerite)simplification).estVrai())
                {
                    simplification=n.get(i);
                }
                else
                {
                    simplification=(Enonce)new ValVerite("",false);
                }
            }
            else if(n.get(i) instanceof ValVerite)
            {
                if(!((ValVerite)n.get(i)).estVrai())
                {
                    simplification=(Enonce)new ValVerite("",false);
                }
            }
            else
            {
                simplification=(Enonce)new Et(simplification,n.get(i));
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
                sortie+=" && ";
            }
        }
        sortie+=")";
        return sortie;
    }
    
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Et))
        {
            return false;
        }
        else
        {
            if(n.size()==((Et)obj).n.size())
            {
                boolean egal=true;
                for(int i=0;i<n.size()&&egal;i++)
                {
                    egal&=n.contains(((Et)obj).n.get(i));
                    egal&=((Et)obj).n.contains(n.get(i));
                }
                return egal;
            }
            return false;
        }
    }
}
