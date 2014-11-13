package net.kolipass.wworld;

import net.kolipass.wworld.agent.AbstractAgent;

public class Supmuw {
    ////////

    public int x;
    public int y;
    public boolean isDead;
    public boolean isInPit;
    public boolean isFriendly;
    public boolean hasFood;
    private CaveNode supmuwNode;

    private final int scoreForFood = 100;


    public Supmuw(int x, int y, CaveNode supmuwNode) {
        this.x = x;
        this.y = y;
        isDead = false;
        hasFood = true;
        this.supmuwNode = supmuwNode;
    }

    public String encounter(AbstractAgent agent) {
        if (this.isFriendly && this.hasFood) {
            this.hasFood = false;
            agent.hasFood = true;
            agent.score += this.scoreForFood;

            return "FOODGET";
        } else if (!this.isFriendly) {
            agent.die("Supmuw");
            return "DIE";
        }
        return "SUPMUWWAVES";
    }

    public void updateMode() {
        if (supmuwNode.hasStench)
            isFriendly = false;
        else
            isFriendly = true;

        if (supmuwNode.hasPit)
            isInPit = true;
        else
            isInPit = false;
    }

    public String getAction(AbstractAgent abstractAgent) {
        if (!isDead) {
            if (abstractAgent.x == x && abstractAgent.y == y) {

                String friendly = encounter(abstractAgent);
                if (friendly.equals("FOODGET"))
                    return friendly;
            }
        }
        return null;
    }

}
