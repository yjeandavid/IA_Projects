package main.java.net.kolipass.wworld.agent;

import net.kolipass.gameEngine.Keyboard;
import main.java.net.kolipass.wworld.Action;
import main.java.net.kolipass.wworld.CaveNode;
import main.java.net.kolipass.wworld.PriorityCaveNode;
import main.java.net.kolipass.wworld.WumplusEnvironment;

import java.awt.*;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 * Created by kolipass on 11.12.13.
 */
public class AIAgent extends AbstractAgent {
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


    // we initially know nothing of the wumpus or supmuw's location.
    private int eastmostStench = -1;
    private int westmostStench = 12;
    private int northmostStench = -1;
    private int southmostStench = 12;


    private int eastmostMoo = -1;
    private int westmostMoo = 12;
    private int northmostMoo = -1;
    private int southmostMoo = 12;

    /**
     * @param agentBlog if null, records not be write
     */
    public AIAgent(AgentBlog agentBlog) {
        this.agentBlog = agentBlog;
    }

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
        {
            CaveNode curNode = we.grid.get(new Point(this.x, this.y));

            addNewNote("My curNode: " + curNode.toShortString());

            // use the agent's knowledge to attempt to deduce pit locations.
            this.checkAllForPits(we);

            // use the agent's knowledge to attempt to deduce the wumpus's location
            //if(!this.wumpusFound)
            this.checkAllForWumpus(we);

            // use the agent's knowledge to attempt to deduce the supmuw's location

            this.checkAllForSupmuw(we);

            // if the agent has deduced the wumpus's location, it will shoot it if it is convenient to do so.

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

            // if we are in a square that has gold, grab the gold!
            if (curNode.hasGold) {
                addNewNote("curNode has gold and i grab the gold.");
                return Action.GRAB;
            }
            // if the agent wants to leave and is at entrance, climb out!

            if (this.wantsToGoHome && this.x == 1 && this.y == 1) {
                addNewNote("I wants to leave and I am at entrance, climb out!");
                return Action.CLIMB;
            }

            // use A* search to visit reasonably safe unvisited nodes.

            char goDirection = 'I';
            if (!this.wantsToGoHome) {
                if (!this.hasFood && supmuwFriendlyProbability == 1.0 && this.supmuwFound) {
                    addNewNote("I not have food, supmuw is friendly and i found Supmuw. I go to Supmuw:("
                            + knownSupmuwX + "," + knownSupmuwY + ")");
                    goDirection = this.shortestSafePathToPoint(we, new Point(knownSupmuwX, knownSupmuwY));
                } else {
                    addNewNote(" I go to shortest Safe Path To Unvisited");
                    goDirection = this.shortestSafePathToUnvisited(we);
                }
            }

            // if the A* becomes too scared to try any more unvisited nodes, then it will head back to the entrance.

            if (goDirection == 'I') {
                addNewNote("goDirection is IDLE. I go home");
                this.wantsToGoHome = true;
                goDirection = shortestSafePathToPoint(we, new Point(1, 1));
            }
            addNewNote("My direction is: " + goDirection);
            if (goDirection == this.direction) {
                addNewNote("New and last directions equals. I go forward.");
                return Action.GOFORWARD;
            } else if (goDirection == getLeftDirection(this.direction)) {
                addNewNote("I turn left.");
                return Action.TURN_LEFT;
            } else if (goDirection == getRightDirection(this.direction) || goDirection == getBackDirection(this.direction)) {
                addNewNote("I turn right.");
                return Action.TURN_RIGHT;
            }
            addNewNote("My action is idle.");
            return Action.IDLE;
        }

    }

    /**
     * The agent checks if arrow would hit a known wall, known unfriendly supmuw, or known wumpus. Returns true if yes.
     */

    private boolean projectArrowShot(WumplusEnvironment we) {
        Hashtable<Point, CaveNode> grid = we.grid;

        CaveNode target = grid.get(new Point(this.x, this.y));

        //log.d("Wumpus deduced point: " + knownWumpusX + "," + knownWumpusY);
        //log.d("Supmuw deduced point: " + knownSupmuwX + "," + knownSupmuwY);

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


    private void checkAllForPits(WumplusEnvironment we) {
        Hashtable<Point, CaveNode> grid = we.grid;

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Point point = new Point(j, i);
                CaveNode node = grid.get(point);

                if (node.wasVisited) {
                    checkAreaForPits(we, node);
                }
            }
        }
    }


    private void checkAreaForPits(WumplusEnvironment we, CaveNode node) {
        Vector<CaveNode> neighbors = we.get4AdjacentNodes(node);
        if (!node.hasBreeze) // neighbors of this node cannot have pits.
        {

            for (CaveNode neighbor : neighbors) {
                neighbor.pitProbability = 0.0;
            }

        } else // this node is breezy
        {
            // initially assume that there is a 50% chance that a node adjacent to a
            // breeze has a pit unless it has been deduced to not have a pit.
            for (CaveNode neighbor : neighbors) {
                if (!neighbor.wasVisited && neighbor.pitProbability > 0.0 && neighbor.pitProbability < 1.0)
                    neighbor.pitProbability = 0.5;
            }

            for (int i = 0; i < 4; i++) {
                CaveNode frontNode = neighbors.get(i);
                CaveNode rightNode = neighbors.get((i + 1) % 4);
                CaveNode backNode = neighbors.get((i + 2) % 4);
                CaveNode leftNode = neighbors.get((i + 3) % 4);

                boolean leftNoPit = (leftNode.pitProbability == 0.0);
                boolean backNoPit = (backNode.pitProbability == 0.0);
                boolean rightNoPit = (rightNode.pitProbability == 0.0);

                if (leftNoPit && rightNoPit && backNoPit) {
                    frontNode.pitProbability = 1.0; // we can deduce for certain that frontNode is a pit if we know that the left, right, and back squares aren't pits.
                }
            }
        }
    }


    private void checkAllForWumpus(WumplusEnvironment we) {
        Hashtable<Point, CaveNode> grid = we.grid;

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Point point = new Point(j, i);
                CaveNode node = grid.get(point);

                if (node.wasVisited) {
                    if (checkAreaForWumpus(we, node))
                        return;
                }
            }
        }
    }

    private boolean checkAreaForWumpus(WumplusEnvironment we, CaveNode node) {
        Vector<CaveNode> neighbors = we.get4AdjacentNodes(node);
        if (!node.hasStench) // neighbors of this node cannot have the wumpus.
        {
            for (CaveNode neighbor : neighbors) {
                neighbor.wumpusProbability = 0.0;
            }
        } else // this node stinks
        {
            neighbors = we.get8AdjacentNodes(node);
            // initially assume that there is a 50% chance that a node adjacent to a
            // stench has a wumpus unless it has been deduced to not have a wumpus.
            for (CaveNode neighbor : neighbors) {
                if (!neighbor.wasVisited && neighbor.wumpusProbability > 0.0 && neighbor.wumpusProbability < 1.0)
                    neighbor.wumpusProbability = 0.5;
            }

            for (int i = 0; i < 8; i += 2) {

                CaveNode frontNode = neighbors.get(i);
                CaveNode frontRightNode = neighbors.get((i + 1) % 8);
                CaveNode rightNode = neighbors.get((i + 2) % 8);
                CaveNode backRightNode = neighbors.get((i + 3) % 8);
                CaveNode backNode = neighbors.get((i + 4) % 8);
                CaveNode backLeftNode = neighbors.get((i + 5) % 8);
                CaveNode leftNode = neighbors.get((i + 6) % 8);
                CaveNode frontLeftNode = neighbors.get((i + 7) % 8);

                //log.d("Left : " + leftNode.x + "," + leftNode.y + " " + leftNode.wumpusProbability);
                //log.d("Right : " + rightNode.x + "," + rightNode.y + " " + rightNode.wumpusProbability);
                //log.d("Back : " + backNode.x + "," + backNode.y + " " + backNode.wumpusProbability);
                //log.d("Front : " + frontNode.x + "," + frontNode.y + " " + frontNode.wumpusProbability);

                boolean leftNoWumpus = (leftNode.wumpusProbability == 0.0);
                boolean backNoWumpus = (backNode.wumpusProbability == 0.0);
                boolean rightNoWumpus = (rightNode.wumpusProbability == 0.0);

                // Case 1
                //
                //	#W#
                //	.S.
                //	#.#
                //

                if (leftNode.wasVisited && !leftNode.hasStench && rightNode.wasVisited && !rightNode.hasStench && backNode.wasVisited && !backNode.hasStench) {
                    //log.d("case 1");
                    pinPointWumpus(frontNode, we);
                    return true;
                }

                // Case 2
                //
                //	#WS
                //	#S.
                //	###
                //

                if (rightNoWumpus && frontRightNode.wasVisited && frontRightNode.hasStench) {
                    //	log.d("case 2");
                    pinPointWumpus(frontNode, we);
                    return true;
                }

                // Case 3
                //
                //	SW#
                //	.S#
                //	###
                //

                if (leftNoWumpus && frontLeftNode.wasVisited && frontLeftNode.hasStench) {
                    //	log.d("case 3");
                    pinPointWumpus(frontNode, we);
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * updates agent knowledge when it deduces the wumpus's location.
     */

    private void pinPointWumpus(CaveNode wumpusNode, WumplusEnvironment we) {
        Hashtable<Point, CaveNode> grid = we.grid;
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                Point point = new Point(j, i);
                CaveNode node = grid.get(point);

                node.wumpusProbability = 0.0;
            }
        }

        if (!this.hasArrow)
            wumpusNode.wumpusProbability = 1.0; // If the agent ever finds itself in a position
        // where it's about to move into the wumpus's square, the agent now knows to shoot it first anyways.
        wumpusFound = true;
        this.knownWumpusX = wumpusNode.x;
        this.knownWumpusY = wumpusNode.y;
    }


    /**
     * updates the player's knowledge of the supmuw's whereabouts
     */

    private void checkAllForSupmuw(WumplusEnvironment we) {
        Hashtable<Point, CaveNode> grid = we.grid;

        // try to deduce supmuw probabilities

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Point point = new Point(j, i);
                CaveNode node = grid.get(point);

                if (node.wasVisited) {
                    if (checkAreaForSupmuw(we, node)) { // return;
                    }
                }
            }
        }

        // use known extreme positions of stenches and moos to deduce whether or not the Supmuw is friendly.


        if (this.supmuwFriendlyProbability > 0.0 && this.supmuwFriendlyProbability < 1.0 && eastmostMoo != -1) {
            if (this.knownSupmuwX != -1 && Math.abs(this.knownSupmuwX - this.knownWumpusX) == 1 && Math.abs(this.knownSupmuwY - this.knownWumpusY) == 0) {
                addNewNote("Deduced that Supmuw is murderous. X adjacent.");
                this.supmuwFriendlyProbability = 0.0;
                CaveNode supmuwNode = grid.get(new Point(this.knownSupmuwX, this.knownSupmuwY));
                if (this.hasArrow) {
                    supmuwNode.supmuwProbability = 0;
                }
            } else if (this.knownSupmuwX != -1 && Math.abs(this.knownSupmuwX - this.knownWumpusX) == 0 && Math.abs(this.knownSupmuwY - this.knownWumpusY) == 1) {
                addNewNote("Deduced that Supmuw is murderous. Y adjacent.");
                this.supmuwFriendlyProbability = 0.0;
                CaveNode supmuwNode = grid.get(new Point(this.knownSupmuwX, this.knownSupmuwY));
                if (this.hasArrow) {
                    supmuwNode.supmuwProbability = 0;
                }
            } else if (this.knownSupmuwX != -1 && this.knownWumpusX != -1 && this.knownSupmuwX < eastmostStench - 1) {
                this.supmuwFriendlyProbability = 1.0;
                addNewNote("Deduced that Supmuw is friendly. Too far west.");
            } else if (this.knownSupmuwX != -1 && this.knownWumpusX != -1 && this.knownSupmuwX > westmostStench + 1) {
                this.supmuwFriendlyProbability = 1.0;
                addNewNote("Deduced that Supmuw is friendly. Too far east.");
            } else if (this.knownSupmuwX != -1 && this.knownWumpusX != -1 && this.knownSupmuwY < southmostStench - 1) {
                this.supmuwFriendlyProbability = 1.0;
                addNewNote("Deduced that Supmuw is friendly. Too far south.");
            } else if (this.knownSupmuwX != -1 && this.knownWumpusX != -1 && this.knownSupmuwY > northmostStench + 1) {
                this.supmuwFriendlyProbability = 1.0;
                addNewNote("Deduced that Supmuw is friendly. Too far north.");
            } else {
                addNewNote("Cannot deduce Supmuw's friendliness.");
            }

        }


    }

    private boolean checkAreaForSupmuw(WumplusEnvironment we, CaveNode node) {
        Vector<CaveNode> neighbors = we.get8AdjacentNodes(node);
        if (!node.hasMoo) // neighbors of this node cannot have the wupmuw.
        {
            for (CaveNode neighbor : neighbors) {
                neighbor.supmuwProbability = 0.0;
            }
        } else // can hear moo
        {
            //log.d("heard a moo");
            // initially assume that there is a 50% chance that a node adjacent to a
            // moo has a supmuw unless it has been deduced to not have a supmuw.
            for (CaveNode neighbor : neighbors) {
                if (!neighbor.wasVisited && neighbor.supmuwProbability > 0.0 && neighbor.supmuwProbability < 1.0)
                    neighbor.supmuwProbability = 0.5;
            }

            for (int i = 0; i < 8; i += 2) {

                CaveNode frontNode = neighbors.get(i);
                CaveNode frontRightNode = neighbors.get((i + 1) % 8);
                CaveNode rightNode = neighbors.get((i + 2) % 8);
                CaveNode backRightNode = neighbors.get((i + 3) % 8);
                CaveNode backNode = neighbors.get((i + 4) % 8);
                CaveNode backLeftNode = neighbors.get((i + 5) % 8);
                CaveNode leftNode = neighbors.get((i + 6) % 8);
                CaveNode frontLeftNode = neighbors.get((i + 7) % 8);

                //log.d("Left : " + leftNode.x + "," + leftNode.y + " " + leftNode.wumpusProbability);
                //log.d("Right : " + rightNode.x + "," + rightNode.y + " " + rightNode.wumpusProbability);
                //log.d("Back : " + backNode.x + "," + backNode.y + " " + backNode.wumpusProbability);
                //log.d("Front : " + frontNode.x + "," + frontNode.y + " " + frontNode.wumpusProbability);

                boolean leftNoSupmuw = (leftNode.supmuwProbability == 0.0);
                boolean backNoSupmuw = (backNode.supmuwProbability == 0.0);
                boolean rightNoSupmuw = (rightNode.supmuwProbability == 0.0);


                // Case 1
                //
                //	#.#
                //	MMM
                //	#S#
                //

                if (leftNode.wasVisited && leftNode.hasMoo && rightNode.wasVisited && rightNode.hasMoo && backNoSupmuw) {
                    pinPointSupmuw(frontNode, we);
                    //	log.d("case 1");
                    return true;
                }

                // Case 2
                //
                //	###
                //	#M#
                //	MSM
                //

                if (frontRightNode.wasVisited && frontRightNode.hasMoo && frontLeftNode.wasVisited && frontLeftNode.hasMoo && frontNode.wasVisited && frontNode.hasMoo) {
                    pinPointSupmuw(frontNode, we);
                    //	log.d("case 2");
                    return true;
                }

                // Case 3
                //
                //	#.#
                //	MMM
                //	MSM
                //

                if (frontLeftNode.wasVisited && frontLeftNode.hasMoo && frontRightNode.wasVisited && frontRightNode.hasMoo && leftNode.wasVisited && leftNode.hasMoo && rightNode.wasVisited && rightNode.hasMoo) {
                    pinPointSupmuw(frontNode, we);
                    log.d("case 3");
                    return true;
                }

                // Case 4
                //
                //	#.#
                //	#M.
                //	S##
                //

                if (leftNode.wasVisited && !leftNode.hasMoo && backNode.wasVisited && !backNode.hasMoo) {
                    pinPointSupmuw(frontRightNode, we);
                    //	log.d("case 4");
                    return true;
                }

                // Case 5
                //
                //	#.#
                //	.M#
                //	##S
                //

                if (rightNode.wasVisited && !rightNode.hasMoo && backNode.wasVisited && !backNode.hasMoo) {
                    pinPointSupmuw(frontLeftNode, we);
                    //	log.d("case 5");
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * updates agent knowledge when it deduces the wupmuw's location.
     */

    private void pinPointSupmuw(CaveNode supmuwNode, WumplusEnvironment we) {
        Hashtable<Point, CaveNode> grid = we.grid;
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                Point point = new Point(j, i);
                CaveNode node = grid.get(point);

                node.supmuwProbability = 0.0;

            }
        }
        //	log.d("Deduced supmuw's location: " + supmuwNode.x + "," + supmuwNode.y);
        supmuwNode.supmuwProbability = 1.0;
        supmuwFound = true;
        this.knownSupmuwX = supmuwNode.x;
        this.knownSupmuwY = supmuwNode.y;
    }


    /**
     * shortestSafePathToUnvisited(WumplusEnvironment we)
     * determines which direction to go to get to the current least-cost unvisited node.
     * Returns N S E or W if an unvisited node can be reached with little or no chance of death
     */

    private char shortestSafePathToUnvisited(WumplusEnvironment we) {
        // initialize visited nodes list so that we don't revisited nodes we've already visited in this bfs.
        Hashtable<Point, Boolean> isExhausted = new Hashtable<Point, Boolean>();
        //	log.d("****************************************Path to unvisited Start A* search******************************************");
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
            // initialize this step.

            PriorityCaveNode curPNode = pq.remove();
            CaveNode node = curPNode.node;
            char curDir = curPNode.directions.lastElement().charValue();
            int curCost = curPNode.cost;

            Point curPoint = new Point(node.x, node.y);

            // If we have reached a node that was not visited, then we'll return the direction taken to get to it!

            if (node.wasVisited == false) {
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

                aStarTravel(pq, we, curPNode, curDir, curCost, null);
                aStarTravel(pq, we, curPNode, leftDir, curCost + 1, null);
                aStarTravel(pq, we, curPNode, rightDir, curCost + 1, null);
                aStarTravel(pq, we, curPNode, backDir, curCost + 2, null);
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


}
