package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author StellarLane
 */
public class Commit implements Objects {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private final String message;
    /** The time of the commit. */
    private final Date time;
    /** The parent of the commit */
    private final String parent;
    /** The blobs tracked, with the filepath as key and shaID as value */
    private final HashMap<String, String> trackedBlobs;
    /** The shaID of the commit */
    private final String shaID;
    /** The extra parent commit ID for merge operations, with a empty default value. */
    private String mergeParent = "";

    /**
     * the default commit constructor
     * @param commitMessage
     * @param commitParents
     * @param tracked
     */
    public Commit(String commitMessage, String commitParents, HashMap<String, String> tracked) {
        message = commitMessage;
        time = new Date();
        parent = commitParents;
        trackedBlobs = tracked;
        shaID = createShaID();
        ArrayList<String> allCommitIDs = readObject(Repository.ALL_COMMITS, ArrayList.class);
        allCommitIDs.add(shaID);
        writeObject(Repository.ALL_COMMITS, allCommitIDs);
    }

    /**
     * the constructor for commits recording a merge operation
     */
    public Commit(String commitMessage,
                  String commitParents,
                  String commitMergeParent,
                  HashMap<String, String> tracked) {
        message = commitMessage;
        time = new Date();
        parent = commitParents;
        trackedBlobs = tracked;
        mergeParent = commitMergeParent;
        shaID = createShaID();
        ArrayList<String> allCommitIDs = readObject(Repository.ALL_COMMITS, ArrayList.class);
        allCommitIDs.add(shaID);
        writeObject(Repository.ALL_COMMITS, allCommitIDs);
    }

    /**
     * The initial commit constructor.
     */
    public Commit() {
        message = "initial commit";
        time = new Date(0);
        parent = "";
        trackedBlobs = new HashMap<>();
        shaID = createShaID();
        ArrayList<String> allCommitIDs = new ArrayList<>();
        allCommitIDs.add(shaID);
        writeObject(Repository.ALL_COMMITS, allCommitIDs);
    }

    private String createShaID() {
        return Utils.sha1(message, time.toString(), parent, mergeParent, trackedBlobs.toString());
    }

    public HashMap<String, String> getTrackedBlobs() {
        return trackedBlobs;
    }

    public String getParent() {
        return parent;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z").format(time);
    }

    @Override
    public String getShaID() {
        return shaID;
    }

    @Override
    public void save() {
        File parentDir = join(Repository.COMMITS_DIR, shaID.substring(0, 2));
        parentDir.mkdir();
        File targetFile = join(parentDir, shaID.substring(2));
        writeObject(targetFile, this);
    }
}
