package com.jarkos.file;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by jkostrzewa on 2017-08-09.
 */
public class FileUpdater {
    private static final Logger logger = Logger.getLogger(FileUpdater.class);

    public static void addResultData(String newResult, String filePath) {
        FileUpdater rfu = new FileUpdater();
        try {
            rfu.updateResultFile(newResult, filePath);
        } catch (IOException e) {
            logger.info("Nie można otowrzyc pliku i dodac rezusltatu ");
            e.printStackTrace();
        }
    }

    private void updateResultFile(String result, String filePath) throws IOException {
        Writer output;
        output = new BufferedWriter(new FileWriter(filePath, true));
        output.append(System.lineSeparator() + result);
        output.close();
    }

}
