package gitlet;

import java.io.File;
import java.util.*;
import java.util.Objects;

import static gitlet.Helper.StatusFuncs.*;
import static gitlet.Utils.*;
import static gitlet.Helper.*;

/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author StellarLane
 */
public class Repository {
    /**
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
        HashMap<String, String> commitBlobs = parentCommit.getTrackedBlobs();
        if (commitBlobs.equals(stagingAreaAdded)) {
            System.out.println("No changes added to the commit.");
            return;
        }
        commitBlobs.putAll(stagingAreaAdded);
        for (String fileRemoved : readIndex().getRemoved()) {
            commitBlobs.remove(fileRemoved);
        }
        Commit newCommit = new Commit(commitMessage, parentCommitString, commitBlobs);
        newCommit.save();
        saveBlob(stagingAreaAdded);
        setPointer(newCommit);
        new Stage(commitBlobs, new HashMap<>(), new HashSet<>());
    }

    public static void removeFile(String rmFileName) {
        HashMap<String, String> stagingAreaSaved = readIndex().getCurCommit();
        HashMap<String, String> stagingAreaAdded = readIndex().getAdded();
        HashSet<String> stagingAreaRemoved = readIndex().getRemoved();
        if (checkCommitBlob(rmFileName)) {
            stagingAreaRemoved.add(rmFileName);
            stagingAreaSaved.remove(rmFileName);
            if (!join(Repository.CWD, rmFileName).exists()) {
                new Stage(stagingAreaSaved, stagingAreaAdded, stagingAreaRemoved);
                return;
            }
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
        while (!currentCommit.getParent().isEmpty()) {
            System.out.println(logFormat(currentCommit));
            currentCommit = loadCommit(currentCommit.getParent());
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
        if (checkUnstaged()) {
            System.out.println(
                    "There is an untracked file in the way; delete it, or add and commit it first."
            );
            return;
        }
        if (!checkBranchExists(branchName)) {
            System.out.println("No such branch exists.");
        } else if (join(GITLET_DIR, readContentsAsString(HEAD)).getName().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
        } else {
            Set<String> prevCommitFiles = loadCommit(getPointer()).getTrackedBlobs().keySet();
            for (String file : prevCommitFiles) {
                restrictedDelete(join(CWD, file));
            }
            setHEAD(branchName);
            new Stage(loadCommit(getPointer()).getTrackedBlobs(), new HashMap<>(), new HashSet<>());
            Set<String> curCommitFiles = loadCommit(getPointer()).getTrackedBlobs().keySet();
            for (String file : curCommitFiles) {
                checkoutFile(file);
            }
        }
    }

    public static void createBranch(String branchName) {
        if (checkBranchExists(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        File branchPointer = join(HEADS_DIR, branchName);
        String curCommitID = getPointer();
        writeContents(branchPointer, curCommitID);
    }

    public static void removeBranch(String branchName) {
        if (!checkBranchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (getCurrentBranch().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        join(HEADS_DIR, branchName).delete();
    }

    public static void resetCommit(String shaID) {
        if (checkUnstaged()) {
            System.out.println(
                    "There is an untracked file in the way; delete it, or add and commit it first."
            );
            return;
        }
        if (!checkCommitExists(shaID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        for (String everyFile : plainFilenamesIn(CWD)) {
            restrictedDelete(join(CWD, everyFile));
        }
        Set<String> trackedFileNames = loadCommit(shaID).getTrackedBlobs().keySet();
        for (String everyFile : trackedFileNames) {
            checkoutFileCommit(shaID, everyFile);
        }
        new Stage(loadCommit(shaID).getTrackedBlobs(), new HashMap<>(), new HashSet<>());
        setPointer(loadCommit(shaID));
    }

    public static void mergeBranch(String mergeBranch) {
        boolean conflict = false;
        Commit curHeadCommit = loadCommit(getPointer());
        Commit mergeBranchCommit = loadCommit(readContentsAsString(join(HEADS_DIR, mergeBranch)));
        Commit splitPoint = loadCommit(findSplitPoint(curHeadCommit, mergeBranchCommit));
        if (splitPoint.getShaID().equals(mergeBranchCommit.getShaID())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        } else if (splitPoint.getShaID().equals(curHeadCommit.getShaID())) {
            System.out.println("Current branch fast-forwarded.");
            checkoutBranch(mergeBranch);
            return;
        }
        HashMap<String, String> splitPointFiles = splitPoint.getTrackedBlobs();
        HashMap<String, String> mergeBranchFiles = mergeBranchCommit.getTrackedBlobs();
        HashMap<String, String> curHeadFiles = curHeadCommit.getTrackedBlobs();
        HashMap<String, String> mergeResult = new HashMap<>();
        HashMap<String, String> mergeResultConflict = new HashMap<>();
        Set<String> totalFiles =  new HashSet<>(curHeadFiles.keySet());
        totalFiles.addAll(mergeBranchFiles.keySet());
        String tmp = "";
        for (String file : Objects.requireNonNull(CWD.list())) {
            restrictedDelete(join(CWD, file));
        }
        for (String file : totalFiles) {
            switch (checkMatch(file, mergeBranchFiles, curHeadFiles, splitPointFiles)) {
                case 0:
                    mergeResult.put(file, mergeBranchFiles.get(file));
                    break;
                case 10:
                    tmp = conflictHandler(file, curHeadFiles.get(file), mergeBranchFiles.get(file));
                    writeContents(join(CWD, file), tmp);
                    mergeResultConflict.put(file, sha1(file, tmp));
                    conflict = true;
                    break;
                case 11:
                    tmp = conflictHandler(file, curHeadFiles.get(file), 1);
                    writeContents(join(CWD, file), tmp);
                    mergeResultConflict.put(file, sha1(file, tmp));
                    conflict = true;
                    break;
                case 12:
                    tmp = conflictHandler(file, mergeBranchFiles.get(file), 2);
                    writeContents(join(CWD, file), tmp);
                    mergeResultConflict.put(file, sha1(file, tmp));
                    conflict = true;
                    break;
                case 21:
                    mergeResult.put(file, mergeBranchFiles.get(file));
                    break;
                case 22:
                    mergeResult.put(file, curHeadFiles.get(file));
                    break;
                case 3:
                    break;
                default:
            }
        }
        Commit mergeCommit = new Commit(
                "Merged " + mergeBranch + " into " + getCurrentBranch() + ".",
                curHeadCommit.getShaID(),
                mergeBranchCommit.getShaID(),
                mergeResult
        );
        mergeCommit.save();
        for (String file : mergeResult.keySet()) {
            writeContents(join(CWD, file), loadBlob(mergeResult.get(file)).getSourceFileString());
        }
        mergeResult.putAll(mergeResultConflict);
        saveBlob(mergeResult);
        setPointer(mergeCommit);
        new Stage(mergeResult, new HashMap<>(), new HashSet<>());
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }
}
