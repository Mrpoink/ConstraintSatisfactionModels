package core_algorithms;

import csp_problems.CSPProblem;
import csp_problems.CSPProblem.Variable;

import java.util.*;

/**
 * A generic solver for CSPs of the Alldiff type of constraints.
 * This solver implements the following techniques:
 *   maintaining arc consistency (MAC) +
 *   minimum-remaining-values (MRV)
 * Note: MAC = backtracking search + dynamic arc consistency (AC-3)
 *
 * @param <X> the data type of the "names" of variables
 *  *  *     (e.g., for Sudoku, we could use Strings such as "03", "75", etc.
 *           to name the squares of the 9x9 board, where the first digit specifies
 *           the row # and the second the column #.)
 * @param <V> the data type of values.
 *     (e.g., in Sudoku, values should be integers between 1 and 9.)
 */
public abstract class BacktrackingSearch <X, V> {

    /**
     * The data type that represents an arc in the AC-3 algorithm.
     * We defind the equals method for this data type.
     * @param head
     * @param tail
     * @param <X>
     */
    public  record Arc<X>(X head, X tail){
        @Override
        public boolean equals(Object o){
            if (this == o){
                return true;
            }
            if (o == null){
                return false;
            }
            if(getClass() != o.getClass()){
                return false;
            }
            Arc<?> arc = (Arc<?>) o;
            return Objects.equals(head, arc.head) &&
                    Objects.equals(tail, arc.tail);
        }
    }

    //a map from variable names to variable objects
    private Map<X,Variable<X,V>> allVariables;

    //keeps track of the variables that have been assigned so far
    private final Set<X> assigned;

    private final CSPProblem<X,V> problem;

    public BacktrackingSearch(CSPProblem<X,V> problem){
        this.problem = problem;
        this.allVariables = problem.getAllVariables();
        //populate the assigned set with the names of any pre-assigned variables
        this.assigned = new HashSet<>(problem.getPreAssignedVariables());
    }

    /**
     * An implementation of the AC-3 algorithm; see textbook, Figure 6.3 on page 186
     * Note that the revise() is a separate method that you will need to
     * implement in BacktrackingSearch_Sudoku.java
     * @param arcs the list of arcs for which consistency will be maintained
     * @return false if consistency could not be maintained, true otherwise
     */
    public boolean AC3(Queue<Arc<X>> arcs){
        //to avoid adding the same arc to queue multiple times
        Set<Arc<X>> unique = new HashSet<>(arcs);
        while(!arcs.isEmpty()) {
            //TODO: complete AC-3 algorithm here *inside* the while loop
            // NO need to modify other part of this method.
            Arc<X> arc = arcs.poll();
            unique.remove(arc);
            X head = arc.head;
            X tail = arc.tail;
            if (revise(tail, head)){
                if (allVariables.get(tail).domain().isEmpty()){
                    return false;
                }
                for (X v: problem.getNeighborsOf(tail)) {
                    Arc<X> arcy = new Arc<>(tail, v);
                    if (!unique.contains(arcy)){
                        unique.add(arcy);
                        arcs.add(arcy);
                    }
                }

            }
        }
        return true;
    }

    /**
     * Performs the AC-3 algorithm as preprocessing before search begins
     * @return true or false
     */
    public boolean initAC3(){
        Queue<Arc<X>> arcs = new LinkedList<>();
        for(X v : allVariables.keySet()){
            for(X n : problem.getNeighborsOf(v)){
                arcs.add(new Arc<>(v,n));
            }
        }
        return AC3(arcs);
    }

    /**
     * An implementation of the backtracking search with maintaining arc consistency (MAC)
     * @return
     */
    public boolean search(){
        X u = selectUnassigned();
        if(u == null){
            //there is no more unassigned variable,
            //we have reached the solution.
            return true;
        }
        assigned.add(u);
        while(!allVariables.get(u).domain().isEmpty()) {
            //TODO: complete the MAC algorithm *inside* the while loop.
            // NO need to modify other part of this method.
            V value = allVariables.get(u).domain().removeFirst();
            Map<X,Variable<X,V>> el = deepClone();
            allVariables.get(u).domain().clear();
            allVariables.get(u).domain().add(value);

            Queue<Arc<X>> arcs = new LinkedList<>();
            List<X> neighbor  = problem.getNeighborsOf(u);
            for (X v : neighbor) {
                Arc<X> arc = new Arc<>(u,v);
                arcs.add(arc);
            }
            boolean inferences = AC3(arcs);
            if(inferences){
                if(search()){
                    return true;
                }
            }
            allVariables = el;
        }
        //We cannot find a valid value to assign to variable u.
        //We must backtrack now.
        assigned.remove(u);
        return false;
    }

    /**
     * Create a deep clone of the allVariables map in case we will need to back track in future
     * (Deep clone means to clone every element of the list.)
     * This method should be used inside search().
     */
    public Map<X,Variable<X,V>> deepClone(){
        Map<X,Variable<X,V>> allVariablesClone = new HashMap<>();
        for(Variable<X,V> var : allVariables.values()){
            //deep clone the variable domain
            Variable<X,V> varClone =
                    new Variable<>(var.name(), new LinkedList<>(var.domain()));
            allVariablesClone.put(var.name(),varClone);
        }
        return allVariablesClone;
    }

    /**
     * Revert the allVariables map to the deep clone copy.
     * This method should be used inside search().
     */
    public void revert(Map<X,Variable<X,V>> allVariablesClone){
        allVariables = allVariablesClone;
    }

    /**
     *
     * @return allVariables map
     */
    public Map<X,Variable<X, V>> getAllVariables() {
        return allVariables;
    }


    /**
     * Check if the variable of the given name has been assigned a value already.
     * @param name name of the variable whose assignment will be checked
     * @return true if assigned, false otherwise
     */
    public boolean assigned(X name){
        return assigned.contains(name);
    }

    //the two abstract methods below should be implemented in BacktrackingSearch_Sudoku.java

    /**
     * revise an arc to maintain arc consistency
     *
     * @param tail
     * @param head
     * @return false if no value is deleted, true otherwise
     */
    public abstract boolean revise(X tail, X head);

    /**
     *
     * @return an unassigned variable according to the MRV heuristic;
     *         null if all variables have been assigned
     */
    public abstract X selectUnassigned();

}
