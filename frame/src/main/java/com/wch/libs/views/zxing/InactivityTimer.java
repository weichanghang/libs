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

import android.app.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Title:Finishes an activity after a period of inactivity.
 * @Description:
 * @Author:13050527
 * @Since:2013-9-24
 * @Version:
 */
public final class InactivityTimer
{

    private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;
    private final ScheduledExecutorService inactivityTimer = Executors
            .newSingleThreadScheduledExecutor(new DaemonThreadFactory());//按时间安排执行任务对象
    private final Activity activity; // 调用的activity对象
    private ScheduledFuture<?> inactivityFuture = null;

    /**
     * @param activity
     *            调用的activity
     */
    public InactivityTimer(Activity activity)
    {
        this.activity = activity;
        onActivity();
    }

    /**
     * @Description:初始化
     * @Author 13050527
     * @Date 2013-9-24
     */
    public void onActivity()
    {
        cancel();
        inactivityFuture = inactivityTimer.schedule(
                new FinishListener(activity), INACTIVITY_DELAY_SECONDS,
                TimeUnit.SECONDS);
    }

    /**
     * @Description:删除
     * @Author 13050527
     * @Date 2013-9-24
     */
    private void cancel()
    {
        if (inactivityFuture != null)
        {
            inactivityFuture.cancel(true);
            inactivityFuture = null;
        }
    }

    /**
     * @Description:释放资源
     * @Author 13050527
     * @Date 2013-9-24
     */
    public void shutdown()
    {
        cancel();
        inactivityTimer.shutdown();
    }


    /**
     *@Title:DaemonThreadFactory
     *@Description:
     *@Author:13050527
     *@Since:2013-9-24
     *@Version:
     */
    private static final class DaemonThreadFactory implements ThreadFactory
    {
        public Thread newThread(Runnable runnable)
        {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }

}
