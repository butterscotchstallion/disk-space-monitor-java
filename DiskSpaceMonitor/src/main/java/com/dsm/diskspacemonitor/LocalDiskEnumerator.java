package com.dsm.diskspacemonitor;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;

public class LocalDiskEnumerator {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LocalDiskEnumerator.class.getName());
    public ArrayList<LocalDiskDrive> getLocalDiskDrives() {
        ArrayList<LocalDiskDrive> drives = new ArrayList<>();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File[] allDrives = File.listRoots();

        if (allDrives != null) {
            if (allDrives.length == 0) {
                log.warning("No drives found on the system.");
                return drives;
            }
            for (File drive : allDrives) {
                drives.add(LocalDiskDrive.builder()
                        .name(fsv.getSystemDisplayName(drive))
                        .freeSpace(drive.getFreeSpace())
                        .totalSpace(drive.getTotalSpace())
                        .percentDiskSpaceUsed((int) ((drive.getTotalSpace() - drive.getFreeSpace()) * 100 / drive.getTotalSpace()))
                        .build());
            }
        } else {
            log.warning("No drives found on the system.");
        }

        return drives;
    }
}
