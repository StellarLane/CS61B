package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.join;
import static gitlet.Utils.writeObject;

public interface Objects extends Serializable {
    String getShaID();
    void save();
}
