package com.dsm.diskspacemonitor;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LocalDiskDrive {
    public String name;
    public long freeSpace;
    public long totalSpace;
    public int percentDiskSpaceUsed;
}
