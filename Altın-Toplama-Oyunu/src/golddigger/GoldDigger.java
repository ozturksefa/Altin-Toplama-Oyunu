
package golddigger;

public class GoldDigger {

    public static void main(String[] args) {
        /*Board br = new Board();
        br.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        br.show();*/
        Settings settings = new Settings();
        Start start = new Start(settings);
        start.setVisible(true);
    }
    
}
