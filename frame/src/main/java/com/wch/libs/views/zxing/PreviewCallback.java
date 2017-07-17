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

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @Title:浏览图片
 * @Description:
 * @Author:13050527
 * @Since:2013-9-24
 * @Version:
 */
final class PreviewCallback implements Camera.PreviewCallback
{

    private static final String TAG = PreviewCallback.class.getSimpleName();// log的名字
    private final CameraConfigurationManager configManager;// CameraConfigurationManager对象
    private final boolean useOneShotPreviewCallback;
    private Handler previewHandler;// 预览的handler
    private int previewMessage;// what（handler发送的消息）

    /**
     * @param configManager
     *            CameraConfigurationManager对象
     * @param useOneShotPreviewCallback
     */
    PreviewCallback(CameraConfigurationManager configManager,
                    boolean useOneShotPreviewCallback)
    {
        this.configManager = configManager;
        this.useOneShotPreviewCallback = useOneShotPreviewCallback;
    }

    /**
     * @Description: 设置一些参数
     * @Author 13050527
     * @Date 2013-9-24
     */
    void setHandler(Handler previewHandler, int previewMessage)
    {
        this.previewHandler = previewHandler;
        this.previewMessage = previewMessage;
    }

    /**
     * @Description: data为预览的内容
     * @Author 13050527
     * @Date 2013-9-24
     */
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        Point cameraResolution = configManager.getCameraResolution();
        if (!useOneShotPreviewCallback)
        {
            camera.setPreviewCallback(null);// 清空对象
        }
        if (previewHandler != null)
        {
            Message message = previewHandler.obtainMessage(previewMessage,
                    cameraResolution.x, cameraResolution.y, data);
            message.sendToTarget();// 发送handler
            previewHandler = null;
        }
        else
        {
            Log.d(TAG, "Got preview callback, but no handler for it");
        }
    }

}
