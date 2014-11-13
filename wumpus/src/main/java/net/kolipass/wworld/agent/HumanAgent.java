package main.java.net.kolipass.wworld.agent;

import main.java.net.kolipass.ConfigWumpus;
import main.java.net.kolipass.gameEngine.*;
import main.java.net.kolipass.wworld.*;
import net.kolipass.gameEngine.Keyboard;

/**
 * Created by kolipass on 11.12.13.
 */
public abstract class HumanAgent extends AbstractAgent {
    private final ConfigWumpus config;

    public HumanAgent(ConfigWumpus config) {
        this.config = config;
    }

    public int getNextAction(WumplusEnvironment we, Keyboard keyboard) {
        int action = Action.IDLE;
        if (keyboard.isTyped(config.getVK_LEFT())) {
            action = Action.TURN_LEFT;
        }
        if (keyboard.isTyped(config.getVK_RIGHT())) {
            action = Action.TURN_RIGHT;
        }
        if (keyboard.isTyped(config.getVK_UP())) {
            action = Action.GOFORWARD;
        }
        if (keyboard.isTyped(config.getVK_ENTER())) {
            action = Action.GRAB;
        }
        if (keyboard.isTyped(config.getVK_SHOT())) {
            action = Action.SHOOT;
        }
        if (keyboard.isTyped(config.getVK_CLIMB())) {
            action = Action.CLIMB;
        }
        return action;
    }

    
}
