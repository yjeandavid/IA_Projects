/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logic;

/**
 *
 * @author Gabriel
 */

import java.util.ArrayList;

public class KBPropositionnelle {
    
    //Syntaxe: page 259 du livre
    public interface Enonce {}
    
    //Syntaxe: page 259 du livre
    public class EnonceComplexe implements Enonce{
        private Enonce a;
        private Enonce b;
        
        public Enonce getA()
        {
            return a;
        }
        
        public Enonce getB()
        {
            return b;
        }
        
        public void biconditionnel()
        {
            if(a instanceof DblImpl)
            {
                a=((DblImpl)a).biconditionnel2();
            }
        }
        public void elimineImpl()
        {
            if(a instanceof Impl)
            {
                a=((Impl)a).elimineImpl2();
            }
        }
        public void commutativite()
        {
            if(a instanceof Ou)
            {
                a=((Ou)a).commutativite2();
            }
            else if(a instanceof Et)
            {
                a=((Et)a).commutativite2();
            }
        }

        public void distributivite()
        {
            if(a instanceof Ou)
            {
                a=((Ou)a).distributivite2();
            }
            else if(a instanceof Et)
            {
                a=((Et)a).distributivite2();
            }
        }
        
        public void dblNega()
        {
            if(a instanceof Pas)
            {
                a=((Pas)a).dblNega2();
            }
        }

        public void deMorgan()
        {
            if(a instanceof Pas)
            {
                a=((Pas)a).deMorgan2();
            }
        }
        
        public void reveler(boolean valeur)
        {
            if(a instanceof Symbole)
            {
                a=((Symbole)a).reveler(valeur);
            }
        }
        
        public void simplifier()
        {
            if(a instanceof EnonceComplexe)
            {
                ((EnonceComplexe)a).simplifier2();
            }
        }
        
        protected Enonce simplifier2()
        {
            return (Enonce)this;
        }
    }
    
    //Syntaxe: page 259 du livre
    public interface EnonceAtomique extends Enonce{}
    
    //Double implication logique
    public class DblImpl extends EnonceComplexe{
        private Enonce a;
        private Enonce b;
        
        private Enonce biconditionnel2()
        {
            return (Enonce)new Et((Enonce)new Impl(a,b),(Enonce)new Impl(a,b));
        }
        
        @Override
        protected Enonce simplifier2()
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
        
        public DblImpl(Enonce a_,Enonce b_)
        {
            a=a_;
            b=b_;
        }

        @Override
        public Enonce getA()
        {
            return a;
        }

        @Override
        public Enonce getB()
        {
            return b;
        }
    }
    
    //Implication logique
    public class Impl extends EnonceComplexe{
        private Enonce a;
        private Enonce b;
        
        private Enonce elimineImpl2()
        {
            return (Enonce)new Ou((Enonce)new Pas(a),b);
        }

        //À compléter
        /*public Enonce contraposition()
        {

        }*/
        
        @Override
        protected Enonce simplifier2()
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
        
        public Impl(Enonce a_,Enonce b_)
        {
            a=a_;
            b=b_;
        }

        @Override
        public Enonce getA()
        {
            return a;
        }

        @Override
        public Enonce getB()
        {
            return b;
        }
    }
    
    //Ou logique
    public class Ou extends EnonceComplexe{
        private Enonce a;
        private Enonce b;

        private Enonce commutativite2()
        {
            return (Enonce)new Ou(b,a);
        }

        private Enonce distributivite2()
        {
            if(b instanceof Et)
            {
                return (Enonce)new Et((Enonce)new Ou(a,((Et)b).getA()),(Enonce)new Ou(a,((Et)b).getB()));
            }
            return (Enonce)this;
        }
        
        @Override
        protected Enonce simplifier2()
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
        
        public Ou(Enonce a_,Enonce b_)
        {
            a=a_;
            b=b_;
        }
        
        @Override
        public Enonce getA()
        {
            return a;
        }

        @Override
        public Enonce getB()
        {
            return b;
        }
    }

    //Et logique
    public class Et extends EnonceComplexe{
        private Enonce a;
        private Enonce b;

        private Enonce commutativite2()
        {
            return (Enonce)new Et(b,a);
        }

        private Enonce distributivite2()
        {
            if(b instanceof Ou)
            {
                return (Enonce)new Ou((Enonce)new Et(a,((Ou)b).getA()),(Enonce)new Et(a,((Ou)b).getB()));
            }
            return (Enonce)this;
        }
        
        @Override
        protected Enonce simplifier2()
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
        
        public Et(Enonce a_,Enonce b_)
        {
            a=a_;
            b=b_;
        }

        @Override
        public Enonce getA()
        {
            return a;
        }

        @Override
        public Enonce getB()
        {
            return b;
        }
    }

    //Négation logique
    public class Pas extends EnonceComplexe{
        private Enonce a;
        
        private Enonce deMorgan2()
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
        
        private Enonce dblNega2()
        {
            if(a instanceof Pas)
            {
                return ((Pas)a).getA();
            }
            return (Enonce)this;
        }
        
        @Override
        protected Enonce simplifier2()
        {
            if(a instanceof ValVerite)
            {
                return (Enonce)new ValVerite(((ValVerite)a).getNom(),!((ValVerite)a).estVrai());
            }
            return (Enonce)this;
        }
        
        public Pas(Enonce a_)
        {
            a=a_;
        }
        
        @Override
        public Enonce getA()
        {
            return a;
        }

        @Override
        public Enonce getB()
        {
            return a;
        }
    }
    
    //Symboles
    //Exemple: P|Q|R
    public class Symbole implements EnonceAtomique{
        private String nom;

        private ValVerite reveler(boolean valeur)
        {
            return new ValVerite(nom,valeur);
        }
        
        public Symbole(String nom_)
        {
            nom=nom_;
        }

        public String getNom()
        {
            return nom;
        } 
        
        public boolean equals(Symbole symbole){
            return nom.equals(symbole.nom);
        }
    }
    
    //Valeur de vérité
    //Exemple: Vrai|Faux
    public class ValVerite implements EnonceAtomique{
        private boolean valeur;
        private String nom;
        
        public ValVerite(String nom_,boolean valeur_)
        {
            nom=nom_;
            valeur=valeur_;
        }
        
        public String getNom()
        {
            return nom;
        }

        public boolean estVrai()
        {
            return valeur;
        }
    }
    
    private ArrayList<Enonce> KB;
    
    public boolean demander(ArrayList<Enonce> a)
    {
        return TTEntails(KB,a);
    }
    
    public void raconter(Enonce a)
    {
        KB.add(a);
    }
    
    private void getSymboles(Enonce enonce,ArrayList<Symbole> symboles)
    {
        if(enonce instanceof EnonceComplexe)
        {
            getSymboles(((EnonceComplexe)enonce).getA(),symboles);
            getSymboles(((EnonceComplexe)enonce).getB(),symboles);
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
    private boolean modeleValide(ArrayList<Enonce> KB,ArrayList<ValVerite> modele)
    {
        /*for(int i=0;i<KB.size();i++)
        {
            
        }*/
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
            
            return TTCheckAll(KB,a,reste,modeleVrai)&&
                TTCheckAll(KB,a,reste,modeleFaux);
        }
    }
}






