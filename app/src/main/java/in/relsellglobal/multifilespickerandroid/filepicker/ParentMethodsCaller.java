/*
 * Copyright (c) 2017. Relsell Global
 */

/*
 * Copyright (c) 2017. Relsell Global
 */

package in.relsellglobal.multifilespickerandroid.filepicker;

import android.os.Bundle;

import java.util.List;

/**
 * Created by anilkukreti on 06/12/17.
 */

public interface ParentMethodsCaller {
    void invokeSelectedFolderFragment(Bundle bundle, int containerId, ParentMethodsCaller parentMethodsCaller);
    void invokePicCollageImageShow(List<String> uris);
}
