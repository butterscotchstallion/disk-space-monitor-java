package com.dsm.diskspacemonitor;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
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

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(10);

        FlowGridPane pane = setUpPercentageTiles(drives);
        Scene scene = new Scene(pane);
        scene.setCamera(camera);
        stage.setScene(scene);
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
                        new BackgroundFill(Color.web("#11080B"), CornerRadii.EMPTY, Insets.EMPTY)
                )
        );
        drives.forEach(drive -> {
            /*Tile percentageTile = TileBuilder
                    .create()
                    .skinType(Tile.SkinType.PERCENTAGE)
                    .prefSize(500, 200)
                    .title(drive.getName())
                    .unit("%")
                    .description("Free Space")
                    .maxValue(100)
                    .build();*/
            Tile colorTile = TileBuilder
                    .create()
                    .skinType(Tile.SkinType.PERCENTAGE)
                    .prefSize(655, 192)
                    .title(drive.getName())
                    .unit("%")
                    .description("Space Used")
                    .maxValue(100)
                    .autoScale(true)
                    .barColor(drive.percentDiskSpaceUsed < 90 ? Color.GREEN : Color.RED)
                    .build();
            colorTile.setValue(drive.percentDiskSpaceUsed);
            pane.getChildren().add(colorTile);
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