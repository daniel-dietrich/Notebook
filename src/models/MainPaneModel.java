package models;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.LinkedList;
import java.util.Optional;

import static toolkit.Toolkit.*;

/**
 * @author Daniel Dietrich
 */
public class MainPaneModel {
    private final String rootPath = System.getProperty("user.home") + "/.notebook";

    private LinkedList<String> folderList;
    private LinkedList<String> noteList;

    private String selectedDirectory;
    private String selectedFile;
    private final String format = ".txt";

    public final String LIGHT_THEME_PATH = "stylesheets/light-theme.css";
    public final String DARK_THEME_PATH = "stylesheets/dark-theme.css";

    public MainPaneModel() {
        createDirectory("Domyślny", rootPath);
    }

    public void close() {
        System.exit(0);
    }

    public Optional<ButtonType> showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public Optional<String> showNameInputDialog(String title) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText("Wprowadź nazwę :");

        return dialog.showAndWait();
    }

    public void setFolderList(LinkedList<String> folderList) {
        this.folderList = folderList;
    }

    public LinkedList<String> getFolderList() {
        return folderList;
    }

    public void setNoteList(LinkedList<String> noteList) {
        this.noteList = noteList;
    }

    public LinkedList<String> getNoteList() {
        return noteList;
    }

    public void setSelectedDirectory(String selectedDirectory) {
        this.selectedDirectory = selectedDirectory;
    }

    public String getSelectedDirectory() {
        return selectedDirectory;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }

    public String getSelectedFile() {
        return selectedFile;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getAbsoluteDirectoryPath() {
        return getRootPath() + "/" + getSelectedDirectory();
    }

    public String getAbsoluteFilePath() {
        return getAbsoluteDirectoryPath() + "/" + getSelectedFile();
    }

    public String getFormat() {
        return format;
    }

    public boolean isFileSelected() {
        return (getSelectedFile() != null && !getSelectedFile().isEmpty());
    }
}
