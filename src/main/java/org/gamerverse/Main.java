package org.gamerverse;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File readFile = new File("src/test/resources/readFile.md");
        Formater formater = new Formater(readFile);
        formater.format();
        formater.writeToJSON("src/test/resources/outputFile.json");
        formater.write("src/test/resources/outputFile.md");
    }
}