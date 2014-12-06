/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.java.net.kolipass.wworld.agent;

import java.awt.Point;
import java.util.Hashtable;
import java.util.Vector;
import main.java.net.kolipass.gameEngine.Keyboard;
import main.java.net.kolipass.logique.propositionnelle.KBPropositionnelle;
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
        
        addNewNote("My curNode: " + curNode.toShortString());
        
        if (curNode.hasBreeze) {
            
        } else {
            
        }
        
        if (curNode.hasStench) {
            
        } else {
            
        }
        
        if (curNode.hasMoo) {
            
        } else {
            
        }
        
        if (curNode.hasGold) {
            
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
