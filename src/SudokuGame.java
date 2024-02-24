import java.util.Scanner;

public class SudokuGame {

    //tabuleiro atual
    private int[][] board;
    //tabuleiro original do jogo
    private int[][] originalBoard;

    public SudokuGame() {
        board = new int[9][9];
        originalBoard = new int[9][9];
        initializeBoard();
        generateSudoku();
        copyBoard(board, originalBoard);
    }

//  Inicializa a matriz board com zeros.
    private void initializeBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0;
            }
        }
    }

//    Copia os valores de uma matriz de origem para uma matriz de destino.
    private void copyBoard(int[][] source, int[][] destination) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, 9);
        }
    }

//    Imprime o tabuleiro na tela, destacando os números originais em verde.
    private void printBoard() {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("---------------------");
            }
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }

            }
            System.out.println();
        }
    }



//  Verifica se é válido inserir um número em uma determinada posição do tabuleiro.
    private boolean isValidMove(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

//    Utiliza a técnica de backtracking(diversas possibilidades) para resolver o Sudoku.
    private boolean solveSudoku() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValidMove(row, col, num)) {
                            board[row][col] = num;

                            if (solveSudoku()) {
                                return true;
                            }

                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

//    Gera um Sudoku resolvido e remove alguns valores para criar um tabuleiro inicial para o jogador.
    private void generateSudoku() {
        solveSudoku();
        copyBoard(board, originalBoard);

        int emptyCells = 40;

        while (emptyCells > 0) {
            int row = (int) (Math.random() * 9);
            int col = (int) (Math.random() * 9);

            if (board[row][col] != 0) {
                board[row][col] = 0;
                emptyCells++;
            }
        }
    }

    private boolean isFilled() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBoardValid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (originalBoard[i][j] != 0 && board[i][j] != originalBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isRowComplete(int row) {
        for (int j = 0; j < 9; j++) {
            if (board[row][j] == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isColumnComplete(int col) {
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isSquareComplete(int row, int col) {
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }

        return true;
    }


    private boolean isGameComplete() {
        for (int i = 0; i < 9; i++) {
            if (!isRowComplete(i) || !isColumnComplete(i)) {
                return false;
            }
        }

        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                if (!isSquareComplete(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SudokuGame sudokuGame = new SudokuGame();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao Sudoku!");
        System.out.println("Tabuleiro Inicial:");
        sudokuGame.printBoard();

        while (!sudokuGame.isGameComplete() || !sudokuGame.isBoardValid()) {
            System.out.println("\nInsira sua jogada (linha coluna valor), ou '0' para sair:");
            int row = scanner.nextInt();
            if (row == 0) {
                System.out.println("Jogo encerrado.");
                break;
            }
            int col = scanner.nextInt();
            int value = scanner.nextInt();

            if (row < 1 || row > 9 || col < 1 || col > 9 || value < 1 || value > 9) {
                System.out.println("Jogada inválida. Tente novamente.");
                continue;
            }

            if (sudokuGame.originalBoard[row - 1][col - 1] != 0) {
                System.out.println("Você não pode modificar este valor. Tente novamente.");
                continue;
            }

            if (sudokuGame.isValidMove(row - 1, col - 1, value)) {
                sudokuGame.board[row - 1][col - 1] = value;
                sudokuGame.printBoard();

                // Verificar se uma linha, coluna ou quadrado foi completado
                if (sudokuGame.isRowComplete(row - 1) || sudokuGame.isColumnComplete(col - 1) ||
                        sudokuGame.isSquareComplete((row - 1) / 3 * 3, (col - 1) / 3 * 3)) {
                    System.out.println("Parabéns! Você completou uma linha, coluna ou quadrado!");
                }

            } else {
                System.out.println("Jogada inválida. Tente novamente.");
            }
        }

        scanner.close();
    }
}