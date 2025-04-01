package csp_problems;

import java.io.*;
import java.util.*;

public class Sudoku implements CSPProblem<Square, Integer> {
    private final Map<Square, Variable<Square, Integer>> allVariables;
    private final Map<Square, Set<Square>> neighbors = new HashMap<>();
    private final List<Integer> DEFAULT_DOMAIN = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private final String filename;
    private final int size;

    public Sudoku(String filename) {
        this.filename = filename;
        this.size = 9; // Standard 9x9 grid
        this.allVariables = loadPuzzleFromFile(filename);
        initializeNeighbors();
    }

    public Sudoku(int size) {
        this.filename = null;
        this.size = size;
        this.allVariables = createEmptyPuzzle(size);
        initializeNeighbors();
    }

    private Map<Square, Variable<Square, Integer>> loadPuzzleFromFile(String filename) {
        Map<Square, Variable<Square, Integer>> variables = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            for (int i = 0; i < size; i++) {
                if ((line = reader.readLine()) != null && !line.isEmpty()) {
                    String[] numbers = line.trim().split(" ");
                    for (int j = 0; j < size; j++) {
                        Square square = new Square(i, j);
                        int value = Integer.parseInt(numbers[j]);
                        Variable<Square, Integer> variable = (value > 0 && value <= 9) ?
                                new Variable<>(square, new LinkedList<>(List.of(value))) :
                                new Variable<>(square, new LinkedList<>(DEFAULT_DOMAIN));
                        variables.put(square, variable);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return variables;
    }

    private Map<Square, Variable<Square, Integer>> createEmptyPuzzle(int size) {
        Map<Square, Variable<Square, Integer>> variables = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square square = new Square(i, j);
                variables.put(square, new Variable<>(square, new LinkedList<>(DEFAULT_DOMAIN)));
            }
        }
        return variables;
    }

    private void initializeNeighbors() {

        for (int i = 0; i < size; i++) {
            Set<Square> rowNeighbors = new HashSet<>();
            Set<Square> colNeighbors = new HashSet<>();

            for (int j = 0; j < size; j++) {
                rowNeighbors.add(new Square(i, j));
                colNeighbors.add(new Square(j, i));
            }

            for (int j = 0; j < size; j++) {
                Square sqRow = new Square(i, j);
                Square sqCol = new Square(j, i);
                neighbors.putIfAbsent(sqRow, new HashSet<>(rowNeighbors));
                neighbors.putIfAbsent(sqCol, new HashSet<>(colNeighbors));
            }
        }

            for (int boxRow = 0; boxRow < 3; boxRow++) {
                for (int boxCol = 0; boxCol < 3; boxCol++) {
                    Set<Square> boxNeighbors = new HashSet<>();
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            Square sq = new Square(boxRow * 3 + x, boxCol * 3 + y);
                            boxNeighbors.add(sq);
                        }
                    }
                    for (Square sq : boxNeighbors) {
                        neighbors.get(sq).addAll(boxNeighbors);
                    }
                }
            }

            // Remove a square from its own neighbor set
            for (Map.Entry<Square, Set<Square>> entry : neighbors.entrySet()) {
                entry.getValue().remove(entry.getKey());
            }
    }

    @Override
    public Map<Square, Variable<Square, Integer>> getAllVariables() {
        return allVariables;
    }

    public void printPuzzle(Map<Square, Variable<Square, Integer>> allVariables) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square sq = new Square(i, j);
                if (allVariables.get(sq).domain().size() == 1) {
                    System.out.print("[" + allVariables.get(sq).domain().getFirst() + "] ");
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public List<Square> getNeighborsOf(Square sq) {
        return new ArrayList<>(neighbors.getOrDefault(sq, Collections.emptySet()));
    }

    @Override
    public List<Square> getPreAssignedVariables() {
        List<Square> preAssigned = new ArrayList<>();
        for (Map.Entry<Square, Variable<Square, Integer>> entry : allVariables.entrySet()) {
            if (entry.getValue().domain().size() == 1) {
                preAssigned.add(entry.getKey());
            }
        }
        return preAssigned;
    }

    public int getSize() {
        return size;
    }
}
