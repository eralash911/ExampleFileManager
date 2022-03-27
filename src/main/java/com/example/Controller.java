package com.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller {
    @FXML
    VBox leftPanel, rightPanel;

    public void exitBtn(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void btnCopy(ActionEvent actionEvent) {
        ControllerSec leftCS = (ControllerSec) leftPanel.getProperties().get("ctrl");
        ControllerSec rightCS = (ControllerSec) rightPanel.getProperties().get("ctrl");

        if (leftCS.getSelectedFileName() == null && rightCS.getSelectedFileName() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "files has been no choose", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        ControllerSec srcPC = null, dctPC = null;
        if (leftCS.getSelectedFileName() != null){
            srcPC = leftCS;
            dctPC = rightCS;
        }
        if (rightCS.getSelectedFileName() != null) {
            srcPC = rightCS;
            dctPC = leftCS;
        }
        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path distPath = Paths.get(dctPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.copy(srcPath, distPath);
            dctPC.updateList(Paths.get(dctPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Can't copy file", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
