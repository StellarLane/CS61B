package gitlet;

// TODO: any imports you need here
import gitlet.Utils.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date; // TODO: You'll likely use this in this class

import static gitlet.Utils.join;

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

    /** The message of this Commit. */
    private final String message;
    private final Date date;
    private final ArrayList<String> parents;
    private final String shaID;
//    the file stored in the objects folder of the instance.
    private final File file;

    /* TODO: fill in the rest of this class. */
    public Commit(String commitMessage, ArrayList<String> commitParents) {
        message = commitMessage;
        date = new Date();
        parents = commitParents;
        shaID = Utils.sha1(date.toString(), message, parents);
        file = join(Repository.OBJECTS_DIR, shaID.substring(0,2), shaID.substring(2));
    }

//    initial commit
    public Commit() {
        message = "Initial commit";
        date = new Date(0);
        parents = null;
        shaID = Utils.sha1(date.toString(), message, parents);
        file = join(Repository.OBJECTS_DIR, shaID.substring(0,2), shaID.substring(2));
    }
}
