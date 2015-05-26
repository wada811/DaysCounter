/*
 * Copyright 2013 wada811<at.wada811@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wada811.android.utils;

import android.os.Build;

public class AndroidUtils {

    /**
     * Android OS バージョンが指定のバージョン以上の場合は true
     *
     * @param versionCode
     * @return
     */
    public static boolean isMoreThanBuildVersion(int versionCode){
        return Build.VERSION.SDK_INT >= versionCode;
    }

    /**
     * Android OS バージョンが指定のバージョン未満の場合は true
     *
     * @param versionCode
     * @return
     */
    public static boolean isLessThanBuildVersion(int versionCode){
        return Build.VERSION.SDK_INT < versionCode;
    }
}
