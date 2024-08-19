package gitlet;

import java.io.File;
import java.util.*;
import java.io.Serializable;

import static gitlet.Utils.*;

/** An interface for the instances in the object directory, implemented by blob and commit
 * @author StellarLane
 * */

public interface Objects extends Serializable{

    File file = null;

    default void save() {
        System.out.println(file.toString());
        file.getParentFile().mkdir();
        writeObject(file, this);
    }

    default File readFromFile(String id) {
        return join(Repository.OBJECTS_DIR, id.substring(0,2), id.substring(2));
    }
}
