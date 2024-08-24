package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Helper.*;

/**
 * An implementation for blob, an Objects class that tracks file and its changes.
 */
public class Blob implements Objects {
    /** The content of the file */
    private final String sourceFileString;
    /** The shaID */
    private final String shaID;


    public Blob(String sourceFileName) {
        File sourceFile = join(Repository.CWD, sourceFileName);
        String sourceName = sourceFile.getName();
        sourceFileString = readContentsAsString(sourceFile);
        shaID = sha1(sourceName, sourceFileString);
        this.save();
    }

    public String getSourceFileString() {
        return sourceFileString;
    }

    @Override
    public String getShaID() {
        return shaID;
    }

    @Override
    public void save() {
        File parentDir = join(Repository.BLOBS_DIR, shaID.substring(0, 2));
        parentDir.mkdir();
        File targetFile = join(parentDir, shaID.substring(2));
        writeObject(targetFile, this);
    }
}
