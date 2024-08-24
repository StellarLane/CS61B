package gitlet;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

import java.io.File;
import java.util.*;

public class Helper {
    /**
     * Read the index file
     * @return the current staging area
     */
    public static Stage readIndex() {
        return readObject(Repository.INDEX, Stage.class);
    }

    /**
     * load the blob with the shaID
     * @param shaID
     * @return the correspondent blob
     */
    public static Blob loadBlob(String shaID) {
        File tmp = join(Repository.BLOBS_DIR, shaID.substring(0, 2), shaID.substring(2));
        return readObject(tmp, Blob.class);
    }

    /**
     * load the commit with the shaID
     * @param shaID
     * @return the correspondent commit
     */
    public static Commit loadCommit(String shaID) {
        File tmp = join(Repository.COMMITS_DIR, shaID.substring(0, 2), shaID.substring(2));
        return readObject(tmp, Commit.class);
    }

    /**
     * save all the files stored in the hashmap into blobs
     * @param commitBlobs
     */
    public static void saveBlob(HashMap<String, String> commitBlobs) {
        Set<String> commitBlobsKeySet = commitBlobs.keySet();
        for (String singleBlob : commitBlobsKeySet) {
            new Blob(singleBlob);
        }
    }

    /**
     * check if a branch called branchname exists
     * @param branchName
     * @return t/f
     */
    protected static boolean checkBranchExists(String branchName) {
        String[] branchList = HEADS_DIR.list();
        assert branchList != null;
        for (String branch : branchList) {
            if (branch.equals(branchName)) {
                return true;
            }
        }
        return false;
    }

    protected static void statusFormat(String content) {
        System.out.println("=== " + content + " ===");
    }

    /**
     * A class of helper functions for the status commend.
     */
    public static class StatusFuncs {
        protected static void statusBranch() {
            String[] allBranches = HEADS_DIR.list();
            assert allBranches != null;
            for (String branch : allBranches) {
                if (branch.equals(join(GITLET_DIR, readContentsAsString(HEAD)).getName())) {
                    System.out.print("*");
                }
                System.out.println(branch);
            }
            System.out.println();
        }

        protected static void statusStaged() {
            printFileName(readIndex().getAdded().keySet());
        }

        protected static void statusRemoved() {
            printFileName(readIndex().getRemoved());
        }

//        protected static void statusModified() {
//            Set<String> modified = new HashSet<>();
//            Set<String> deleted = new HashSet<>();
//            List<String> fileSet = plainFilenamesIn(CWD);
//            HashMap<String, String> commited = readIndex().getCurCommit();
//            HashMap<String, String> staged = readIndex().getAdded();
//            HashSet<String> removed = readIndex().getRemoved();
//            for (String everyFile : fileSet) {
//                File curFile = join(CWD, everyFile);
//                if ()
//            }
//        }

        protected static void printFileName(Set<String> fileset) {
            List<String> fileList = new ArrayList<>(fileset);
            Collections.sort(fileList);
            for (String file : fileList) {
                System.out.println(file);
            }
            System.out.println();
        }
    }

    /**
     * check if there's unstaged changes that may abort the branch command.
     * @return true if abortion is needed.
     */
    protected static boolean checkBranchAvailable() {
        List<String> filesCWD = plainFilenamesIn(CWD);
        HashMap<String, String> curCommitBlobs = loadCommit(getPointer()).getTrackedBlobs();
        HashMap<String, String> filesMap = new HashMap<>();
        for (String file : filesCWD) {
            File tmp = join(CWD, file);
            filesMap.put(file, sha1(file, readContentsAsString(tmp)));
        }
        return !filesMap.equals(curCommitBlobs);
    }

    protected static String logFormat(Commit commit) {
        return "===\n"
                + "commit " + commit.getShaID() + "\n"
                + "Date: " + commit.getTime() + "\n"
                + commit.getMessage() + "\n";
    }

    protected static boolean checkCommitBlob(String fileName) {
        return loadCommit(getPointer()).getTrackedBlobs().containsKey(fileName);
    }

    protected static void setPointer(Commit curCommit) {
        String pointerHEAD = readContentsAsString(HEAD);
        File pointer = join(GITLET_DIR, pointerHEAD);
        writeContents(pointer, curCommit.getShaID());
    }

    /**
     * get the shaID of the commit that the HEAD that is pointing at
     * @return the ShaID of the pointed commit.
     */
    protected static String getPointer() {
        return readContentsAsString(join(GITLET_DIR, readContentsAsString(HEAD)));
    }

    protected static String getCurrentBranch() {
        return join(GITLET_DIR, readContentsAsString(HEAD)).getName();
    }
}
