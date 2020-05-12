package ru.otus.helpers;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class FileHelper {
    private FileHelper() {
    }

    public static String localFileNameOrResourceNameToFullPath(String fileOrResourceName){
        String path = null;
        File file = new File(String.format("./%s",fileOrResourceName));
        if (file.exists()) {
            path = URLDecoder.decode(file.toURI().getPath(), StandardCharsets.UTF_8);
        }

        if (path == null) {
            System.out.println("Local file not found, looking into resources");
            path = Optional.ofNullable(FileHelper.class.getClassLoader().getResource(fileOrResourceName))
                    .orElseThrow(() ->  new RuntimeException(String.format("File \"%s\" not found", fileOrResourceName)))
                    .toExternalForm();
        }
        return path;
    }
}
