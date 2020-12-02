package com.pqtran.workspace.utility;

import java.io.*;

public class ResourceFile {
    String resourceName;

    public ResourceFile(String resourceName) {
        this.resourceName = resourceName;
    }

    public BufferedReader getReader() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
