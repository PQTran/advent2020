package com.pqtran.workspace.adventcalendar.day9;

import java.io.*;
import java.util.*;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;

public class Day9 implements Executable {
    private ResourceFile resourceFile;

    public Day9(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
    }

    XmasData parseData() throws IOException {
        int windowSize = 0;
        List<Long> dataList = new ArrayList<>();
        try (BufferedReader reader = resourceFile.getReader()) {
            windowSize = Integer.valueOf(reader.readLine());
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                dataList.add(Long.valueOf(line));
            }
        }

        return new XmasData(dataList, windowSize);
    }

    boolean containsPair(LinkedList<Long> window, long sum) {
        for (int i = 0; i < window.size() - 1; i++) {
            for (int j = i + 1; j < window.size(); j++) {
                if (window.get(i) + window.get(j) == sum)
                    return true;
            }
        }

        return false;
    }

    long findDataWeakness(List<Long> data, int windowSize) {
        LinkedList<Long> window = new LinkedList<>();
        for (int i = 0; i < windowSize; i++) {
            Long dataVal = data.get(i);
            window.add(dataVal);
        }

        for (int index = windowSize; index < data.size(); index++) {
            Long dataVal = data.get(index);
            if (!containsPair(window, dataVal))
                return dataVal;

            window.add(dataVal);
            window.remove();
        }

        return -1;
    }

    public void executePartOne() throws Exception {
        XmasData xmasData = parseData();
        long value = findDataWeakness(xmasData.data, xmasData.windowSize);

        System.out.printf("weakness value: %s\n", value);
    }

    long getEncryptionWeakness(List<Long> data, long weaknessValue) throws IllegalArgumentException {
        long windowSum = data.get(0) + data.get(1);
        int headIndex = 1;
        int tailIndex = 0;

        while (headIndex < data.size()) {
            System.out.printf("tail: %s, head: %s\n", data.get(tailIndex), data.get(headIndex));
            System.out.printf("sum: %s\n", windowSum);
            if (windowSum == weaknessValue) {
                break;
            } else if (windowSum < weaknessValue) {
                windowSum += data.get(++headIndex);
            } else {
                windowSum -= data.get(tailIndex++);
            }
        }

        long min = data.get(tailIndex);
        long max = data.get(headIndex);
        for (int index = tailIndex; index <= headIndex; index++) {
            min = Math.min(min, data.get(index));
            max = Math.max(max, data.get(index));
        }

        return min + max;
    }

    public void executePartTwo() throws Exception {
        XmasData xmasData = parseData();
        long weaknessValue = findDataWeakness(xmasData.data, xmasData.windowSize);

        long encryptionWeakness = getEncryptionWeakness(xmasData.data, weaknessValue);
        System.out.printf("encryption weakness is: %s\n", encryptionWeakness);
    }

    class XmasData {
        List<Long> data;
        int windowSize;

        XmasData(List<Long> data, int windowSize) {
            this.data = data;
            this.windowSize = windowSize;
        }
    }
}
