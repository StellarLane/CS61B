package gitlet;

import java.io.File;

import static gitlet.Helper.abbrHandler;
import static gitlet.Helper.checkMergeAvailable;
import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author StellarLane
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if (validateNumArgs("init", args, 1)) {
                    break;
                }
                if (Repository.GITLET_DIR.exists()) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    return;
                } else {
                    Repository.init();
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                if (validateNumArgs("add", args, 2 )) {
                    break;
                }
                File addFile = join(Repository.CWD, args[1]);
                if (addFile.exists()) {
                    Repository.add(addFile);
                } else {
                    System.out.println("File does not exist.");
                    return;
                }
                break;
            case "commit":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (args.length == 1 || args[1].isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    return;
                }
                Repository.makeCommit(args[1]);
                break;
            case "rm":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("rm", args, 2)) {
                    break;
                }
                Repository.removeFile(args[1]);
                break;
            case "log":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("log", args, 1)) {
                    break;
                }
                Repository.getLog();
                break;
            case "global-log":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("global-log", args, 1)) {
                    break;
                }
                Repository.getGlobalLog();
                break;
            case "find":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("find", args, 2)) {
                    break;
                }
                Repository.findCommit(args[1]);
                break;
            case "status":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("status", args, 1)) {
                    break;
                }
                Repository.checkStatus();
                break;
            case "checkout":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (args[1].equals("--")) {
                    if (validateNumArgs("checkout", args, 3)) {
                        break;
                    }
                    Repository.checkoutFile(args[2]);
                    break;
                } else if (args.length == 4 && args[2].equals("--")) {
                    if (validateNumArgs("checkout", args, 4)) {
                        break;
                    }
                    Repository.checkoutFileCommit(abbrHandler(args[1]), args[3]);
                    break;
                } else {
                    if (validateNumArgs("checkout", args, 2)) {
                        break;
                    }
                    Repository.checkoutBranch(args[1]);
                    break;
                }
            case "branch":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("branch", args, 2)) {
                    break;
                }
                Repository.createBranch(args[1]);
                break;
            case "rm-branch":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("rm-branch", args, 2)) {
                    break;
                }
                Repository.removeBranch(args[1]);
                break;
            case "reset":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("reset", args, 2)) {
                    break;
                }
                Repository.resetCommit(abbrHandler(args[1]));
                break;
            case "merge":
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not an initialized Gitlet directory.");
                    return;
                }
                if (validateNumArgs("merge", args, 2)) {
                    break;
                }
                if (!checkMergeAvailable(args[1])) {
                    break;
                }
                Repository.mergeBranch(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
            // TODO: FILL THE REST IN
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.(copied this from lab6)
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    private static boolean validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands");
            return true;
        } else {
            return false;
        }
    }
}
