package csp_problems;

import java.util.*;
import java.util.stream.IntStream;

public class NQueens implements CSPProblem<Integer,Integer>{
    private final int N;
    private final List<Integer> DEFAULT_DOMAIN;

    public NQueens(int n){
        this.N = n;
        DEFAULT_DOMAIN = IntStream.rangeClosed(1, N)
                                  .boxed()
                                  .toList();
    }

    public Map<Integer, Variable<Integer, Integer>> getAllVariables(){
        Map<Integer, Variable<Integer,Integer>> allVariables = new HashMap<>();
        for(int i=1; i<=N; i++){
            List<Integer> domain = new LinkedList<>(DEFAULT_DOMAIN);
            allVariables.put(i, new Variable<>(i, domain));
        }
        return allVariables;
    }

    //For a given column i, the neighbors are all other columns j (where j != i).
    public List<Integer> getNeighborsOf(Integer value){
        List<Integer> neighbors =  new LinkedList<>(DEFAULT_DOMAIN);
        neighbors.remove(value);
        return neighbors;
    }

    //NQueens does not have any pre-assigned variables
    //so simply return an empty list.
    public List<Integer> getPreAssignedVariables(){
        return Collections.emptyList();
    }

    public void printPuzzle(Map<Integer, Variable<Integer,Integer>> allVariables) {
        for (int row = 1; row <= N; row++) {
            for (int col = 1; col <= N; col++) {
                if (allVariables.get(col).domain().size() == 1 &&
                        allVariables.get(col).domain().getFirst() == row) {
                    System.out.print(" Q "); // Queen position
                } else {
                    System.out.print(" . "); // Empty space
                }
            }
            System.out.println();
        }
    }

    public int getN(){
        return N;
    }


}
