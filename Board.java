import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Arrays;

public class Board {

    private char[][] board;
    private final int boardLength;
    private final char[][] goalBoard;
    private final int rowLength;
    private final int columnLength;
    private int firstBlockRow = -1;
    private int firstBlockColumn = -1;
    private int secondBlockRow = -1;
    private int secondBlockColumn = -1;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        board = new char[blocks.length][blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                board[i][j] = (char) blocks[i][j];
            }
        }

        boardLength = board.length;
        rowLength = boardLength;
        columnLength = boardLength;

        goalBoard = new char[boardLength][boardLength];

        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                if (i == rowLength - 1 && j == columnLength - 1) {
                    goalBoard[rowLength - 1][columnLength - 1] = 0;
                }
                else {
                    goalBoard[i][j] = (char) ((i * columnLength) + (j + 1));
                }
            }
        }

    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of blocks out of place
    public int hamming() {
        int hammingPriority = 0;
        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                if (board[i][j] != 0) {
                    if (board[i][j] != goalBoard[i][j]) {
                        hammingPriority++;
                    }
                }
            }
        }
        return hammingPriority;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhanttanNumber = 0;
        int currentBlockValue;
        int rowOfBlockValueOnGoalBoard;
        int columnOfBlockValueOnGoalBoard;

        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                if (board[i][j] != 0) {
                    if (board[i][j] != goalBoard[i][j]) {
                        // int blockValueOnGoalBoard = goalBoard[i][j];
                        currentBlockValue = board[i][j];

                        if (currentBlockValue % columnLength == 0) {
                            rowOfBlockValueOnGoalBoard = (currentBlockValue / columnLength) - 1;
                            columnOfBlockValueOnGoalBoard = columnLength - 1;
                        }
                        else {
                            rowOfBlockValueOnGoalBoard = currentBlockValue / columnLength;
                            columnOfBlockValueOnGoalBoard = currentBlockValue % columnLength - 1;
                        }

                        manhanttanNumber += Math.abs(rowOfBlockValueOnGoalBoard - i) +
                                Math.abs(columnOfBlockValueOnGoalBoard - j);

                    }
                }
            }
        }
        return manhanttanNumber;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // for (int i = 0; i < boardLength; i++) {
        //     for (int j = 0; j < boardLength; j++) {
        //         if (board[i][j] != goalBoard[i][j]) {
        //             return false;
        //         }
        //     }
        // }
        return Arrays.deepEquals(board, goalBoard);
        // return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board twin = new Board(new int[rowLength][columnLength]);
        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                twin.board[i][j] = board[i][j];
            }
        }

        if (firstBlockRow == -1 && firstBlockColumn == -1 &&
                secondBlockRow == -1 && secondBlockColumn == -1) {
            firstBlockRow = StdRandom.uniform(0, columnLength);
            firstBlockColumn = StdRandom.uniform(0, columnLength);
            while (board[firstBlockRow][firstBlockColumn] == 0) {
                firstBlockRow = StdRandom.uniform(0, columnLength);
                firstBlockColumn = StdRandom.uniform(0, columnLength);
            }

            secondBlockRow = StdRandom.uniform(0, columnLength);
            secondBlockColumn = StdRandom.uniform(0, columnLength);
            while (board[secondBlockRow][secondBlockColumn] == 0 ||
                    board[secondBlockRow][secondBlockColumn]
                            == board[firstBlockRow][firstBlockColumn]) {
                secondBlockRow = StdRandom.uniform(0, columnLength);
                secondBlockColumn = StdRandom.uniform(0, columnLength);
            }
        }
        twin.board[firstBlockRow][firstBlockColumn] = board[secondBlockRow][secondBlockColumn];
        twin.board[secondBlockRow][secondBlockColumn] = board[firstBlockRow][firstBlockColumn];

        return twin;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        Board castedBoard;
        if (y.getClass() == this.getClass()) {
            castedBoard = (Board) y;

            if (boardLength != castedBoard.boardLength) {
                return false;
            }

            // for (int i = 0; i < boardLength; i++) {
            //     for (int j = 0; j < boardLength; j++) {
            //         if (board[i][j] != castedBoard.board[i][j]) {
            //             return false;
            //         }
            //     }
            // }
            return Arrays.deepEquals(board, castedBoard.board);
        }
        else {
            return false;
        }
        // return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int emptySlotRow = 0;
        int emptySlotColumn = 0;
        // List<Board> listNeighbouringBoards = new ArrayList<>();
        Queue<Board> queueNeighbouringBords = new Queue<>();

        // find the empty slot
        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                if (board[i][j] == 0) {
                    emptySlotRow = i;
                    emptySlotColumn = j;
                }
            }
        }

        // if the empty slot is not at the top edge, add to the array a neighbouring board formed by moving the block on top
        if (emptySlotRow > 0) {
            // board formed by moving into the empty space a block on its top
            Board topBlockBoard = new Board(new int[rowLength][columnLength]);
            for (int i = 0; i < boardLength; i++) {
                for (int j = 0; j < boardLength; j++) {
                    topBlockBoard.board[i][j] = board[i][j];
                }
            }
            topBlockBoard.board[emptySlotRow][emptySlotColumn] =
                    topBlockBoard.board[emptySlotRow - 1][emptySlotColumn];
            topBlockBoard.board[emptySlotRow - 1][emptySlotColumn] = 0;

            queueNeighbouringBords.enqueue(topBlockBoard);
        }


        // if the empty slot is not at the left edge, add to the array a neighbouring board formed by moving the block on its left
        if (emptySlotColumn > 0) {
            // board formed by moving into the empty space a block on its left
            Board leftBlockBoard = new Board(new int[rowLength][columnLength]);
            for (int i = 0; i < boardLength; i++) {
                for (int j = 0; j < boardLength; j++) {
                    leftBlockBoard.board[i][j] = board[i][j];
                }
            }
            leftBlockBoard.board[emptySlotRow][emptySlotColumn] =
                    leftBlockBoard.board[emptySlotRow][emptySlotColumn - 1];
            leftBlockBoard.board[emptySlotRow][emptySlotColumn - 1] = 0;

            queueNeighbouringBords.enqueue(leftBlockBoard);
        }

        // if the empty slot is not at the right edge, add to the array a neighbouring board formed by moving the block on its right
        if (emptySlotColumn < columnLength - 1) {
            // board formed by moving into the empty space a block on its right
            Board rightBlockBoard = new Board(new int[rowLength][columnLength]);
            for (int i = 0; i < boardLength; i++) {
                for (int j = 0; j < boardLength; j++) {
                    rightBlockBoard.board[i][j] = board[i][j];
                }
            }
            rightBlockBoard.board[emptySlotRow][emptySlotColumn] =
                    rightBlockBoard.board[emptySlotRow][emptySlotColumn + 1];
            rightBlockBoard.board[emptySlotRow][emptySlotColumn + 1] = 0;

            queueNeighbouringBords.enqueue(rightBlockBoard);
        }

        // if the empty slot is not at the bottom edge, add to the array a neighbouring board formed by moving the block on its bottom
        if (emptySlotRow < rowLength - 1) {
            // board formed by moving into the empty space a block on its bottom
            Board bottomBlockBoard = new Board(new int[rowLength][columnLength]);
            for (int i = 0; i < boardLength; i++) {
                for (int j = 0; j < boardLength; j++) {
                    bottomBlockBoard.board[i][j] = board[i][j];
                }
            }
            bottomBlockBoard.board[emptySlotRow][emptySlotColumn] =
                    bottomBlockBoard.board[emptySlotRow + 1][emptySlotColumn];
            bottomBlockBoard.board[emptySlotRow + 1][emptySlotColumn] = 0;

            queueNeighbouringBords.enqueue(bottomBlockBoard);
        }
        return queueNeighbouringBords;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(rowLength + "\n");
        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                string.append((int) board[i][j] + " ");
                if (j == boardLength - 1) {
                    string.append("\n");
                }
            }
        }
        return string.toString();
    }
}
