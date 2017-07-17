/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wch.libs.views.zxing;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @Title:Camera自动对焦
 * @Description:
 * @Author:13050527
 * @Since:2013-9-24
 * @Version:
 */
final class AutoFocusCallback implements Camera.AutoFocusCallback
{

    private static final String TAG = AutoFocusCallback.class.getSimpleName();// log是的名字
    private static final long AUTOFOCUS_INTERVAL_MS = 1500L;// 延时1500毫秒
    private Handler autoFocusHandler;// 自动对焦后的发送消息
    private int autoFocusMessage;// 消息的数字（what）

    /**
     * @Description:设置一些参数
     * @Author 13050527
     * @Date 2013-9-24
     */
    void setHandler(Handler autoFocusHandler, int autoFocusMessage)
    {
        this.autoFocusHandler = autoFocusHandler;
        this.autoFocusMessage = autoFocusMessage;
    }

    /**
     * 对焦的监控
     */
    public void onAutoFocus(boolean success, Camera camera)
    {
        if (autoFocusHandler != null)// 判断是否要接收结束的handler
        {
            Message message = autoFocusHandler.obtainMessage(autoFocusMessage,
                    success);
            autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
            autoFocusHandler = null;// 清空handler
        }
        else
        {
            Log.d(TAG, "Got auto-focus callback, but no handler for it");
        }
    }

}
