#Popular Movies

##Overview

Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, I have build an app to allow users to discover the most popular movies playing. The complete functionality of this app is built in two stages which were submitted separately.

![screenshot1](/screenshot/Screenshot_2016-03-19-08-54-25.png)   ![screenshot2](/screenshot/Screenshot_2016-03-19-09-01-01.png)

![screenshot3](/screenshot/device-2016-03-19-093719.png)

![screenshot4](/screenshot/device-2016-03-19-102457.png)
 
**Stage 1: Main Discovery Screen, A Details View, and Settings**

In this stage the app will:

• Upon launch, present the user with an grid arrangement of movie posters.

• Allow the user to change sort order via a setting:

• The sort order can be by most popular, or by highest-rated

• Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
    
   (i) original title

   (ii) movie poster image thumbnail
 
   (iii) A plot synopsis (called overview in the api)
 
   (iv) user rating (called top_rated in the api)
 
   (v) release date
 
**Stage 2: Trailers, Reviews, and Favorites**

In this stage I have added more information to movie details view:

•  Allow users to view and play trailers ( either in the youtube app or a web browser).

• Allow users to read reviews of a selected movie.

• Allow users to mark a movie as a favorite in the details view by tapping a button(star).

• Modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection. Also optimized app experience for tablet.

For further details see [Guidelines](https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.7sxo8jefdfll)


##Prerequisites

 * The samples are building with compileSdkVersion 23 which requires [JDK 7](http://oracle.com/technetwork/java/javase/downloads/index.html) or higher

 * Android Studio

 * To fetch popular movies, you will use the API from [theMovieDb](https://www.themoviedb.org/).
 
    If you don’t already have an account, you will need to create one in order to request an API Key. In your request for a key, state that your usage will be foreducational/non-commercial use. You will also need to provide some personal information to complete the request. Once you submit your request, you should receive your key via email shortly after.

 * Once you obtain your key, in your **Popular_Movies/gradle.properties**,
uncomment #MovieKey= and append the key like `#MovieKey=<your_key>`

 * In your **Popular_Movies/app/build.gradle** add dependency for Picasso library which handles image loading and caching
 `compile 'com.squareup.picasso:picasso:2.5.2'` 
 
 * Also I have added dependencies for **Butterknife,Support libraries, Gson, Retrofit,Schematic,RecyclerView, Stetho** in Popular_Movies/app/build.gradle like
 
    `compile 'com.android.support:appcompat-v7:23.1.0'`
    `compile 'com.android.support:design:23.1.0'`  
    `compile 'com.squareup.picasso:picasso:2.5.2'`
    `compile 'com.android.support:support-v4:23.1.0'`
    `compile 'com.jakewharton:butterknife:7.0.1'`
    `compile 'com.google.code.gson:gson:2.3'`
    `compile 'com.squareup.retrofit:retrofit:2.0.0-beta2'`
    `compile 'com.squareup.retrofit:converter-gson:2.0.0-beta2'`
    `apt 'net.simonvt.schematic:schematic-compiler:0.6.3'`
    `compile 'net.simonvt.schematic:schematic:0.6.3'`
    `compile 'com.android.support:recyclerview-v7:23.0.+'`
    `compile 'com.facebook.stetho:stetho:1.2.0'`

##Instructions

###Get the source codes

Get the source code of the library and example app, by cloning git repository or downloading archives.

 * If you use **git**, execute the following command in your workspace directory.
 
    `$ git clone https://github.com/Ruchita7/ Popular_Movies.git`
    
* If you are using Windows, try it on GitBash or Cygwin or something that supports git.
 
###Import the project to Android Studio
 
Once the project is cloned to disk you can import into Android Studio:

 * From the toolbar select **File > Import Project**, or Import Non-Android Studio project from the Welcome Quick Start.

 *  Select the directory that is cloned. If you can't see your cloned directory, click "Refresh" icon and find it.

 *  Android Studio will import the project and build it. This might take minutes to complete. Even when the project window is opened, wait until the Gradle tasks are finished and indexed.

 *  Connect your devices to your machine and select app from the select Run/Debug Configuration drop down.Click the Run button

###Build and install using Gradle

If you just want to install the app to your device, you don't have to import project to Android Studio.

 •  After cloning the project, make sure **ANDROID_HOME** environment variable is set to point to your Android SDK. See [Getting Started with Gradle](https://guides.codepath.com/android/Getting-Started-with-Gradle).

 •  Connect an Android device to your computer or start an Android emulator.

 •  Compile the sample and install it. Run gradlew installDebug. Or if you on a Windows computer, use **gradlew.bat** instead.
 
###Contributing

Please follow the **"fork-and-pull"** Git workflow while contributing to this project

 **Fork** the repo on GitHub

 **Commit** changes to a branch in your fork

 **Pull request "upstream"** with your changes

 **Merge** changes in to "upstream" repo

**NOTE:** Be sure to merge the latest from **"upstream"** before making a pull request!
 
 
 
