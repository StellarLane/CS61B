package gitlet;

// TODO: any imports you need here

//import afu.org.checkerframework.checker.oigj.qual.O;

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

import static gitlet.Utils.*;
import static gitlet.Helper.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author StellarLane
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    private final String message;
    private final Date date;
    /** The parent commits. */
    private final String parent;
    private final String shaID;
    /** the file stored in the objects folder containing the instance. */
    private final File file;
    /** the tracked blobs in the current commit,
     * stored in a map with the file path as the key and the shaID as the value.*/
    private final HashMap<String, String> trackedBlobs;


    /* TODO: fill in the rest of this class. */

    /**
     * This is a general constructor for commit, it will create a commit instance
     * @param commitMessage
     * @param commitParent will be null if is the initial commit.
     * @param blobs containing the blobs tracked in the commit.
     */
    public Commit(String commitMessage, String commitParent, HashMap<String, String> blobs) {
        message = commitMessage;
        if (message.equals("Initial commit")) {
            date = new Date(0);
        } else {
            date = new Date();
        }
        parent = commitParent;
        shaID = Utils.sha1(date.toString(), message);
        file = join(Repository.OBJECTS_DIR, shaID.substring(0,2), shaID.substring(2));
        trackedBlobs = blobs;
    }

    /**
     * get the commit from serialized contents.
     * @param id
     * @return Commit
     */
    public Commit readCommitFromFile(String id) {
        return readObject(Helper.readFromFile(id), Commit.class);
    }
}
