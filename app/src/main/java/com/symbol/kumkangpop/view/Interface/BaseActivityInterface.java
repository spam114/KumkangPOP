package com.symbol.kumkangpop.view.Interface;

import android.os.Handler;

public interface BaseActivityInterface {
    void progressON();

    void progressON(String message);

    void progressON(String message, Handler handler);

    void progressOFF2();
}
