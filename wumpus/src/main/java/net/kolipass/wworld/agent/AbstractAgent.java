package main.java.net.kolipass.wworld.agent;

import main.java.net.kolipass.Log;
import main.java.net.kolipass.gameEngine.Keyboard;
import main.java.net.kolipass.wworld.*;

import java.awt.*;
import java.util.Stack;

/**
 * @author Kruti Mehta, Chris Leatherwood
 *         Defines the data and behavior associated with an AbstractAgent.
 *         An agent is an alien to the Wumpus World, and is not
 *         part of the environment.
 * @version 2.0
 */
public abstract class AbstractAgent {
    public int score;
    protected Log log = new Log();
    private final int scoreForAction = -1;
    private final int scoreForGold = 1000;
    private final int scoreForDying = -1000;


    public boolean isDead;
    public boolean isVictorious;
    public boolean hasGold;
    public boolean hasFood;
    public boolean hasArrow;

    public int x;
    public int y;

    public char direction;
    public Stack latestDirections;

    /**
     * Constructor.The agent can be allowed to have more number of
     * arrows initially
     * by changing the maxarr parameter in this constructor
     */
    public AbstractAgent() {
        this.score = 0;
        this.hasGold = false;
        this.hasFood = false;
        this.hasArrow = true;
        this.isDead = false;

        x = 1;
        y = 1;
        direction = 'E';
        latestDirections = new Stack();
        
        
    }


    /**
     * act(int action, WumplusEnvironment we)
     * makes the agent take some sort of action.
     */
    public String act(int action, WumplusEnvironment we) {
        String result = "";
        if (action == Action.GOFORWARD) {
            result = move(we);
            score += scoreForAction;
        } else if (action == Action.TURN_LEFT) {
            turnLeft();
            score += scoreForAction;
        } else if (action == Action.TURN_RIGHT) {
            turnRight();
            score += scoreForAction;
        } else if (action == Action.GRAB) {
            result = grab(we);
            if (result.equals(""))
                result = shoot(we);
            if (result.equals(""))
                climb();
            score += scoreForAction;
        } else if (action == Action.SHOOT) {
            result = shoot(we);
        } else if (action == Action.CLIMB) {
            climb();
        }

        return result;
    }

    /**
     * turnLeft()
     * turns the agent left 90 degrees.
     */

    private void turnLeft() {
        switch (direction) {
            case 'N':
                direction = 'W';
                break;
            case 'W':
                direction = 'S';
                break;
            case 'S':
                direction = 'E';
                break;
            case 'E':
                direction = 'N';
                break;
            default:
                break;
        }
    }

    /**
     * turnRight()
     * turns the agent left 90 degrees.
     */

    private void turnRight() {
        switch (direction) {
            case 'N':
                direction = 'E';
                break;
            case 'W':
                direction = 'N';
                break;
            case 'S':
                direction = 'W';
                break;
            case 'E':
                direction = 'S';
                break;
            default:
                break;
        }
    }

    public String move(WumplusEnvironment we) {
        CaveNode curNode = we.grid.get(new Point(this.x, this.y));
        CaveNode nextNode = we.getNextNode(curNode, direction);

        nextNode.wasVisited = true;
        we.unvisitedNodes.remove(nextNode);

        // check for obstacle

        if (nextNode.hasObstacle) {
            switch (this.direction) {
                case 'N':
                    curNode.foundNorthWall = true;
                    break;
                case 'S':
                    curNode.foundSouthWall = true;
                    break;
                case 'E':
                    curNode.foundEastWall = true;
                    break;
                case 'W':
                    curNode.foundWestWall = true;
                    break;
                default:
                    break;
            }
            nextNode.isSafe = false;

            return "BUMP";
        } else if (nextNode.hasSupmuw && nextNode.hasPit) {
            nextNode.isSafe = false;

            return "BUMP";
        } else {
            this.x = nextNode.x;
            this.y = nextNode.y;

            nextNode.pitProbability = 0.0;
            nextNode.wumpusProbability = 0.0;
            if (!latestDirections.contains(curNode) && !latestDirections.contains(nextNode))
                latestDirections.add(curNode);
            else if (!latestDirections.contains(curNode) && latestDirections.contains(nextNode)) 
                latestDirections.pop();
                
            return "MOVED";
        }
    }

    public String grab(WumplusEnvironment we) {
        Gold gold = we.gold;
        CaveNode node = we.grid.get(new Point(gold.x, gold.y));

        if (this.x == gold.x && this.y == gold.y && !gold.taken) {
            gold.taken = true;
            node.hasGold = false;
            this.hasGold = true;
            this.score += scoreForGold;
            return "GOLDGET";
        }

        return "";
    }

    public void climb() {
        if (this.x == 1 && this.y == 1) {
            this.isVictorious = true;
        }
    }

    public String shoot(WumplusEnvironment we) {
        if (this.hasArrow) {
            this.hasArrow = false;
            CaveNode target = we.grid.get(new Point(this.x, this.y));

            while (!target.hasObstacle) {
                if (target.hasWumpus) {
                    we.wumpus.isDead = true;
                    target.hasWumpus = false;
                    return "SCREAM";
                }
                if (target.hasSupmuw) {
                    we.supmuw.isDead = true;
                    target.hasSupmuw = false;
                    return "SCREAM";
                }
                target = we.getNextNode(target, direction);
            }
            return "MISS";
        }
        return "";
    }

    /**
     * Handles the agent status and statistics when he dies.
     *
     * @param pit
     */
    public void die(String pit) {
        this.score += scoreForDying;
        this.isDead = true;
        log.d("The agent died because met: " + pit);
    }

    public int getScore() {
        return score;
    }

    public abstract int getNextAction(WumplusEnvironment we, Keyboard keyboard);

    public void getGameState(GameState gameState) {
        gameState.hasGold = hasGold;
        gameState.hasArrow = hasArrow;
        gameState.hasFood = hasFood;
        gameState.agentIsDied = isDead;
    }
}
