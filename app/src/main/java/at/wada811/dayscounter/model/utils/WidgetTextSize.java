/*
 * Copyright 2014 wada811<at.wada811@gmail.com>
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
package at.wada811.dayscounter.model.utils;

import at.wada811.dayscounter.R;

public class WidgetTextSize {

    private WidgetTextSize() {

    }

    public static int getSize(int textLength){
        int dimenId;
        switch(textLength){
            case 1:
                dimenId = R.dimen.widgetDaysTextSize1;
                break;
            case 2:
                dimenId = R.dimen.widgetDaysTextSize2;
                break;
            case 3:
                dimenId = R.dimen.widgetDaysTextSize3;
                break;
            case 4:
                dimenId = R.dimen.widgetDaysTextSize4;
                break;
            case 5:
                dimenId = R.dimen.widgetDaysTextSize5;
                break;
            default:
                dimenId = R.dimen.widgetDaysTextSize5;
                break;
        }
        return dimenId;
    }
}
