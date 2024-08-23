package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Helper.*;

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
            System.out.println("At least one argument must be given");
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
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    return;
                }
                Repository.makeCommit(args[1]);
                break;
            case "rm":
                if (validateNumArgs("rm", args, 2)) {
                    break;
                }
                if (join(Repository.CWD, args[1]).exists()) {
                    Repository.removeFile(args[1]);
                } else {
                    System.out.println("File does not exist.");
                    return;
                }
                break;
            case "log":
                if (validateNumArgs("log", args, 1)) {
                    break;
                }
                Repository.getLog();
                break;
            case "global-log":
                if (validateNumArgs("global-log", args, 1)) {
                    break;
                }
                Repository.getGlobalLog();
                break;
            case "find":
                if (validateNumArgs("find", args, 2)) {
                    break;
                }
                Repository.findCommit(args[1]);
                break;
            case "status":
                if (validateNumArgs("status", args, 1)) {
                    break;
                }
                Repository.checkStatus();
                break;
            default:
                System.out.println("No command called " + args[0] + " found");
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
            System.out.println("invalid argument for " + cmd);
            return true;
        } else {
            return false;
        }
    }
}
