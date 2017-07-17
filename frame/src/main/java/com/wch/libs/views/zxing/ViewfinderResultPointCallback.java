/*
 * Copyright (C) 2009 ZXing authors
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

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

/**
 * @Title:ViewfinderResultPointCallback
 * @Description:
 * @Author:13050527
 * @Since:2013-9-24
 * @Version:
 */
public final class ViewfinderResultPointCallback implements ResultPointCallback
{

    private final ViewfinderView viewfinderView;// viewfinderView的对象

    /**
     * @param viewfinderView
     *            设置viewfinderView对象
     */
    public ViewfinderResultPointCallback(ViewfinderView viewfinderView)
    {
        this.viewfinderView = viewfinderView;
    }

    public void foundPossibleResultPoint(ResultPoint point)
    {
        viewfinderView.addPossibleResultPoint(point);
    }

}
