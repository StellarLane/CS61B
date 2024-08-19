package gitlet;

//import net.sf.saxon.trans.SymbolicName;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static gitlet.Utils.*;
import static gitlet.Helper.*;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Stellarlane
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("no argument were given");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs("init", args, 1);
                init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs("add", args, 2);
                add(args[1]);
                break;
            // TODO: FILL THE REST IN
        }
    }


    public static void init() {
        if (Repository.GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        Repository.init();
    }

    public static void add(String file) {
        File fileAdded;
        if (Paths.get(file).isAbsolute()) {
            fileAdded = new File(file);
        } else {
            fileAdded = join(Repository.CWD, file);
        }
        if (fileAdded.exists()) {
            Repository.add(fileAdded);
        } else {
            System.out.println("File does not exist.");
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match. (copied this from lab6)
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
