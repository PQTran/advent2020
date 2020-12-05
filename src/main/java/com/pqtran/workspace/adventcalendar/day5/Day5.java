package com.pqtran.workspace.adventcalendar.day5;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.IllegalArgumentException;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;

public class Day5 implements Executable {
    private ResourceFile resourceFile;

    public Day5(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
     }

    List<String> parseSeatCodes() throws IOException {
        List<String> seatCodes = new ArrayList<>();
        try (BufferedReader reader = resourceFile.getReader()) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                seatCodes.add(line);
            }
        }

        return seatCodes;
    }

    public void executePartOne() throws Exception {
        List<String> seatCodes = parseSeatCodes();

        int maxSeatId = seatCodes.stream()
            .map(x -> (new Seat(x)).getId())
            .max((a, b) -> a - b)
            .get();
        System.out.printf("max seat id: %s\n", maxSeatId);
    }

    public void executePartTwo() throws Exception {
        List<String> seatCodes = parseSeatCodes();

        List<Integer> seatIds = seatCodes.stream()
            .map(sc -> new Seat(sc))
            .filter(x -> x.row > 0 && x.row < 127)
            .map(x -> x.getId())
            .sorted()
            .collect(Collectors.toList());

        for (int index = 0; index < seatIds.size() - 1; index++) {
            if (seatIds.get(index) + 1 != seatIds.get(index + 1)) {
                System.out.println(seatIds.get(index) + 1);
            }
        }
    }

    class Seat {
        final int ROW_LENGTH = 8;
        int row;
        int column;

        Seat(String code) throws IllegalArgumentException {
            Map<String, Integer> properties = parse(code);
            this.row = properties.get("row");
            this.column = properties.get("column");
        }

        Map<String, Integer> parse(String code) throws IllegalArgumentException {
            Map<String, Integer> properties = new HashMap<>();
            final char FRONT = 'F';
            final char BACK = 'B';
            final int FRONT_INDEX = 0;
            final int BACK_INDEX = 127;

            final char LEFT = 'L';
            final char RIGHT = 'R';
            final int LEFT_INDEX = 0;
            final int RIGHT_INDEX = 7;

            String rowCode = code.substring(0, 7);
            properties.put("row", decode(rowCode, FRONT, BACK, FRONT_INDEX, BACK_INDEX));

            String colCode = code.substring(7);
            properties.put("column", decode(colCode, LEFT, RIGHT, LEFT_INDEX, RIGHT_INDEX));

            return properties;
        }

        int decode(String code, char lowerKey, char upperKey, int min, int max) throws IllegalArgumentException {
            for (Character c : code.toCharArray()) {
                double mid = ((double) min + max) / 2;

                if (c == lowerKey) {
                    max = (int) Math.floor(mid);
                } else if (c == upperKey) {
                    min = (int) Math.ceil(mid);
                } else {
                    throw new IllegalArgumentException("Unrecognized code character");
                }
            }

            if (min != max) {
                throw new IllegalArgumentException("Unable to decode single value");
            }

            return min;
        }

        int getId() {
            return (this.row * ROW_LENGTH) + this.column;
        }
    }
}
