import java.io.InterruptedIOException;


public class GUIThread extends Thread implements Runnable{
    public GUI gui;
    private String[] args;

    public GUIThread(GUI g, String[] args){
        gui = g;
        this.args = args;
    }

    @Override
    public void run() {
        gui.launchGUI(this.args);
    }
}
