package cn.mdruby.videocache;


import java.io.File;

import cn.mdruby.videocache.file.DiskUsage;
import cn.mdruby.videocache.file.FileNameGenerator;

/**
 * Configuration for proxy cache.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
class Config {

    public final File cacheRoot;
    public final FileNameGenerator fileNameGenerator;
    public final DiskUsage diskUsage;

    Config(File cacheRoot, FileNameGenerator fileNameGenerator, DiskUsage diskUsage) {
        this.cacheRoot = cacheRoot;
        this.fileNameGenerator = fileNameGenerator;
        this.diskUsage = diskUsage;
    }

    File generateCacheFile(String url) {
        String name = fileNameGenerator.generate(url);
        return new File(cacheRoot, name);
    }

}
