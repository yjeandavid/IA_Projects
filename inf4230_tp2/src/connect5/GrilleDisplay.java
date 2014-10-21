/*
 * INF4230 - Intelligence artificielle
 * TP2 - Algorithme minimax avec élagage alpha-beta appliqué au jeu Connect5GUI
 *
 * (C) Éric Beaudry 2014.
 * UQAM - Département d'informatique
 */

package connect5;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author Eric Beaudry
 */
public class GrilleDisplay extends JPanel {
    
    protected Grille grille = new Grille(8, 8);
    
    private static final int tailleCase = 30;
    
    public GrilleDisplay(){
        setBackground(Color.white);
        setMinimumSize(new Dimension(200, 200));
        setPreferredSize(new Dimension(200, 200));
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    void setGrille(Grille g){
        grille = g;
        Dimension d = new Dimension(grille.getData[0].length * tailleCase,
                                    grille.getData.length * tailleCase);
        setMinimumSize(d);
        setPreferredSize(d);
        repaint();
    }
    
    public void setCaseListener(GrilleDisplayListener l){
        listener = l;
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if(e.getID() == MouseEvent.MOUSE_PRESSED){
            if(listener!=null && grille!=null){
                int l = e.getY() / tailleCase;
                int c = e.getX() / tailleCase;
                
                if(l<grille.getData.length && c<grille.getData[0].length)
                    listener.caseClicked(l, c);
            }
        }
    }
    
    public GrilleDisplayListener listener = null;

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(grille!=null){
            int nbligne = grille.getData.length;
            int nbcol = grille.getData[0].length;
            
            for(int l=0;l<=nbligne;l++)
                g.drawLine(0, l*tailleCase, nbcol*tailleCase, l*tailleCase);
            for(int c=0;c<=nbcol;c++)
                g.drawLine(c*tailleCase, 0, c*tailleCase, nbligne*tailleCase);
         
            for(int l=0;l<nbligne;l++)
                for(int c=0;c<nbcol;c++){
                    switch(grille.getData[l][c]){
                        case 0:
                            continue;
                        case 1:
                            g.setColor(Color.black);
                            break;
                        case 2:
                            g.setColor(Color.LIGHT_GRAY);
                            break;
                    }
                    
                    int x1 = 2 + c*tailleCase;
                    int y1 = 2 + l*tailleCase;
                    g.fillOval(x1, y1, tailleCase-4, tailleCase-4);
                    g.setColor(Color.black);
                    g.drawOval(x1, y1, tailleCase-4, tailleCase-4);
                }
        }
    }
}
