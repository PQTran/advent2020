package com.pqtran.workspace.adventcalendar.day11;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;
import com.pqtran.workspace.adventcalendar.day11.Direction.*;

public class Day11 implements Executable {
    final char floor_char = '.';
    final char empty_char = 'L';
    final char occupied_char = '#';

    private ResourceFile resourceFile;

    public Day11(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
    }

    List<List<Character>> parseSeatingGrid() throws IOException {
        List<List<Character>> grid = new ArrayList<>();
        try (BufferedReader reader = resourceFile.getReader()) {

            int r;
            while ((r = reader.read()) != -1) {
                List<Character> row = new ArrayList<>();
                while (r != -1 && !Character.isWhitespace((char) r)) {
                    row.add((char) r);
                    r = reader.read();
                }

                grid.add(row);
            }
        }

        return grid;
    }

    boolean shouldOccupy(List<List<Character>> grid, int row, int col, Difficulty difficulty) {
        final int criteria = 0;

        if (grid.get(row).get(col) != empty_char)
            return false;

        List<Position> positions;
        if (difficulty.equals(Difficulty.Basic)) {
            positions = getSurroundingPositions(grid, row, col);
        } else {
            positions = getVisiblePositions(grid, row, col);
        }

        return positions.stream().filter(pos -> grid.get(pos.row).get(pos.col) == occupied_char).count() == criteria;
    }

    Position getPosition(List<List<Character>> grid, int row, int col, Direction direction, int offset) {
        int posRow;
        int posCol;
        switch (direction) {
            case TopLeft:
                posRow = row - offset;
                posCol = col - offset;
                break;
            case Top:
                posRow = row - offset;
                posCol = col;
                break;
            case TopRight:
                posRow = row - offset;
                posCol = col + offset;
                break;
            case Left:
                posRow = row;
                posCol = col - offset;
                break;
            case Right:
                posRow = row;
                posCol = col + offset;
                break;
            case BottomLeft:
                posRow = row + offset;
                posCol = col - offset;
                break;
            case Bottom:
                posRow = row + offset;
                posCol = col;
                break;
            case BottomRight:
                posRow = row + offset;
                posCol = col + offset;
                break;
            default:
                posRow = -1;
                posCol = -1;
        }

        Position result = null;
        if (posRow >= 0 && posRow < grid.size() &&
            posCol >= 0 && posCol < grid.get(0).size()) {
            result = new Position(posRow, posCol);
        }

        return result;
    }

    List<Position> getSurroundingPositions(List<List<Character>> grid, int row, int col) {
        List<Direction> directions = Direction.valuesAsList();
        int offset = 1;
        return directions.stream()
            .map(d -> getPosition(grid, row, col, d, offset))
            .filter(pos -> pos != null)
            .collect(Collectors.toList());
    }

    List<Position> getVisiblePositions(List<List<Character>> grid, int row, int col) {
        List<Position> positions = new ArrayList<>();
        List<Direction> directions = Direction.valuesAsList();

        for (Direction direction : directions) {
            int offset = 1;
            Position pos;
            while ((pos = getPosition(grid, row, col, direction, offset++)) != null) {
                positions.add(pos);

                if (grid.get(pos.row).get(pos.col) == occupied_char ||
                    grid.get(pos.row).get(pos.col) == empty_char)
                    break;
            }
        }

        return positions;
    }

    boolean shouldEmpty(List<List<Character>> grid, int row, int col, Difficulty difficulty) {
        int criteria = difficulty.equals(Difficulty.Basic) ? 4 : 5;

        if (grid.get(row).get(col) != occupied_char)
            return false;

        List<Position> positions;
        if (difficulty.equals(Difficulty.Basic)) {
            positions = getSurroundingPositions(grid, row, col);
        } else {
            positions = getVisiblePositions(grid, row, col);
        }

        return positions.stream().filter(pos -> grid.get(pos.row).get(pos.col) == occupied_char).count() >= criteria;
    }

    boolean updateSeatingGrid(List<List<Character>> grid, Difficulty difficulty) {
        List<Position> updateOccupied = new ArrayList<>();
        List<Position> updateEmpty = new ArrayList<>();
        for (int row = 0; row < grid.size(); row ++) {
            for (int col = 0; col < grid.get(0).size(); col++) {
                if (shouldOccupy(grid, row, col, difficulty)) {
                    updateOccupied.add(new Position(row, col));
                } else if (shouldEmpty(grid, row, col, difficulty)) {
                    updateEmpty.add(new Position(row, col));
                }
            }
        }

        for (Position pos : updateOccupied) {
            grid.get(pos.row).set(pos.col, occupied_char);
        }

        for (Position pos : updateEmpty) {
            grid.get(pos.row).set(pos.col, empty_char);
        }

        return updateOccupied.size() + updateEmpty.size() > 0;
    }

    int countOccupiedSeats(List<List<Character>> grid) {
        return (int) grid.stream().flatMap(Collection::stream).filter(c -> c == occupied_char).count();
    }

    public void executePartOne() throws IOException {
        List<List<Character>> grid = parseSeatingGrid();

        int updateCount = 0;
        while (updateSeatingGrid(grid, Difficulty.Basic)) {
            updateCount++;
        }

        System.out.printf("updated %s times\n", updateCount);

        System.out.printf("occupied seat count: %s\n", countOccupiedSeats(grid));
    }

    public void executePartTwo() throws IOException {
        List<List<Character>> grid = parseSeatingGrid();

        int updateCount = 0;
        while (updateSeatingGrid(grid, Difficulty.Intermediate)) {
            updateCount++;
        }

        System.out.printf("updated %s times\n", updateCount);

        System.out.printf("occupied seat count: %s\n", countOccupiedSeats(grid));
    }

    enum Difficulty {
        Basic,
        Intermediate
    }

    class Position {
        int row;
        int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
