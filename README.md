# 📸 Constantio

A photo-sharing social app for Android, built natively with Kotlin and Jetpack Compose on a Firebase backend. You sign up, post photos, follow people, and see their posts in a feed.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-DD2C00?style=flat&logo=firebase&logoColor=white)
![Android](https://img.shields.io/badge/Android-7.0%2B-3DDC84?style=flat&logo=android&logoColor=white)

<!-- Screenshots: save phone screenshots in docs/ and uncomment.
<p align="center">
  <img src="docs/feed.png" width="24%" alt="Feed">
  <img src="docs/profile.png" width="24%" alt="Profile">
  <img src="docs/search.png" width="24%" alt="Search">
  <img src="docs/add-post.png" width="24%" alt="New post">
</p>
-->

---

## ✨ Features

- **Accounts.** Register, log in and reset your password through Firebase Authentication. Each auth error (weak password, email already used, wrong credentials) shows its own message.
- **Posts.** Pick photos from the gallery, add a description and publish. Images are compressed on the device before upload to Firebase Storage.
- **Feed.** Posts from the people you follow, newest first. Following or unfollowing someone updates the feed immediately.
- **Profiles.** A profile picture, post count and a grid of your posts. You can change your picture at any time.
- **Search and follow.** Find other users, open their profiles and follow or unfollow them.

## 🏗️ How it's built

| Layer | What's used |
|---|---|
| UI | Jetpack Compose, Material 3, Navigation Compose, Coil for image loading |
| State | ViewModels with LiveData, Kotlin coroutines for all Firebase calls |
| Auth | Firebase Authentication (email + password) |
| Data | Firebase Realtime Database for users, posts and follow lists |
| Media | Firebase Storage for profile pictures and post images |

```
app/src/main/java/com/domagojleskovic/constantio/
  ui/                        Compose screens: Login, Register, ForgotPassword,
                             Main (feed), Profile, Post, AddPost, Search
  EmailPasswordManager.kt    auth, database and storage access
  *ViewModel.kt              screen state for login, registration and search
  UserParser.kt              maps database snapshots to Profile objects
```

## 🚀 Running it

**Requirements:** Android Studio (Hedgehog or newer), JDK 17, and a device or emulator on Android 7.0 (API 24) or newer.

1. Clone the repo and open it in Android Studio.
2. The app is connected to my Firebase project through `app/google-services.json`. To use your own:
   1. Create a Firebase project.
   2. Enable **Email/Password** authentication, the **Realtime Database** and **Storage**.
   3. Download your own `google-services.json` and replace the file in `app/`.
3. Run the `app` configuration.

## 🗺️ Status

Built between December 2023 and February 2024 as a learning project.

**Not implemented yet:**
- Likes and comments: the buttons are UI only.
- Stories: the story bar is UI only.
- Direct messages.
