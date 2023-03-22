import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;

/**
 * CSC 309 UML Tutor
 * Handles metrics of user
 *
 * @author Thien An Tran
 * @version 1.0
 */
public class Metrics implements Observer{
    private int attemptsCount = 0;
    private JFrame frame = new JFrame("User's Metrics");
    private JLabel timeLabel = new JLabel();
    private JPanel panel = new JPanel();
    private int elapsedTime = 0;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private String secondsString = String.format("%02d", seconds);
    private String minutesString = String.format("%02d", minutes);
    private String hoursString = String.format("%02d", hours);
    private JLabel attemptLabel = new JLabel();

    private JLabel overallTimeLabel = new JLabel();
    private int overallTime = 0;
    private int overallSec = 0;
    private int overallMin = 0;
    private int overallHr = 0;
    private String overallSecStr = String.format("%02d", overallSec);
    private String overallMinStr = String.format("%02d", overallMin);
    private String overallHrStr = String.format("%02d", overallHr);

    /**
     * Metrics constructor
     */
    public Metrics(){

    }

    private Timer attemptTimer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            elapsedTime = elapsedTime + 1000;
            hours = (elapsedTime/(1000*60*60)) % 24;
            minutes = (elapsedTime/(1000*60)) % 60;
            seconds = (elapsedTime/1000) % 60;
            secondsString = String.format("%02d", seconds);
            minutesString = String.format("%02d", minutes);
            hoursString = String.format("%02d", hours);
            timeLabel.setText("\tAttempt Time taken: "+hoursString+":"+minutesString+":"+secondsString);
        }
    });

    private Timer overallTimer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            overallTime = overallTime + 1000;
            overallHr = (overallTime/(1000*60*60)) % 24;
            overallMin = (overallTime/(1000*60)) % 60;
            overallSec = (overallTime/1000) % 60;
            overallSecStr = String.format("%02d", overallSec);
            overallMinStr = String.format("%02d", overallMin);
            overallHrStr = String.format("%02d", overallHr);
            overallTimeLabel.setText("\tOverall Time taken: "+overallHrStr+":"+overallMinStr+":"+ overallSecStr);
        }
    });

    /**
     * Displays the current metrics stored
     */
    public void showMetrics(){
        timeLabel.setText("\tTime taken: "+hoursString+":"+minutesString+":"+secondsString);
        attemptLabel.setText("\tAttempts taken: " + attemptsCount);
        overallTimeLabel.setText("\tOverall time taken: "+overallHr+":"+overallMin+":"+overallSec);

        GridLayout grid = new GridLayout(3, 1);
        panel.setLayout(grid);
        panel.add(timeLabel);
        panel.add(attemptLabel);
        panel.add(overallTimeLabel);
        frame.add(panel);

        frame.setSize(200,200);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Starts the timer for how long a problem is being attempted & the overall timer
     */
    public void startTimer(){
        attemptTimer.start();
        overallTimer.start();
    }

    /**
     * Stops all the active timer
     */
    public void stopTimer() {
        attemptTimer.stop();
        overallTimer.stop();
    }

    /**
     * Sets the attempt timer to 0
     */
    public void resetAttemptTimer() {
        elapsedTime = 0;
        seconds = 0;
        minutes = 0;
        hours = 0 ;
        secondsString = String.format("%02d", seconds);
        minutesString = String.format("%02d", minutes);
        hoursString = String.format("%02d", hours);
        timeLabel.setText("\tAttempt Time taken: "+hoursString+":"+minutesString+":"+secondsString);
    }

    /**
     * Sets all timers to 0
     */
    public void resetAllTimer(){
        overallTime = 0;
        overallSec = 0;
        overallMin = 0;
        overallHr = 0;
        overallSecStr = String.format("%02d", overallSec);
        overallMinStr = String.format("%02d", overallMin);
        overallHrStr = String.format("%02d", overallHr);
        overallTimeLabel.setText("\tOverall time taken: "+overallHrStr+":"+overallMinStr+":"+ overallSecStr);

        elapsedTime = 0;
        seconds = 0;
        minutes = 0;
        hours = 0 ;
        secondsString = String.format("%02d", seconds);
        minutesString = String.format("%02d", minutes);
        hoursString = String.format("%02d", hours);
        timeLabel.setText("\tAttempt time taken: "+hoursString+":"+minutesString+":"+secondsString);
    }

    /**
     * Increment the amount of attempts the user has made
     */
    public void incAttemptCount(){
        attemptsCount++;
    }

    /**
     * Gets the amount of time taken to solve problem
     * @return The string of the time taken
     */
    public String getTimeTaken(){
        return hoursString+":"+minutesString+":"+secondsString;
    }

    /**
     * Gets the current date and time for submission
     * @return The string of the cuurent date time
     */
    public String getCurrDateTime(){
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ldt.format(myFormatObj);
    }

    /**
     * Gets the number of attempts
     * @return The number of attempts
     */
    public int getAttemptsCount() {
        return attemptsCount;
    }

    /**
     * Gets the number of hours
     * @return The number of hours
     */
    public int getHours() {
        return hours;
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub

    }

}
