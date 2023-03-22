import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * CSC 309 UML Tutor
 * This class handles file writing and reading for saving and loading
 * This is a static class as it has no relevant information, only methods
 *
 * @author Alex Arrieta
 * @version 1.1
 */
public class SaveLoad {

    /**
     * Saves the current workspace to a file
     * If a file with the specified name exists, this does nothing
     * @param fileName File name to use for saving
     */
    public static void save(String fileName){
        try {
            File toSave = new File(fileName);
            if (toSave.createNewFile()) {
                //System.out.println("File created: " + toSave.getName());
                FileWriter writer = new FileWriter(fileName);
                writer.write(new TextPanel().recode());
                writer.close();
            } else {
                JOptionPane.showMessageDialog(null, "File with that name already exists");
                //System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the workspace from a file
     * If no file with such name exists this does nothing
     * @param fileName File to pull information from
     */
    public static void load(String fileName){
        try {
            File toLoad = new File(fileName);
            Scanner myReader = new Scanner(toLoad);
            String text = "";
            while(myReader.hasNextLine()) {
                text = text + myReader.nextLine() +"\n";
            }
            new TextPanel().parseInput(text);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No such file found");
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}