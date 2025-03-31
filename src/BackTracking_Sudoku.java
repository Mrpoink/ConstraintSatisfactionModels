import core_algorithms.BacktrackingSearch;
import csp_problems.Sudoku;

import java.util.*;

public class BackTracking_Sudoku extends BacktrackingSearch<Integer, Integer> {

    private final Sudoku csp;

    public BackTracking_Sudoku(Sudoku csp){
        super(csp);
        this.csp = csp;
    }

    //Basically, 1%size=column, i/size=row
    //We have to make each 3x3 cell a list
    //We can have a hashmap of lists as the full problem
    //we can then use the column and row information to
    //develop the neighbors

    public boolean revise(List<Integer> head, List<Integer> tail){
        boolean revised=false;
        Iterator<Integer> itr = getAllVariables().get(tail).domain().iterator();\
        while (itr.hasNext()){
            List<Integer> Cell_tail = itr.next();
            boolean hasSupport = false;
            for (int Cell_head: getAllVariables().get(head).domain()){
                if (!Conflicts_outer(tail, Cell_tail, head, Cell_head)){
                    hasSupport = false;
                }
            }
        }
    }

    private static boolean Conflicts_inner(Integer num, List<Integer> Cell){
        return Cell.contains(num);
    }
    private static boolean Conflict_outer(Integer num, Map<Integer, List<Integer>> problem){
        Integer conflict_index = 0;
        Map<Integer, List<Integer>> new_problem = problem;

        /**
         * We're trying to get the Map of Lists tailored to the current integer
         * Thats a Cell
         * Then we check the cell for the num
         * If the cell does not contain the num, we move on and see if the others do
         * If they don't then we can continue, otherwise we must check to see the row and column
         */
        for (int i = 0; i <= problem.size(); i++){ //Choose Cell
            List<Integer> Cell = new_problem.get(i);
            Integer start_num = Cell.indexOf(num);
            Integer column = start_num  % SIZE;
        }
    }
}
