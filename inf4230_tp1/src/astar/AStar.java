/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package astar;

import chemin.ActionGoto;
import java.awt.Point;
import java.text.NumberFormat;
import java.util.*;
import sokoban.EtatSokoban;

public class AStar {

    private static int nbreEtatGenere = -1, nbreEtatVisite = -1;
    
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
                    cout = n1.g;
                    plan = new ArrayList<>();
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
        } else {
            sokoban.EtatSokoban n1 = (sokoban.EtatSokoban) n;
            
            if (n1.getBlocs().size() == 1) {
                //EtatSokoban etatBut = generateEtatFromBlockToGoal(monde, n1.getBlocs, heuristique, heuristique)
                Iterator<Action> it = actions.iterator();
            
                while (it.hasNext()) {
                    
                }

                Collection<Etat> c = successeurs.values();
                for(Etat e : c) {
                    open.add(e);
                }
            }
        }
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
    
    protected static Etat generateEtatFromBlocksToGoal(Monde m, Point p, But b, Heuristique h) {
        sokoban.Grille monde = (sokoban.Grille) m;
        Map<Double,Etat> possibilite = new TreeMap<>();
        EtatSokoban etat = new EtatSokoban();
        sokoban.But but = (sokoban.But) b;
        
        if (but.getBlocs().size() == 1) {
            Point t1 = new Point(p.x-1, p.y);
            Point t2 = new Point(p.x+1, p.y);
            if (monde.getGrille().get(t1) == null && monde.getGrille().get(t2) == null) {
                ArrayList<Point> blocs = new ArrayList<>();
                blocs.add(t1);
                etat.setBlocs(blocs);
                possibilite.put(h.estimerCoutRestant(etat, b), etat);
                blocs.remove(t1);
                etat = new EtatSokoban();
                blocs.add(t2);
                etat.setBlocs(blocs);
                possibilite.put(h.estimerCoutRestant(etat, b), etat);
            }
            t1 = new Point(p.x,p.y-1);
            t2 = new Point(p.x,p.y+1);
            if (monde.getGrille().get(t1) == null && monde.getGrille().get(t2) == null) {
                ArrayList<Point> blocs = new ArrayList<>();
                blocs.add(t1);
                etat.setBlocs(blocs);
                possibilite.put(h.estimerCoutRestant(etat, b), etat);
                blocs.remove(t1);
                etat = new EtatSokoban();
                blocs.add(t2);
                etat.setBlocs(blocs);
                possibilite.put(h.estimerCoutRestant(etat, b), etat);
            }
        }
        
        for(Map.Entry<Double, Etat> entry : possibilite.entrySet()){
            EtatSokoban etat2 = (EtatSokoban) entry.getValue();
            if (etat2.getBlocs().get(0).getX() < p.getX()) {
                etat = new EtatSokoban();
                etat.setBonhomme(new Point(p.x+1,p.y));
                etat2.getBlocs().get(0).setLocation(p.x, p.y);
                etat.setBlocs(etat2.getBlocs());
            } else if (etat2.getBlocs().get(0).getX() > p.getX()) {
                etat = new EtatSokoban();
                etat.setBonhomme(new Point(p.x-1,p.y));
                etat2.getBlocs().get(0).setLocation(p.x, p.y);
                etat.setBlocs(etat2.getBlocs());
            } else if (etat2.getBlocs().get(0).getY() > p.getY()) {
                etat = new EtatSokoban();
                etat.setBonhomme(new Point(p.x,p.y-1));
                etat2.getBlocs().get(0).setLocation(p.x, p.y);
                etat.setBlocs(etat2.getBlocs());
            } else if (etat2.getBlocs().get(0).getY() < p.getY()) {
                etat = new EtatSokoban();
                etat.setBonhomme(new Point(p.x,p.y+1));
                etat2.getBlocs().get(0).setLocation(p.x, p.y);
                etat.setBlocs(etat2.getBlocs());
            }
            break;
        }
        
        return etat;
    }
}
