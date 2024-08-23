package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Helper.*;

/**
 * An implementation for blob, an Objects class that tracks file and its changes.
 */
public class Blob implements Objects{
    /** The file the blob tracks */
    private final File sourceFile;
    /** The name of the source file */
    private final String sourceName;
    /** The shaID */
    private String shaID;


    public Blob(String sourceFileName) {
        sourceFile = join(Repository.CWD, sourceFileName);
        sourceName = sourceFile.getName();
        String sourceFileString = readContentsAsString(sourceFile);
        shaID = sha1(sourceName, sourceFileString);
        this.save();
    }

    @Override
    public String getShaID() {
        return shaID;
    }

    @Override
    public void save() {
        File parentDir = join(Repository.BLOBS_DIR, shaID.substring(0,2));
        parentDir.mkdir();
        File targetFile = join(parentDir, shaID.substring(2));
        writeObject(targetFile, this);
    }
}
