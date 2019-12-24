package com.example.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class MyGame {

    // Name-constants to represent the seeds and cell contents
    public final int EMPTY = 0;
    public final int CROSS = 1;
    public final int NOUGHT = 2;

    // Name-constants to represent the various states of the game
    public final int PLAYING = 0;
    public final int CROSS_WON = 1;
    public final int NOUGHT_WON = 2;
    public final int DRAW = 3;

    // The game board and the game status
    public static final int ROWS = 3, COLS = 3; // number of rows and columns
    public static int[][] board = new int[ROWS][COLS]; // game board in 2D array
    //  containing (EMPTY, CROSS, NOUGHT)
    public static int currentState;  // the current state of the game
    // (PLAYING, DRAW, CROSS_WON, NOUGHT_WON)
    public static int currentPlayer; // the current player (CROSS or NOUGHT)
    public static int currentRow, currentCol; // current seed's row and column

    MainActivity minimaxActivity;

    public MyGame(MainActivity minimaxActivity) {
        this.minimaxActivity = minimaxActivity;
    }

    public void resetBoard() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                board[i][j] = 0;
            }
        }
    }

    /**
     * Get next best move for computer. Return int[2] of {row, col}
     */
    public int[] move() {
        int[] result = minimax(2, NOUGHT); // depth, max turn
        return new int[]{result[1], result[2]};   // row, col
    }

    public int[] minimax(int depth, int player) {
        // Generate possible next moves in a List of int[2] of {row, col}.
        List<int[]> nextMoves = generateMoves();

        // mySeed(NOUGHT) is maximizing; while oppSeed(CROSS) is minimizing
        int bestScore = (player == NOUGHT) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves) {
                board[move[0]][move[1]] = player;
                if (player == NOUGHT) { // NOUGHT is Maximizing Player
                    currentScore = minimax(depth - 1, CROSS)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else { // CROSS is Minimizing Player
                    currentScore = minimax(depth - 1, NOUGHT)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                // Undo move
                board[move[0]][move[1]] = EMPTY;
            }
        }
        return new int[]{bestScore, bestRow, bestCol};
    }

    private int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
        return score;
    }

    /**
     * The heuristic evaluation function for the given line of 3 cells
     *
     * @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     * -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        // First cell
        if (board[row1][col1] == NOUGHT) {
            score = 1;
        } else if (board[row1][col1] == CROSS) {
            score = -1;
        }

        // Second cell
        if (board[row2][col2] == NOUGHT) {
            if (score == 1) {   // cell1 is mySeed
                score = 10;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (board[row2][col2] == CROSS) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (board[row3][col3] == NOUGHT) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (board[row3][col3] == CROSS) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List

        int State = CheckGameState();
        // If gameover, i.e., no next move
        if (State == 1 || // X Won
                State == 2 || // O Won
                State == 3)   // Draw
        {
            return nextMoves;   // return empty list
        }

        // Search for empty cells and add to the List
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == EMPTY) {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }
        return nextMoves;
    }

    public void placeAMove(int x, int y, int player) {
        board[x][y] = player;   //player = 1 for X, 2 for O
    }

    private int CheckGameState() {
        //

        //0 - Playing

        //1 - X Won

        //2 - O Won

        //3 - Draw

        //

        // Check Rows - Horizontal Lines
        for (int i = 0; i < ROWS; i++) {
            if (board[i][0] == CROSS &&
                    board[i][1] == CROSS &&
                    board[i][2] == CROSS) {
                return CROSS_WON;
            }
            if (board[i][0] == NOUGHT &&
                    board[i][1] == NOUGHT &&
                    board[i][2] == NOUGHT) {
                return NOUGHT_WON;
            }
        }

        // Check Columns - Vertical Lines
        for (int i = 0; i < COLS; i++) {
            if (board[0][i] == CROSS &&
                    board[1][i] == CROSS &&
                    board[2][i] == CROSS) {
                return CROSS_WON;
            }
            if (board[0][i] == NOUGHT &&
                    board[1][i] == NOUGHT &&
                    board[2][i] == NOUGHT) {
                return NOUGHT_WON;
            }
        }

        // Check Diagonal
        if (board[0][0] == CROSS &&
                board[1][1] == CROSS &&
                board[2][2] == CROSS) {
            return CROSS_WON;
        }
        if (board[0][0] == NOUGHT &&
                board[1][1] == NOUGHT &&
                board[2][2] == NOUGHT) {
            return NOUGHT_WON;
        }


        // Check Reverse-Diagonal
        if (board[0][2] == CROSS &&
                board[1][1] == CROSS &&
                board[2][0] == CROSS) {
            return CROSS_WON;
        }
        if (board[0][2] == NOUGHT &&
                board[1][1] == NOUGHT &&
                board[2][0] == NOUGHT) {
            return NOUGHT_WON;
        }

        // Check for Tie
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] != CROSS && board[i][j] != NOUGHT) {
                    return PLAYING;
                }
            }
        }

        return DRAW;
    }

}