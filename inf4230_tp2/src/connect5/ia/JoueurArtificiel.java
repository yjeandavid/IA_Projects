package connect5.ia;

/*
 * Si vous utilisez Java, vous devez modifier ce fichier-ci.
 *
 * Vous pouvez ajouter d'autres classes sous le package connect5.ia.
 *
 * Christian TÉLÉMAQUE    (TELC10058803)
 * Claude-Clément YAPO    (YAPC01129002)
 */
import connect5.Grille;
import connect5.GrilleVerificateur;
import connect5.Joueur;
import connect5.Position;
import java.util.ArrayList;
import java.util.Random;

public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();
    private int numJoueur = 0;
    private int delai = 0;
    private long begin = 0;
    private boolean go = true;
    private int duree = 0;

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
        int nbcol = grille.getData()[0].length;
        int nblig = grille.getData().length;
        int profondeur = 1;
        int choix = 0;
        duree = 0;
        long start, stop;
        delai = delais;
        go = true;

        numJoueur = (nbcol*nblig) % 2 == grille.nbLibre() % 2 ? 1 : 2;
        
        start = System.currentTimeMillis();
        begin = start;
        try {
            choix = AlphaBetaSearch(grille, profondeur);
        } catch(Exception e) {
            go = false;
        }
        stop = System.currentTimeMillis();
        int temp = (int) (stop - start);
        duree += temp;
        while (go) {
            ++profondeur;
            start = System.currentTimeMillis();
            try {
                choix = AlphaBetaSearch(grille, profondeur);
                stop = System.currentTimeMillis();
                temp = (int) (stop - start);
                duree += temp;
            } catch(Exception e) {
                go = false;
            }
        }

        return new Position(choix / nbcol, choix % nbcol);
    }

    @Override
    public String getAuteurs() {
        return "Christian TÉLÉMAQUE (TELC10058803)  et  Claude-Clément YAPO (YAPC01129002)";
    }

    private int AlphaBetaSearch(Grille grille, int profondeur) throws Exception {
        EtatSuccesseur g = new EtatSuccesseur(grille);
        EtatSuccesseur v = MaxValue(g, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, profondeur);
        
        return v.getAction();
    }
    
    private EtatSuccesseur MaxValue(EtatSuccesseur grille, Double alpha, Double beta, int profondeur) throws Exception {
        GrilleVerificateur verif = new GrilleVerificateur();
        EtatSuccesseur gagnant = grille;
        double v;
        ArrayList<EtatSuccesseur> etatsSuccesseurs;
        long stop = System.currentTimeMillis();
        int rest = (int) (stop - begin);
        
        if (rest > delai)
            throw new Exception();
        
        if (verif.determineGagnant(grille.getGrille()) != 0 || grille.getGrille().nbLibre() == 0) {
            v = utility(grille.getGrille());
            grille.setUtilite(v);
        } else if (profondeur == 0) {
            v = eval(grille.getGrille());
            grille.setUtilite(v);
        } else {
            v = Double.NEGATIVE_INFINITY;
            etatsSuccesseurs = GenererSuccesseurs(grille.getGrille());
            for (EtatSuccesseur e : etatsSuccesseurs) {
                EtatSuccesseur min = MinValue(e, alpha, beta, profondeur-1);
                e.setUtilite(min.getUtilite());
                v = Math.max(v, min.getUtilite());
                if (v >= beta) {
                    break;
                }   
                alpha = Math.max(alpha, v);
            }
             for (EtatSuccesseur e : etatsSuccesseurs) {
                if (v == e.getUtilite()) {
                    gagnant = e;
                    break;
                }
            }
        }
        
        return gagnant;
    }
    
    private EtatSuccesseur MinValue(EtatSuccesseur grille, Double alpha, Double beta, int profondeur) throws Exception {
        GrilleVerificateur verif = new GrilleVerificateur();
        EtatSuccesseur gagnant = grille;
        double v;
        ArrayList<EtatSuccesseur> etatsSuccesseurs;
        long stop = System.currentTimeMillis();
        int rest = (int) (stop - begin);
        
        if (rest > delai)
            throw new Exception();
        
        if (verif.determineGagnant(grille.getGrille()) != 0 || grille.getGrille().nbLibre() == 0) {
            v = utility(grille.getGrille());
            grille.setUtilite(v);
        } else if (profondeur == 0) {
            v = eval(grille.getGrille());
            grille.setUtilite(v);
        } else {
            v = Double.POSITIVE_INFINITY;
            etatsSuccesseurs = GenererSuccesseurs(grille.getGrille());
            for (EtatSuccesseur e : etatsSuccesseurs) {
                EtatSuccesseur max = MaxValue(e, alpha, beta, profondeur-1);
                e.setUtilite(max.getUtilite());
                v = Math.min(v, max.getUtilite());
                if (v <= alpha) {
                    break;
                }
                beta = Math.min(beta, v);
            }
            for (EtatSuccesseur e : etatsSuccesseurs) {
                if (v == e.getUtilite()) {
                    gagnant = e;
                    break;
                }
            }
        }
        
        return gagnant;
    }
    
    private ArrayList<EtatSuccesseur> GenererSuccesseurs(Grille grille) {
        ArrayList<Integer> casesvides = new ArrayList<>();
        ArrayList<EtatSuccesseur> successeurs = new ArrayList<>();
        int nbcol = grille.getData()[0].length;
        int nblig = grille.getData().length;
        int idJoueur;
        
        for (int l = 0; l < nblig; l++) {
            for (int c = 0; c < nbcol; c++) {
                if (grille.getData()[l][c] == 0) {
                    casesvides.add(l * nbcol + c);
                }
            }
        }
        
        idJoueur = (nbcol*nblig) % 2 == casesvides.size() % 2 ? 1 : 2;
        
        for (int i : casesvides) {
            Grille copieGrille = grille.clone();
            copieGrille.set(new Position(i / nbcol, i % nbcol), idJoueur);
            EtatSuccesseur e = new EtatSuccesseur(copieGrille);
            e.setAction(i);
            successeurs.add(e);
        }
        
        return successeurs;
    }
    
    private double eval(Grille grille) {
        int Maxligne = 0, Maxcol = 0, Maxdiag = 0;
        int Minligne = 0, Mincol = 0, Mindiag = 0;
        int nbIdDiag = 0, nbOppo = 0;
        int nbcol = grille.getData()[0].length;
        int lignes = grille.getData().length;
        byte[][] donne = grille.getData();
        
        // horizontal
        for (int l = 0; l < lignes; l++) {
            int evalLigneMax = 0;
            int evalLigneMin = 0;
            for (int c = 0; c < nbcol; c++) {
                evalLigneMax = (donne[l][c] == 0 || donne[l][c] == numJoueur) ? ++evalLigneMax : 0;
                evalLigneMin = (donne[l][c] == 0 || donne[l][c] != numJoueur) ? ++evalLigneMin : 0;
                Maxligne = evalLigneMax == 5 ? ++Maxligne : Maxligne;
                Minligne = evalLigneMin == 5 ? ++Minligne : Minligne;

                c = ((evalLigneMax >= 5) && (evalLigneMin >= 5)) ? nbcol : c;
            }
        }
        
        // vertical
        for (int c = 0; c < nbcol; c++) {
            int evalcolonneMax = 0;
            int evalcolonneMin = 0;
            for (int l = 0; l < lignes; l++) {
                evalcolonneMax = (donne[l][c] == 0 || donne[l][c] == numJoueur) ? ++evalcolonneMax : 0;
                evalcolonneMin = (donne[l][c] == 0 || donne[l][c] != numJoueur) ? ++evalcolonneMin : 0;
                Maxcol = (evalcolonneMax == 5) ? ++Maxcol : Maxcol;
                Mincol = (evalcolonneMin == 5) ? ++Mincol : Mincol;

                l = ((evalcolonneMax >= 5) && (evalcolonneMin >= 5)) ? lignes : l;
            }
        }
        
        // Diagonal \\\\\\\
        for (int c = -lignes; c < nbcol; c++) {
            int c2 = c;
            nbIdDiag = nbOppo = 0;
            int l = 0;
            if (c2 < 0) {
                l = -c2;
                c2 = 0;
            }
            for (; c2 < nbcol && l < lignes; c2++, l++) {
                nbOppo = (donne[l][c2] != numJoueur || donne[l][c2] == 0) ? ++nbOppo : 0;
                nbIdDiag = (donne[l][c2] == numJoueur || donne[l][c2] == 0) ? ++nbIdDiag : 0;
                Maxdiag = nbIdDiag == 5 ? ++Maxdiag : Maxdiag;
                Mindiag = nbOppo == 5 ? ++Mindiag : Mindiag;
                
                c2 = ((nbIdDiag >= 5) && (nbOppo >= 5)) ? nbcol : c2;
            }
            
        }
        
        
        // Diagonal //////
        for (int c = -lignes; c < nbcol; c++) {
            int c2 = c;
            nbIdDiag = nbOppo = 0;
            int l = lignes - 1;
            if (c2 < 0) {
                l += c2;
                c2 = 0;
            }
            for (; c2 < nbcol && l >= 0; c2++, l--) {
                nbOppo = (donne[l][c2] != numJoueur || donne[l][c2] == 0) ? ++nbOppo : 0;
                nbIdDiag = (donne[l][c2] == numJoueur || donne[l][c2] == 0) ? ++nbIdDiag : 0;
                Maxdiag = nbIdDiag == 5 ? ++Maxdiag : Maxdiag;
                Mindiag = nbOppo == 5 ? ++Mindiag : Mindiag;
                
                c2 = ((nbIdDiag >= 5) && (nbOppo >= 5)) ? nbcol : c2; 
            }
            
        }
        
        
        return (double) ((Maxligne + Maxcol + Maxdiag) - (Minligne + Mincol + Mindiag));
    }

    private double utility(Grille grille) {
        GrilleVerificateur verif = new GrilleVerificateur();
        int gagnant = verif.determineGagnant(grille);
        double poids;
        
        if (gagnant != numJoueur) {
            poids = (-1.0);
        } else if (gagnant == 0) {
            poids = 0.0;
        } else {
            poids = 1.0;
        }
        
        return poids;
    }
}
