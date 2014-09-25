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
        return false;
    }

    @Override
    public double estimerCoutRestant(astar.Etat e, astar.But b) {
        return 0;
    }
    
}
