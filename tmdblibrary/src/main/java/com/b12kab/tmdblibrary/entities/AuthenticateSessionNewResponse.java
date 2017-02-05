/*
 * Copyright (c) 2016 by Keith Beatty.
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

package com.b12kab.tmdblibrary.entities;

import com.google.gson.annotations.SerializedName;

public class AuthenticateSessionNewResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("session_id")
    private String sessionId;

    public boolean getSuccess() {
        return success;
    }

    public String getSessionId() {
        return sessionId;
    }
}
