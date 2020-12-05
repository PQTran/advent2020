package com.pqtran.workspace.adventcalendar.day4;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Day4 implements Executable {
    private ResourceFile resourceFile;

    public Day4(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
    }

    List<TravelDocument> parseTravelDocuments() throws IOException {
        List<TravelDocument> documents = new ArrayList<>();
        try (BufferedReader reader = resourceFile.getReader()) {
            String line = reader.readLine();
            while (line != null) {
                StringBuilder sb = new StringBuilder();
                while (line != null && !line.trim().isEmpty()) {
                    if (sb.length() != 0) {
                        sb.append(" ");
                    }

                    sb.append(line.trim());
                    line = reader.readLine();
                }

                if (sb.toString().length() > 0)
                    documents.add(new TravelDocument(sb.toString()));

                line = reader.readLine();
            }
        }

        return documents;
    }

    public void executePartOne() throws IOException {
        List<TravelDocument> documents = parseTravelDocuments();

        int validDocumentCount = (int) documents.stream().filter(d -> d.isValidForTravel(Validation.Basic)).count();
        System.out.printf("valid documents for travel: %s\n", validDocumentCount);
    }

    public void executePartTwo() throws IOException {
        List<TravelDocument> documents = parseTravelDocuments();

        int validDocumentCount = (int) documents.stream().filter(d -> d.isValidForTravel(Validation.Complex)).count();
        System.out.printf("valid documents for travel: %s\n", validDocumentCount);
    }

    enum Validation {
        Basic,
        Complex
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    class TravelDocument {
        String birthYear;
        String issueYear;
        String expYear;
        String height;
        String hairColor;
        String eyeColor;
        String passportId;
        String countryId;

        TravelDocument(String line) {
            Map<String, String> kvMap = parse(line);
            this.birthYear = kvMap.getOrDefault("byr", "");
            this.issueYear = kvMap.getOrDefault("iyr", "");
            this.expYear = kvMap.getOrDefault("eyr", "");
            this.height = kvMap.getOrDefault("hgt", "");
            this.hairColor = kvMap.getOrDefault("hcl", "");
            this.eyeColor = kvMap.getOrDefault("ecl", "");
            this.passportId = kvMap.getOrDefault("pid", "");
            this.countryId = kvMap.getOrDefault("cid", "");
        }

        Map<String, String> parse(String line) {
            Map<String, String> map = new HashMap<>();
            String[] kvPairs = line.split(" ");
            for (String pair : kvPairs) {
                String[] keyValue = pair.split(":");
                map.put(keyValue[0], keyValue[1]);
            }

            return map;
        }

        boolean isValidForTravel(Validation validation) {
            return validateBirthYear(validation) && validateIssueYear(validation) &&
                validateExpYear(validation) && validateHeight(validation) &&
                validateHairColor(validation) && validateEyeColor(validation) &&
                validatePassportId(validation);
        }

        boolean simpleValidation(String value) {
            return value.length() > 0;
        }

        boolean validateRange(String year, int lower, int upper) {
            try {
                Integer value = Integer.valueOf(year);
                return value >= lower && value <= upper;
            } catch (NumberFormatException exception) {
                return false;
            }
        }

        boolean validateBirthYear(Validation validation) {
            if (validation == Validation.Basic)
                return simpleValidation(this.birthYear);

            return validateRange(this.birthYear, 1920, 2002);
        }

        boolean validateIssueYear(Validation validation) {
            if (validation == Validation.Basic)
                return simpleValidation(this.issueYear);

            return validateRange(this.issueYear, 2010, 2020);
        }

        boolean validateExpYear(Validation validation) {
            if (validation == Validation.Basic)
                return simpleValidation(this.expYear);

            return validateRange(this.expYear, 2020, 2030);
        }

        boolean validateHeightHelper(Matcher matcher, int lower, int upper) {
            try {
                int heightVal = Integer.valueOf(matcher.group(1));
                return heightVal >= lower && heightVal <= upper;
            } catch (Exception exception) {
                return false;
            }
        }

        boolean validateHeight(Validation validation) {
            if (validation == Validation.Basic)
                return simpleValidation(this.height);

            Pattern cmPattern = Pattern.compile("([0-9]*)cm");
            Matcher cmMatcher = cmPattern.matcher(this.height);
            if (cmMatcher.matches()) {
                return validateHeightHelper(cmMatcher, 150, 193);
            }

            Pattern inchPattern = Pattern.compile("([0-9]*)in");
            Matcher inchMatcher = inchPattern.matcher(this.height);
            if (inchMatcher.matches()) {
                return validateHeightHelper(inchMatcher, 59, 76);
            }

            return false;
        }

        boolean validateHairColor(Validation validation) {
            if (validation == Validation.Basic)
                return simpleValidation(this.hairColor);

            return Pattern.matches("#[0-9a-f]{6}", this.hairColor);
        }

        boolean validateEyeColor(Validation validation) {
            if (validation == Validation.Basic)
                return simpleValidation(this.eyeColor);

            switch(this.eyeColor) {
                case "amb":
                case "blu":
                case "brn":
                case "gry":
                case "grn":
                case "hzl":
                case "oth":
                    return true;
                default:
                    return false;
            }
        }

        boolean validatePassportId(Validation validation) {
            if (validation == Validation.Basic)
                return simpleValidation(this.passportId);

            return Pattern.matches("[0-9]{9}", this.passportId);
        }

        @Override
        public String toString() {
            String str;
            try {
                ObjectMapper mapper = new ObjectMapper();
                str = mapper.writeValueAsString(this);
            } catch (Exception exception) {
                exception.printStackTrace();
                str = super.toString();
            }

            return str;
        }

        void printValidation(Validation validation) {
            System.out.printf("%s validation results: %s, %s, %s, %s, %s, %s, %s\n",
                              validation.toString(), validateBirthYear(validation),
                              validateIssueYear(validation), validateExpYear(validation),
                              validateHeight(validation), validateHairColor(validation),
                              validateEyeColor(validation), validatePassportId(validation));
        }
    }
}
