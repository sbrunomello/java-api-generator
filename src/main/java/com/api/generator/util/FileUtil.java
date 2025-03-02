package com.api.generator.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static void saveToFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Cria diretórios se não existirem

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                writer.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
