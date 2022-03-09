Change Log
==========
0.12.0
----------------
* Upgraded project to use Retrofit 2 / OkHttp 3
* Upgraded gradle in project
* Synchronized structures with current TMDb v3 API
* Updated unit tests
* Added helpers for implementations of services
* Created unit tests for new helpers

0.11.9
----------------
* Upgrade to API 32

0.11.8
----------------
* Updated to remove bintray upload

0.11.7
----------------
* Updated gradle, API 27 to 29, moved to androidx; moved to JUnit 5; now requires Java 8.

0.11.6
----------------
* Version skipped

0.11.5
----------------
* Updated gradle, API 26 to API 27

0.11.4
----------------
* Updated Configuration and Configuration test

0.11.3
----------------
* Updated README.md to current version

0.11.2
----------------
* Split VideoResult from Video
* Created base class for MovieResult, MovieFull
* Renamed MovieResult to MovieAbbreviated

0.11.1
----------------
* Updated Tmdb object to add checkTmdbAPIKeyPopulated() method to check if the API key has been applied 
* Updated BaseMember to add missing gender JSON object, added methods to access member variables
* Updated CastMember to add missing cast_id JSON object, added methods to access member variables
* Updated dependencies
* Updated JavaDoc to fix various errors

0.11.0
-----------------
Updated use to [retrofit][1] 1.9.0. This is the last version of Retrofit v1.
Cloned from [tmdb-java][2] 


* Removal of Movie object from [tmdb-java][2]
* Addition of a movie list (MovieResult) and movie detail (MovieFull).
* Addition of TMDb account information, to sign into the user's TMDb account. This allows user favorite / unfavorite a specific movie as well as the user's adding / updating / removal rating on a specific movie.
* The addition of obtaining the movie release date information.
* Altered the testing to junit 4.12
* Alter the build process from Maven to gradle w/in Android Studio 

Note: to differentitate this version from the tmdb-java version, this library will use 0.11+ version numbers.

Supported API calls
-------------------

 * [Account](https://developers.themoviedb.org/3/account) _incomplete_
 * [Configuration](https://developers.themoviedb.org/3/configuration)
 * [Collections](https://developers.themoviedb.org/3/collections)
 * [Discover](https://developers.themoviedb.org/3/discover)
 * [Find](https://developers.themoviedb.org/3/find)
 * [Movies](https://developers.themoviedb.org/3/movies) 
 * [People](https://developers.themoviedb.org/3/people) _incomplete_
 * [Search](https://developers.themoviedb.org/3/search) _incomplete_
 * [TV](https://developers.themoviedb.org/3/tv) _incomplete_
 * [TV Seasons](https://developers.themoviedb.org/3/tv-seasons) _incomplete_
 * [TV Episodes](https://developers.themoviedb.org/3/tv-episodes) _incomplete_
 

  [1]: https://github.com/square/retrofit/tree/version-one
  [2]: https://github.com/UweTrottmann/tmdb-java/releases/tag/v0.9.0
