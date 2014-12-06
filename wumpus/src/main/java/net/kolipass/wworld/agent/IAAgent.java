/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.java.net.kolipass.wworld.agent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import main.java.net.kolipass.gameEngine.Keyboard;
import main.java.net.kolipass.logique.propositionnelle.DblImpl;
import main.java.net.kolipass.logique.propositionnelle.Enonce;
import main.java.net.kolipass.logique.propositionnelle.KBPropositionnelle;
import main.java.net.kolipass.logique.propositionnelle.Ou;
import main.java.net.kolipass.logique.propositionnelle.Pas;
import main.java.net.kolipass.logique.propositionnelle.Symbole;
import main.java.net.kolipass.wworld.Action;
import main.java.net.kolipass.wworld.CaveNode;
import main.java.net.kolipass.wworld.WumplusEnvironment;

/**
 *
 * @author Claude-Clément YAPO
 */
public class IAAgent extends AbstractAgent {

    private boolean wantsToGoHome = false;
    private int knownWumpusX = -1;
    private int knownWumpusY = -1;
    private double supmuwFriendlyProbability = 0.5;
    private boolean supmuwFound = false;
    private boolean supmuwKilled = false;
    private int knownSupmuwX = -1;
    private int knownSupmuwY = -1;
    private boolean wumpusKilled = false;
    private boolean wumpusFound = false;

    private AgentBlog agentBlog = null;
    private KBPropositionnelle kb = null;

    // we initially know nothing of the wumpus or supmuw's location.
    private int eastmostStench = -1;
    private int westmostStench = 12;
    private int northmostStench = -1;
    private int southmostStench = 12;


    private int eastmostMoo = -1;
    private int westmostMoo = 12;
    private int northmostMoo = -1;
    private int southmostMoo = 12;

    public IAAgent (AgentBlog agentBlog) {
        this.agentBlog = agentBlog;
        kb = new KBPropositionnelle();
    }
    
    @Override
    public String move(WumplusEnvironment we) {
        CaveNode curNode = we.grid.get(new Point(this.x, this.y));
        CaveNode nextNode = we.getNextNode(curNode, direction);

        if (nextNode.hasStench) {
            if (nextNode.x < westmostStench)
                westmostStench = nextNode.x;
            if (nextNode.x > eastmostStench)
                eastmostStench = nextNode.x;
            if (nextNode.y < southmostStench)
                southmostStench = nextNode.y;
            if (nextNode.y > northmostStench)
                northmostStench = nextNode.y;
        }

        if (nextNode.hasMoo) {
            if (nextNode.x < westmostMoo)
                westmostMoo = nextNode.x;
            if (nextNode.x > eastmostMoo)
                eastmostMoo = nextNode.x;
            if (nextNode.y < southmostMoo)
                southmostMoo = nextNode.y;
            if (nextNode.y > northmostMoo)
                northmostMoo = nextNode.y;
        }
        return super.move(we);
    }
    
    private void addNewNote(String note) {
        if (agentBlog != null) agentBlog.note(note);
    }
    
    @Override
    public int getNextAction(WumplusEnvironment we, Keyboard keyboard) {
        
        CaveNode curNode = we.grid.get(new Point(this.x, this.y));
        Vector<CaveNode> neighbors = we.get4AdjacentNodes(curNode);
        Vector<CaveNode> Mneighbors = we.get8AdjacentNodes(curNode);

        
        addNewNote("My curNode: " + curNode.toShortString());
        
        if (curNode.hasBreeze) {
            Enonce e = new Symbole("B" + this.x + "" + this.y);
            kb.raconter(e);
            Enonce e1 = new Ou(new Symbole("P" + neighbors.get(0).x + "" + neighbors.get(0).y), 
                                new Symbole("P" + neighbors.get(1).x + "" + neighbors.get(1).y));
            Enonce e2 = new Ou(new Symbole("P" + neighbors.get(2).x + "" + neighbors.get(2).y), 
                                new Symbole("P" + neighbors.get(3).x + "" + neighbors.get(3).y));
            e = new DblImpl(e, new Ou(e1, e2));
            kb.raconter(e);
        } else {
            kb.raconter(new Pas(new Symbole("B" + this.x + "" + this.y)));
            kb.raconter(new Pas(new Symbole("P" + neighbors.get(0).x + "" + neighbors.get(0).y)));
            kb.raconter(new Pas(new Symbole("P" + neighbors.get(1).x + "" + neighbors.get(1).y)));
            kb.raconter(new Pas(new Symbole("P" + neighbors.get(2).x + "" + neighbors.get(2).y)));
            kb.raconter(new Pas(new Symbole("P" + neighbors.get(3).x + "" + neighbors.get(3).y)));
        }
        
        if (curNode.hasStench) {
            
            Enonce e = new Symbole("S" + this.x + "" + this.y);
            kb.raconter(e);
            
            Enonce e1 = new Ou(new Symbole("W" + neighbors.get(0).x + "" + neighbors.get(0).y), 
                                new Symbole("W" + neighbors.get(1).x + "" + neighbors.get(1).y));
            Enonce e2 = new Ou(new Symbole("W" + neighbors.get(2).x + "" + neighbors.get(2).y), 
                                new Symbole("W" + neighbors.get(3).x + "" + neighbors.get(3).y));
            e = new DblImpl(e, new Ou(e1, e2));
            kb.raconter(e);
            
        } else {
            
            kb.raconter(new Pas(new Symbole("S" + this.x + "" + this.y)));
            kb.raconter(new Pas(new Symbole("W" + neighbors.get(0).x + "" + neighbors.get(0).y)));
            kb.raconter(new Pas(new Symbole("W" + neighbors.get(1).x + "" + neighbors.get(1).y)));
            kb.raconter(new Pas(new Symbole("W" + neighbors.get(2).x + "" + neighbors.get(2).y)));
            kb.raconter(new Pas(new Symbole("W" + neighbors.get(3).x + "" + neighbors.get(3).y)));
            
        }
        
        if (curNode.hasMoo) {
            
            
            Enonce e = new Symbole("M" + this.x + "" + this.y);
            kb.raconter(e);
            
            Enonce e1 = new Ou(new Symbole("C" + Mneighbors.get(0).x + "" + Mneighbors.get(0).y), 
                                new Symbole("C" + Mneighbors.get(1).x + "" + Mneighbors.get(1).y));
            Enonce e2 = new Ou(new Symbole("C" + Mneighbors.get(2).x + "" + Mneighbors.get(2).y), 
                                new Symbole("C" + Mneighbors.get(3).x + "" + Mneighbors.get(3).y));
            Enonce e3 = new Ou(new Symbole("C" + Mneighbors.get(4).x + "" + Mneighbors.get(4).y), 
                                new Symbole("C" + Mneighbors.get(5).x + "" + Mneighbors.get(5).y));
            Enonce e4 = new Ou(new Symbole("C" + Mneighbors.get(6).x + "" + Mneighbors.get(6).y), 
                                new Symbole("C" + Mneighbors.get(7).x + "" + Mneighbors.get(7).y));
            
            e = new DblImpl(e, new Ou(new Ou(e1, e2), new Ou (e3,e4)));
            kb.raconter(e);
            
        } else {
            
            kb.raconter(new Pas(new Symbole("M" + this.x + "" + this.y)));
            kb.raconter(new Pas(new Symbole("C" + Mneighbors.get(0).x + "" + Mneighbors.get(0).y)));
            kb.raconter(new Pas(new Symbole("C" + Mneighbors.get(1).x + "" + Mneighbors.get(1).y)));
            kb.raconter(new Pas(new Symbole("C" + Mneighbors.get(2).x + "" + Mneighbors.get(2).y)));
            kb.raconter(new Pas(new Symbole("C" + Mneighbors.get(3).x + "" + Mneighbors.get(3).y)));
            kb.raconter(new Pas(new Symbole("C" + Mneighbors.get(4).x + "" + Mneighbors.get(4).y)));
            kb.raconter(new Pas(new Symbole("C" + Mneighbors.get(5).x + "" + Mneighbors.get(5).y)));
            kb.raconter(new Pas(new Symbole("C" + Mneighbors.get(6).x + "" + Mneighbors.get(6).y)));
            kb.raconter(new Pas(new Symbole("C" + Mneighbors.get(7).x + "" + Mneighbors.get(7).y)));
            
            
        }
        
        if (curNode.hasGold) {
            addNewNote("curNode has gold and i grab the gold.");
            return Action.GRAB;
        }
        
        
         if (this.hasArrow && projectArrowShot(we)) {
            addNewNote("I has arrow and i shot.");
            return Action.SHOOT;
        }

        // go home if both gold and food were found.

        if (this.hasGold && this.hasFood) {
            addNewNote("I has gold and i has food, i go home.");
            this.wantsToGoHome = true;
        }

        // go home if have gold and supmuw has been deduced to be unfriendly.

        if (this.hasGold && this.supmuwFriendlyProbability == 0.0) {
            addNewNote("I has gold and Supmuw not Friendly, i go home.");
            this.wantsToGoHome = true;
        }



        if (this.wantsToGoHome && this.x == 1 && this.y == 1) {
             addNewNote("I wants to leave and I am at entrance, climb out!");
             return Action.CLIMB;
        }


        char goDirection = 'I';
        if (!this.wantsToGoHome) {
            for (CaveNode neighbor : neighbors) {
                if (!neighbor.wasVisited) {
                    ArrayList<Enonce> elist = new ArrayList();
                    elist.add(new Pas(new Symbole("W" + neighbor.x + "" + neighbor.y)));
                    boolean wumpus = kb.demander(elist);
                    elist.clear();
                    elist.add(new Pas(new Symbole("P" + neighbor.x + "" + neighbor.y)));
                    boolean pit = kb.demander(elist);
                    elist.clear();
                    elist.add(new Pas(new Symbole("C" + neighbor.x + "" + neighbor.y)));
                    boolean supmuw = kb.demander(elist);
                    
                    if (!wumpus && !pit && !supmuw) {
                        
                    }
                }
            }
            

        }
        
        return Action.IDLE;
    }
    
    private boolean projectArrowShot(WumplusEnvironment we) {
        Hashtable<Point, CaveNode> grid = we.grid;

        CaveNode target = grid.get(new Point(this.x, this.y));

        while (true) {
            //log.d(target.x + " , " + target.y);
            if (target.x == this.knownWumpusX && target.y == this.knownWumpusY) {
                addNewNote("shoot the wumpus!");
                wumpusKilled = true;
                return true;
            }
            if (target.x == this.knownSupmuwX && target.y == this.knownSupmuwY) {
                if (this.supmuwFriendlyProbability == 0.0) {
                    addNewNote("shoot the supmuw!");
                    supmuwKilled = true;
                    return true;
                } else
                    return false;
            }
            if (target.hasObstacle && target.wasVisited) {
                return false;
            }
            if (!target.wasVisited) {
                return false;
            }
            target = we.getNextNode(target, direction);
        }
    }
}
