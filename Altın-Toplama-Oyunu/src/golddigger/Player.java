/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golddigger;

import java.awt.Color;

/**
 *
 * @author farukarig
 */
public class Player {
    public String name;
    public int locY = 0;
    public int locX = 0;
    public int gold;
    
    public Block target = null;
    
    public Color color;
    
    public int m1;
    public int m2;
    
    public int selectType;
    
    public int openHidGoldLim=0;
    
    public boolean stop=false;
    
    
    public Player(Color color, int gold,String name,int m1,int m2,int selectType){
        this.color = color;
        this.gold = gold;
        this.name = name;
        this.m1 = m1;
        this.m2 = m2;
        this.selectType = selectType;
    }
    
    
}
