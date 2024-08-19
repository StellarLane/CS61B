package gitlet;

import java.io.File;
import java.util.*;
import java.io.Serializable;

import static gitlet.Utils.*;
import static gitlet.Helper.*;

/**
 * The blob implementation.
 * A blob stands for an instance that a commit uses to track file changes.
 * @author StellarLane
 */

public class Blob implements Serializable{

    /** The file that the blob is tracking */
    File sourceFile;

    /** The hashcode to be generated */
    String shaID;

    /** The file containing the serialized blob */
    File file;

    /**
     * constructs a blob instance
     * @param source
     */
    public Blob(File source) {
        sourceFile = source;
        String sourceContents = readContentsAsString(sourceFile);
        shaID = sha1(sourceContents);
        file = join(Repository.OBJECTS_DIR, shaID.substring(0,2), shaID.substring(2));
    }


    /**
     * get the Blob from serialized contents.
     * @param id
     * @return Blob
     */
    public Blob readBlobFromFile(String id) {
        File tmp = readFromFile(id);
        return readObject(tmp, Blob.class);
    }
}
