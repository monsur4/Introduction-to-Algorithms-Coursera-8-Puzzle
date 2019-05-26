import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Node solutionNode;
    private int numberOfMovesToSolution;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException(
                    "your argument cannot be null");
        }

        Node firstNode;
        firstNode = new Node(initial, 0, null, true);

        Board twinBoardSolution = firstNode.board.twin();

        Node twinNode = new Node(twinBoardSolution, 0, null, false);

        MinPQ<Node> pq = new MinPQ<>();
        pq.insert(firstNode);
        pq.insert(twinNode);

        Node exploringNode = pq.delMin();

        // I fixed the timing issue by caching whether each node is a twin node or not as boolean
        // value by obtaining this value from its corresponding preceding node.
        // So, to determine if the board is solvable or not, I just check the boolean variable in
        // the goal node (goal board) if it derives from the twin or not.
        // As compared to how I implemented it before, where after finding the goalboard,
        // I'd trace it back all the way to the beginning board, then check if the beginning board
        // is the original or the twin.
        while (!exploringNode.board.isGoal()/* && !exploringTwinNode.board.isGoal()*/) {
            for (Board board : exploringNode.board.neighbors()) {
                if (exploringNode.predecessorNode == null) {
                    pq.insert(new Node(board, exploringNode.numberOfMovesMade + 1,
                                       exploringNode, exploringNode.notTwin));
                }
                else if (!board.equals(exploringNode.predecessorNode.board)) {
                    pq.insert(new Node(board, exploringNode.numberOfMovesMade + 1,
                                       exploringNode, exploringNode.notTwin));
                }
            }
            exploringNode = pq.delMin();
        }
        solutionNode = exploringNode;

        if (exploringNode.notTwin) {
            solvable = true;
            numberOfMovesToSolution = solutionNode.numberOfMovesMade;
        }
        else {
            solvable = false;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) {
            return numberOfMovesToSolution;
        }
        else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> solutionBoardsStack = new Stack<>();
        solutionBoardsStack.push(solutionNode.board);
        while (solutionNode.predecessorNode != null) {
            solutionNode = solutionNode.predecessorNode;
            solutionBoardsStack.push(solutionNode.board);
        }
        return solutionBoardsStack;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class Node implements Comparable<Node> {
        private final Board board;
        private final int numberOfMovesMade;
        private final Node predecessorNode;
        private final int manhattanPriority;
        private final boolean notTwin;

        public Node(Board board, int numberOfMovesMade, Node predecessorNode, boolean notTwin) {
            this.board = board;
            this.numberOfMovesMade = numberOfMovesMade;
            this.predecessorNode = predecessorNode;
            manhattanPriority = this.board.manhattan() + this.numberOfMovesMade;
            this.notTwin = notTwin;
        }

        @Override
        public int compareTo(Node that) {
            return (this.manhattanPriority) - (that.manhattanPriority);
        }
    }
}
