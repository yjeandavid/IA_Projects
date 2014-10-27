/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package connect5.ia;

import connect5.Grille;

/**
 *
 * @author Claude-Clément YAPO
 */
public class EtatSuccesseur {

    private Grille grille;
    private int action;
    private double utilite;
    
    public EtatSuccesseur(Grille grille) {
        action = 0;
        utilite = 0.0;
        setGrille(grille);
    }

    public Grille getGrille() {
        return grille;
    }
    
    public int getAction() {
        return action;
    }
    
    public double getUtilite() {
        return utilite;
    }
    
    private void setGrille(Grille grille) {
        this.grille = grille.clone();
    }
    
   public void setAction(int position) {
       action = position;
   }
   
   public void setUtilite(double utility) {
       utilite = utility;
   }
   
}
