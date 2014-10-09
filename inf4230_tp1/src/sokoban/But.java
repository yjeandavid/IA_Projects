/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un but.
 */
public class But implements astar.But, astar.Heuristique {

    // À compléter.
    // Indice : les destinations des blocs.
    
    protected List<Point> blocsButPosition;
    
    public But() {
        blocsButPosition = new ArrayList<>();
    }
    
    public void addBut(Point p) {
        blocsButPosition.add(p);
    }
    
    @Override
    public boolean butSatisfait(astar.Etat e) {
        boolean result = true;
        EtatSokoban etat = (EtatSokoban) e;
        ArrayList<Point> blocs = (ArrayList<Point>) etat.getBlocs();
        
       for (int i = 0; i < blocsButPosition.size(); ++i) {
           Point p = blocs.get(i);
           result &= p.equals(blocsButPosition.get(i));
       }
        
        return result;
    }

    @Override
    public double estimerCoutRestant(astar.Etat e, astar.But b) {
        EtatSokoban etat = (EtatSokoban) e;
        ArrayList<Point> blocs = (ArrayList<Point>) etat.getBlocs();
        Point bonhomme = etat.getBonhomme();
        
        double cout = 0, min = Double.POSITIVE_INFINITY;
        
        for (int i = 0; i < blocs.size(); ++i) {
            double distanceBonhomme = Math.sqrt( Math.pow(bonhomme.getX()-blocs.get(i).getX(), 2) +
                                                Math.pow(bonhomme.getY()-blocs.get(i).getY(), 2) );
            if (distanceBonhomme < min)
                min = distanceBonhomme;
            
            cout += Math.sqrt( Math.pow(blocs.get(i).getX()-blocsButPosition.get(i).getX(), 2) +
                              Math.pow(blocs.get(i).getY()-blocsButPosition.get(i).getY(), 2) );
        }
        //cout /= blocs.size();
        cout += min;
        
        return cout;
    }
    
    public List<Point> getBlocs() {
        return blocsButPosition;
    }
}
