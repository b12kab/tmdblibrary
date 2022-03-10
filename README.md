tmdblibrary
============

A Java wrapper around the [TMDb v3 API][1] using [retrofit][2].

For test usage, you must define in your personal gradle.properties (not this project's file):<p>
* TMDB\_TEST\_ID = TMDb user id<p>
* TMDB\_TEST\_GOOD\_PSWD = password for bullet 1<p>
* TMDB\_TEST\_BAD\_PSWD = invalid password for bullet 1<p>
* TMDB\_TEST\_API\_KEY = your TMDb v3 api key<p>
<p>


##How To Include It:
```
    allprojects {
        repositories {
            // ...
            maven { url "https://jitpack.io" }
        }
    }
```

Add this library to your dependencies:

```groovy
  implementation 'com.github.b12kab.tmdblibrary:tmdblibrary:0.12.1'
```

Dependencies
------------
See project gradle files


Example
-------

See test cases in `tmdblibrary/src/test/` for more examples.

Parent project
----------------

[tmdb-java](https://github.com/UweTrottmann/tmdb-java/releases/tag/v0.9.0)


 [1]: https://developers.themoviedb.org/3
 [2]: https://github.com/square/retrofit
 [3]: https://github.com/UweTrottmann/tmdb-java/releases