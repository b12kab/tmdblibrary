/*
 * Copyright 2014 Chris Banes
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
 *
 */

package com.b12kab.tmdblibrary.entities;

import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppendToResponse {

    private EnumSet<AppendToResponseItem> items = EnumSet.noneOf(AppendToResponseItem.class);

    public AppendToResponse(AppendToResponseItem... items) {
        if (items == null)
            return;

        Collections.addAll(this.items, items);
    }

    @Nullable
    @Override
    public String toString() {
        if (items != null && items.size() > 0) {
            StringBuffer sb = new StringBuffer();
            Iterator iter = items.iterator();
            for (int i = 1; iter.hasNext(); i++) {
                sb.append(iter.next().toString());

                if (i < items.size()) {
                    sb.append(',');
                }
            }

            return sb.toString();
        }

        return null;
    }

    @NonNull
    public List<AppendToResponseItem> toList() {
        List<AppendToResponseItem> itemList = new ArrayList<>();
        for (AppendToResponseItem item: this.items) {
            itemList.add(item);
        }
        return itemList;
    }

    public EnumSet<AppendToResponseItem> values() {
        return this.items;
    }

    public int size() {
        return this.items.size();
    }

    public AppendToResponse merge(AppendToResponse appendToResponse) {
        if (appendToResponse == null)
            return this;

        if (appendToResponse.size() == 0)
            return this;

        if (this.size() == 0 && appendToResponse.size() > 0) {
            return appendToResponse;
        }

//        EnumSet<AppendToResponseItem> originalSet = this.values();
//        EnumSet<AppendToResponseItem> appendSet = appendToResponse.values().clone();
//
//        appendSet.removeAll(originalSet);
//        originalSet.addAll(appendSet);
//
//        return new AppendToResponse(originalSet.toArray(new AppendToResponseItem[originalSet.size()]));
        this.items.addAll(appendToResponse.toList());

        return this;
    }
}
