package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Locale;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author StellarLane
 */
public class Commit implements Objects {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private final String message;
    /** The time of the commit. */
    private final Date time;
    /** The parents of the commit */
    private final String parents;
    /** The blobs tracked, with the filepath as key and shaID as value */
    private final HashMap<String, String> trackedBlobs;
    /** The shaID of the commit */
    private final String shaID;

    /* TODO: fill in the rest of this class. */

    /**
     * the default commit constructor
     * @param commitMessage
     * @param commitParents
     * @param tracked
     */
    public Commit(String commitMessage, String commitParents, HashMap<String, String> tracked) {
        message = commitMessage;
        time = new Date();
        parents = commitParents;
        trackedBlobs = tracked;
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
        parents = "";
        trackedBlobs = new HashMap<>();
        shaID = createShaID();
        ArrayList<String> allCommitIDs = new ArrayList<>();
        allCommitIDs.add(shaID);
        writeObject(Repository.ALL_COMMITS, allCommitIDs);
    }

    private String createShaID() {
        return Utils.sha1(message, time.toString(), parents.toString(), trackedBlobs.toString());
    }

    public HashMap<String, String> getTrackedBlobs() {
        return trackedBlobs;
    }

    public String getParents() { return parents; }

    public String getMessage() { return message; }

    public String getTime() {
        return new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z").format(time);
    }

    @Override
    public String getShaID() {
        return shaID;
    }

    @Override
    public void save() {
        File parentDir = join(Repository.COMMITS_DIR, shaID.substring(0,2));
        parentDir.mkdir();
        File targetFile = join(parentDir, shaID.substring(2));
        writeObject(targetFile, this);
    }
}
