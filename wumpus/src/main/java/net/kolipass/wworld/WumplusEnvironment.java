package main.java.net.kolipass.wworld;

import main.java.net.kolipass.Log;
import main.java.net.kolipass.MasterPanel;

import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import main.java.net.kolipass.wworld.agent.AIAgent;
import main.java.net.kolipass.wworld.agent.AbstractAgent;
import main.java.net.kolipass.wworld.agent.AgentBlog;
import main.java.net.kolipass.wworld.agent.FileAgentBlog;
import main.java.net.kolipass.wworld.agent.HumanAgent;
import main.java.net.kolipass.wworld.agent.IAAgent;
import main.java.net.kolipass.wworld.agent.MemoAgentBlog;

/**
 * @author Chris Leatherwood
 */
public class WumplusEnvironment {
    private Log log = new Log();
    public Hashtable<Point, CaveNode> grid;
    public Vector<CaveNode> unvisitedNodes;

    public AbstractAgent agent;
    public Supmuw supmuw;
    public Wumpus wumpus;
    public Gold gold;

    private final int pitPercent = 7;
    private final int wallPercent = 15;

    public final int MAX_WIDTH = 10;
    public final int MAX_HEIGHT = 10;

    String mapName = "";
    AgentBlog blog = null;

    public WumplusEnvironment(MasterPanel mp) {
        this.grid = new Hashtable<Point, CaveNode>();
        this.unvisitedNodes = new Vector<CaveNode>();

        if (mp.loadFile == null) // create random map
        {
            initRandomEnvironment();
        } else    // read map from file
        {
            initLoadedEnvironment(mp.loadFile);
        }

        createCaveBorder();

        initAllPercepts();

        supmuw.updateMode();
        if (mp.gameMode == 'H') {
            this.agent = new HumanAgent(mp.getConfig());
        } else {
            blog = new FileAgentBlog(new MemoAgentBlog(null),
                    "blogs" + File.separator + mapName + File.separator + currentTime() + ".txt");
            //this.agent = new AIAgent(blog);
            this.agent = new IAAgent(blog);
        }
    }

    public AgentBlog getBlog() {
        return blog;
    }

    private String currentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        String time = dateFormat.format(cal.getTime());
        return time;
    }


    /**
     * initAllPercepts()
     * initializes the percepts for all the CaveNodes.
     */

    private void initAllPercepts() {
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                CaveNode curNode = grid.get(new Point(j, i));
                initPercepts(curNode);
            }
        }
    }

    /**
     * initPercepts(CaveNode node)
     * initializes all the percepts for node.
     */
    private void initPercepts(CaveNode node) {
        Vector<CaveNode> neighbors;
        if (node.hasPit) {
            neighbors = get4AdjacentNodes(node);
            for (CaveNode neighbor : neighbors) {
                neighbor.hasBreeze = true;
            }
        }
        if (node.x == wumpus.x && node.y == wumpus.y) {
            node.hasStench = true;
            neighbors = get4AdjacentNodes(node);
            for (CaveNode neighbor : neighbors) {
                neighbor.hasStench = true;
            }
        }

        if (node.x == supmuw.x && node.y == supmuw.y) {
            node.hasMoo = true;
            neighbors = get8AdjacentNodes(node);
            for (CaveNode neighbor : neighbors) {
                neighbor.hasMoo = true;
            }
        }
    }


    /**
     * Used to generate a random map
     */
    private void initRandomEnvironment() {

        Random rand = new Random();

        for (int i = 1; i <= MAX_HEIGHT; i++) {
            for (int j = 1; j <= MAX_WIDTH; j++) {
                Point point = new Point(j, i);
                CaveNode node = new CaveNode(point.x, point.y);
                grid.put(point, node);

                //add parts to caveNode

                if (i == 1 && j == 1) {
                    node.isSafe = true;
                    node.wasVisited = true;
                    node.pitProbability = 0.0;
                    node.wumpusProbability = 0.0;
                    node.supmuwProbability = 0.0;
                } else {
                    unvisitedNodes.add(node);
                }

                // check to add pit
                int addPit = rand.nextInt(100);
                if (!node.hasGold && !node.hasEntrance && addPit <= this.pitPercent) {
                    initPitNode(node);
                }

                // check to add wall
                int addObstacle = rand.nextInt(100);
                if (!node.hasPit && !node.hasGold && !node.hasEntrance && addObstacle <= this.wallPercent) {
                    initObstacleNode(node);
                }
            }
        }

        // add the mandatory items here

        //use these to track what has been created
        boolean haveWumpus = false;
        boolean haveSupmuw = false;
        boolean haveGold = false;

        //Check to add gold
        while (!haveGold) {
            int xPos = rand.nextInt(MAX_WIDTH) + 1;
            int yPos = rand.nextInt(MAX_HEIGHT) + 1;
            Point point = new Point(xPos, yPos);

            CaveNode node = this.grid.get(new Point(xPos, yPos));

            if (!node.hasObstacle && !node.hasPit) {
                initGoldNode(node);
                haveGold = true;
            }
        }

        while (!haveWumpus) {
            int xPos = rand.nextInt(MAX_WIDTH) + 1;
            int yPos = rand.nextInt(MAX_HEIGHT) + 1;
            CaveNode node = this.grid.get(new Point(xPos, yPos));

            // check to add wumpus
            if (!node.hasEntrance && !node.hasObstacle) {
                initWumpusNode(node);
                haveWumpus = true;

            }
        }

        while (!haveSupmuw) {
            int xPos = rand.nextInt(MAX_WIDTH) + 1;
            int yPos = rand.nextInt(MAX_HEIGHT) + 1;
            CaveNode node = this.grid.get(new Point(xPos, yPos));

            // Check to add supmuw
            if (!node.hasObstacle && !node.hasEntrance) {
                initSupmuwNode(node);
                haveSupmuw = true;
            }
        }

        mapName = "RANDOMIZE";
    }

    public String getMapName() {
        return mapName;
    }

    /**
     * Used to generate a loaded map
     */
    private void initLoadedEnvironment(String path) {

        Fileinput input = new Fileinput(path);

        for (int i = MAX_HEIGHT; i >= 1; i--) {
            String curLine = input.getLine();
            for (int j = 1; j <= MAX_WIDTH; j++) {
                // initialize a CaveNode for this point
                Point point = new Point(j, i);
                CaveNode node = new CaveNode(point.x, point.y);
                grid.put(point, node);

                //add parts to caveNode

                if (i == 1 && j == 1) {
                    node.isSafe = true;
                    node.wasVisited = true;
                    node.pitProbability = 0.0;
                    node.wumpusProbability = 0.0;
                    node.supmuwProbability = 0.0;
                } else {
                    unvisitedNodes.add(node);
                }


                char terrain = curLine.charAt(j - 1);

                if (terrain == 'W') {
                    initObstacleNode(node);
                }
                if (terrain == 'P') {
                    initPitNode(node);
                }
            }
        }

        // add the mandatory items here
        CaveNode node;
        int xPos, yPos;

        int[] goldLoc = input.getParsedInts(" ");
        xPos = goldLoc[0];
        yPos = goldLoc[1];
        node = this.grid.get(new Point(xPos, yPos));
        initGoldNode(node);


        int[] wumpusLoc = input.getParsedInts(" ");
        xPos = wumpusLoc[0];
        yPos = wumpusLoc[1];
        node = this.grid.get(new Point(xPos, yPos));
        initWumpusNode(node);


        int[] supmuwLoc = input.getParsedInts(" ");
        xPos = supmuwLoc[0];
        yPos = supmuwLoc[1];
        node = this.grid.get(new Point(xPos, yPos));
        initSupmuwNode(node);

        mapName = new File(path).getName();

    }


    /**
     * createCaveBorder()
     * Creates the walls that border the cave map.
     */

    public void createCaveBorder() {
        // Create the bordering walls around the main cave map.

        for (int i = 0; i <= 11; i++) // left and right borders
        {
            Point point = new Point(0, i);
            CaveNode borderWall = new CaveNode(point.x, point.y);
            borderWall.hasObstacle = true;
            grid.put(point, borderWall);
            initPercepts(borderWall);

            point = new Point(11, i);
            borderWall = new CaveNode(point.x, point.y);
            borderWall.hasObstacle = true;
            grid.put(point, borderWall);
            initPercepts(borderWall);
        }
        for (int i = 1; i <= 10; i++) // top and bottom borders
        {
            Point point = new Point(i, 0);
            CaveNode borderWall = new CaveNode(point.x, point.y);
            borderWall.hasObstacle = true;
            grid.put(point, borderWall);
            initPercepts(borderWall);

            point = new Point(i, 11);
            borderWall = new CaveNode(point.x, point.y);
            borderWall.hasObstacle = true;
            grid.put(point, borderWall);
            initPercepts(borderWall);
        }
    }


    private void initObstacleNode(CaveNode node) {
        node.hasObstacle = true;
    }


    /**
     * @param node
     */
    private void initSupmuwNode(CaveNode node) {
        node.hasSupmuw = true;
        node.hasMoo = true;
        supmuw = new Supmuw(node.x, node.y, node);
    }


    /**
     * @param node
     */
    private void initWumpusNode(CaveNode node) {
        node.hasWumpus = true;
        node.hasStench = true;
        wumpus = new Wumpus(node.x, node.y);
    }


    /**
     * @param node
     */
    private void initPitNode(CaveNode node) {
        node.hasPit = true;
    }


    /**
     * @param node
     */
    private void initGoldNode(CaveNode node) {
        node.hasGold = true;
        gold = new Gold(node.x, node.y);
    }


    /**
     * returns the adjacent node in the given direction
     */
    public CaveNode getNextNode(CaveNode node, char dir) {
        int xPos = node.x;
        int yPos = node.y;

        switch (dir) {
            case 'N':
                yPos++;
                break;
            case 'S':
                yPos--;
                break;
            case 'E':
                xPos++;
                break;
            case 'W':
                xPos--;
                break;
            default:
                log.d("WumplusEnvironment.getNextNode: Bad direction given: " + dir);
                break;
        }

        return grid.get(new Point(xPos, yPos));
    }

    /**
     * get4AdjacentNodes(CaveNode node)
     * Returns the CaveNodes immediately north, south, east, and west of node.
     */

    public Vector<CaveNode> get4AdjacentNodes(CaveNode node) {
        Vector<CaveNode> neighbors = new Vector<CaveNode>();

        // insert in clockwise order starting from North
        neighbors.add(grid.get(new Point(node.x, node.y + 1)));
        neighbors.add(grid.get(new Point(node.x + 1, node.y)));
        neighbors.add(grid.get(new Point(node.x, node.y - 1)));
        neighbors.add(grid.get(new Point(node.x - 1, node.y)));

        return neighbors;

    }

    /**
     * get8AdjacentNodes(CaveNode node)
     * Returns all 8 CaveNodes adjacent to node.
     */

    public Vector<CaveNode> get8AdjacentNodes(CaveNode node) {
        Vector<CaveNode> neighbors = new Vector<CaveNode>();

        // insert in clockwise order starting from North

        neighbors.add(grid.get(new Point(node.x, node.y + 1)));
        neighbors.add(grid.get(new Point(node.x + 1, node.y + 1)));
        neighbors.add(grid.get(new Point(node.x + 1, node.y)));
        neighbors.add(grid.get(new Point(node.x + 1, node.y - 1)));
        neighbors.add(grid.get(new Point(node.x, node.y - 1)));
        neighbors.add(grid.get(new Point(node.x - 1, node.y - 1)));
        neighbors.add(grid.get(new Point(node.x - 1, node.y)));
        neighbors.add(grid.get(new Point(node.x - 1, node.y + 1)));

        return neighbors;
    }

    /**
     * areNodesAdjacent (CaveNode cn1, CaveNode cn2)
     * Checks if two nodes are adjacent. Returns true if they are.
     * Otherwise returns false.
     */

    public boolean areNodesAdjacent(CaveNode cn1, CaveNode cn2) {
        if (cn1 == null || cn2 == null)
            return false;

        Vector<CaveNode> neighbors = this.get4AdjacentNodes(cn1);

        return neighbors.contains(cn2);
    }

    public String act(AbstractAgent agent) {
        CaveNode curNode = grid.get(new Point(agent.x, agent.y));
        if (curNode.hasWumpus) {
            agent.die("Wumplus");
            return "DIE";
        } else if (curNode.hasPit) {
            agent.die("Pit");
            return "DIE";
        }
        return supmuw.getAction(agent);
    }

    public void getGameState(GameState gameState) {
        gameState.sulpmuwIsDied = supmuw.isDead;
        gameState.wamplusIsDied = wumpus.isDead;
    }



	
	/*
    public CaveNode scanForWumpus(Point location, Directions orientation, Hashtable<CaveNode, Point> worldNodes )
	{
		CaveNode target = table.get(location);
		
		while(target != null && !target.hasObstacle)
		{
			 if(target.hasWumpus)
				return target;
			 target = getNextNode(table, location, orientation);
			 location = worldNodes.get(target);
		}
		
		return null;
	}
	*/
}
