/*
 * INF4230 - Intelligence artificielle
 * TP2 - Algorithme minimax avec élagage alpha-beta appliqué au jeu Connect5GUI
 *
 * (C) Éric Beaudry 2014.
 * UQAM - Département d'informatique
 */

package connect5.ia;

import connect5.Grille;

/**
 *
 * @author Éric Beaudry
 */
public class Utility {

    public int utility(Grille grille) {
        quiGagne = lastValue = 0; // reset status

        // horizontal
        for (int l = 0; l < grille.getData().length; l++) {
            for (int c = 0; c < grille.getData()[0].length; c++) {
                check(grille.getData()[l][c],l*c+c);
            }
            check(0,0);
        }

        // vertical
        for (int c = 0; c < grille.getData()[0].length; c++) {
            for (int l = 0; l < grille.getData().length; l++) {
                check(grille.getData()[l][c],l*c+l);
            }
            check(0,0);
        }

        // Diagonal \\\\\\\
        for (int c = -grille.getData().length; c < grille.getData()[0].length; c++) {
            int c2 = c;
            int l = 0;
            if (c2 < 0) {
                l = -c2;
                c2 = 0;
            }
            for (; c2 < grille.getData()[0].length && l < grille.getData().length; c2++, l++) {
                check(grille.getData()[l][c2],l*c+l);
            }
            check(0,0);
        }

        // Diagonal //////
        for (int c = -grille.getData().length; c < grille.getData()[0].length; c++) {
            int c2 = c;
            int l = grille.getData().length - 1;
            if (c2 < 0) {
                l += c2;
                c2 = 0;
            }
            for (; c2 < grille.getData()[0].length && l >= 0; c2++, l--) {
                check(grille.getData()[l][c2],l*c+l);
            }
            check(0,0);
        }

        return quiGagne;
    }

    private int  check(int value,int action) {
        if (value == lastValue) {
            count++;
            return action;
        } else {
            if (lastValue > 0 && (compteExact ? count == nombreGagnant : count >= nombreGagnant)) {
                quiGagne = action;
            }
            count = 1;
            lastValue = value;
        }
        
        return -1;
    }
    protected int nombreGagnant = 5;
    protected boolean compteExact = true;
    protected int lastValue = 0;
    protected int count = 0;
    protected int quiGagne = 0;
   
}
