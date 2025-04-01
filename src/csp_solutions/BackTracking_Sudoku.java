package csp_solutions;

import core_algorithms.BacktrackingSearch;
import csp_problems.Sudoku;
import csp_problems.CSPProblem.Variable;
import csp_problems.Square;

import java.util.*;

public class BackTracking_Sudoku extends BacktrackingSearch<Square, Integer> {

    private final Sudoku csp;

    public BackTracking_Sudoku(Sudoku csp) {
        super(csp);
        this.csp = csp;
    }

    @Override
    public boolean revise(Square tail, Square head) {
        boolean revised = false;
        Iterator<Integer> itr = getAllVariables().get(tail).domain().iterator();
        while (itr.hasNext()) {
            int tailValue = itr.next();
            boolean hasSupport = false;
            for (int headValue : getAllVariables().get(head).domain()) {
                if (!conflicts(tail, tailValue, head, headValue)) {
                    hasSupport = true;
                    break;
                }
            }
            if (!hasSupport) {
                itr.remove();
                revised = true;
            }
        }
        return revised;
    }


    private boolean conflicts(Square var1, int val1, Square var2, int val2) {
        if (!csp.getNeighborsOf(var1).contains(var2)) {
            return false;
        }
        return val1 == val2;
    }

    @Override
    public Square selectUnassigned() {
        int minDomainSize = Integer.MAX_VALUE;
        Square selectedVar = null;
        for (Variable<Square, Integer> v : getAllVariables().values()) {
            if (!assigned(v.name()) && v.domain().size() < minDomainSize) {
                minDomainSize = v.domain().size();
                selectedVar = v.name();
            }
        }
        return selectedVar;
    }

    public static void main(String[] args) {
        String filename;
        filename = "SudokuTestCases/TestCase1.txt";

        System.out.println("Loading puzzle from " + filename);
        Sudoku csp = new Sudoku(filename);

        System.out.println("Initial Puzzle:");
        csp.printPuzzle(csp.getAllVariables());

        BackTracking_Sudoku agent = new BackTracking_Sudoku(csp);
        if (agent.initAC3() && agent.search()) {
            System.out.println("\nSolved Puzzle:");
            csp.printPuzzle(agent.getAllVariables());
        } else {
            System.out.println("Unable to find a solution.");
        }
    }
}
