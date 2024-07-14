import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

public class SudokuTimer extends Timer{
    private int minRemain;
    private int secRemain;
    private boolean stillGoing;
    private TimerTask timerTask;
    private SudokuMain game;

    public SudokuTimer(int numMin, SudokuMain game) {
        this.minRemain = numMin;
        this.secRemain = 00;
        this.stillGoing = true;
        this.game = game;
    }

    public void start() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        };
        // Schedule the timer task to run every second
        scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    public void end() {
        timerTask.cancel();
        stillGoing = false;
        // Check if time has run out
        if (minRemain == 0 && secRemain == 0) {
            // Display a message indicating that the player has lost
            JOptionPane.showMessageDialog(game, "Time's up! You lose. Hit reset to try again.");
        }
    }


    private void updateTimer() {
        if (secRemain > 0) { //if there are more than 0 seconds it can be decreased
            secRemain--;
        } else if (minRemain > 0) { //if there are 0 seconds and there are more minutes then start a new minute
            minRemain--;
            secRemain = 59;
        } else {
            stillGoing = false;
            end();
        }

        // Update the UI with the new time using the reference to SudokuMain
        game.updateUITimer();
    }

    public int getMinRemain() {
        return minRemain;
    }

    public int getSecRemain() {
        return secRemain;
    }

    public boolean isStillGoing() {
        return stillGoing;
    }

    public void setTime(int numMin) {
        this.minRemain = numMin;
        this.secRemain = 0; // Reset seconds when setting new time
    }
}
