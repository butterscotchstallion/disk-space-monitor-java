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

    @Test
    public void getLowSpaceDrives() {
        LocalDiskEnumerator localDiskEnumerator = new LocalDiskEnumerator();
        ArrayList<LocalDiskDrive> drives = localDiskEnumerator.getLocalDiskDrives();
        LocalDiskDrive fakeLowSpaceDrive = new LocalDiskDrive("Fake drive", 1000000000000L, 1000000000000L, 100);
        drives.add(fakeLowSpaceDrive);
        ArrayList<LocalDiskDrive> lowSpaceDrives = localDiskEnumerator.getLowSpaceDrives(drives);
        assertFalse(lowSpaceDrives.isEmpty());
        assertTrue(lowSpaceDrives.contains(fakeLowSpaceDrive));
    }
}
