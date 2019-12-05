tmdblibrary
============

A Java wrapper around the [TMDb v3 API][1] using [retrofit][2].

The reason for (this) separate library vs. using the [tmdb-java][3] library was I found that when pulling lists of movie information from TMDb, the genere information was in an Integer array. The information was not moved into the Genre object by Retrofit. The only way around this was to remove the Movie object and replace it with a list movie object: MovieResult and movie detail MovieFull.

The partial addition of the Account calls allows the TMDb user to log in as well as set/unset movie favorites and set/remove movie rating value.

Note: This version uses API 29; converted to androidx; upgraded to use JUnit 5; now requires Java 88 (because of JUnit 5).

Note 2: This will need to be fixed in 2020 to allow for packaging in a different way to bintray, as of this: variant.getJavaCompileProvider() will no longer be supported. This is used to exclude the various external packages from the JavaDoc generation.

Usage
-----
Add the following dependency to your Gradle project:

```groovy
compile 'com.b12kab.tmdblibrary:tmdblibrary:0.11.7'
```


Dependencies
------------
Gradle:

```groovy
implementation 'com.squareup.retrofit:retrofit:1.9.0'
implementation 'com.squareup.okhttp:okhttp:2.7.4'
implementation 'com.google.code.gson:gson:2.8.5'
```


Example
-------

See test cases in `tmdblibrary/src/test/` for more examples.

Parent project
----------------

[tmdb-java](https://github.com/UweTrottmann/tmdb-java/releases/tag/v0.9.0)


 [1]: https://developers.themoviedb.org/3
 [2]: https://github.com/square/retrofit
 [3]: https://github.com/UweTrottmann/tmdb-java/releases
