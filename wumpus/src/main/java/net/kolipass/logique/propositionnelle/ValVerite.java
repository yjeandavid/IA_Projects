/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique.propositionnelle;

/**
 *
 * @author Gabriel
 */
//Valeur de vérité
//Exemple: Vrai|Faux
public class ValVerite implements Enonce.Atomique{
    private boolean valeur;
    private String nom;

    @Override
    public String afficher()
    {
        if(valeur)
        {
            return "VRAI";
        }
        else
        {
            return "FAUX";
        }
    }
    
    public ValVerite(String nom_,boolean valeur_)
    {
        nom=nom_;
        valeur=valeur_;
    }

    public String getNom()
    {
        return nom;
    }

    public boolean estVrai()
    {
        return valeur;
    }
}
