package com.company.app;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Dictionary {
    FileWriter fileWriter;
    BufferedWriter bufferedWriter;
    PrintWriter printWriter;
    public String filePath = new File("").getAbsolutePath().concat("\\src\\com\\company\\app\\Dictionary.txt");
    File file;
    public List<String[]> dict = new ArrayList<String[]>();
    Lock lock = new ReentrantLock();

    public void readFile(){

        String line;
        try {
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                line = sc.nextLine();
                if(!line.isEmpty()){
                    String[] word_meaning = line.split("==__==");
                    dict.add(word_meaning);
                }
            }

            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void update() {
        lock.lock();
        try {
            System.out.println("updating");
            //clears the text file
            PrintWriter destroyer = new PrintWriter(filePath);
            destroyer.print("");
            destroyer.close();

            //add the content of the dict array on to the empty text file
            fileWriter = new FileWriter(filePath, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);

            for (String[] line :
                    dict) {
                printWriter.println(line[0] + "==__==" + line[1]);
            }
            printWriter.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }


        lock.unlock();


    }

    public int wordExists(String word) {
        lock.lock();
        System.out.println("checking if word exists");
        int counter = 1;
        for (String[] line : dict) {
            if (line[0].equals(word)) {
                return counter;
            }
            counter++;
        }
        lock.unlock();
        return 0;
    }


    public String addWord(String bundle) {
        String[] word_meaning = bundle.split("==__==", 2);
        if (wordExists(word_meaning[0]) == 0) {
            dict.add(word_meaning);
            update();
            return "word has been successfully added";
        } else {
            return "word is already defined";

        }

    }


    public String getMeaning(String word) {

        int index = wordExists(word);
        if (index != 0) {
            return dict.get(index - 1)[1];

        } else {
            return "word does not exist";

        }


    }

    public String removeWord(String word) {
        int index = wordExists(word);
        if (index != 0) {
            dict.remove(index - 1);
            update();
            return "word has been successfully removed";

        }
        return "word does not exist";

    }

    public static void main(String[] args) {

    }



}

//class test {
//
//    public static void main(String[] args) {
//        Dictionary tester = new Dictionary();
//        tester.ReadfileThread.start();
//        //System.out.println(tester.addWord("test==__==an experiment"));
//        //System.out.println(tester.removeWord("test"));
//        //System.out.println(tester.getMeaning("test"));
//    }
//}
