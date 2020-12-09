package com.pqtran.workspace.adventcalendar.day7;

import java.util.*;
import java.util.stream.Collectors;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;
import com.pqtran.workspace.adventcalendar.day7.*;

public class Day7 implements Executable {
    private ResourceFile resourceFile;

    public Day7(String fileName) {
        this.resourceFile = new ResourceFile(fileName);
    }

    boolean recursiveContains(Map<String, List<BagGroup>> rules, String parentColor, String targetColor) {
        if (!rules.containsKey(parentColor))
            return false;

        List<BagGroup> childrenBagGroups = rules.get(parentColor);
        for (BagGroup bagGroup : childrenBagGroups) {
            if (bagGroup.color.equals(targetColor))
                return true;

            if (recursiveContains(rules, bagGroup.color, targetColor))
                return true;
        }

        return false;
    }

    int getUniqueParents(Map<String, List<BagGroup>> rules, String childColor) {
        int count = 0;

        List<String> keys = rules.keySet().stream().collect(Collectors.toList());
        for (String parentColor : keys) {
            if (recursiveContains(rules, parentColor, childColor))
                count++;
        }

        return count;
    }

    public void executePartOne() throws Exception {
        String targetBagColor = "shiny gold";
        Map<String, List<BagGroup>> rules = BagRuleParser.getParentChildrenMapping(resourceFile);

        int uniqueParents = getUniqueParents(rules, targetBagColor);
        System.out.printf("number of unique parent bags: %s\n", uniqueParents);
    }

    int getNestedBagCount(Map<String, List<BagGroup>> rules, String parentColor) {
        List<BagGroup> childrenBagGroups = rules.getOrDefault(parentColor, new ArrayList<BagGroup>());

        int currLevelCount = childrenBagGroups.stream().map(bagGroup -> bagGroup.quantity).reduce((a, b) -> a + b).orElse(0);

        int nestedCount = 0;
        for (BagGroup bagGroup : childrenBagGroups) {
            nestedCount += bagGroup.quantity * getNestedBagCount(rules, bagGroup.color);
        }

        return currLevelCount + nestedCount;
    }

    public void executePartTwo() throws Exception {
        String targetBagColor = "shiny gold";
        Map<String, List<BagGroup>> rules = BagRuleParser.getParentChildrenMapping(resourceFile);

        int count = getNestedBagCount(rules, targetBagColor);
        System.out.printf("number of nested children bags: %s\n", count);
    }
}
