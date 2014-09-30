/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sokoban;

import java.awt.Point;

/**
 *
 * @author Claude-Clément YAPO
 */
public class monPoint extends Point implements Comparable<monPoint> {

    public monPoint() {
        super();
    }
    
    public monPoint(int x, int y) {
        super(x,y);
    }
    
    @Override
    public int compareTo(monPoint o) {
        return this.x - o.x == 0 ? this.y - o.y : this.x - o.x;
    }

}
