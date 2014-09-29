/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
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
        boolean result = true, tempResult = false;
        EtatSokoban etat = (EtatSokoban) e;
        ArrayList<Point> blocs = (ArrayList<Point>) etat.getBlocs();
        Iterator it = blocs.iterator();
        
       for (int i = 0; i < blocsButPosition.size() && result; ++i) {
           while (it.hasNext() && !tempResult) {
               Point p = (Point) it.next();
               tempResult = p.equals(blocsButPosition.get(i));
           }
           result &= tempResult;
       }
        
        return result;
    }

    @Override
    public double estimerCoutRestant(astar.Etat e, astar.But b) {
        EtatSokoban etat = (EtatSokoban) e;
        ArrayList<Point> blocs = (ArrayList<Point>) etat.getBlocs();
        But but = (But) b;
        double cout = 0;
        
        if (blocs.size() == 1) {
            cout = Math.pow(blocs.get(0).getX()-but.blocsButPosition.get(0).getX(),2) + 
                    Math.pow(blocs.get(0).getY()-but.blocsButPosition.get(0).getY(),2);
        }
        
        return cout;
    }
    
    public List<Point> getBlocs() {
        return blocsButPosition;
    }
}
