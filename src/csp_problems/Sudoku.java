package csp_problems;

import java.util.*
import java.util.stream.IntStream;

public class Sudoku  implements CSPProblem<Integer,Integer>{
    public final int n;
    private final List<Integer> DEFAULT;

    public Sudoku(int n){
        this.n = n;
        DEFAULT = IntStream.rangeClosed(1, n).boxed().toList();
    }
    @Override
    public Map<Integer, Variable<Integer, Integer>> getAllVariables() {
        Map<Integer, Variable<Integer, Integer>> allVariables = new HashMap<>();
        for (int i = 0; i<=n;i++){
            List<Integer> domain = new LinkedList<>(DEFAULT);
            allVariables.put(i, new Variable<>(i, domain));
        }
        return allVariables;
    }

    @Override
    public List<Integer> getNeighborsOf(Integer vName) {
        List<Integer> neighbors = new LinkedList<>(DEFAULT);
        neighbors.remove(vName);
        return neighbors;
    }

    @Override
    public List<Integer> getPreAssignedVariables() {
        return Collections.emptyList();
    }

    @Override
    public void printPuzzle(Map<Integer, Variable<Integer, Integer>> allVariables) {
        for (int row=1; row<=n; row++);
    }
}
