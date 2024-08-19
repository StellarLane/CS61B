package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class Helper {
    public static File readFromFile(String id) {
        return join(Repository.OBJECTS_DIR, id.substring(0,2), id.substring(2));
    }

    public static <serializeType> void save(File filePath, serializeType content) {

    }

}
