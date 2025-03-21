package csp_problems;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <X> the data type of the "names" of variables
 *  *     (e.g., for Sudoku, we could use Strings such as "A3", "B8", etc. to name squares of the 9x9 board.)
 * @param <V> the data type of values.
 *  *     (e.g., for Sudoku, a value is an integer between 1 and 9.)
 */
public interface CSPProblem<X,V> {
    record Variable<X,V>(X name, List<V> domain) {
        public Variable(X name, V value){
            this(name, Collections.singletonList(value));
        }
    }
    Map<X, Variable<X,V>> getAllVariables();

    /**
     * Given the name of a variable, return the names of its neighbors
     * @param vName
     * @return list of names of the neighbors
     */
    List<X> getNeighborsOf(X vName);

    /**
     * @return a list of variables that have already been assigned values as part of the problem.
     */
    List<X> getPreAssignedVariables();

    /**
     * Print the puzzle based on the passed map of all variables
     */
    void printPuzzle(Map<X, Variable<X,V>> allVariables);

}
