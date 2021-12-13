package com.project.westudentmain.util;

import androidx.annotation.NonNull;

public interface CustomDataListener {

    /**
     *
     * @param data the data object
     */
    void onDataChange(@NonNull Object data);

    /**
     *
     * @param error the error reason
     */
    void onCancelled(@NonNull String error);

}
