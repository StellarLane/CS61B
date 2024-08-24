package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Helper.statusFuncs.*;
import static gitlet.Utils.*;
import static gitlet.Helper.*;

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

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The objects directory. */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    /** The commits directory */
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");
    /** The blobs directory */
    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    /** The refs directory. */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The heads directory */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    /** The index file (serialized staging area) */
    public static final File INDEX = join(GITLET_DIR, "index");
    /**  The HEAD file */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /** A file containing all the commitIDs */
    public static final File ALL_COMMITS = join(GITLET_DIR, "all_commits");

    /* TODO: fill in the rest of this class. */

    /**
     * created the directories needed,
     * create the initial commit,
     * let the HEAD pointer points at the main pointer,
     * and let the main pointer points at the initial commit.
     */
    public static void init() {
        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        REFS_DIR.mkdir();
        HEADS_DIR.mkdir();
        writeContents(HEAD, "refs/heads/master");
        Commit initialCommit = new Commit();
        initialCommit.save();
        setPointer(initialCommit);
        new Stage();
    }

    public static void add(File addFile) {
        Stage stagingArea = readIndex();
        String fileName = addFile.getName();
        String shaID = sha1(fileName, readContentsAsString(addFile));
        stagingArea.add(fileName, shaID);
    }

    public static void makeCommit(String commitMessage) {
        HashMap<String, String> stagingAreaAdded = readIndex().getCurCommit();
        stagingAreaAdded.putAll(readIndex().getAdded());
        Commit parentCommit = loadCommit(getPointer());
        String parentCommitString = parentCommit.getShaID();
        HashMap<String, String> CommitBlobs = parentCommit.getTrackedBlobs();
        if (CommitBlobs.equals(stagingAreaAdded)) {
            System.out.println("No changes added to the commit.");
            return;
        }
        CommitBlobs.putAll(stagingAreaAdded);
        Commit newCommit = new Commit(commitMessage, parentCommitString, CommitBlobs);
        newCommit.save();
        saveBlob(stagingAreaAdded);
        setPointer(newCommit);
        new Stage(CommitBlobs, new HashMap<>(), new HashSet<>());
    }

    public static void removeFile(String rmFileName) {
        HashMap<String, String> stagingAreaSaved = readIndex().getCurCommit();
        HashMap<String, String> stagingAreaAdded = readIndex().getAdded();
        HashSet<String> stagingAreaRemoved = readIndex().getRemoved();
        if (checkCommitBlob(rmFileName)) {
            stagingAreaRemoved.add(rmFileName);
            stagingAreaSaved.remove(rmFileName);
            restrictedDelete(join(Repository.CWD, rmFileName));
            new Stage(stagingAreaSaved, stagingAreaAdded, stagingAreaRemoved);
        } else if (stagingAreaAdded.containsKey(rmFileName)) {
            stagingAreaAdded.remove(rmFileName);
            new Stage(stagingAreaSaved, stagingAreaAdded, stagingAreaRemoved);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    public static void getLog() {
        Commit currentCommit = loadCommit(getPointer());
        while (!currentCommit.getParents().isEmpty()) {
            System.out.println(logFormat(currentCommit));
            currentCommit = loadCommit(currentCommit.getParents());
        }
        System.out.println(logFormat(currentCommit));
    }

    public static void getGlobalLog() {
        ArrayList<String> allCommitsID = readObject(ALL_COMMITS, ArrayList.class);
        for (String everyCommitID : allCommitsID) {
            System.out.println(logFormat(loadCommit(everyCommitID)));
        }
    }

    public static void findCommit(String commitMessage) {
        ArrayList<String> allCommitsID = readObject(ALL_COMMITS, ArrayList.class);
        Boolean tmp = false;
        for (String everyCommitID : allCommitsID) {
            Commit curCommit = loadCommit(everyCommitID);
            if (curCommit.getMessage().equals(commitMessage)) {
                System.out.println(curCommit.getShaID());
                tmp = true;
            }
        }
        if (!tmp) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void checkStatus() {
        statusFormat("Branches");
        statusBranch();
        statusFormat("Staged Files");
        statusStaged();
        statusFormat("Removed Files");
        statusRemoved();
        statusFormat("Modifications Not Staged For Commit");
        System.out.println();
        statusFormat("Untracked Files");
        System.out.println();
    }
    //TODO: modified but not staged, untracked

    public static void checkoutFile(String file) {
        File fileCheckout = join(CWD, file);
        HashMap<String, String> curCommitBlob = loadCommit(getPointer()).getTrackedBlobs();
        if (curCommitBlob.containsKey(file)) {
            writeContents(fileCheckout, loadBlob(curCommitBlob.get(file)).getSourceFileString());
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }

    public static void checkoutFileCommit(String commitID, String file) {
        if (!readObject(ALL_COMMITS, ArrayList.class).contains(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        File fileCheckout = join(CWD, file);
        HashMap<String, String> commitBlob = loadCommit(commitID).getTrackedBlobs();
        if (commitBlob.containsKey(file)) {
            writeContents(fileCheckout, loadBlob(commitBlob.get(file)).getSourceFileString());
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }

    public static void checkoutBranch(String branchName) {
        if (checkBranchAvailable()) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }
        if (!checkBranchExists(branchName)) {
            System.out.println("No such branch exists.");
            return;
        } else if (join(GITLET_DIR, readContentsAsString(HEAD)).getName().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
        }
    }

    public static void createBranch(String branchName) {
        if (checkBranchExists(branchName)) {
            System.out.println("A branch with that name already exists.");
        }
        File branchPointer = join(HEADS_DIR, branchName);
        String curCommitID = loadCommit(getPointer()).getShaID();
        writeContents(branchPointer, curCommitID);
    }
}
