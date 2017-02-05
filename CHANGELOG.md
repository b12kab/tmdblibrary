Change Log
==========

0.11.0
-----------------
Updated use to [retrofit][1] 1.9.0. This is the last version of Retrofit v1.
Cloned from https://github.com/UweTrottmann/tmdb-java/releases/tag/v0.9.0
Main difference between 0.9.x and this version is the removal of the Movie object
It was found that when pulling (non specific) movie information from TMDb, the genere information was in an Integer array. The information was not moved into the Genre object by Retrofit. Therefore the Movie object needed to be split into two objects: a overview (MovieResult) and a detail (MovieFull).
Addition of TMDb account information, to sign into the user's TMDb account. This allows user favorite / unfavorite a specific movie as well as the user's adding / updating / removal rating on a specific movie.
The addition of obtaining the movie release date information.
Altered the testing to junit 4.12
Alter the build process from Maven to gradle w/in Android Studio 

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
