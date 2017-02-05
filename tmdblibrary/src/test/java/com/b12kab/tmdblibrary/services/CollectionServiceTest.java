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
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.Collection;
import com.b12kab.tmdblibrary.entities.Images;

import org.junit.Test;

import java.text.ParseException;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CollectionServiceTest extends BaseTestCase {
  public CollectionServiceTest() {
  }

  @Test
  public void test_summary() {
      Collection collection = this.getManager().collectionService().summary(TestData.MOVIE_COLLECTION_ID, null, null);
      assertNotNull("Collection is null", collection);
      assertTrue("Collection name is not :" + TestData.MOVIE_COLLECTION_TITLE,
              collection.name.equals(TestData.MOVIE_COLLECTION_TITLE));
      assertEquals("Collections id not equal", (long) collection.id, 1241L);
      assertNotNull("Collections overview is null", collection.overview);
      assertFalse("Collections overview string length is empty", collection.overview.isEmpty());
      assertNotNull("Collections backdrop_path is null", collection.backdrop_path);
      assertFalse("Collections backdrop_path string is empty", collection.backdrop_path.isEmpty());
      assertNotNull("Collections poster_path is null", collection.poster_path);
      assertFalse("Collections poster_path string is empty", collection.poster_path.isEmpty());
      assertNotNull("Collections parts List is null", collection.parts);
      assertTrue("Collections parts List length is < 0", collection.parts.size() > 0);
      assertEquals("Collections parts List element 0 id is not 671", collection.parts.get(0).id, 671);
      assertEquals("Collections parts List element 1 id is not 672", collection.parts.get(1).id, 672);
  }

  @Test
  public void test_images() {
      Images images = this.getManager().collectionService().images(TestData.MOVIE_COLLECTION_ID, null);
      assertNotNull("Images is null", images);
      assertNotNull("Images id is null", images.id);
      assertEquals("Images id is not 1241", images.id, (Integer)1241);
      ////////////////////////////////////////////////////
      assertNotNull("Images backdrops List is null", images.backdrops);
      assertNotEquals("Images backdrops List size != 0", images.backdrops.size(), 0);
      assertNotNull("Images backdrops element 0 file_path is null", images.backdrops.get(0).file_path);
      assertFalse("Images backdrops element 0 file_path is empty", images.backdrops.get(0).file_path.isEmpty());
      assertNotNull("Images backdrops element 0 width is null", images.backdrops.get(0).width);
      assertEquals("Images backdrops element 0 width length is not 1920", (long) images.backdrops.get(0).width, (long) 1920);

      assertNotNull("Images backdrops element 0 height is null", images.backdrops.get(0).height);
      assertEquals("Images backdrops element 0 height length is not 1080", (long) images.backdrops.get(0).height, (long) 1080);

      assertNotNull("Images backdrops element 0 iso_639_1 is null", images.backdrops.get(0).iso_639_1);

      assertNotNull("Images backdrops element 0 aspect_ratio is null", images.backdrops.get(0).aspect_ratio);
      assertTrue("Images backdrops element 0 aspect_ratio < 1.7F", images.backdrops.get(0).aspect_ratio > 1.7F);

      assertNotNull("Images backdrops element 0 vote_average is null", images.backdrops.get(0).vote_average);
      assertTrue("Images backdrops element 0 vote_average < 1", images.backdrops.get(0).vote_average > 0);

      assertNotNull("Images backdrops element 0 vote_count is null", images.backdrops.get(0).vote_count);
      assertTrue("Images backdrops element 0 vote_count < 1", images.backdrops.get(0).vote_count > 0);
      ////////////////////////////////////////////
      assertNotNull("Images posters List is null", images.posters);
      assertNotEquals("Images posters List size is 0", images.posters.size(), 0);
      assertNotNull("Images posters element 0 file_path is null", images.posters.get(0).file_path);
      assertFalse("Images posters element 0 file_path is empty", images.posters.get(0).file_path.isEmpty());
      assertNotNull("Images posters element 0 width is null", images.posters.get(0).width);
      assertEquals("Images posters element 0 width length is 1000", (long) images.posters.get(0).width, (long) 1000);

      assertNotNull("Images posters element 0 height is null", images.posters.get(0).height);
      assertEquals("Images posters element 0 height length is 1500", (long) images.posters.get(0).height, (long) 1500);

      assertNotNull("Images posters element 0 iso_639_1 is null", images.posters.get(0).iso_639_1);
      assertEquals("Images posters element 0 iso_639_1 is not en", images.posters.get(0).iso_639_1, "en");

      assertNotNull("Images posters element 0 aspect_ratio is null", images.posters.get(0).aspect_ratio);
      assertTrue("Images posters element 0 aspect_ratio < 0.6F", images.posters.get(0).aspect_ratio > 0.6F);

      assertNotNull("Images posters element 0 vote_average is null", images.posters.get(0).vote_average);
      assertTrue("Images posters element 0 vote_average < 1", images.posters.get(0).vote_average > 0);

      assertNotNull("Images posters element 0 vote_count is null", images.posters.get(0).vote_count);
      assertTrue("Images posters element 0 vote_count < 1", images.posters.get(0).vote_count > 0);
  }
}