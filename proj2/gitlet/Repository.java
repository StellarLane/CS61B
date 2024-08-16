package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author StellarLane
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    static final File CWD = new File(System.getProperty("user.dir"));
    static final File GITLET_DIR = join(CWD, ".gitlet");
    static final File REF_DIR = join(GITLET_DIR, "refs");
    static final File HEADS_DIR = join(REF_DIR, "heads");
    static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    /* TODO: fill in the rest of this class. */
    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A gitlet system already exits in the directory.");
            return;
        }
        GITLET_DIR.mkdir();
        REF_DIR.mkdir();
        HEADS_DIR.mkdir();
        OBJECTS_DIR.mkdir();
    }
}
