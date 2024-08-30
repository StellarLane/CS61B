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
            new Blob(singleBlob, commitBlobs.get(singleBlob));
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
            ArrayList<String> branches = new ArrayList<>(Arrays.asList(HEADS_DIR.list()));
            for (String branch : branches) {
                if (branch.equals(join(GITLET_DIR, readContentsAsString(HEAD)).getName())) {
                    System.out.print("*");
                    System.out.println(branch);
                    branches.remove(branch);
                    break;
                }
            }
            for (String branch : branches) {
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
    protected static boolean checkUnstaged() {
        List<String> filesCWD = plainFilenamesIn(CWD);
        HashMap<String, String> curStaged = readIndex().getAdded();
        curStaged.putAll(readIndex().getCurCommit());
        HashMap<String, String> filesMap = new HashMap<>();
        for (String file : filesCWD) {
            File tmp = join(CWD, file);
            filesMap.put(file, sha1(file, readContentsAsString(tmp)));
        }
        return !filesMap.equals(curStaged);
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

    /**
     * get the current name of the branch.
     * @return the current branch name, in string.
     */
    protected static String getCurrentBranch() {
        return join(GITLET_DIR, readContentsAsString(HEAD)).getName();
    }

    /**
     * check if the map contains the key and the value as a pair.
     * @param map
     * @param key
     * @param value
     * @return true if contained.
     */
    protected static boolean checkContains(HashMap<String, String> map, String key, String value) {
        return map.containsKey(key) && map.get(key).equals(value);
    }

    /**
     * check if there exists a commit with the shaID
     * @param shaID of the commit
     * @return t/f
     */
    protected static boolean checkCommitExists(String shaID) {
        return readObject(ALL_COMMITS, ArrayList.class).contains(shaID);
    }

    /**
     * changed the branch specified in the HEAD file to the given branch name
     * @param branchName
     */
    protected static void setHEAD(String branchName) {
        writeContents(HEAD, ("refs/heads/" + branchName));
    }

    /**
     * Find the splitting point of the two commits
     * @param firstCommit
     * @param secondCommit
     * @return the shaID of the splitting point.
     */
    protected static String findSplitPoint(Commit firstCommit, Commit secondCommit) {
        Commit tmp = firstCommit;
        HashSet<String> parentCommitList = new HashSet<>();
        while (!tmp.getParent().isEmpty()) {
            parentCommitList.add(tmp.getShaID());
            tmp = loadCommit(tmp.getParent());
        }
        parentCommitList.add(tmp.getShaID());
        tmp = secondCommit;
        while (!tmp.getParent().isEmpty()) {
            if (parentCommitList.contains(tmp.getShaID())) {
                return tmp.getShaID();
            }
            tmp = loadCommit(tmp.getParent());
        }
        return tmp.getShaID();
    }

    protected static String getSplitPoint(Commit firstCommit, Commit secondCommit) {
        Comparator<Commit> commitComparator = Comparator.comparing(Commit::getDate).reversed();
        Queue<Commit> commitQueue = new PriorityQueue<>(commitComparator);
        Set<String> checkedCommits = new HashSet<>();
        commitQueue.add(firstCommit);
        commitQueue.add(secondCommit);
        checkedCommits.add(firstCommit.getShaID());
        checkedCommits.add(secondCommit.getShaID());
        while (true) {
            Commit latest = commitQueue.poll();
            Commit parentCommit = loadCommit(latest.getParent());
            if (!latest.getMergeParent().isEmpty()) {
                Commit mergeCommit = loadCommit(latest.getMergeParent());
                if (checkedCommits.contains(mergeCommit.getShaID())) {
                    return parentCommit.getShaID();
                }
                checkedCommits.add(mergeCommit.getShaID());
            }
            if (checkedCommits.contains(parentCommit.getShaID())) {
//                System.out.println(parentCommit.getMessage());
                return parentCommit.getShaID();
            }
            commitQueue.add(parentCommit);
            checkedCommits.add(parentCommit.getShaID());
        }
    }

    /**
     * A helper function to test if a merging action is available
     * @param branchName
     * @return t/f
     */
    protected static boolean checkMergeAvailable(String branchName) {
        if (!readIndex().getAdded().isEmpty() && readIndex().getRemoved().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return false;
        }
        if (checkUnstaged()) {
            System.out.println(
                    "There is an untracked file in the way; delete it, or add and commit it first."
            );
            return false;
        }
        if (!checkBranchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return false;
        }
        if (branchName.equals(getCurrentBranch())) {
            System.out.println("Cannot merge a branch with itself.");
            return false;
        }
        return true;
    }

    /**
     * A helper function to define what to do with the file
     * @param fileName name of the processed file
     * @param map1
     * @param map2
     * @param mapO the blobs of the splitting point.
     * @return 0 if the file is ok and can be added to the final commit,
     * 10 if a conflict happens but file exists in both branches,
     * 11 if a conflict happens and file is deleted in map1 branch,
     * 12 if a conflict happens and file is deleted in map2 branch,
     * 21 if it's a completely new file in map1 branch,
     * 22 if it's a completely new file in map2 branch,
     * 3 if it's unmodified in one branch and removed in the other,
     * will not present in the final commit.
     */
    protected static int checkMatch(String fileName,
                                    HashMap<String, String> map1,
                                    HashMap<String, String> map2,
                                    HashMap<String, String> mapO) {
        if (map1.containsKey(fileName) && map2.containsKey(fileName)) {
            if (map1.get(fileName).equals(map2.get(fileName))) {
                return 0;
            } else {
                return 10;
            }
        } else {
            if (map2.containsKey(fileName)) {
                if (mapO.containsKey(fileName)) {
                    if (map2.get(fileName).equals(mapO.get(fileName))) {
                        return 3;
                    } else {
                        return 11;
                    }
                } else {
                    return 22;
                }
            } else {
                if (mapO.containsKey(fileName)) {
                    if (map1.get(fileName).equals(mapO.get(fileName))) {
                        return 3;
                    } else {
                        return 12;
                    }
                } else {
                    return 21;
                }
            }
        }
    }

    /**
     * Helper method for handling file with conflicts
     * @param fileName
     * @param firstShaID
     * @param secondShaID
     * @return a string showing the contents of the conflict.
     */
    protected static String conflictHandler(
            String fileName,
            String firstShaID,
            String secondShaID) {
        return "<<<<<<< HEAD\n"
                + loadBlob(firstShaID).getSourceFileString()
                + "=======\n"
                + loadBlob(secondShaID).getSourceFileString()
                + ">>>>>>>\n";
    }

    /**
     * Helper method for handling file that is modified in one branch and is deleted in the other.
     * @param fileName
     * @param shaID
     * @param location 1 or 2
     * @return a string showing the contents of the conflict.
     */
    protected static String conflictHandler(String fileName, String shaID, int location) {
        if (location == 1) {
            return "<<<<<<< HEAD\n"
                    + loadBlob(shaID).getSourceFileString()
                    + "=======\n>>>>>>>\n";
        } else {
            return "<<<<<<< HEAD\n=======\n"
                    + loadBlob(shaID).getSourceFileString()
                    + ">>>>>>>\n";
        }
    }

    /**
     * A helper function to handle abbreviation situations.
     * @param shaID
     * @return the full shaID, or the original shaID if no commit matches.
     */
    protected static String abbrHandler(String shaID) {
        if (shaID.length() == 40) {
            return shaID;
        }
        int len = shaID.length();
        HashMap<String, String> commitAbbrMap = new HashMap<>();
        ArrayList<String> allCommitsID = readObject(ALL_COMMITS, ArrayList.class);
        for (String commitID : allCommitsID) {
            commitAbbrMap.put(commitID.substring(0, len), commitID);
        }
        return commitAbbrMap.getOrDefault(shaID, shaID);
    }
}
