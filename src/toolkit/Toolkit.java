package toolkit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * @author Daniel Dietrich
 */
public class Toolkit {
    public static LinkedList<String> readDirectories(String path) {
        LinkedList<String> directories = new LinkedList<>();

        Stream.of(new File(path).listFiles())
                .filter(File::isDirectory)
                .map(File::getName)
                .forEach(directories::push);

        return directories;
    }

    public static LinkedList<String> readFiles(String path) {
        LinkedList<String> files = new LinkedList<>();

        Stream.of(new File(path).listFiles())
                .filter(File::isFile)
                .map(File::getName)
                .forEach(files::push);

        return files;
    }

    /**
     * @param directoryName name of a new directory
     * @param path          path of a new directory
     * @return false if input field (directoryName) is empty
     */
    public static boolean createDirectory(String directoryName, String path) {
        if (!directoryName.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(path, directoryName));
            } catch (IOException e) {
                System.err.println("Failed to create directory!" + e.getMessage());
            }
            return true;
        } else return false;
    }

    /**
     * @param fileName name of a new file
     * @param path     path of a new file
     * @return false if input field (fileName) is empty
     */
    public static boolean createFile(String fileName, String path) {
        if (!fileName.startsWith(".")) {
            try {
                Files.createFile(Paths.get(path, fileName));
            } catch (IOException e) {
                System.err.println("Failed to create file!" + e.getMessage());
            }
            return true;
        } else return false;
    }

    public static String trimBrackets(String str) {
        return str.substring(1, str.length() - 1);
    }

    public static String trimFormat(String str) {
        if (str.isBlank() || !str.contains(".")) return str;

        return str.substring(0, str.lastIndexOf('.'));
    }

    public static void delete(String directory) {
        Path pathToBeDeleted = Path.of(directory);

        try {
            Files.walk(pathToBeDeleted)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean tryRename(String oldPath, String newPath) {
        File oldFilename = new File(oldPath);
        File newFilename = new File(newPath);

        return (!oldFilename.equals(newFilename) && oldFilename.renameTo(newFilename));
    }
}
