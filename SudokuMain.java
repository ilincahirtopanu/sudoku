import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SudokuMain extends JFrame {
    private JTextField[][] grid;
    private JPanel panel;
    private SudokuTimer timer;
    private JLabel timeDisplayLabel;
    private int hintCount = 3; // amount of hints, you can edit this
    private JLabel setTimerText;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private JLabel selectDifficulty;
    private String[] options = {"easy", "medium", "hard"};
    private JComboBox<String> dropdown;
    private JButton generateButton;
    private JButton startButton;
    private JButton resetButton;

    

    public SudokuMain() {
        super("Sudoku Game");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeGrid();
        initializeUI();

        timer = new SudokuTimer(5, this);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int finalI = i;
                int finalJ = j;
                grid[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectedRow = finalI;
                        selectedCol = finalJ;
                        
                    }
                });
            }
        }
    }

    private void initializeGrid() {
        grid = new JTextField[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new JTextField();
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
            }
        }
    }

    private void initializeUI() {
        panel = new JPanel(new GridLayout(9, 9, 2, 2));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                panel.add(grid[i][j]);
                //bottom right cell of 3x3
                if ((i + 1) % 3 == 0 && (j + 1) % 3 == 0) {
                    grid[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
                } 
                //bottom cell
                else if ((i + 1) % 3 == 0) {
                    grid[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK));
                } 
                //right cell of 3x3
                else if ((j + 1) % 3 == 0) {
                    grid[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK));
                }
                //regular cell 
                else {
                    grid[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                }
            }
        }
        
    JButton hintButton = new JButton("Give Hint");
    hintButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (hintCount > 0) {
                // Find a random unfilled cell
                int emptyRow, emptyCol;
                do {
                    emptyRow = (int) (Math.random() * 9);
                    emptyCol = (int) (Math.random() * 9);
                } while (!grid[emptyRow][emptyCol].getText().isEmpty());

                int[][] currentPuzzle = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        String text = grid[i][j].getText();
                        currentPuzzle[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
                    }
                }
                SudokuSolver.solveSudoku(currentPuzzle);

                int correctNumber = currentPuzzle[emptyRow][emptyCol];
                grid[emptyRow][emptyCol].setText(String.valueOf(correctNumber));

                hintCount--;

                hintButton.setText("Give Hint (" + hintCount + " left)");
            } else {
                JOptionPane.showMessageDialog(SudokuMain.this, "No more hints available.");
            }
        }
    });

    JButton checkBoxButton = new JButton("Check Box");
    checkBoxButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[][] currentPuzzle = new int[9][9];
            boolean isCorrect = true;
            int incorrectRow = -1;
            int incorrectCol = -1;
    
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = grid[row][col].getText();
                    currentPuzzle[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
                }
            }
    
            boolean isGridFilled = true;
    
            // Check rows and columns
            outerLoop:
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    for (int k = j + 1; k < 9; k++) {
                        if (currentPuzzle[i][j] != 0 && currentPuzzle[i][k] != 0 && currentPuzzle[i][j] == currentPuzzle[i][k]) {
                            isCorrect = false;
                            incorrectRow = i;
                            incorrectCol = j; // Assign the first incorrect column
                            break outerLoop;
                        }
                        if (currentPuzzle[j][i] != 0 && currentPuzzle[k][i] != 0 && currentPuzzle[j][i] == currentPuzzle[k][i]) {
                            isCorrect = false;
                            incorrectRow = k; // Assign the first incorrect row
                            incorrectCol = i;
                            break outerLoop;
                        }
                    }
                }
            }
    
            // Check if the grid is completely filled
            outerLoop:
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (currentPuzzle[i][j] == 0) {
                        isGridFilled = false;
                        break outerLoop;
                    }
                }
            }
    
            if (isCorrect && isGridFilled) {
                JOptionPane.showMessageDialog(SudokuMain.this, "You Win!");
                clearGrid();
            } else {
                if (!isCorrect) {
                    JOptionPane.showMessageDialog(SudokuMain.this, "Incorrect at row " + (incorrectRow + 1) + ", column " + (incorrectCol + 1));
                } else {
                    JOptionPane.showMessageDialog(SudokuMain.this, "The grid is not fully filled. Keep going!");
                }
            }
        }
    });


    generateButton = new JButton("Generate New Board");
    generateButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Reset hintCount when generating a new board
            hintCount = 3;
            hintButton.setText("Give Hint (" + hintCount + " left)");
            startButton.setVisible(true);
            int[][] puzzle = new int[9][9];
            SudokuSolver.solveSudoku(puzzle);
            //chosing difficulty
            String selection = (String)dropdown.getSelectedItem();
            int difficulty;
            if (selection.equals("easy")) {
                difficulty = 45;
            } else if (selection.equals("medium")) {
                difficulty = 30;
            } else {
                difficulty = 15;
            }
            SudokuSolver.generateSudokuPuzzle(puzzle, difficulty);
            displayPuzzle(puzzle);
        }
    });

    //start the game
    JTextField timeTextField = new JTextField("5", 5); // Default time is 5 minutes
    startButton = new JButton("Start");
    startButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Parse the entered time and set it to the timer
            try {
                int newTime = Integer.parseInt(timeTextField.getText());
                timer.setTime(newTime);
                //hiding unneccessary things
                timeTextField.setVisible(false);
                startButton.setVisible(false);   
                setTimerText.setVisible(false); 
                generateButton.setVisible(false);
                selectDifficulty.setVisible(false);
                dropdown.setVisible(false);

                //setting up buttons
                hintButton.setVisible(true);
                checkBoxButton.setVisible(true);
                resetButton.setVisible(true);

                //setting up timer
                int minutes = timer.getMinRemain();
                int seconds = timer.getSecRemain();
                timeDisplayLabel.setText("Time Remaining: " + minutes + ":" + seconds);
                timeDisplayLabel.setVisible(true);
                timer.start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(SudokuMain.this, "Invalid time format. Please enter a valid integer.");
            }
        }
    });

    resetButton = new JButton("Reset");
    resetButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer.end();
            timeTextField.setVisible(true);
            startButton.setVisible(true);   
            setTimerText.setVisible(true); 
            generateButton.setVisible(true);
            selectDifficulty.setVisible(true);
            dropdown.setVisible(true);
            hintButton.setVisible(false);
            checkBoxButton.setVisible(false);
            timeDisplayLabel.setVisible(false);
            startButton.setVisible(false);
            hintCount = 3;
            resetButton.setVisible(false);
            clearGrid();
        }
    });

        JPanel top = new JPanel();
        setTimerText = new JLabel("Set Timer (minutes):");
        top.add(setTimerText);
        top.add(timeTextField);

        timeDisplayLabel = new JLabel("Time Remaining: ");
        timeDisplayLabel.setVisible(false);
        top.add(timeDisplayLabel);

        JPanel buttons = new JPanel();
        hintButton.setVisible(false);
        buttons.add(hintButton);
        checkBoxButton.setVisible(false);
        buttons.add(checkBoxButton);

        selectDifficulty = new JLabel("Set Difficulty(easy, medium, hard):");
        dropdown = new JComboBox<String>(options);

        buttons.add(selectDifficulty);
        buttons.add(dropdown);
        buttons.add(generateButton);
        startButton.setVisible(false);
        buttons.add(startButton);
        buttons.add(resetButton);
        resetButton.setVisible(false);


        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        container.add(buttons, BorderLayout.SOUTH);
        container.add(top, BorderLayout.NORTH);
    }
    

    private void clearGrid() {
        selectedRow = -1;  // Resets selected box 
        selectedCol = -1;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setText("");
            }
        }
    }

    private void displayPuzzle(int[][] puzzle) {
        selectedRow = -1;
        selectedCol = -1;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setText(puzzle[i][j] == 0 ? "" : String.valueOf(puzzle[i][j]));
            }
        }
    }

    public void updateUITimer() {
        int minutes = timer.getMinRemain();
        int seconds = timer.getSecRemain();
        timeDisplayLabel.setText("Time Remaining: " + minutes + ":" + seconds);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuMain().setVisible(true);
            }
        });
    }
}
