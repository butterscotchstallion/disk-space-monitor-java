package com.dsm.diskspacemonitor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class testLocalDiskEnumerator {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(testLocalDiskEnumerator.class.getName());

    @Test
    public void getLocalDiskDrives() {
        LocalDiskEnumerator localDiskEnumerator = new LocalDiskEnumerator();
        ArrayList<LocalDiskDrive> drives = localDiskEnumerator.getLocalDiskDrives();

        assertFalse(drives.isEmpty());

        for (LocalDiskDrive drive : drives) {
            log.info(drive.name + "; free space=" + drive.freeSpace + "; total space=" + drive.totalSpace + "; percentage used=" + drive.percentDiskSpaceUsed);
        }
    }
}
