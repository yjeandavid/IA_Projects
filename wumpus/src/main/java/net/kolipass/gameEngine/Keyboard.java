package net.kolipass.gameEngine;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

public class Keyboard implements KeyListener {
    public int lastTyped = 0;
    private Hashtable<Integer, Boolean> keysPressed; // a keycode is mapped to true if it is pressed, false if it isn't pressed.
    private Hashtable<Integer, Boolean> keysTyped; // a keycode is mapped to true if it is typed, false if it wasn't just typed.

    public Keyboard() {
        keysPressed = new Hashtable<Integer, Boolean>();

        keysTyped = new Hashtable<Integer, Boolean>();
    }


    public void keyPressed(KeyEvent e) {
        keysPressed.put(new Integer(e.getKeyCode()), new Boolean(true));
        lastTyped = e.getKeyCode();
    }

    public void keyTyped(KeyEvent e) {
        //lastTyped = e.getKeyCode();
    }

    public void keyReleased(KeyEvent e) {
        keysPressed.put(new Integer(e.getKeyCode()), new Boolean(false));
        keysTyped.put(new Integer(e.getKeyCode()), new Boolean(false));
    }

    public boolean isPressed(int keycode) {
        //return false;
        try {
            Boolean mykey = keysPressed.get(keycode);
            if (mykey == null)
                return false;
            else
                return mykey;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean isTyped(int keycode) {
        try {
            Boolean pressedKey = keysPressed.get(keycode);
            Boolean myKey = keysTyped.get(keycode);
            if (pressedKey == null)
                return false;
            else if (pressedKey.booleanValue() && (myKey == null || !myKey.booleanValue())) {
                keysTyped.put(keycode, new Boolean(true));
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * updateTyped()
     * For isTyped(int keycode) to work properly, this method must be called at the end of the level.
     */

    public void updateTyped() {
        keysTyped.clear();
    }


}
