package controllers;

import javafx.scene.layout.VBox;
import models.MainPaneModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import toolkit.Toolkit;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static toolkit.Toolkit.*;

/**
 * @author Daniel Dietrich
 */
public class MainPaneController implements Initializable {
    @FXML
    public MenuItem menuRenameFolder;
    @FXML
    public MenuItem menuDeleteFolder;
    @FXML
    public MenuItem menuRenameNote;
    @FXML
    public MenuItem menuDeleteNote;
    @FXML
    public VBox areaEditor;
    @FXML
    public VBox areaNotes;
    @FXML
    public TextField searchField;
    @FXML
    public CheckMenuItem darkModeMenuItem;
    @FXML
    private TextArea textArea;
    @FXML
    private ListView<String> folderList, noteList;
    @FXML
    private TextField titleField;
    @FXML
    private Button newNoteButton;
    @FXML
    private MenuItem menuNewNote;

    private final MainPaneModel model;

    public MainPaneController(MainPaneModel model) {
        this.model = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshDirectoryList();
    }

    private void refreshDirectoryList() {
        folderList.getItems().clear();
        model.setFolderList(readDirectories(model.getRootPath()));

        for (String folder : model.getFolderList()) {
            folderList.getItems().add(folder);
        }

        refreshFileList();
    }

    // includes search logic
    private void refreshFileList() {
        noteList.getItems().clear();

        String searched = searchField.getText();

        String selectedDirectory = trimBrackets(folderList.getSelectionModel().getSelectedItems().toString());
        model.setSelectedDirectory(selectedDirectory);
        model.setNoteList(readFiles(model.getAbsoluteDirectoryPath()));

        for (String note : model.getNoteList()) {
            if (searched.isEmpty()) {
                noteList.getItems().add(trimFormat(note));
            } else if (trimFormat(note).contains(searched)) {
                noteList.getItems().add(trimFormat(note));
            }
        }

        refreshFileContents();
    }

    private void refreshFileContents() {
        textArea.clear();
        titleField.clear();

        String selectedFile = trimBrackets(noteList.getSelectionModel().getSelectedItems().toString()) + model.getFormat();
        model.setSelectedFile(selectedFile);
        updateEditorArea();

        if (model.isFileSelected()) {
            readFileContents(model.getAbsoluteFilePath());
        }
    }

    @FXML
    private void onNewNote() {
        Optional<String> result = model.showNameInputDialog("Nowa notatka");
        result.ifPresent(str -> {
            if (!createFile(str + model.getFormat(), model.getAbsoluteDirectoryPath())) {
                model.showAlert("Błąd", "Pole nie może być puste", Alert.AlertType.WARNING);
            }
        });

        refreshFileList();
    }

    @FXML
    private void onNewFolder() {
        Optional<String> result = model.showNameInputDialog("Nowy folder");
        result.ifPresent(str -> {
            if (!createDirectory(str, model.getRootPath())) {
                model.showAlert("Błąd", "Pole nie może być puste", Alert.AlertType.WARNING);
            }
        });

        refreshDirectoryList();
    }

    @FXML
    private void onAbout() {
        String title = "O programie";
        String content = "Prosta aplikacja do organizacji notatek\n\nAutor: Daniel Dietrich";
        model.showAlert(title, content, Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onClose() {
        model.close();
    }

    @FXML
    private void onMouseClickFolderList() {
        refreshFileList();

        updateNoteArea();
    }

    @FXML
    private void onMouseClickNoteList() {
        refreshFileContents();

        updateEditorArea();
    }

    @FXML
    private void onWrite() {
        writeFileContent(model.getAbsoluteFilePath());

        String oldName = model.getAbsoluteFilePath();
        String newName = model.getAbsoluteDirectoryPath() + "/" + titleField.getText() + model.getFormat();
        boolean isRenamed = Toolkit.tryRename(oldName, newName);

        if (isRenamed) refreshFileList();
    }

    private void readFileContents(String path) {
        String title = trimFormat(model.getSelectedFile());
        titleField.appendText(title);

        String line;

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                textArea.appendText(line + "\n");
            }

            bufferedReader.close();
        } catch (IOException ignored) {
        }
    }

    private void writeFileContent(String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(textArea.getText());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRenameFolder() {
        Optional<String> result = model.showNameInputDialog("Zmień nazwę folderu");

        result.ifPresent(newName -> {
            String oldPath = model.getAbsoluteDirectoryPath();
            boolean isRenamed = Toolkit.tryRename(oldPath, model.getRootPath() + "/" + newName);
            if (isRenamed) refreshDirectoryList();
        });
    }

    public void onDeleteFolder() {
        String title = "Usuwanie folderu";
        String content = "Czy napewno chcesz usunąć folder " + model.getSelectedDirectory() + "?";
        Optional<ButtonType> result = model.showAlert(title, content, Alert.AlertType.CONFIRMATION);

        result.ifPresent(response -> {
            if (response == ButtonType.OK) {
                Toolkit.delete(model.getAbsoluteDirectoryPath());
                refreshDirectoryList();
                updateNoteArea();
            }
        });
    }

    public void onRenameNote() {
        Optional<String> result = model.showNameInputDialog("Zmień nazwę notatki");

        result.ifPresent(newName -> {
            String oldPath = model.getAbsoluteFilePath();
            boolean isRenamed = Toolkit.tryRename(oldPath, model.getAbsoluteDirectoryPath() + "/" + newName + model.getFormat());
            if (isRenamed) refreshFileList();
        });
    }

    public void onDeleteNote() {
        String title = "Usuwanie notatki";
        String content = "Czy napewno chcesz usunąć notatkę " + model.getSelectedFile() + "?";
        Optional<ButtonType> result = model.showAlert(title, content, Alert.AlertType.CONFIRMATION);

        result.ifPresent(response -> {
            if (response == ButtonType.OK) {
                Toolkit.delete(model.getAbsoluteFilePath());
                refreshFileList();
            }
        });
    }

    public void onNoteContextMenuShown() {
        boolean isSelected = noteList.getSelectionModel().isEmpty();

        menuDeleteNote.setDisable(isSelected);
        menuRenameNote.setDisable(isSelected);
    }

    public void onFolderContextMenuShown() {
        boolean isSelected = folderList.getSelectionModel().isEmpty();

        menuDeleteFolder.setDisable(isSelected);
        menuRenameFolder.setDisable(isSelected);
    }

    private void updateNoteArea() {
        boolean isSelected = !(model.getSelectedDirectory() != null && !model.getSelectedDirectory().isEmpty());

        areaNotes.setDisable(isSelected);
        menuNewNote.setDisable(isSelected);
    }

    private void updateEditorArea() {
        boolean isSelected = !trimFormat(model.getSelectedFile()).isEmpty();

        areaEditor.setDisable(!isSelected);
    }

    public void OnSearchInput() {
        refreshFileList();
    }

    public void onToggleDarkMode() {
        String disableTheme = darkModeMenuItem.isSelected() ? model.LIGHT_THEME_PATH : model.LIGHT_THEME_PATH;
        String enableTheme = darkModeMenuItem.isSelected() ? model.DARK_THEME_PATH : model.LIGHT_THEME_PATH;

        main.Notebook.getScene().getStylesheets().remove(disableTheme);
        main.Notebook.getScene().getStylesheets().add(enableTheme);
    }

}
