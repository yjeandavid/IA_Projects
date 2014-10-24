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

public class JoueurArtificiel_1 implements Joueur {

    private final Random random = new Random();
    private int numJoueur = 0;

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
        //ArrayList<Integer> casesvides = new ArrayList<>();
        int nbcol = grille.getData()[0].length;
        int nblig = grille.getData().length;
        /*for (int l = 0; l < nblig; l++) {
            for (int c = 0; c < nbcol; c++) {
                if (grille.getData()[l][c] == 0) {
                    casesvides.add(l * nbcol + c);
                }
            }
        }*/
        numJoueur = (nbcol*nblig) % 2 == grille.nbLibre() % 2 ? 1 : 2;

        int choix = AlphaBetaSearch(grille);

        //int choix = random.nextInt(casesvides.size());
        //choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);
    }

    @Override
    public String getAuteurs() {
        return "Christian TÉLÉMAQUE (TELC10058803)  et  Claude-Clément YAPO (YAPC01129002)";
    }

    private int AlphaBetaSearch(Grille grille) {
        //int choix = 0;
        
        //double v = MaxValue(grille, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        EtatSuccesseur g = new EtatSuccesseur(grille);
        EtatSuccesseur v = MaxValue(g, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        return v.getAction();
    }
    
    /*private double MaxValue(Grille grille, Double alpha, Double beta) {
        GrilleVerificateur verif = new GrilleVerificateur();
        double v;
        ArrayList<Grille> etatsSuccesseurs;
        
        if (verif.determineGagnant(grille) != 0 || grille.nbLibre() == 0) {
            v = utility(grille);
        } else {
            v = Double.NEGATIVE_INFINITY;
            etatsSuccesseurs = GenererSuccesseurs(grille);
            for (Grille g : etatsSuccesseurs) {
                v = Math.max(v, MinValue(g, alpha, beta));
                if (v >= beta) {
                    break;
                }
                alpha = Math.max(alpha, v);
            }
        }
        
        return v;
    }*/
    
    private EtatSuccesseur MaxValue(EtatSuccesseur grille, Double alpha, Double beta) {
        GrilleVerificateur verif = new GrilleVerificateur();
        EtatSuccesseur gagnant = grille;
        double v;
        ArrayList<EtatSuccesseur> etatsSuccesseurs;
        
        if (verif.determineGagnant(grille.getGrille()) != 0 || grille.getGrille().nbLibre() == 0) {
            v = utility(grille.getGrille());
            grille.setUtilite(v);
        } else {
            v = Double.NEGATIVE_INFINITY;
            etatsSuccesseurs = GenererSuccesseurs(grille.getGrille());
            for (EtatSuccesseur e : etatsSuccesseurs) {
                EtatSuccesseur min = MinValue(e, alpha, beta);
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
    
    /*private double MinValue(Grille grille, Double alpha, Double beta) {
        GrilleVerificateur verif = new GrilleVerificateur();
        double v;
        ArrayList<Grille> etatsSuccesseurs;
        
        if (verif.determineGagnant(grille) != 0 || grille.nbLibre() == 0) {
            v = utility(grille);
        } else {
            v = Double.POSITIVE_INFINITY;
            etatsSuccesseurs = GenererSuccesseurs(grille);
            for (Grille g : etatsSuccesseurs) {
                v = Math.min(v, MaxValue(g, alpha, beta));
                if (v <= alpha) {
                    break;
                }
                beta = Math.min(beta, v);
            }
        }
        
        return v;
    }*/
    
    private EtatSuccesseur MinValue(EtatSuccesseur grille, Double alpha, Double beta) {
        GrilleVerificateur verif = new GrilleVerificateur();
        EtatSuccesseur gagnant = grille;
        double v;
        ArrayList<EtatSuccesseur> etatsSuccesseurs;
        
        if (verif.determineGagnant(grille.getGrille()) != 0 || grille.getGrille().nbLibre() == 0) {
            v = utility(grille.getGrille());
            grille.setUtilite(v);
        } else {
            v = Double.POSITIVE_INFINITY;
            etatsSuccesseurs = GenererSuccesseurs(grille.getGrille());
            for (EtatSuccesseur e : etatsSuccesseurs) {
                EtatSuccesseur max = MaxValue(e, alpha, beta);
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

    /*private ArrayList<Grille> GenererSuccesseurs(Grille grille) {
        ArrayList<Integer> casesvides = new ArrayList<>();
        ArrayList<Grille> successeurs = new ArrayList<>();
        int nbcol = grille.getData()[0].length;
        int nblig = grille.getData().length;
        int numJoueur;
        
        for (int l = 0; l < nblig; l++) {
            for (int c = 0; c < nbcol; c++) {
                if (grille.getData()[l][c] == 0) {
                    casesvides.add(l * nbcol + c);
                }
            }
        }
        
        numJoueur = (nbcol*nblig) % 2 == casesvides.size() % 2 ? 1 : 2;
        
        for (int i : casesvides) {
            Grille copieGrille = grille.clone();
            copieGrille.set(new Position(i / nbcol, i % nbcol), numJoueur);
            successeurs.add(copieGrille);
        }
        
        return successeurs;
    }*/
    
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
