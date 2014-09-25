/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package chemin;

/**
 * Représente une action de déplacement sur un tronçon de route.
 * @author Éric Beaudry
 */
public class ActionGoto extends astar.Action {
    
    public ActionGoto(double distance, Noeud prochainnoeud){
        super(distance);
        this.prochain = prochainnoeud;
    }

    /** Prochain noeud à atteindre à partir du noeud courant.
        Doit être immédiatement accessible.
    */
    protected Noeud prochain;
    
    public Noeud getProchain() {
        return prochain;
    }

    @Override
    public boolean equals(Object o) {
        ActionGoto ag = (ActionGoto) o;
        return prochain.equals(ag.prochain);
    }
   
    @Override
    public String toString(){
        return prochain.toString();
    }
    
     
}
