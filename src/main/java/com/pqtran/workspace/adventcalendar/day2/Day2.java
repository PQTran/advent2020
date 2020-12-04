package com.pqtran.workspace.adventcalendar.day2;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;

public class Day2 implements Executable {
    private ResourceFile resourceFile;

    public Day2(String dataFile) {
        this.resourceFile = new ResourceFile(dataFile);
    }

    private List<PasswordPolicy> parsePolicies() throws IOException {
        List<PasswordPolicy> policies = new ArrayList<>();

        try (BufferedReader reader = this.resourceFile.getReader()) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                policies.add(new PasswordPolicy(line));
            }
        }

        return policies;
    }

    public void executePartOne() throws IOException, IllegalArgumentException {
        List<PasswordPolicy> policies = parsePolicies();

        System.out.printf("valid passwords: %s\n",
                          policies.stream().filter(p -> p.verifyPartOne()).count());
    }

    public void executePartTwo() throws IOException, IllegalArgumentException {
        List<PasswordPolicy> policies = parsePolicies();

        System.out.printf("valid passwords: %s\n",
                          policies.stream().filter(p -> p.verifyPartTwo()).count());
    }

    class PasswordPolicy {
        int keyA;
        int keyB;
        char policyChar;
        String password;

        PasswordPolicy(String line) throws IllegalArgumentException {
            Matcher matcher = parse(line);
            this.keyA = Integer.valueOf(matcher.group(1));
            this.keyB = Integer.valueOf(matcher.group(2));
            this.policyChar = matcher.group(3).charAt(0);
            this.password = matcher.group(4);
        }

        private Matcher parse(String line) throws IllegalArgumentException {
            Pattern pattern = Pattern.compile("(\\d+)-(\\d+) (\\w): (\\w+)");
            Matcher matcher = pattern.matcher(line);

            if (!matcher.matches()) {
                throw new IllegalArgumentException(String.format("incorrect format: %s", line));
            }

            return matcher;
        }

        boolean verifyPartOne() {
            int minCount = this.keyA;
            int maxCount = this.keyB;

            int policyCharCount = (int) password.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> c == this.policyChar)
                .count();

            return policyCharCount >= minCount && policyCharCount <= maxCount;
        }

        boolean verifyPartTwo() {
            int indexA = this.keyA - 1;
            int indexB = this.keyB - 1;
            char passwordCharA = this.password.charAt(indexA);
            char passwordCharB = this.password.charAt(indexB);

            return (passwordCharA == this.policyChar && passwordCharB != this.policyChar) ||
                (passwordCharA != this.policyChar && passwordCharB == this.policyChar);
        }
    }
}
