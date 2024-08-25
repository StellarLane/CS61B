package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.*;
import static gitlet.Helper.*;

public class Stage implements Serializable {
    /**
     * An implementation for the gitlet staging area.
     * @author StellarLane
     */

    /** A copy of the blobs tracked in the current commit. */
    private HashMap<String, String> curCommit;
    /** The added files, with the file name as key and shaID as value */
    private HashMap<String, String> added;
    /** The removed files */
    private HashSet<String> removed;

    public Stage() {
        curCommit = new HashMap<>();
        added = new HashMap<>();
        removed = new HashSet<>();
        writeObject(Repository.INDEX, this);
    }

    public Stage(HashMap savedFiles, HashMap addedFiles, HashSet removedFiles) {
        curCommit = savedFiles;
        added = addedFiles;
        removed = removedFiles;
        writeObject(Repository.INDEX, this);
    }

    public void add(String fileName, String shaID) {
        if (checkContains(curCommit, fileName, shaID)) {
            return;
        }
        if (removed.contains(fileName)) {
            removed.remove(fileName);
            writeObject(Repository.INDEX, this);
            return;
        }
        added.put(fileName, shaID);
        writeObject(Repository.INDEX, this);
    }

    public HashMap<String, String> getAdded() {
        return added;
    }

    public HashSet<String> getRemoved() {
        return removed;
    }

    public HashMap<String, String> getCurCommit() {
        return curCommit;
    }

}
