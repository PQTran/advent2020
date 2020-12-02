package com.pqtran.workspace.app;

import java.util.*;
import com.pqtran.workspace.utility.ArgumentParser;
import com.pqtran.workspace.utility.Executor;

public class App {
    private static String DEFAULT_INPUT = "data";

    public static void main(String[] args) throws Exception {
        Map<String, String> argMap = ArgumentParser.parseArguments(args);
        argMap.put("dataType", argMap.getOrDefault("dataType", DEFAULT_INPUT));

        validateArg(argMap, "day");
        validateArg(argMap, "part");

        Executor.execute(argMap.get("day"),
                         argMap.get("part"),
                         argMap.get("dataType"));
    }

    static void validateArg(Map<String, String> argMap, String key) {
        if (!argMap.containsKey(key)) {
            System.out.printf("Include '%s' arg.\n", key);
            System.exit(1);
        }
    }
}
