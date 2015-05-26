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

import android.webkit.MimeTypeMap;
import java.io.File;

public class MediaUtils {

    /**
     * ファイルから MIME Type を取得する
     *
     * @param file
     *
     * @return
     */
    public static String getMimeType(File file){
        return MediaUtils.getMimeType(file.getAbsolutePath());
    }

    /**
     * ファイルパスの拡張子から MIME Type を取得する
     *
     * @param filePath
     *
     * @return
     */
    public static String getMimeType(String filePath){
        String mimeType = null;
        String extension = FileNameUtils.getExtension(filePath);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        if(extension != null){
            mimeType = mime.getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }
}