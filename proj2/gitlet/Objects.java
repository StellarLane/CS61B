package gitlet;

import java.io.Serializable;

public interface Objects extends Serializable {
    String getShaID();
    void save();
}
