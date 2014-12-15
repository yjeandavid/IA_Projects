/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.java.net.kolipass.wworld.agent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;
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
import main.java.net.kolipass.wworld.PriorityCaveNode;
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
        int action = Action.IDLE;
        
        addNewNote("My curNode: " + curNode.toShortString());
        Enonce e0 = new Pas(new Symbole("P" + curNode.x + "" + curNode.y));
        kb.raconter(e0);
        e0 = new Pas(new Symbole("W" + curNode.x + "" + curNode.y));
        kb.raconter(e0);
        e0 = new Pas(new Symbole("C" + curNode.x + "" + curNode.y));
        kb.raconter(e0);
        
        if (curNode.hasBreeze) {
            Enonce e = new Symbole("B" + this.x + "" + this.y);
            kb.raconter(e);
            Enonce e1 = new Symbole("P" + neighbors.get(0).x + "" + neighbors.get(0).y); 
            Enonce e2 = new Symbole("P" + neighbors.get(1).x + "" + neighbors.get(1).y);
            Enonce e3 = new Symbole("P" + neighbors.get(2).x + "" + neighbors.get(2).y); 
            Enonce e4 = new Symbole("P" + neighbors.get(3).x + "" + neighbors.get(3).y);
            e = new DblImpl(e, new Ou(e1, e2, e3, e4));
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
            Enonce e1 = new Symbole("W" + neighbors.get(0).x + "" + neighbors.get(0).y); 
            Enonce e2 = new Symbole("W" + neighbors.get(1).x + "" + neighbors.get(1).y);
            Enonce e3 = new Symbole("W" + neighbors.get(2).x + "" + neighbors.get(2).y); 
            Enonce e4 = new Symbole("W" + neighbors.get(3).x + "" + neighbors.get(3).y);
            e = new DblImpl(e, new Ou(e1, e2, e3, e4));
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
            Enonce e1 = new Symbole("C" + Mneighbors.get(0).x + "" + Mneighbors.get(0).y);
            Enonce e2 = new Symbole("C" + Mneighbors.get(1).x + "" + Mneighbors.get(1).y);
            Enonce e3 = new Symbole("C" + Mneighbors.get(2).x + "" + Mneighbors.get(2).y); 
            Enonce e4 = new Symbole("C" + Mneighbors.get(3).x + "" + Mneighbors.get(3).y);
            Enonce e5 = new Symbole("C" + Mneighbors.get(4).x + "" + Mneighbors.get(4).y); 
            Enonce e6 = new Symbole("C" + Mneighbors.get(5).x + "" + Mneighbors.get(5).y);
            Enonce e7 = new Symbole("C" + Mneighbors.get(6).x + "" + Mneighbors.get(6).y); 
            Enonce e8 = new Symbole("C" + Mneighbors.get(7).x + "" + Mneighbors.get(7).y);;
            e = new DblImpl(e, new Ou(e1,e2,e3,e4,e5,e6,e7,e8));
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
                    boolean nowumpus = kb.demander(elist);
                    elist.clear();
                    elist.add(new Pas(new Symbole("P" + neighbor.x + "" + neighbor.y)));
                    boolean nopit = kb.demander(elist);
                    elist.clear();
                    elist.add(new Pas(new Symbole("C" + neighbor.x + "" + neighbor.y)));
                    boolean nosupmuw = kb.demander(elist);
                    
                    if (nowumpus && nopit && nosupmuw) {
                        if (this.x == neighbor.x && this.y+1 == neighbor.y) {
                            goDirection = 'N';
                        } else if (this.x == neighbor.x && this.y-1 == neighbor.y) {
                            goDirection = 'S';
                        } else if (this.x+1 == neighbor.x && this.y == neighbor.y) {
                            goDirection = 'E';
                        } else if (this.x-1 == neighbor.x && this.y == neighbor.y) {
                            goDirection = 'W';
                        }
                        
                        break;
                    }
                }
            }
            if (goDirection == 'I') {
                if (!latestDirections.isEmpty()) {
                    CaveNode previousNode = (CaveNode) latestDirections.peek();
                    if (this.x == previousNode.x && this.y+1 == previousNode.y) {
                        goDirection = 'N';
                    } else if (this.x == previousNode.x && this.y-1 == previousNode.y) {
                        goDirection = 'S';
                    } else if (this.x+1 == previousNode.x && this.y == previousNode.y) {
                        goDirection = 'E';
                    } else if (this.x-1 == previousNode.x && this.y == previousNode.y) {
                        goDirection = 'W';
                    }
                } else {
                    this.wantsToGoHome = true;
                }
            }
        } else {
            addNewNote("goDirection is IDLE. I go home");
            this.wantsToGoHome = true; 
            goDirection = shortestSafePathToPoint(we, new Point(1, 1));
        }
        
        if (goDirection != 'I') {
            addNewNote("My direction is: " + goDirection);
            if (goDirection == this.direction) {
                addNewNote("New and last directions equals. I go forward.");
                action = Action.GOFORWARD;
            } else if (goDirection == getLeftDirection(this.direction)) {
                addNewNote("I turn left.");
                action = Action.TURN_LEFT;
            } else if (goDirection == getRightDirection(this.direction) || goDirection == getBackDirection(this.direction)) {
                addNewNote("I turn right.");
                action = Action.TURN_RIGHT;
            }
        } else {
            addNewNote("My action is idle.");
        }
        
        return action;
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
    
    private char getLeftDirection(char curDir) {
        if (curDir == 'N')
            return 'W';
        if (curDir == 'W')
            return 'S';
        if (curDir == 'S')
            return 'E';
        if (curDir == 'E')
            return 'N';
        return 'I';
    }

    private char getRightDirection(char curDir) {
        if (curDir == 'N')
            return 'E';
        if (curDir == 'W')
            return 'N';
        if (curDir == 'S')
            return 'W';
        if (curDir == 'E')
            return 'S';
        return 'I';
    }

    private char getBackDirection(char curDir) {
        if (curDir == 'N')
            return 'S';
        if (curDir == 'W')
            return 'E';
        if (curDir == 'S')
            return 'N';
        if (curDir == 'E')
            return 'W';
        return 'I';
    }
    
    /**
     * shortestSafePathToUnvisited(WumplusEnvironment we)
     * determines which direction to go to get to the current least-cost unvisited node.
     * Returns N S E or W if an unvisited node can be reached with little or no chance of death
     */
    private char shortestSafePathToPoint(WumplusEnvironment we, Point point) {
        // initialize visited nodes list so that we don't revisited nodes we've already visited in this bfs.
        Hashtable<Point, Boolean> isExhausted = new Hashtable<Point, Boolean>();
        //	log.d("####################Path to Point " + point + " start A* search ####################################");
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                isExhausted.put(new Point(j, i), false);
            }
        }

        // initialize other things

        PriorityQueue<PriorityCaveNode> pq = new PriorityQueue<PriorityCaveNode>();
        Hashtable<Point, CaveNode> grid = we.grid;

        PriorityCaveNode first = new PriorityCaveNode(grid.get(new Point(this.x, this.y)), 0, new Vector<Character>(), this.direction);
        pq.add(first);

        // A* Search

        while (!pq.isEmpty()) {
            //log.d("Entered A* Search");
            // initialize this step.

            PriorityCaveNode curPNode = pq.remove();
            CaveNode node = curPNode.node;
            char curDir = curPNode.directions.lastElement().charValue();
            int curCost = curPNode.cost;

            Point curPoint = new Point(node.x, node.y);

            // If we have reached a node that was not visited, then we'll return the direction taken to get to it!

            if (node.x == point.x && node.y == point.y) {
                try {
                    return curPNode.directions.get(1).charValue();
                } catch (Exception e) {
                    return 'I';
                }
            }

            // proceed if current node hasn't been visited in the bfs yet and it isn't a wall.

            if (!isExhausted.get(curPoint).booleanValue() && !node.hasObstacle) {
                char leftDir = getLeftDirection(curDir);
                char rightDir = getRightDirection(curDir);
                char backDir = getBackDirection(curDir);

                // attempt to travel in all directions. Account for cost of turning.

                aStarTravel(pq, we, curPNode, curDir, curCost, point);
                aStarTravel(pq, we, curPNode, leftDir, curCost + 1, point);
                aStarTravel(pq, we, curPNode, rightDir, curCost + 1, point);
                aStarTravel(pq, we, curPNode, backDir, curCost + 2, point);
            }
            isExhausted.put(curPoint, true);
        }

        return 'I'; // I for IDLE : it was not possible to safely reach an unvisited node
    }
    
    /**
     * performs a step through an A* search for the agent
     */

    private void aStarTravel(PriorityQueue<PriorityCaveNode> pq, WumplusEnvironment we, PriorityCaveNode pNode, char direction, int curCost, Point destinationPoint) {
        CaveNode node = pNode.node;

        CaveNode nextNode = we.getNextNode(node, direction);

        // determine cost of moving foward into the next node.

        double chanceOfFriendlySupmuw = nextNode.supmuwProbability * this.supmuwFriendlyProbability;
        double chanceOfMurderousSupmuw = nextNode.supmuwProbability * (1 - this.supmuwFriendlyProbability);
        if (this.supmuwKilled)
            chanceOfMurderousSupmuw = 0.0;

        double chanceOfWumpusDeath = (nextNode.wumpusProbability);
        if (wumpusKilled)
            chanceOfWumpusDeath = 0.0;

        double chanceOfDeath = 1.0 - ((1.0 - nextNode.pitProbability) * (1.0 - chanceOfWumpusDeath) * (1.0 - chanceOfMurderousSupmuw));
        if (chanceOfDeath == 1.0)
            return;

        double chanceOfGold = 1.0 / we.unvisitedNodes.size();
        if (this.hasGold)
            chanceOfGold = 0.0;

        if (this.hasFood)
            chanceOfFriendlySupmuw = 0.0;

        int moveForwardCost = (int) (1 + chanceOfDeath * 1000 + chanceOfGold * -1000 + chanceOfFriendlySupmuw * -100);
        //log.d("chanceDeath: " + (chanceOfDeath) + " Gold: " + (chanceOfGold) + " FriendSupmuw: " + chanceOfFriendlySupmuw);
        //log.d("chancePit: " + (nextNode.pitProbability) + " Wumpus: " + (nextNode.wumpusProbability) + " UnFriendSupmuw: " + chanceOfMurderousSupmuw);

        // add weight to unvisited nodes

        if (nextNode.wasVisited && destinationPoint == null) {
            //	log.d("was visited");
            moveForwardCost += 100;
        }
        if (destinationPoint != null) {
            //	log.d("heading to point " + destinationPoint);
            moveForwardCost += Math.abs(nextNode.x - destinationPoint.x) + Math.abs(nextNode.y - destinationPoint.y);
            if (!nextNode.wasVisited) {
                moveForwardCost += 100;
            }
        }

        // add the next node to our priority queue if the agent isn't likely to die by doing so.

        if (moveForwardCost < 500 + 1) {
            //log.d("cost to move " + direction + " to " + nextNode.x + "," + nextNode.y + " : " + (curCost + moveForwardCost) + " : " + curCost + " " + moveForwardCost);
            PriorityCaveNode nextPNode = new PriorityCaveNode(nextNode, curCost + moveForwardCost, pNode.directions, direction);
            pq.add(nextPNode);
        }
    }
}
