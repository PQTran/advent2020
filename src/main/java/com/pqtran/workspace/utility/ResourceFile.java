package com.pqtran.workspace.utility;

import java.io.*;

public class ResourceFile {
    String resourceName;

    public ResourceFile(String resourceName) {
        this.resourceName = resourceName;
    }

    public BufferedReader getReader() throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);

        if (inputStream == null) {
            throw new FileNotFoundException(String.format("resource file: %s not found", this.resourceName));
        }

        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
