package com.dsm.diskspacemonitor;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DSMApplication extends Application {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(DSMApplication.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(DSMApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Disk Space Monitor");

        FXTrayIcon icon = this.setSystemTrayIcon(stage);

        LocalDiskEnumerator localDiskEnumerator = new LocalDiskEnumerator();
        ArrayList<LocalDiskDrive> drives = localDiskEnumerator.getLocalDiskDrives();
        ArrayList<LocalDiskDrive> lowSpaceDrives = localDiskEnumerator.getLowSpaceDrives(drives);

        if (lowSpaceDrives.isEmpty()) {
            this.showWarningMessageForLowSpaceDrives(icon, lowSpaceDrives);
        } else {
            log.info("No drives with low space");
        }

        FlowGridPane pane = setUpPercentageTiles(lowSpaceDrives);
        stage.setScene(pane.getScene());
        stage.show();
    }

    private FlowGridPane setUpPercentageTiles(ArrayList<LocalDiskDrive> drives) {
        FlowGridPane pane = new FlowGridPane(1, drives.size());
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setAlignment(Pos.CENTER);
        pane.setCenterShape(true);
        pane.setPadding(new Insets(5));
        pane.setBackground(
                new Background(
                        new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)
                )
        );
        drives.forEach(drive -> {
            Tile percentageTile = TileBuilder
                    .create()
                    .skinType(Tile.SkinType.PERCENTAGE)
                    .prefSize(500, 500)
                    .title(drive.getName())
                    .unit("%")
                    .description("Free Space")
                    .maxValue(100)
                    .build();
            percentageTile.setValue(drive.percentDiskSpaceUsed);
            pane.getChildren().add(percentageTile);
            log.info("Added tile for drive " + drive.getName() + " with " + drive.percentDiskSpaceUsed + "% used.");
        });
        return pane;
    }

    private void showWarningMessageForLowSpaceDrives(FXTrayIcon icon, ArrayList<LocalDiskDrive> lowSpaceDrives) {
        log.info("Low space disks found: " + lowSpaceDrives.size());
        StringBuilder warningMsg = new StringBuilder();
        for (LocalDiskDrive drive : lowSpaceDrives) {
            warningMsg.append(String.format("%s: %s%% used", drive.name, drive.percentDiskSpaceUsed));
            warningMsg.append("\n");
        }
        icon.showWarningMessage(
                "Low space on disks",
                "There are " + lowSpaceDrives.size() + " low space disks: " + warningMsg
        );
    }

    private FXTrayIcon setSystemTrayIcon(Stage stage) {
        FXTrayIcon icon = new FXTrayIcon(
                stage,
                Objects.requireNonNull(getClass().getResource("disk_space_monitor_icon.png"))
        );
        icon.addExitItem("Exit");
        icon.show();
        return icon;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override public void stop() {
        System.exit(0);
    }
}