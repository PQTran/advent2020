package com.pqtran.workspace.adventcalendar.day1;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
import com.pqtran.workspace.adventcalendar.Executable;
import com.pqtran.workspace.utility.ResourceFile;

public class Day1 implements Executable {
    private static final int EXPENSE_TOTAL = 2020;
    private ResourceFile resourceFile;

    public Day1(String dataFile) {
        this.resourceFile = new ResourceFile(dataFile);
    }

    public void executePartOne() throws IOException {
        List<Integer> expenses = getExpenses();

        System.out.println(calculateExpenseReport(expenses, EXPENSE_TOTAL));
    }

    private List<Integer> getExpenses() throws IOException {
        List<Integer> values = new ArrayList<>();
        BufferedReader reader = this.resourceFile.getReader();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Integer value = Integer.parseInt(line);
            values.add(value);
        }

        return values;
    }

    private int calculateExpenseReport(List<Integer> expenses, int targetSum) {
        Set<Integer> visited = new HashSet<>();

        for (int i = 0; i < expenses.size(); i++) {
            int expense = expenses.get(i);
            int otherExpense = targetSum - expense;

            if (visited.contains(otherExpense)) {
                return expense * otherExpense;
            } else {
                visited.add(expense);
            }
        }

        return -1;
    }

    public void executePartTwo() throws IOException {
        List<Integer> expenses = getExpenses();
        System.out.println(calculateComplexExpenseReport(expenses, EXPENSE_TOTAL));
    }

    private int calculateComplexExpenseReport(List<Integer> expenses, int targetSum) {
        Map<Integer, Integer> expenseCount = new HashMap<>();
        expenses.stream().forEach(expense ->
                                  expenseCount.put(expense, expenseCount.getOrDefault(expense, 0) + 1));

        for (int i = 0; i < expenses.size(); i++) {
            for (int j = i + 1; j < expenses.size(); j++) {
                Integer firstVal = expenses.get(i);
                Integer secondVal = expenses.get(j);
                expenseCount.put(firstVal, expenseCount.get(firstVal) - 1);
                expenseCount.put(secondVal, expenseCount.get(secondVal) - 1);

                Integer thirdVal = targetSum - (firstVal + secondVal);
                if (expenseCount.containsKey(thirdVal) &&
                    expenseCount.get(thirdVal) > 0) {
                    return firstVal * secondVal * thirdVal;
                } else {
                    expenseCount.put(firstVal, expenseCount.get(firstVal) + 1);
                    expenseCount.put(secondVal, expenseCount.get(secondVal) + 1);
                }
            }
        }

        return -1;
    }
}
