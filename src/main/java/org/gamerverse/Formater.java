package org.gamerverse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.TreeSet;

public class Formater {
    public final File readFile;
    public LinkedHashMap<String, TreeSet<String>> linkedHashMap;

    Formater(File readFile) {
        this.readFile = readFile;
        this.linkedHashMap = new LinkedHashMap<>();
    }

    public void format() {
        try {
            this.parse();
            this.filter();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse() throws FileNotFoundException {
        Scanner myReader = new Scanner(this.readFile);
        String currentSection = "";

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.startsWith("## ")) {
                currentSection = line.split("## ")[1];
                this.linkedHashMap.put(currentSection, new TreeSet<>());
            }
            if (line.startsWith("- ")) {
                String current = line.split("- ")[1];
                this.linkedHashMap.get(currentSection).add(current);
            }
        }

        myReader.close();
    }

    private void filter() {
        if (this.linkedHashMap.containsKey("Mods Removed")) {
            if (this.linkedHashMap.containsKey("Mods Updated")) {
                TreeSet<String> removedMods = this.linkedHashMap.get("Mods Removed");
                TreeSet<String> updatedMods = this.linkedHashMap.get("Mods Updated");

                for (String mod : removedMods) {
                    updatedMods.remove(mod);
                }
            }
            if (this.linkedHashMap.containsKey("Mods Added")) {
                TreeSet<String> removedMods = this.linkedHashMap.get("Mods Removed");
                TreeSet<String> addedMods = this.linkedHashMap.get("Mods Added");

                for (String mod : removedMods) {
                    addedMods.remove(mod);
                }
            }
        }
        if (this.linkedHashMap.containsKey("Mods Added")) {
            if (this.linkedHashMap.containsKey("Mods Updated")) {
                TreeSet<String> addedMods = this.linkedHashMap.get("Mods Added");
                TreeSet<String> updatedMods = this.linkedHashMap.get("Mods Updated");

                for (String mod : addedMods) {
                    updatedMods.remove(mod);
                }
            }
        }
    }

    public void writeToJSON(File outputLocation) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputLocation, this.linkedHashMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToJSON(String outputLocation) {
        this.writeToJSON(new File(outputLocation));
    }

    public void write(File outputLocation) {
        StringBuilder finalResult = new StringBuilder();
        for (String section : this.linkedHashMap.keySet()) {
            System.out.println(section);
            finalResult.append(MessageFormat.format("## {0}\n", section));
            for (String mod : this.linkedHashMap.get(section)) {
                finalResult.append(MessageFormat.format("- {0}\n", mod));
            }
            finalResult.append("\n");
        }

        try {
            FileWriter fileWriter = new FileWriter(outputLocation);
            fileWriter.write(finalResult.toString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String outputLocation) {
        this.write(new File(outputLocation));
    }
}
