import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

/**
 * CSC 309 UML Tutor
 * Handles status update display
 *
 * @author Mario Vuletic
 * @author Alex Arrieta
 * @version 1.1
 */
public class StatusPanel extends JPanel implements Observer {
    private JLabel statuslabel;

    /**
     * Builds the status panel
     */
    public StatusPanel(){
        statuslabel = new JLabel(" Status: ");
        statuslabel.setFont(new Font("Arial", Font.PLAIN, 14));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setLayout(new BorderLayout());
        this.add(statuslabel, BorderLayout.CENTER);
    }

    /**
     * Sets what will be displayed in the status panel
     * @param text Text to be displayed as the status
     */
    public void setStatus(String text){
        this.statuslabel.setText(" Status: " + text);
        statuslabel.revalidate();
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     * @param o the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg == null){return;}
        setStatus((String) arg);
    }
}
