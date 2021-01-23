/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golddigger;

/**
 *
 * @author farukarig
 */
public class Gold {
    public int locY;
    public int locX;
    public int val;
    public boolean isHidden;
    
    public Gold(int locY,int locX, int val, boolean isHidden){
        this.locY = locY;
        this.locX = locX;
        this.val = val;
        this.isHidden = isHidden;
    }
}
