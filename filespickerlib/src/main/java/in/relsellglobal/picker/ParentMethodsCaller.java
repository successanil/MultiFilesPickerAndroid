/*
 * Copyright (c) 2017. Relsell Global
 */

/*
 * Copyright (c) 2017. Relsell Global
 */

/*
 * Copyright (c) 2017. Relsell Global
 */

/*
 * Copyright (c) 2017. Relsell Global
 */

package in.relsellglobal.picker;

import android.os.Bundle;

import java.util.List;

import in.relsellglobal.picker.pojo.IBean;

/**
 * Created by anilkukreti on 06/12/17.
 */

public interface ParentMethodsCaller {
    void invokeSelectedFolderFragment(Bundle bundle, int containerId, ParentMethodsCaller parentMethodsCaller);
    void selectedFilesSendToCaller(List<IBean> fileList);
}
