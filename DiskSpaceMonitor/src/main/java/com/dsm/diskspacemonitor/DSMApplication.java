package com.dsm.diskspacemonitor;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DSMApplication extends Application {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(DSMApplication.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DSMApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        FXTrayIcon icon = new FXTrayIcon(stage, Objects.requireNonNull(getClass().getResource("disk_space_monitor_icon.png")));
        icon.addExitItem("Exit");
        icon.show();

        LocalDiskEnumerator localDiskEnumerator = new LocalDiskEnumerator();
        ArrayList<LocalDiskDrive> drives = localDiskEnumerator.getLocalDiskDrives();
        ArrayList<LocalDiskDrive> lowSpaceDrives = localDiskEnumerator.getLowSpaceDrives(drives);
        if (!lowSpaceDrives.isEmpty()) {
            log.info("Low space disks found: {}");
            StringBuilder warningMsg = new StringBuilder();
            for (LocalDiskDrive drive : lowSpaceDrives) {
                warningMsg.append(String.format("%s: %s%% used", drive.name, drive.percentDiskSpaceUsed));
                warningMsg.append("\n");
            }
            icon.showWarningMessage(
                    "Low space on disks",
                    "There are " + lowSpaceDrives.size() + " low space disks: "+warningMsg
            );
        } else {
            log.info("No low space disks found.");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}