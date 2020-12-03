package com.pqtran.workspace.adventcalendar.day3;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;

public class Day3 implements Executable {
    private ResourceFile resourceFile;

    public Day3(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
    }

    private int getGridRowCount() throws IOException {
        BufferedReader reader = this.resourceFile.getReader();
        return (int) reader.lines().count();
    }

    private char[][] getGrid() throws IOException {
        BufferedReader reader = this.resourceFile.getReader();

        int rows = getGridRowCount();
        char[][] grid = new char[rows][];

        List<String> lines = reader.lines().collect(Collectors.toList());
        for (int index = 0; index < lines.size(); index++) {
            grid[index] = lines.get(index).toCharArray();
        }

        return grid;
    }

    private int treesEncountered(int rowStep, int colStep) throws IOException {
        char[][] grid = getGrid();
        int gridWidth = grid[0].length;

        int colIndex = 0;
        int treeCount = 0;
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex += rowStep) {
            if (grid[rowIndex][colIndex % gridWidth] == '#') {
                treeCount++;
            }

            colIndex += colStep;
        }

        return treeCount;
    }

    public void executePartOne() throws IOException {
        int treeCount = treesEncountered(1, 3);
        System.out.printf("Trees encountered: %s\n", treeCount);
    }

    public void executePartTwo() throws IOException {
        long acc = 1;
        int[][] slopes = new int[][] {{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};
        for (int[] slope : slopes) {
            int colStep = slope[0];
            int rowStep = slope[1];

            int treeCount = treesEncountered(rowStep, colStep);
            acc *= (long) treeCount;
        }

        System.out.printf("computed value: {%s}\n", acc);
    }
}
