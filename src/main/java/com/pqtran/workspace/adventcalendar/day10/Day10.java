package com.pqtran.workspace.adventcalendar.day10;

import java.util.*;
import java.io.*;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;

public class Day10 implements Executable {
    private ResourceFile resourceFile;

    public Day10(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
    }

    List<Integer> parseVoltageData() throws IOException, IllegalArgumentException {
        List<Integer> data = new ArrayList<>();
        try (BufferedReader reader = resourceFile.getReader()) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                data.add(Integer.valueOf(line));
            }
        }

        if (data.size() == 0)
            throw new IllegalArgumentException("data file empty");

        return data;
    }

    void organizeAdapters(List<Integer> voltageData) {
        voltageData.add(0);
        voltageData.sort(null);
        voltageData.add(voltageData.get(voltageData.size() - 1) + 3);
    }

    Integer calculateVoltageJumpMetric(List<Integer> voltageData) {
        Map<Integer, Integer> jumpCount = new HashMap<>();
        int prev = 0;
        for (Integer curr : voltageData) {
            int jumpLength = curr - prev;
            jumpCount.put(jumpLength, jumpCount.getOrDefault(jumpLength, 0) + 1);
            prev = curr;
        }

        return jumpCount.getOrDefault(1, 0) * jumpCount.getOrDefault(3, 0);
    }

    public void executePartOne() throws IOException, IllegalArgumentException {
        List<Integer> data = parseVoltageData();
        organizeAdapters(data);
        Integer metricVal = calculateVoltageJumpMetric(data);

        System.out.printf("voltage metric: %s\n", metricVal);
    }

    boolean withinJumpRange(int baseVoltage, int otherVoltage) {
        final int range = 3;
        return baseVoltage - otherVoltage <= range;
    }

    long countArrangements(List<Integer> voltageData) {
        List<Long> solutions = new ArrayList<>();

        for (int index = 0; index < voltageData.size(); index++) {
            long pathCount;
            if (index <= 1) {
                pathCount = 1;
            } else {
                if (index >= 3 && withinJumpRange(voltageData.get(index), voltageData.get(index - 3))) {
                    pathCount = solutions.get(index - 3) + solutions.get(index - 2) + solutions.get(index - 1);
                } else if (withinJumpRange(voltageData.get(index), voltageData.get(index - 2))) {
                    pathCount = solutions.get(index - 2) + solutions.get(index - 1);
                } else {
                    pathCount = solutions.get(index - 1);
                }
            }

            solutions.add(index, pathCount);
        }

        System.out.println(solutions.toString());
        return solutions.get(solutions.size() - 1);
    }

    public void executePartTwo() throws IOException, IllegalArgumentException {
        List<Integer> data = parseVoltageData();
        organizeAdapters(data);
        long arrangements = countArrangements(data);

        System.out.printf("total arrangements: %s\n", arrangements);
    }
}
