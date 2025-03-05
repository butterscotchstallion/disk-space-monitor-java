package com.dsm.diskspacemonitor;

import lombok.Builder;

@Builder
public class LocalDiskDrive {
    public String name;
    public long freeSpace;
    public long totalSpace;
    public int percentDiskSpaceUsed;
}
