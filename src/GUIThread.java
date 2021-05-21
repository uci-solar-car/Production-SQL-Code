import java.io.InterruptedIOException;


public class GUIThread extends Thread {
    private GUI gui;
    private String[] args;

    public GUIThread(GUI gui, String[] args){
        this.gui = gui;
        this.args = args;
    }

    public void run() {
        this.gui.launchGUI(this.args);
    }
}
