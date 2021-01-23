/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golddigger;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;

/**
 *
 * @author farukarig
 */
public class Board extends javax.swing.JFrame {

    /**
     * Creates new form Board
     */
    public Board() {
        initComponents();
    }
    
    Settings settings;
    Block[][] blocks;
    ArrayList<Gold> goldList=new ArrayList<Gold>();
    int orderPlayer = 0;
    
    int[][] statistics;
    
    Player[] players = new Player[4];
    
    Board(Settings settings,int AM1,int AM2,int BM1,int BM2,int CM1,int CM2,int DM1,int DM2, int openHidGoldLim) {
        initComponents();
        this.settings = settings;
        this.blocks = new Block[settings.sizeY][settings.sizeX];
               
        // Kutuların oluşturulması
        for(int i = 0; i<settings.sizeY; i++){
            for(int j = 0; j<settings.sizeX; j++){
                blocks[i][j] = new Block(new JToggleButton(),null);
                blocks[i][j].button.setSize(gameBoard.getWidth()/settings.sizeX, gameBoard.getHeight()/settings.sizeY );
                blocks[i][j].button.setLocation(j*gameBoard.getWidth()/settings.sizeX, i*gameBoard.getHeight()/settings.sizeY);
                blocks[i][j].button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                //blocks[i][j].button.setText(i + " - " + j);
                blocks[i][j].button.setContentAreaFilled(true);
                blocks[i][j].button.setOpaque(true);
                blocks[i][j].button.setEnabled(false);
                gameBoard.add(blocks[i][j].button);
            }
        }
        
        // Altınların Dağıtılması
        int goldCount = settings.sizeX*settings.sizeY*settings.goldPer/100;
        int hiddenGoldCountW = goldCount*settings.hiddenGoldPer/100;
        
        //this.goldList = new Gold[0];
        
        for(int i=0;i<goldCount;i++){
            createRandomGold(false);
        }
        
        for(int i=0;i<hiddenGoldCountW;i++){
            createRandomGold(true);
        }
        this.players[0] = new Player(Color.RED,settings.startGold, "A",AM1,AM2,1);
        this.players[1] = new Player(Color.BLUE,settings.startGold, "B",BM1,BM2,2);
        this.players[2] = new Player(Color.GREEN,settings.startGold, "C",CM1,CM2,3);
        this.players[3] = new Player(Color.MAGENTA,settings.startGold, "D",DM1,DM2,4);
        
        this.players[2].openHidGoldLim = openHidGoldLim;
        
        movePlayer(this.players[1],0,settings.sizeX-1);
        movePlayer(this.players[2],settings.sizeY-1,0);
        movePlayer(this.players[3],settings.sizeY-1,settings.sizeX-1);
        movePlayer(this.players[0],0,0);
        
        this.statistics = new int[4][4];
        
        this.setVisible(true);
        //play();
        
    }
    
    public void play(){
            Player ordP = players[orderPlayer];
            if(ordP.target==null){
                selectTarget(ordP);
            }else if(ordP.target.gold==null){
                selectTarget(ordP);
            }
            

            if(!players[orderPlayer].stop){
                if(ordP.gold>=ordP.m1){
                ordP.gold -= ordP.m1;
                statistics[1][orderPlayer] +=ordP.m1;
                for(int i=0;i<settings.step;i++){
                    if(ordP.locX<ordP.target.gold.locX){
                        movePlayer(ordP,ordP.locY,ordP.locX+1);
                    }else if(ordP.locX>ordP.target.gold.locX){
                        movePlayer(ordP,ordP.locY,ordP.locX-1);
                    }else if(ordP.locY<ordP.target.gold.locY){
                        movePlayer(ordP,ordP.locY+1,ordP.locX);
                    }else if(ordP.locY>ordP.target.gold.locY){
                        movePlayer(ordP,ordP.locY-1,ordP.locX);
                    }

                    if(blocks[ordP.locY][ordP.locX].gold!=null){
                        if(blocks[ordP.locY][ordP.locX].gold.isHidden){
                            blocks[ordP.locY][ordP.locX].gold.isHidden = false;
                            blocks[ordP.locY][ordP.locX].button.setBackground(Color.ORANGE);
                        }else{
                            if(ordP.locY==ordP.target.gold.locY && ordP.locX==ordP.target.gold.locX){

                                int delIndex = 0;
                                for(Gold gold:goldList){
                                    if(gold.locX==ordP.locX && gold.locY==ordP.locY){
                                        goldList.remove(delIndex);
                                        break;
                                    }
                                    delIndex++;
                                }
                                System.out.println(ordP.name + "oyuncusu altın aldı: " + blocks[ordP.locY][ordP.locX].gold.val);
                                ordP.gold += blocks[ordP.locY][ordP.locX].gold.val;
                                statistics[2][orderPlayer] += blocks[ordP.locY][ordP.locX].gold.val;
                                blocks[ordP.locY][ordP.locX].button.setBackground(Color.WHITE);

                                ordP.target = null;
                                blocks[ordP.locY][ordP.locX].gold = null;
                                isGameFinish();
                                break;
                            }
                        }
                    }
                }
                }else{
                    System.out.println("Altını hamle yapmaya yetmediği için elenen oyuncu: "+ordP.name);
                }
            }else{
                System.out.println(players[orderPlayer].name + " duruyor");
            }
        orderPlayer++;
        if(orderPlayer==4)
            orderPlayer =0;
    }
    
    public void isGameFinish(){
        boolean gameFinish = true;
        for(Gold gold:goldList){
            gameFinish = false;
        }
        if(gameFinish){
            this.Play.setEnabled(false);
            statistics[3][0] = players[0].gold;
            statistics[3][1] = players[1].gold;
            statistics[3][2] = players[2].gold;
            statistics[3][3] = players[3].gold;
            new Finish(statistics).setVisible(true);
        }
            
    }
    
    public void selectTarget(Player player){
        if(player.gold<player.m2){
            System.out.println("Altını hedeflemeye yetmediği için elenen oyuncu: "+player.name);
            player.stop = true;
            return;
        }
        
        Gold tmpGold = null;
        
        if(player.selectType==1){
            int minDistance = 999;
            for(Gold gold:goldList){
                if(!gold.isHidden){
                    int distance = Math.abs(player.locX-gold.locX) + Math.abs(player.locY-gold.locY);
                    if(distance < minDistance){
                        minDistance = distance;
                        tmpGold = gold;
                    }
                }
                //System.out.println(gold.locY + " - " + gold.locX + " Uzaklık: " + distance);
            }
        }else if(player.selectType==2){
            double maxGain = -999;
            for(Gold gold:goldList){
                if(!gold.isHidden){
                    double gain = gold.val-Math.ceil(Math.abs(player.locX-gold.locX) + Math.abs(player.locY-gold.locY)/3)*player.m1;
                    if(gain > maxGain){
                        maxGain = gain;
                        tmpGold = gold;
                    }
                    //System.out.println(gold.locY + " - " + gold.locX + " Kazanç: " + gain);
                }
            }
        }else if(player.selectType==3){
            for(int i=0;i<player.openHidGoldLim;i++){
                int minDistance = 999;
                Gold tmpHidGold = null;
                for(Gold gold:goldList){
                    if(gold.isHidden){
                        int distance = Math.abs(player.locX-gold.locX) + Math.abs(player.locY-gold.locY);
                        if(distance < minDistance){
                            minDistance = distance;
                            tmpHidGold = gold;
                        }
                    }
                }
                if(tmpHidGold!=null){
                    blocks[tmpHidGold.locY][tmpHidGold.locX].gold.isHidden = false;
                    blocks[tmpHidGold.locY][tmpHidGold.locX].button.setBackground(Color.ORANGE);
                }
            }
            double maxGain = -999;
            for(Gold gold:goldList){
                if(!gold.isHidden){
                    double gain = gold.val-Math.ceil(Math.abs(player.locX-gold.locX) + Math.abs(player.locY-gold.locY)/3)*player.m1;
                    if(gain > maxGain){
                        maxGain = gain;
                        tmpGold = gold;
                    }
                    //System.out.println(gold.locY + " - " + gold.locX + " Kazanç: " + gain);
                }
            }
        }else if(player.selectType==4){
            double maxGain = -999;
            for(Gold gold:goldList){
                if(!gold.isHidden){
                    double gain = gold.val-Math.ceil(Math.abs(player.locX-gold.locX) + Math.abs(player.locY-gold.locY)/3)*player.m1;
                    if(gain > maxGain){
                        boolean notTarget = false;
                        for(int i=0;i<4;i++){
                            if(players[i].target!=null && players[i].target.gold!=null){
                                if(players[i].target.gold.locY == gold.locY && players[i].target.gold.locX == gold.locX){
                                    int otherDistance = Math.abs(players[i].locX-gold.locX) + Math.abs(players[i].locY-gold.locY);
                                    int ordPDistance = Math.abs(player.locX-gold.locX) + Math.abs(player.locY-gold.locY);
                                    if(otherDistance<=ordPDistance){
                                        notTarget = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if(!notTarget){
                            maxGain = gain;
                            tmpGold = gold;
                        }else{
                            continue;
                        }
                    }
                    //System.out.println(gold.locY + " - " + gold.locX + " Kazanç: " + gain);
                }
            }
        }
        if(tmpGold!=null){
            player.stop = false;
            player.target = blocks[tmpGold.locY][tmpGold.locX];
            player.gold -= player.m2;
            statistics[1][orderPlayer] +=player.m2;
        }else{
            player.stop = true;
        }
        
        if(player.target!=null)
            System.out.println(player.name + " oyuncusu hedefini belirledi: "+player.target.gold.locY + "-" + player.target.gold.locX);
        
    }
    
    public void createRandomGold(boolean isHidden){
        int randomY = new Random().nextInt(settings.sizeY);
        int randomX = new Random().nextInt(settings.sizeX);
        
        if(
            (randomY==0 && randomX==settings.sizeX-1) ||
            (randomY==settings.sizeY-1 && randomX==0) ||
            (randomY==settings.sizeY-1 && randomX==settings.sizeX-1) ||
            (randomY==0 && randomX==0)
        ){
            createRandomGold(isHidden);
            return;
        }
        
        if(isHidden){
            if(blocks[randomY][randomX].gold!=null && !blocks[randomY][randomX].gold.isHidden){
                blocks[randomY][randomX].gold.isHidden = true;
                blocks[randomY][randomX].button.setBackground(Color.DARK_GRAY);
            }else{
                createRandomGold(isHidden);
            }
        }else{
            if(blocks[randomY][randomX].gold==null){
                int gold = (new Random().nextInt(4)+1)*5;
                Gold newGold = new Gold(randomY,randomX,gold,false);
                goldList.add(newGold);
                blocks[randomY][randomX].gold = newGold;
                blocks[randomY][randomX].button.setText(String.valueOf(gold));
                blocks[randomY][randomX].button.setBackground(Color.ORANGE);
            }else{
                createRandomGold(isHidden);
            }
        }
    }

    public void movePlayer(Player player,int newY,int newX){
        
        blocks[player.locY][player.locX].button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        for(int i=0;i<players.length;i++){
            if(player!=players[i])
                if(players[i].locX==player.locX && players[i].locY==player.locY){
                    blocks[players[i].locY][players[i].locX].button.setBorder(BorderFactory.createLineBorder(players[i].color, 3));
                }
        }
        if(statistics!=null)
            statistics[0][orderPlayer]++;
        
        player.locX = newX;
        player.locY = newY;
        
        blocks[player.locY][player.locX].button.setBorder(BorderFactory.createLineBorder(player.color, 3));
        
        for(int i=0;i<players.length;i++){
            if(player!=players[i])
                if(players[i].locX==player.locX && players[i].locY==player.locY){
                    blocks[players[i].locY][players[i].locX].button.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(player.color, 3),players[i].name + "-"+player.name));
                }
        }

    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gameBoard = new javax.swing.JPanel();
        Play = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 650));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        Play.setText("Oynat");
        Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gameBoardLayout = new javax.swing.GroupLayout(gameBoard);
        gameBoard.setLayout(gameBoardLayout);
        gameBoardLayout.setHorizontalGroup(
            gameBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gameBoardLayout.createSequentialGroup()
                .addContainerGap(371, Short.MAX_VALUE)
                .addComponent(Play)
                .addGap(368, 368, 368))
        );
        gameBoardLayout.setVerticalGroup(
            gameBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gameBoardLayout.createSequentialGroup()
                .addContainerGap(585, Short.MAX_VALUE)
                .addComponent(Play)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        
        for(int i = 0; i<settings.sizeY; i++){
            for(int j = 0; j<settings.sizeX; j++){ 
                blocks[i][j].button.setSize(gameBoard.getWidth()/settings.sizeX, gameBoard.getHeight()/settings.sizeY );
                gameBoard.add(blocks[i][j].button);
                blocks[i][j].button.setLocation(j*gameBoard.getWidth()/settings.sizeX, i*gameBoard.getHeight()/settings.sizeY);
                
            }
        }
    
    }//GEN-LAST:event_formComponentResized

    private void PlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayActionPerformed
        // TODO add your handling code here:
        play();
    }//GEN-LAST:event_PlayActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Board().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Play;
    private javax.swing.JPanel gameBoard;
    // End of variables declaration//GEN-END:variables
}
