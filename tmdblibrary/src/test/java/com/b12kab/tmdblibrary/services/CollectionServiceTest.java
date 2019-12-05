/*
 * Copyright 2015 Manuel Laggner
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
 *
 * Keith Beatty 2017 - Converted to jUnit 4.12
 * Keith Beatty 2019 - Converted to jUnit 5.x
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.Collection;
import com.b12kab.tmdblibrary.entities.Images;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionServiceTest extends BaseTestCase {

    @BeforeEach
    void init() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            System.out.println(e);
        }
    }

  @Test
  public void test_summary() {
      Collection collection = this.getManager().collectionService().summary(TestData.MOVIE_COLLECTION_ID, null, null);

      assertNotNull(collection, "Collection is null");
      assertEquals(collection.name, TestData.MOVIE_COLLECTION_TITLE, "Collection name is not :" + TestData.MOVIE_COLLECTION_TITLE);
      assertEquals((long) collection.id, 1241L, "Collections id not equal" );
      assertNotNull(collection.overview, "Collections overview is null");
      assertFalse(collection.overview.isEmpty(), "Collections overview string length is empty");
      assertNotNull(collection.backdrop_path, "Collections backdrop_path is null");
      assertFalse(collection.backdrop_path.isEmpty(), "Collections backdrop_path string is empty");
      assertNotNull(collection.poster_path, "Collections poster_path is null");
      assertFalse(collection.poster_path.isEmpty(), "Collections poster_path string is empty");
      assertNotNull(collection.parts, "Collections parts List is null");
      assertTrue(collection.parts.size() > 0, "Collections parts List length is < 0" );
      assertEquals(collection.parts.get(0).id, 671, "Collections parts List element 0 id is not 671");
      assertEquals(collection.parts.get(1).id, 672, "Collections parts List element 1 id is not 672" );
  }

  @Test
  public void test_images() {
      Images images = this.getManager().collectionService().images(TestData.MOVIE_COLLECTION_ID, null);

      assertNotNull(images, "Images is null");
      assertNotNull(images.id, "Images id is null");
      assertEquals(images.id, (Integer)1241, "Images id is not 1241" );

      assertNotNull(images.backdrops, "Images backdrops List is null");
      assertNotEquals(images.backdrops.size(), 0, "Images backdrops List size != 0");
      assertNotNull(images.backdrops.get(0).file_path, "Images backdrops element 0 file_path is null");
      assertFalse(images.backdrops.get(0).file_path.isEmpty(), "Images backdrops element 0 file_path is empty");
      assertNotNull(images.backdrops.get(0).width, "Images backdrops element 0 width is null");
      assertEquals((long) images.backdrops.get(0).width, (long) 1920, "Images backdrops element 0 width length is not 1920" );

      assertNotNull(images.backdrops.get(0).height, "Images backdrops element 0 height is null");
      assertEquals((long) images.backdrops.get(0).height, (long) 1080, "Images backdrops element 0 height length is not 1080");

      assertNotNull(images.backdrops.get(0).iso_639_1, "Images backdrops element 0 iso_639_1 is null");

      assertNotNull( images.backdrops.get(0).aspect_ratio, "Images backdrops element 0 aspect_ratio is null");
      assertTrue(images.backdrops.get(0).aspect_ratio > 1.7F, "Images backdrops element 0 aspect_ratio < 1.7F" );

      assertNotNull(images.backdrops.get(0).vote_average, "Images backdrops element 0 vote_average is null" );
      assertTrue(images.backdrops.get(0).vote_average > 0, "Images backdrops element 0 vote_average < 1");

      assertNotNull(images.backdrops.get(0).vote_count, "Images backdrops element 0 vote_count is null" );
      assertTrue(images.backdrops.get(0).vote_count > 0, "Images backdrops element 0 vote_count < 1");
      ////////////////////////////////////////////
      assertNotNull(images.posters, "Images posters List is null");
      assertNotEquals(images.posters.size(), 0, "Images posters List size is 0" );
      assertNotNull(images.posters.get(0).file_path, "Images posters element 0 file_path is null" );
      assertFalse(images.posters.get(0).file_path.isEmpty(), "Images posters element 0 file_path is empty" );
      assertNotNull(images.posters.get(0).width, "Images posters element 0 width is null" );
      assertEquals((long) images.posters.get(0).width, (long) 1000, "Images posters element 0 width length is 1000" );

      assertNotNull(images.posters.get(0).height, "Images posters element 0 height is null");
      assertEquals((long) images.posters.get(0).height, (long) 1500, "Images posters element 0 height length is 1500");

      assertNotNull(images.posters.get(0).iso_639_1, "Images posters element 0 iso_639_1 is null");
      assertEquals(images.posters.get(0).iso_639_1, "en","Images posters element 0 iso_639_1 is not en" );

      assertNotNull(images.posters.get(0).aspect_ratio, "Images posters element 0 aspect_ratio is null");
      assertTrue(images.posters.get(0).aspect_ratio > 0.6F, "Images posters element 0 aspect_ratio < 0.6F");

      assertNotNull(images.posters.get(0).vote_average, "Images posters element 0 vote_average is null" );
      assertTrue(images.posters.get(0).vote_average > 0, "Images posters element 0 vote_average < 1");

      assertNotNull(images.posters.get(0).vote_count, "Images posters element 0 vote_count is null" );
      assertTrue(images.posters.get(0).vote_count > 0, "Images posters element 0 vote_count < 1");
  }
}
