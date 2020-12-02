package com.pqtran.workspace.utility;

import java.util.*;

public class ArgumentParser {
    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> argMap = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (!args[i].startsWith("--") ||
                i == args.length - 1)
                continue;

            String key = args[i].substring(2);
            String value = args[i + 1];
            argMap.put(key, value);
        }

        return argMap;
    }
}
