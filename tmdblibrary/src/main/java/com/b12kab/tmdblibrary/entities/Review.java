/*
 * Copyright 2015 Miguel Teixeira
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

import java.util.Date;

public class Review {

    public String id;
    public String author;
    public AuthorDetail author_details;
    public String content;
    public Date created_at;
    public Date updated_at;
    public String url;

    // optional
    public String iso_639_1;
    public Integer media_id;
    public String media_title;
    public String media_type;
}
