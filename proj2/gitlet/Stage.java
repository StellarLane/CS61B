package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.*;

public class Stage implements Serializable {
    /**
     * An implementation for the gitlet staging area.
     * @author StellarLane
     */

    /** The added files, with the file name as key and shaID as value */
    private HashMap<String, String> added;
    /** The removed files */
    private HashSet<String> removed;

    public Stage() {
        added = new HashMap<>();
        removed = new HashSet<>();
        writeObject(Repository.INDEX, this);
    }

    public Stage(HashMap<String, String> trackedBlobs, HashSet<String> removedBlobs) {
        added = trackedBlobs;
        removed = removedBlobs;
        writeObject(Repository.INDEX, this);
    }

    public void add(String fileName, String shaID) {
        added.put(fileName, shaID);
        writeObject(Repository.INDEX, this);
    }

    public HashMap<String, String> getAdded() {
        return added;
    }

    public HashSet<String> getRemoved() {
        return removed;
    }

}
