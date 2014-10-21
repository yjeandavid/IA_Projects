/*
 * INF4230 - Intelligence artificielle
 * TP2 - Algorithme minimax avec élagage alpha-beta appliqué au jeu Connect5GUI
 *
 * (C) Éric Beaudry 2014.
 * UQAM - Département d'informatique
 */

package connect5;

/**
 *
 * @author Eric Beaudry
 */
public class Grille {

    public Grille(int nblignes, int nbcols){
        getData = new byte[nblignes][nbcols];
    }

    public Grille(int nblignes, int nbcols, int[] data1d){
        getData = new byte[nblignes][nbcols];
        for(int l=0;l<nblignes;l++)
            for(int c=0;c<nbcols;c++){
                getData[l][c] = (byte) (data1d[l*nbcols+c] + 1);
            }
    }
    public void set(int l, int c, int v){
        getData[l][c] = (byte) v;
    }
    public void set(Position p, int v){
        getData[p.ligne][p.colonne] = (byte)v;
    }
    
    public int get(int l, int c){
        return getData[l][c];
    }
    public int get(Position p){
        return getData[p.ligne][p.colonne];
    }
    
    public void reset(){
        for(byte[] b : getData)
            for(int i=0;i<b.length;i++)
                b[i] = 0;
    }
    
    public int nbLibre(){
        int n=0;
        for(byte[] b : getData)
            for(byte bb : b)
                if(bb==0)
                    n++;
        return n;
    }

    public int getSize(){
        return getData.length * getData[0].length;
    }
    
    @Override
    public String toString(){
        char[] table = {'0', 'N', 'B' };
        String result = "" + getData.length + " " + getData[0].length + "\n";
        for(byte[] b : getData){
            char[] c = new char[b.length];
            for(int i=0;i<b.length;i++)
                c[i] = table[b[i]];
            result += new String(c);
            result += '\n';
        }
        return result;
    }
    
    public byte[][] getData(){
        return getData;
    }
    
    private Grille(){
        
    }

    @Override
    public Grille clone(){
        Grille copie = new Grille(getData.length, getData[0].length);
        for(int l=0;l<getData.length;l++)
            System.arraycopy(getData[l], 0, copie.getData[l], 0, getData[l].length);
        return copie;
    }
    
    protected byte[][]     getData;
    
}
