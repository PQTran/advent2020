package com.pqtran.workspace.adventcalendar.day6;

import java.util.*;
import java.io.*;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;

public class Day6 implements Executable {
    private ResourceFile resourceFile;

    public Day6(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
    }

    List<CustomsDeclaration> parseDeclarations() throws IOException {
        List<CustomsDeclaration> declarationList = new ArrayList<>();
        try (BufferedReader reader = resourceFile.getReader()) {
            String line = reader.readLine();

            while (line != null) {
                CustomsDeclaration declaration = new CustomsDeclaration();
                while (line != null && line.length() > 0) {
                    declaration.addResponses(line);
                    line = reader.readLine();
                }

                declarationList.add(declaration);
                line = reader.readLine();
            }
        }

        return declarationList;
    }

    public void executePartOne() throws IOException {
        List<CustomsDeclaration> declarationList = parseDeclarations();

        int questionTotal = declarationList.stream()
            .map(x -> x.getQuestionCount())
            .reduce((a, b) -> a + b)
            .get();

        System.out.printf("total questions: %s\n", questionTotal);
    }

    public void executePartTwo() throws IOException {
        List<CustomsDeclaration> declarationList = parseDeclarations();

        int completeResponseTotal = declarationList.stream()
            .map(x -> x.getCompleteYesResponseCount())
            .reduce((a, b) -> a + b)
            .get();

        System.out.printf("complete responses: %s\n", completeResponseTotal);
    }

    class CustomsDeclaration {
        int groupMembers = 0;
        Map<Character, Integer> yesResponses = new HashMap<>();

        void addResponses(String str) {
            this.groupMembers++;
            for (Character c : str.toCharArray()) {
                yesResponses.put(c, yesResponses.getOrDefault(c, 0) + 1);
            }
        }

        int getQuestionCount() {
            return yesResponses.keySet().size();
        }

        int getCompleteYesResponseCount() {
            return (int) yesResponses.entrySet().stream().filter(kv -> kv.getValue() == this.groupMembers).count();
        }
    }
}
