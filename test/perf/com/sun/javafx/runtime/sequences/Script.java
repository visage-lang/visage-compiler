package com.sun.javafx.runtime.sequences;

import java.util.*;
import java.io.*;

public class Script {
    public List<Test> tests = new ArrayList<Test>();
    public List<String> commands = new ArrayList<String>();
    public List<String> args = new ArrayList<String>();

    private static Map<String, Test> testMap = new HashMap<String, Test>();
    static {
        testMap.put("SE", new SequenceExerciser());
        try {
            testMap.put("JE", (Test)Class.forName("com.sun.javafx.runtime.sequences.JPEGEncoder").newInstance());
        } catch (Exception ex) {
            // ignore
        }
    }
    
    public static Script readFromFile(String script) {
        Script result = new Script();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(script));
            String line;
            String[] parts;
            Test test;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;   // ignore comments
                if ((line = line.trim()).equals("")) 
                    continue;  // ignore empty lines

                parts = line.split(" ", 2);
                test = testMap.get(parts[0]);
                if (test == null) {
                    System.err.println("Error while reading test-script");
                    System.err.println("Command not found: " + line);
                    reader.close();
                    return null;
                }
                result.tests.add(test);
                result.commands.add(parts[0]);
                result.args.add(parts[1]);
            }

            reader.close();
        } catch (FileNotFoundException ex) {
            System.err.println("FileNotFound: " + script);
            return null;
        } catch (IOException ex) {
            System.err.println("IOException while reading test-script: " + ex.getLocalizedMessage());
            return null;
        }
        return result;
    }
}
