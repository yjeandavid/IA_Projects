/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package astar;

import chemin.ActionGoto;
import java.text.NumberFormat;
import java.util.*;

public class AStar {

    private static int nbreEtatGenere = 0, nbreEtatVisite = 0;
    
    public static List<Action> genererPlan(Monde monde, Etat etatInitial, But but, Heuristique heuristique){
        long starttime = System.currentTimeMillis();
        ArrayList<Etat> open, close;
        List<Action> plan = null;
        double cout = Double.POSITIVE_INFINITY;
        
        open = new ArrayList<>();
        close = new ArrayList<>();
        
        etatInitial.g = 0;
        etatInitial.h = heuristique.estimerCoutRestant(etatInitial, but);
        etatInitial.f = etatInitial.g + etatInitial.h;
        
        
        open.add(etatInitial);
        ++nbreEtatGenere;
        while (true) {
            if (!open.isEmpty()) {
                Etat n1 = open.get(0);
                open.remove(n1);
                close.add(n1);
                ++nbreEtatVisite;
                if (but.butSatisfait(n1)) {
                    plan = new ArrayList<>();
                    cout = n1.g;
                    while(n1.parent!= null)
                    {
                        chemin.Etat fromto = (chemin.Etat) n1;
                        chemin.Noeud temp = fromto.getEmplacement();
                        plan.add(0,new ActionGoto(0.0,temp));
                        n1 = n1.parent;
                    }
                    
                    break;
                }
                genererSuccesseur(monde,n1,heuristique, open, close);
                open = trier(open);
            } else {
                break;
            }
       
        }
        

        long lastDuration = System.currentTimeMillis() - starttime;
        // Les lignes écrites débutant par un dièse '#' seront ignorées par le valideur de solution.
        System.out.println("# Nombre d'états générés : " + nbreEtatGenere);
        System.out.println("# Nombre d'états visités : " + nbreEtatVisite);
        System.out.println("# Durée : " + lastDuration + " ms");
        System.out.println("# Coût : " + nf.format(cout));
        return plan;
    }
    
    static final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    static {
        nf.setMaximumFractionDigits(1);
    }
    
    protected static void genererSuccesseur(Monde monde,Etat n, Heuristique heurisitique, ArrayList<Etat> open, ArrayList<Etat> close) {
        Map<Double, Etat> successeurs = new TreeMap<>();
        ArrayList<Action> actions = (ArrayList<Action>) monde.getActions(n);

        if (n.getClass() == chemin.Etat.class) {
            chemin.Etat n1 = (chemin.Etat) n;
            Iterator<Action> it = actions.iterator();
            
            while (it.hasNext()) {
                chemin.ActionGoto ag = (chemin.ActionGoto) it.next();
                chemin.Etat n2 = new chemin.Etat(ag.getProchain());
                n2.g = n1.g + ag.cout;
                n2.h = heurisitique.estimerCoutRestant(n2, null);
                n2.f = n2.g + n2.h;
                n2.parent = n1;
                
                if (open.contains(n2)) {
                    chemin.Etat n3 = (chemin.Etat) getEtatIn(open, n2);
                    if (n2.f < n3.f) {
                        open.remove(n3);
                        successeurs.put(n2.f, n2);
                        ++nbreEtatGenere;
                    }
                } else if (close.contains(n2)) {
                    chemin.Etat n3 = (chemin.Etat) getEtatIn(close, n2);
                    if (n2.f < n3.f) {
                        open.remove(n3);
                        successeurs.put(n2.f, n2);
                        ++nbreEtatGenere;
                    }
                } else {
                    successeurs.put(n2.f, n2);
                    ++nbreEtatGenere;
                }
            }
            
            Collection<Etat> c = successeurs.values();
            for(Etat e : c) {
                open.add(e);
            }
        }
    }
    
    protected static Etat getEtatIn(ArrayList<Etat> search, Etat filter) {
        Iterator<Etat> it = search.iterator();
        Etat n = null;
        
        while (it.hasNext()) {
            n = it.next();
            if (n.equals(filter))
                break;
        }
        
        return n;
    }
    
    protected static ArrayList<Etat> trier(ArrayList<Etat> treeToSort) {
        
        ArrayList<Etat> result = new ArrayList<>();
        Iterator<Etat> it = treeToSort.iterator();
        
        Etat element;
        
        while (it.hasNext()) {
            element = it.next();
            if(result.isEmpty())result.add(element);
            else
            {
              for(int i = 0;i < result.size();i++)
              {
                  if(element.f< result.get(i).f){
                      
                      result.add(i, element);
                   break;   
                  }
              }
              if(element.f>(result.get((result.size()-1)).f))result.add(element);
            }
                
        }
        
        return result;
    }

   
}
