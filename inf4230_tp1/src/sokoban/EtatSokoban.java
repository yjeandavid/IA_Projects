/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

package sokoban;

import astar.Etat;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un état d'un monde du jeu Sokoban.
 */

public class EtatSokoban extends Etat {

    // À compléter ...
    // - Ajoutez la représentation d'un état.
    // - Indice : positions du bonhomme et des blocs.
    private Point bonhomme;
    private List<Point> blocs; //represente l'ensemble des '$'

    
    public EtatSokoban () {
        
        this.bonhomme = new monPoint();
        this.blocs = new ArrayList<>();
        
    }

    public EtatSokoban ( Point homme) {
        
        this(homme,null);
    }
     public EtatSokoban (  List<Point> blocs) {
        
       this(null,blocs);
    }
    
    public EtatSokoban ( Point homme, List<Point> blocs) {
        
         this.bonhomme = homme;
        this.blocs = blocs;
        
    }
    
   public Point getBonhomme() {
        return bonhomme;
    }

    public List<Point> getBlocs() {
        return blocs;
    }

    public void setBonhomme(Point bonhomme) {
        this.bonhomme = bonhomme;
    }

    public void setBlocs(List<Point> blocs) {
        this.blocs = blocs;
    }
    
    @Override
    public EtatSokoban clone() {
        EtatSokoban c = new EtatSokoban();
        // À compléter : vous devez faire une copie complète de l'objet.
        c.addEtatSokoban(this);
        return c;
    }

    @Override
    public int compareTo(Etat o) {
        EtatSokoban eTmp = (EtatSokoban) o;
        // À compléter.
        // La comparaison est essentielle pour ajouter des EtatSokoban dans un TreeSet open ou close dans l'algorithme A*.
        if(this.bonhomme.equals(eTmp.getBonhomme())&&this.blocs.equals(eTmp.getBlocs())) return 0;
        
        return -1;
    }
    
    
    public void addEtatSokoban(EtatSokoban o){

        if(o.bonhomme != null){
            setBonhomme(o.getBonhomme());
        }
        if(o.blocs != null){

            for(Point eachBloc : o.blocs){
                this.blocs.add(eachBloc);
            }
        
        }
        
        
    }
    
}
