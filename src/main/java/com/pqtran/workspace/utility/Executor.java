package com.pqtran.workspace.utility;

import com.pqtran.workspace.adventcalendar.Executable;

public class Executor {
    public static void execute(String day, String part, String dataType) throws Exception {
        String className = String.format("com.pqtran.workspace.adventcalendar.day%s.Day%s", day, day);

        Executable exe = (Executable) Class.forName(className)
            .getConstructor(String.class)
            .newInstance(String.format("day%s-%s.txt", day, dataType));

        if (part.equals("1")) {
            exe.executePartOne();
        } else if (part.equals("2")) {
            exe.executePartTwo();
        } else {
            System.out.println("part arg incorrect.");
        }
    }
}
