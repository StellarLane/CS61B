package gitlet;

import static gitlet.Utils.*;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Helper {
    public static Stage readIndex() {
        return readObject(Repository.INDEX, Stage.class);
    }

    public static Blob loadBlob(String shaID) {
        return readObject(join(Repository.BLOBS_DIR, shaID.substring(0,2), shaID.substring(2)), Blob.class);
    }

    public static Commit loadCommit(String shaID) {
        return readObject(join(Repository.COMMITS_DIR, shaID.substring(0,2), shaID.substring(2)), Commit.class);
    }

    public static void saveBlob(HashMap<String, String> commitBlobs) {
        Set<String> commitBlobsKeySet = commitBlobs.keySet();
        for (String singleBlob : commitBlobsKeySet) {
            new Blob(singleBlob);
        }
    }
}
