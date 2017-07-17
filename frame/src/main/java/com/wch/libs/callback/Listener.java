package com.wch.libs.callback;


import android.view.View;

import java.util.Objects;

public class Listener {
    public interface Click {
        void select(int position);
    }

    public interface Select {
        void select(Object tag, int position, String str, boolean bool);
    }

    public interface ClickDialog {
        void choose(int tag, int position, View view);
    }

    public interface onScrollChanged {
        void ScrollChanged(int x, int y, int oldx, int oldy);
    }
}
