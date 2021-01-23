/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golddigger;

import javax.swing.JToggleButton;

/**
 *
 * @author farukarig
 */
public class Block {
   public JToggleButton button;
   
   public Gold gold = null;
    
   public Block(JToggleButton button,Gold gold) {
       this.button = button;
       this.gold = gold;
   }
}
