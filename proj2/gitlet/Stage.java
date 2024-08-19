package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.Serializable;
import static gitlet.Helper.*;

/**
 * the staging area implementation
 * the staging area contains a map storing the filename and the shaID of the files that are added
 * and another list containing the removed files.
 * @author Stellarlane
 */

public class Stage implements Serializable{

    /** file name as key, shaID as the value */
    private HashMap<String, String> added;
    private List<String> removed;

    public Stage() {
        added = new HashMap<>();
        removed = new ArrayList<>();
    }

    public void addFile(String file, String id) {
        added.put(file, id);
    }

    public void removeFile(String file) {
        removed.add(file);
    }

    public void clearStage() {
        added = new HashMap<>();
        removed = new ArrayList<>();
    }

    public HashMap<String, String> getAdded() {
        return added;
    }

    public List<String> getRemoved() {
        return removed;
    }

}
