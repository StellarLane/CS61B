package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;
import static gitlet.Helper.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author StellarLane
 *
 */
public class Repository {
    /** The current working directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Where the .gitlet locates. */
    static final File GITLET_DIR = join(CWD, ".gitlet");

    /** Where the refs locates. */
    static final File REF_DIR = join(GITLET_DIR, "refs");

    /** The directory storing object references such as commit. */
    static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    /**  Where the branch locates. */
    static final File BRANCHES_DIR = join(GITLET_DIR, "branches");

    /** Where the head pointer locates. */
    static final File HEAD = join(GITLET_DIR, "HEAD.txt");

    /** the staging area */
    static Stage stagingArea;

    /* TODO: fill in the rest of this class. */
    public static void init() {
        GITLET_DIR.mkdir();
        REF_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        Commit initalCommit = new Commit("Initial commit", null, null);
        initalCommit.save();
        writeObject(HEAD, "ref: refs/heads/main");
        stagingArea = new Stage();
    }

    public static void add(File file) {
        System.out.println(1);
    }
}
