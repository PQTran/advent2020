package com.pqtran.workspace.adventcalendar.day7;

import java.lang.IllegalArgumentException;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import com.pqtran.workspace.utility.ResourceFile;
import com.pqtran.workspace.adventcalendar.day7.BagGroup;

class BagRuleParser {
    static Pattern bagQuantityPattern = Pattern.compile("([0-9]*).*");

    // consider refactoring to simpler regex
    static Pattern bagColorPattern = Pattern.compile("[0-9]*\\s?(.*?) bags?.*");
    static Pattern bagListPattern = Pattern.compile(".* contain (.*).");

    static Integer parseBagQuantity(String line) throws IllegalArgumentException {
        Matcher bagQuantityMatcher = bagQuantityPattern.matcher(line);
        if (!bagQuantityMatcher.matches())
            throw new IllegalArgumentException("Unable to parse bag quantity");

        return Integer.valueOf(bagQuantityMatcher.group(1));
    }

    static String parseBagColor(String line) throws IllegalArgumentException {
        Matcher bagColorMatcher = bagColorPattern.matcher(line);
        if (!bagColorMatcher.matches())
            throw new IllegalArgumentException("Unable to parse bag color");

        return bagColorMatcher.group(1);
    }

    static String[] parseBagList(String line) throws IllegalArgumentException {
        Matcher bagListMatcher = bagListPattern.matcher(line);
        if (!bagListMatcher.matches())
            throw new IllegalArgumentException("Unable to parse list of bags");

        return bagListMatcher.group(1).split(", ");
    }

    static Map<String, List<BagGroup>> getParentChildrenMapping(ResourceFile resourceFile) throws IOException, IllegalArgumentException {
        Map<String, List<BagGroup>> mapping = new HashMap<>();

        try (BufferedReader reader = resourceFile.getReader()) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                List<BagGroup> children = new ArrayList<>();

                String parentColor = parseBagColor(line);
                if (line.indexOf("no other bags") != -1) {
                    mapping.put(parentColor, children);
                    continue;
                }

                String[] childrenBags = parseBagList(line);
                for (String bagStr : childrenBags) {
                    String color = parseBagColor(bagStr);
                    Integer quantity = parseBagQuantity(bagStr);

                    children.add(new BagGroup(color, quantity));
                }

                mapping.put(parentColor, children);
            }
        }

        return mapping;
    }

}
