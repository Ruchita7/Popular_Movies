### **Project Summary**
Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, I have build an app to allow users to discover the most popular movies playing.
The complete functionality of this app is built in **two** stages which were submitted separately.


###**Why this Project?**
To be an Android developer, we must need to know how to build clean and compelling user interfaces (UIs), fetch data from network services, and optimize the experience for various mobile devices. One will hone these fundamental skills in this project.
This app demonstrates the understanding of the foundational elements of programming for Android and provides a responsive and delightful user experience.

###**Stage 1:  Main Discovery Screen, A Details View, and Settings**

####**User Experience**

In this stage we’ll build the core experience of the movies app.

The app will:

  •	Upon launch, present the user with an grid arrangement of movie posters.

  •	Allow the user to change sort order via a setting:

  •	The sort order can be by most popular, or by highest-rated

  •	Allow the user to tap on a movie poster and transition to a details screen with additional information such as:

    •	original title

    •	movie poster image thumbnail

    •	A plot synopsis (called overview in the api)

    •	user rating (called top_rated in the api)

    •	release date


####**Implementation Guidance**

#####**Image Library - Picasso**

I have used Picasso library for handling image loading and caching. For further details check the website at http://square.github.io/picasso/

In your app/build.gradle file, add:
 
repositories {

    mavenCentral()
    
}

Next, add compile **'com.squareup.picasso:picasso:2.5.2'** to your dependencies block.

You can use Picasso to easily load album art thumbnails into your views using:

Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);

Picasso will handle loading the images on a background thread, image decompression and caching the images.

#####**Working with the themoviedb.org APIAPI Hints**

  1.	To fetch popular movies, you will use the API from themoviedb.org.

    •	If you don’t already have an account, you will need to create one in order to request an API Key.

      •	In your request for a key, state that your usage will be foreducational/non-commercial use. You will also need to provide  some personal information to complete the request. Once you submit your request, you should receive your key via email shortly after.

    •	In order to request popular movies you will want to request data from the/discover/movie endpoint. An API Key is required.

    •	Once you obtain your key, you append it to your HTTP request as a URL parameter like so:

       •	http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]

    •	You will extract the movie id from this request. You will need this in subsequent requests.

**IMPORTANT: PLEASE REMOVE YOUR API KEY WHEN SHARING CODE PUBLICALLY**


#####**API Hints**

You will notice that the API response provides a relative path to a movie poster image when you request the metadata for a specific movie.

For example, the poster path return for Interstellar is “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”

You will need to append a base path ahead of this relative path to build the complete url you will need to fetch the image using Picasso.

It’s constructed using 3 parts:

  1.	The base URL will look like: http://image.tmdb.org/t/p/.

  2.	Then you will need a ‘size’, which will be one of the following: "w92", "w154", "w185","w342", "w500", "w780", or "original". For most phones we recommend using “w185”.

  3.	And finally the poster path returned by the query, in this case “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”

Combining these three parts gives us a final url of http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg 
 
This is also explained explicitly in the API documentation for /configuration.

###**What Will I Learn After Stage 1?**

•	You will fetch data from the Internet with theMovieDB API.

•	You will use adapters and custom list layouts to populate list views.

•	You will incorporate libraries to simplify the amount of code you need to write

###**Stage 2: Trailers, Reviews, and Favorites**

####**User Experience**

In this stage you’ll add additional functionality to the app you built in Stage 1.

You’ll add more information to your movie details view:
  •	You’ll allow users to view and play trailers ( either in the youtube app or a web browser).

  •	You’ll allow users to read reviews of a selected movie.

  •	You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies 
collection that you will maintain and does not require an API request*.

  •	You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.
Lastly, you’ll optimize your app experience for tablet.

####**Implementation Guidance**

####**Working with the themoviedb.org API**

#####**API Hints**

  1.	To fetch trailers you will want to make a request to the /movie/{id}/videos endpoint.

  2.	To fetch reviews you will want to make a request to the /movie/{id}/reviews endpoint

  3.	You should use an Intent to open a youtube link in either the native app or a web browser of choice.

###**What Will I Learn After Stage 2?**
  •	You will build a fully featured application that looks and feels natural on the latest Android operating system (Lollipop, as of May 2015).

  •	You will optimize the UI experience for both phones and tablets.
