package net.kolipass.wworld.agent;

import net.kolipass.ConfigWumpus;
import net.kolipass.gameEngine.Keyboard;
import net.kolipass.wworld.Action;
import net.kolipass.wworld.WumplusEnvironment;

/**
 * Created by kolipass on 11.12.13.
 */
public class HumanAgent extends AbstractAgent {
    private ConfigWumpus config;

    public HumanAgent(ConfigWumpus config) {
        this.config = config;
    }

    @Override
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
