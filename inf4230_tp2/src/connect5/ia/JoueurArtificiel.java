package connect5.ia;

/*
 * Si vous utilisez Java, vous devez modifier ce fichier-ci.
 *
 * Vous pouvez ajouter d'autres classes sous le package connect5.ia.
 *
 * Christian Telemaque    (TELC10058803)
 * Claude-Clément YAPO    (YAPC01129002)
 */
import connect5.Grille;
import connect5.GrilleVerificateur;
import connect5.Joueur;
import connect5.Position;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();
    private static int nbrJouer = 0;
    private static int id = 0;
    
    
    
    
    public JoueurArtificiel(){
        ++nbrJouer;
        id = nbrJouer == 1? 1: nbrJouer == 2 ? 2 : -1;
    }
            
            
    /**
     * Voici la fonction à modifier. Évidemment, vous pouvez ajouter d'autres
     * fonctions dans JoueurArtificiel. Vous pouvez aussi ajouter d'autres
     * classes, mais elles doivent être ajoutées dans le package connect5.ia.
     * Vous de pouvez pas modifier les fichiers directement dans connect., car
     * ils seront écrasés.
     *
     * @param grille Grille reçu (état courrant). Il faut ajouter le prochain
     * coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */
    @Override
    public Position getProchainCoup(Grille grille, int delais) {
        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        int nbcol = grille.getData()[0].length;
        for (int l = 0; l < grille.getData().length; l++) {
            for (int c = 0; c < nbcol; c++) {
                if (grille.getData()[l][c] == 0) {
                    casesvides.add(l * nbcol + c);
                }
            }
        }

        int choix = Minimax(grille,casesvides);

        //int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);
    }

    @Override
    public String getAuteurs() {
        return "Prénom1 Nom1 (TELC10058803)  et  Claude-Clément YAPO (YAPC01129002)";
    }

    public int Minimax(Grille grille,ArrayList<Integer> casesvides) {

        double max = Double.NEGATIVE_INFINITY;
        int tmp, maxI = -1, maxJ = -1;
        Grille cloneGrille = grille.clone();
        byte[][] data = cloneGrille.getData();
        int lignes = data.length;
        int colones = data[0].length;

        Vector succ = create_successeur(cloneGrille,casesvides);
        
        
        return MinimaxDecision(cloneGrille,succ);
 
    }

    int Min(Grille grille, Vector succ) {
        
        GrilleVerificateur terminate_test = new GrilleVerificateur();
        int gagnant = terminate_test.determineGagnant(grille);
        double  min = Double.POSITIVE_INFINITY;
        
        if( gagnant == 1 || gagnant == 2)    
            return Utilite(grille,succ);
        
        Vector tmpSucc = succ;
        
        for(Object g : succ){
            int t = Min((Grille) g,succ);
            if(t < min) min = t;
        }
        return (int)min;

    }

    int Max(Grille grille, Vector succ) {
        
        GrilleVerificateur terminate_test = new GrilleVerificateur();
        int gagnant = terminate_test.determineGagnant(grille);
        double  max = Double.NEGATIVE_INFINITY;
        
        
        if( gagnant == 1 || gagnant == 2)    
            return Utilite(grille,succ);
        
        Vector tmpSucc = succ;
        
        for(Object g : succ){
            int t = Min((Grille) g,succ);
            if(t > max) max = t;
        }
        return (int)max;

    }

    int eval(byte[][] data) {
        int nb_de_pions = 0;
        int lignes = data.length;//optimiser en passant en param
        int colones = data[0].length;// optimiser en passant en param
        GrilleVerificateur evaluation = new GrilleVerificateur();
        Grille g = new Grille(lignes, colones);
        int vainqueur = 0;

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colones; j++) {
                g.set(i, j, data[i][j]);
                if (data[i][j] != 0) {
                    nb_de_pions++;
                }
            }
        }

        if ((vainqueur = evaluation.determineGagnant(g)) != 0) {
            if (vainqueur == 1) {
                return 1000 - nb_de_pions;
            } else if (vainqueur == 2) {
                return -1000 + nb_de_pions;
            } else {
                return 0;
            }
        }

        return 0;

    }

    private Vector create_successeur(Grille cloneGrille,ArrayList<Integer> casesvides) {

    Vector chd = new Vector();
    Grille tmpGrille = cloneGrille.clone();
    int nbcol = cloneGrille.getData()[0].length;
    int lignes = cloneGrille.getData().length;
    
    
    for(int i : casesvides){
        tmpGrille.getData()[i%lignes][i/nbcol] = (byte) (id == 1 ? 1 : id ==2  ? 2 : -1);
        chd.add(tmpGrille);
    }
    
    
    
     return chd;
    }

    private int MinimaxDecision(Grille cloneGrille, Vector succ) {

     int action = -1;

     if (id == 1)
            action = Max(cloneGrille,succ);
        else action = Min(cloneGrille,succ);
    
    
    return action;
    
    }

    private int Utilite(Grille grille, Vector succ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
