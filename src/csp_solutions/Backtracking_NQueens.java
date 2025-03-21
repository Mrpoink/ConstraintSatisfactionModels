package csp_solutions;

import core_algorithms.BacktrackingSearch;
import csp_problems.CSPProblem.Variable;
import csp_problems.NQueens;

import java.util.Iterator;

public class Backtracking_NQueens extends BacktrackingSearch<Integer,Integer> {

    private final NQueens csp;

    public Backtracking_NQueens(NQueens csp){
        super(csp);
        this.csp = csp;
    }

    //revise an arc according to the two constraints:
    //  Alldiff (Q1, Q2, …, QN}
    //  ꓯi,j (i ≠ j), |Qi - Qj| ≠ |i –j|
    public boolean revise(Integer tail, Integer head) {
        boolean revised = false;
        Iterator<Integer> itr = getAllVariables().get(tail).domain().iterator();
        while (itr.hasNext()) {
            int rowTail = itr.next();
            boolean hasSupport = false;
            for (int rowHead : getAllVariables().get(head).domain()) {
                if (!conflicts(tail, rowTail, head, rowHead)) {
                    //found a value at the head that supports this value at the tail
                    hasSupport = true;
                    break;
                }
            }
            if (!hasSupport) {
                //there is no value at the head that supports this tail value
                //delete the value from the tail
                itr.remove();
                revised = true;
            }
        }
        return revised;
    }

    //a helper function for checking if two queens are in conflict
    private static boolean conflicts(int col1, int row1, int col2, int row2) {
        return (row1 == row2 || Math.abs(col1 - col2) == Math.abs(row1 - row2));
    }


    //implements the MRV heuristic
    public Integer selectUnassigned(){
        int smallestDomain = csp.getN()+1;
        Variable<Integer,Integer> mrv = null;
        for(Variable<Integer,Integer> v : getAllVariables().values()){
            if (!assigned(v.name()) && v.domain().size()<smallestDomain){
                smallestDomain = v.domain().size();
                mrv = v;
            }
        }
        if(mrv != null){
            return mrv.name();
        }else{
            return null;
        }
    }

    public static void main(String[] args) {
        int N = 8;
        NQueens csp = new NQueens(N);
        Backtracking_NQueens agent = new Backtracking_NQueens(csp);
        if (agent.initAC3() && agent.search()){
            csp.printPuzzle(agent.getAllVariables());
        }else{
            System.out.println("Unable to find a solution.");
        }
    }


}
