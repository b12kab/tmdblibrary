tmdblibrary
============

A Java wrapper around the [TMDb v3 API][1] using [retrofit][2].

The reason for (this) seperate library vs. using the [tmdb-java][3] library was I found that when pulling lists of movie information from TMDb, the genere information was in an Integer array. The information was not moved into the Genre object by Retrofit. The only way around this was to remove the Movie object and replace it with a list movie object: MovieResult and movie detail MovieFull. 

The partial addition of the Account calls allows the TMDb user to log in as well as set/unset movie favorites and set/remove movie rating value.


Usage
-----
Add the following dependency to your Gradle project:

```groovy
compile 'com.b12kab.tmdblibrary:tmdblibrary:0.11.1'
```


Dependencies
------------
Gradle:

```groovy
compile 'com.squareup.retrofit:retrofit:1.9.0'
compile 'com.squareup.okhttp:okhttp:2.3.0'
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
