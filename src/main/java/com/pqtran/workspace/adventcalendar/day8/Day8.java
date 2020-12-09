package com.pqtran.workspace.adventcalendar.day8;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.IllegalArgumentException;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;

public class Day8 implements Executable {
    private ResourceFile resourceFile;

    public Day8(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
    }

    List<GameInstruction> parseGameInstructions() throws IOException, IllegalArgumentException {
        List<GameInstruction> gameInstructionList = new ArrayList<>();

        Pattern pattern = Pattern.compile("([a-z]+) (\\+?\\-?[0-9]+)");
        try (BufferedReader reader = resourceFile.getReader()) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Matcher matcher = pattern.matcher(line);
                if (!matcher.matches())
                    throw new IllegalArgumentException("invalid data format");

                gameInstructionList.add(new GameInstruction(matcher.group(1), Integer.valueOf(matcher.group(2))));
            }
        }

        return gameInstructionList;
    }

    GameState executeWithoutRepeat(List<GameInstruction> instructions) throws IllegalArgumentException {
        Set<GameInstruction> completed = new HashSet<>();

        int index = 0;
        int acc = 0;
        while (index >= 0 && index < instructions.size()) {
            GameInstruction curr = instructions.get(index);
            if (completed.contains(curr))
                break;

            completed.add(curr);
            if (curr.operation.equals("acc")) {
                acc += curr.argument;
            } else if (curr.operation.equals("jmp")) {
                index += curr.argument;
                continue;
            }

            index++;
        }

        return new GameState(index, acc);
    }

    public void executePartOne() throws Exception {
        List<GameInstruction> instructions = parseGameInstructions();

        GameState state = executeWithoutRepeat(instructions);
        System.out.printf("non repeating instructions acc value: %s\n", state.acc);
    }

    void fixCorruptInstruction(List<GameInstruction> instructions) throws IllegalArgumentException {
        for (GameInstruction curr : instructions) {
            if (curr.operation.equals("nop") || curr.operation.equals("jmp")) {
                curr.operation = curr.operation.equals("jmp") ? "nop" : "jmp";

                GameState state = executeWithoutRepeat(instructions);
                if (state.index == instructions.size())
                    return;

                curr.operation = curr.operation.equals("jmp") ? "nop" : "jmp";
            }
        }

        throw new IllegalArgumentException("input data does not have valid solution");
    }

    public void executePartTwo() throws Exception {
        List<GameInstruction> instructions = parseGameInstructions();

        fixCorruptInstruction(instructions);
        GameState state = executeWithoutRepeat(instructions);
        System.out.printf("fixed instructions acc value: %s\n", state.acc);
    }

    class GameState {
        int index;
        int acc;

        GameState(int index, int acc) {
            this.index = index;
            this.acc = acc;
        }
    }

    class GameInstruction {
        String operation;
        Integer argument;

        GameInstruction(String operation, Integer argument) {
            this.operation = operation;
            this.argument = argument;
        }
    }
}
