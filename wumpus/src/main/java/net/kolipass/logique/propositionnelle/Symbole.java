/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.kolipass.logique.propositionnelle;

import java.util.Objects;

/**
 *
 * @author Gabriel
 */
//Symboles
//Exemple: P|Q|R
public class Symbole implements Enonce.Atomique{
    private String nom;

    public ValVerite reveler(boolean valeur)
    {
        return new ValVerite(nom,valeur);
    }
    
    @Override
    public String afficher()
    {
        return nom;
    }

    public Symbole(String nom_)
    {
        nom=nom_;
    }

    public String getNom()
    {
        return nom;
    } 

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Symbole))
        {
            return false;
        }
        else
        {
            return nom.equals(((Symbole)obj).nom);
        }
    }
}
