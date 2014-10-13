package connect5.ia;

/*
 * Si vous utilisez Java, vous devez modifier ce fichier-ci.
 *
 * Vous pouvez ajouter d'autres classes sous le package connect5.ia.
 *
 * Prénom Nom    (CODE00000001)
 * Prénom Nom    (CODE00000002)
 */
import connect5.Grille;
import connect5.GrilleVerificateur;
import connect5.Joueur;
import connect5.Position;
import java.util.ArrayList;
import java.util.Random;

public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();

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

        int choix = JoeurIA(grille, casesvides.size());

        //int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);
    }

    @Override
    public String getAuteurs() {
        return "Prénom1 Nom1 (CODE00000001)  et  Prénom2 Nom2 (CODE00000002)";
    }

    public int JoeurIA(Grille grille, int profondeur) {

        int max = -10000;
        int tmp, maxI = -1, maxJ = -1;
        byte[][] data = grille.getData();
        int lignes = data.length;
        int colones = data[0].length;

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colones; j++) {
                if (data[i][j] == 0) {
                    data[i][j] = 2;
                    tmp = Min(data, profondeur - 1);

                    if (tmp > max) {
                        max = tmp;
                        maxI = i;
                        maxJ = j;
                    }
                    data[i][j] = 0;
                }
            }
        }

        data[maxI][maxJ] = 1;
        
        System.out.println("--------------------------"+maxI * colones + maxJ);
        return maxI * colones + maxJ;
    }

    int Min(byte[][] data, int profondeur) {
        if (profondeur == 0) {
            return eval(data);
        }

        int min = 10000;
        int tmp;
        int lignes = data.length;//optimiser en passant en param
        int colones = data[0].length;// optimiser en passant en param

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colones; j++) {
                if (data[i][j] == 0) {
                    data[i][j] = 1;
                    tmp = Max(data, profondeur - 1);

                    if (tmp < min) {
                        min = tmp;
                    }
                    data[i][j] = 0;
                }
            }
        }

        return min;

    }

    int Max(byte[][] data, int profondeur) {
        if (profondeur == 0) {
            return eval(data);
        }

        int max = -10000;
        int tmp;
        int lignes = data.length;//optimiser en passant en param
        int colones = data[0].length;// optimiser en passant en param

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colones; j++) {
                if (data[i][j] == 0) {
                    data[i][j] = 2;
                    tmp = Min(data, profondeur - 1);

                    if (tmp > max) {
                        max = tmp;
                    }
                    data[i][j] = 0;
                }
            }
        }

        return max;

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

}
