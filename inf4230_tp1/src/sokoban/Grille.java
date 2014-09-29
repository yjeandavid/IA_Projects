/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import java.awt.Point;
import java.io.*;
import java.util.*;

/**
 * Dans le jeu de sokoban, le «Monde» est une «Grille».
 */
public class Grille implements astar.Monde, astar.But {
    
    // À compléter.
    
    // Mettre la représentation d'une grille ici.
    // Indice : tableau pour stocker les obstacles et les buts.
    
    protected Map<Point, Character> grille;
    
    public Grille(){
        grille = new TreeMap<>();
    }
    
    public void addElement(Point p, char nom) {
        grille.put(p, nom);
    }
    
    @Override
    public List<astar.Action> getActions(astar.Etat e) {
        EtatSokoban etat = (EtatSokoban) e;
        ArrayList<astar.Action> actions = new ArrayList<>();
        Point bonhomme = etat.getBonhomme();
        ArrayList<Point> blocs = (ArrayList<Point>) etat.getBlocs();
        
        if (bonhomme.x >= 1 && bonhomme.y >= 1) {
            if (grille.get(new Point(bonhomme.x+1,bonhomme.y)) == null && !blocs.contains(new Point(bonhomme.x+1,bonhomme.y))) {
                actions.add(new ActionDeplacement("E"));
            } else if (grille.get(new Point(bonhomme.x+1,bonhomme.y)) == null && blocs.contains(new Point(bonhomme.x+1,bonhomme.y))
                && grille.get(new Point(bonhomme.x+2,bonhomme.y)) == null && !blocs.contains(new Point(bonhomme.x+2,bonhomme.y))) {
                actions.add(new ActionDeplacement("E"));
            }
            
            if (grille.get(new Point(bonhomme.x-1,bonhomme.y)) == null && !blocs.contains(new Point(bonhomme.x-1,bonhomme.y))) {
                actions.add(new ActionDeplacement("W"));
            } else if (grille.get(new Point(bonhomme.x-1,bonhomme.y)) == null && blocs.contains(new Point(bonhomme.x-1,bonhomme.y))
                && grille.get(new Point(bonhomme.x-2,1)) == null && !blocs.contains(new Point(bonhomme.x-2,1))) {
                actions.add(new ActionDeplacement("W"));
            }
            
            if (grille.get(new Point(bonhomme.x,bonhomme.y+1)) == null && !blocs.contains(new Point(bonhomme.x,bonhomme.y+1))) {
                actions.add(new ActionDeplacement("S"));
            } else if (grille.get(new Point(bonhomme.x,bonhomme.y+1)) == null && blocs.contains(new Point(bonhomme.x,bonhomme.y+1))
                    && grille.get(new Point(bonhomme.x,bonhomme.y+2)) == null && !blocs.contains(new Point(bonhomme.x,bonhomme.y+2))) {
                actions.add(new ActionDeplacement("S"));
            }
            
            if (grille.get(new Point(bonhomme.x,bonhomme.y-1)) == null && !blocs.contains(new Point(bonhomme.x,bonhomme.y-1))) {
                actions.add(new ActionDeplacement("N"));
            } else if (grille.get(new Point(bonhomme.x,bonhomme.y-1)) == null && blocs.contains(new Point(bonhomme.x,bonhomme.y-1))
                    && grille.get(new Point(bonhomme.x,bonhomme.y-2)) == null && !blocs.contains(new Point(bonhomme.x,bonhomme.y-2))) {
                actions.add(new ActionDeplacement("N"));
            }
        }
        
        return actions;
    }

    @Override
    public astar.Etat executer(astar.Etat e, astar.Action a) {
        return e;
    }
    
    /** Retourne */
    @Override
    public boolean butSatisfait(astar.Etat e){
        return false;
    }


}
