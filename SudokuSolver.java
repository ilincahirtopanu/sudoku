import java.util.Random;

public class SudokuSolver {
    private static final int SIZE = 9;

    // Check if the given number can be placed at the specified position
    public static boolean isSafe(int[][] board, int row, int col, int num) {
        // Check if the number is not present in the current row and column
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num || board[x][col] == num) {
                return false;
            }
        }

        // Check if the number is not present in the current 3x3 subgrid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    // Solve the Sudoku puzzle using backtracking
    public static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                // Find an empty cell
                if (board[row][col] == 0) {
                    // Try placing a number from 1 to 9
                    for (int num = 1; num <= SIZE; num++) {
                        if (isSafe(board, row, col, num)) {
                            // If placing the number is safe, try solving the rest of the puzzle
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true; // Puzzle is solved
                            }
                            // If placing the number leads to an invalid solution, backtrack
                            board[row][col] = 0;
                        }
                    }
                    return false; // No valid number can be placed at this cell
                }
            }
        }
        return true; // Puzzle is solved
    }

    // Generate a Sudoku puzzle by removing numbers from the solved puzzle
    public static void generateSudokuPuzzle(int[][] board, int difficultyLevel) {
        Random random = new Random();
        int cellsToRemove = 81 - difficultyLevel;
        
        while (cellsToRemove > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            if (board[row][col] != 0) {
                board[row][col] = 0;
                cellsToRemove--;
            }
        }
    }
}