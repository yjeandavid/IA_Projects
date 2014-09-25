/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
  
/**
 *  Représente un problème chargé d'un fichier test sokoban??.txt.
 */
public class Probleme {

    private static int calculerTailleY(ArrayList<String> lignes) {
 
    ArrayList<Integer> plusGrandTaille = new ArrayList<>();
    
    for(int i = 0; i < lignes.size(); i++)
    {
        if(plusGrandTaille.isEmpty())plusGrandTaille.add(lignes.get(i).length());
        else {
            for(int u = 0 ; u <  plusGrandTaille.size(); u++){
            if((lignes.get(i).length())< (plusGrandTaille.get(u))){
             plusGrandTaille.add(u, lignes.get(i).length());
            }
        }
            
        if((lignes.get(i).length()) > (plusGrandTaille.get(plusGrandTaille.size())))
            plusGrandTaille.add((lignes.get(i).length()));
            
        }
    }
    return plusGrandTaille.get(plusGrandTaille.size());
    }
    public Grille grille;
    public EtatSokoban etatInitial;
    public But but;
    
    
    private Probleme(){
    }
    
    public static Probleme charger(BufferedReader br) throws IOException{
        // Lire les lignes dans fichiers
        ArrayList<String> lignes = new ArrayList<String>();
        String ligne;
        

         while((ligne = br.readLine())!=null && !ligne.isEmpty()){
            lignes.add(ligne);
        }
        
        Probleme probleme = new Probleme();
        // Traiter les lignes lue. La grille a lignes.size() lignes.
        probleme.grille = new Grille();
        probleme.but = new But();
        probleme.etatInitial = new EtatSokoban();
        // À compléter...
        
        // Un espace ' ' est une case libre.
        // Un dièse '#' est une case obstacle.
        // Un dollar '$' représente la position initiale d'un bloc. ==> etatInitial.
        // Un point '.' représente la position finale d'un bloc. ==> but.
        // Les blocs sont indistinguables.
        
        // Certains grilles pourraient contenir des astérisques '*' et plus '+'. 
        // Ces symboles peuvent être ignorés et traités comme des espaces ' '.
        
        int tailleX = calculerTailleY(lignes);
        char [][] grille = new char [tailleX][lignes.size()];
        
        for(int y = 0; y < lignes.size(); y++){
            int tailleLigne = lignes.get(y).length();
            for(int x = 0; x < tailleLigne; x++){
                
                grille[x][y] = lignes.get(y).charAt(x);
            }
        }
        
        return probleme;
    }
}
