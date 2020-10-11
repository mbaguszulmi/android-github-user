# Android Github User (Kotlin)

This app made for Dicoding Submission. Made with kotlin language and using Android X (Jetpack)
components. Feel free to support this repo with stars or fork.

## Features

1. Load github users from [Official Github REST API](https://docs.github.com/en/free-pro-team@latest/rest)
2. Search github users by name or username
3. Load user details such as name, username, bio, number of public repos, followers, and following.
4. Add or remove user(s) to favorite
5. Show reminder every 9.00 AM to check this App.
6. Show favorite list in other package module (called ```Consumer App```)
7. Change language (available language: English (US) and Bahasa Indonesia)

## Development Info
### System Specification
This app build in laptop with these details:

1. Manufacturers: Samsung
2. Model Number: 355V4C/355V4X/355V5C/355V5X/356V
3. OS: ArchLinux
4. RAM: 4GB
5. CPU: AMD A6-4400M APU (2) @ 2.700GHz
6. AMD ATI Radeon HD 7520G
7. Storage: SSD 256GB

### Applied Components
This app build with this components:

1. [MVVM](https://medium.com/hongbeomi-dev/create-android-app-with-mvvm-pattern-simply-using-android-architecture-component-529d983eaabe) 
(Model, View, View Model) Architecture
2. [Room DB](https://developer.android.com/training/data-storage/room) for database helper
3. [Retrofit 2](https://square.github.io/retrofit/) for fetch API
4. [OkHttp3](https://square.github.io/okhttp/) for logging when accessing API
5. [Android KTX](https://developer.android.com/kotlin/ktx) for synthetic and kotlin approach code writing
6. [CircleImageView](https://github.com/hdodenhof/CircleImageView) displaying image in circle mode (for avatar)
7. [Glide v4](https://github.com/bumptech/glide) for loading image from url
8. [Content Provider](https://developer.android.com/guide/topics/providers/content-provider-basics) for accessing data from outside main app

### Dependencies
From google and default (General Purpose):

```
implementation "androidx.fragment:fragment-ktx:1.2.5"
implementation "androidx.activity:activity-ktx:1.1.0"
implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
implementation 'androidx.appcompat:appcompat:1.2.0'
implementation 'androidx.core:core-ktx:1.3.2'
implementation 'androidx.constraintlayout:constraintlayout:2.0.2'
implementation 'androidx.recyclerview:recyclerview:1.1.0'
implementation 'com.google.android.material:material:1.2.1'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
testImplementation 'junit:junit:4.13.1'
androidTestImplementation 'androidx.test.ext:junit:1.1.2'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
kapt 'androidx.lifecycle:lifecycle-compiler:2.2.0'
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"

// Room
def room_version = "2.2.5"

implementation "androidx.room:room-runtime:$room_version"
kapt "androidx.room:room-compiler:$room_version"

// optional - Kotlin Extensions and Coroutines support for Room
implementation "androidx.room:room-ktx:$room_version"

// optional - Test helpers
testImplementation "androidx.room:room-testing:$room_version"

// Preference screen
implementation 'androidx.preference:preference:1.1.1'
```

From third parties

- CircleImageView
  ```
  implementation 'de.hdodenhof:circleimageview:3.1.0'
  ```
- Glide
  ```
  implementation 'com.github.bumptech.glide:glide:4.11.0'
  kapt 'com.github.bumptech.glide:compiler:4.11.0'
  ```
- Retrofit & OkHttp3
  ```
  implementation 'com.squareup.retrofit2:retrofit:2.6.1'
  implementation 'com.squareup.retrofit2:converter-gson:2.6.1'

  implementation 'com.squareup.okhttp3:logging-interceptor:4.2.0'
  ```

## Licences

This app developed with open source license, Apache 2.0