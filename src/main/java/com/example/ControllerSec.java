package com.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ControllerSec implements Initializable {
    @FXML
    TableView<FileInfo> filesTableOne;
    @FXML
    ComboBox<String>disksBox;
    @FXML
    TextField pathField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>("Type");
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(30);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("File name");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(100);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(80);


        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(100);

        DateTimeFormatter dtm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("last changes");
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtm)));
        fileDateColumn.setPrefWidth(130);
        filesTableOne.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDateColumn);
        filesTableOne.getSortOrder().add(fileNameColumn);


        disksBox.getItems().clear();
        for (Path p: FileSystems.getDefault().getRootDirectories()
        ) {
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

        filesTableOne.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2){
                    Path path = Paths.get(pathField.getText())
                            .resolve(filesTableOne.getSelectionModel()
                                    .getSelectedItem()
                                    .getFileName());
                    if (Files.isDirectory(path)){
                        updateList(path);
                    }
                }
            }
        });


        updateList(Paths.get("."));
    }

    public  void updateList(Path path){
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTableOne.getItems().clear();
            filesTableOne.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTableOne.sort();
        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Не удалось обновить список файлоф", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void btnUp(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null){
            updateList(upperPath );
        }
    }

    public void selectDisk(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFileName(){
        if(!filesTableOne.isFocused()){
            return null;
        }
        return filesTableOne.getSelectionModel().getSelectedItem().getFileName();
    }

    public String getCurrentPath(){
        return pathField.getText();
    }
}
