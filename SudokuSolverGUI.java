import java.awt.*;
import java.awt.event.*;

public class SudokuSolverGUI extends Frame {
    private static final int SIZE = 9;
    private static TextField[][] cells = new TextField[SIZE][SIZE];
    private Button solveButton;

    public SudokuSolverGUI() {
        setLayout(new BorderLayout());
        Panel gridPanel = new Panel(new GridLayout(SIZE, SIZE));

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new TextField(1);
                cells[row][col].setText("");
                gridPanel.add(cells[row][col]);
            }
        }   

        solveButton = new Button("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] board = new int[SIZE][SIZE];    

                try {
                    for (int row = 0; row < SIZE; row++) {
                        for (int col = 0; col < SIZE; col++) {
                            String text = cells[row][col].getText();
                            board[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
                        }
                    }
                    for(int i=0;i<1;i++)
                    if (!isValidInput(board)) {
                        throw new IllegalArgumentException("Invalid input: Duplicate values in the same row or column.");
                    }

                    new Thread(() -> {
                        if (solveSudoku(board)) {
                            updateBoard(board);
                        } else {
                            System.out.println("No solution exists.");
                        }
                    }).start();

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: Please enter only numbers between 1-9.");
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage() + " No solution exists.");
                }
            }
        });
        
        add(gridPanel, BorderLayout.CENTER);
        add(solveButton, BorderLayout.SOUTH);

        setSize(400, 450);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
                //validate();
                
            }
        });
    }

    private static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            updateBoard(board);
                            try {
                                Thread.sleep(500); 
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                            updateBoard(board);
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValidInput(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            boolean[] rowCheck = new boolean[SIZE + 1];
            boolean[] colCheck = new boolean[SIZE + 1];

            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != 0) { 
                    if (rowCheck[board[i][j]]) {
                        return false;
                    }
                    rowCheck[board[i][j]] = true;
                }

                if (board[j][i] != 0) { 
                    if (colCheck[board[j][i]]) {
                        return false;
                    }
                    colCheck[board[j][i]] = true;
                }
            }
        }
        return true;
    }

    private static void updateBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                final int r = row, c = col;
                String value = (board[row][col] == 0) ? "" : String.valueOf(board[row][col]);
                EventQueue.invokeLater(() -> cells[r][c].setText(value));
            }
        }
    }

    public static void main(String[] args) {
        new SudokuSolverGUI();
    }
}
