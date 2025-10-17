package library.util;

import java.io.*;
import java.util.*;

public class FileUtils {
    public static <T> void saveList(List<T> list, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static <T> List<T> loadList(String filename) {
        File f = new File(filename);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); return new ArrayList<>(); }
    }
}
