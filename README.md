# Hebrew Audio Bible

The Hebrew Audio Bible is a modern Android application designed for an immersive study of the Hebrew Bible. It synchronizes audio playback of chapters with verse-by-verse text highlighting, and provides in-depth linguistic details for each word, including its original form, transliteration, translation, and Strong's number definition.

This project is built with a modern Android tech stack, emphasizing a clean, scalable, and maintainable architecture.

## ✨ Features

*   **Synchronized Audio/Text:** Listen to chapters while the currently spoken verse is highlighted in the UI.
*   **Interactive Text:** Tap on any verse to seek the audio to that point.
*   **Rich Word Analysis:** Long-press a verse to navigate to a detailed breakdown of each word, including:
    *   Original Hebrew text
    *   Transliteration
    *   English translation
    *   Strong's number and definition
*   **Dynamic UI:** Toggle the visibility of Hebrew, transliteration, and English text to customize your study view.
*   **Chapter Navigation:** Easily switch between chapters and books using a custom dialog.
*   **Offline First:** The entire biblical text and associated data are pre-packaged in the app using a Room database, ensuring full functionality offline. Strong's definitions are fetched from a remote API and cached locally for future use.

## 🛠️ Tech Stack & Architecture

This project follows modern Android architecture principles and is built with the following specific technologies:

### Architecture

*   **Clean Architecture:** The codebase is separated into `data`, `domain` (as `usecases` and domain models), and `presentation` layers. This promotes separation of concerns, testability, and maintainability.
*   **MVVM (Model-View-ViewModel):** The presentation layer uses the MVVM pattern to separate UI logic from business logic.
*   **Repository Pattern:** A single `BibleRepository` acts as the source of truth, abstracting the `LocalDataSource` (Room) and `RemoteDataSource` (Retrofit).
*   **Modularization:** The project is divided into modules (`:app`, `:data`, `:usecases`) to enforce architectural boundaries.

### Core Technologies

*   **UI:** [**Jetpack Compose**](https://developer.android.com/jetpack/compose) is used for building the entire user interface declaratively.
*   **Navigation:** [**Navigation Compose with Type-Safe Routes**](https://developer.android.com/jetpack/compose/navigation) is used for navigating between screens. Type safety is achieved using `@Serializable` data classes and `kotlinx-serialization`.
*   **Asynchronous Programming:** [**Kotlin Coroutines & Flow**](https://kotlinlang.org/docs/coroutines-guide.html) are used extensively for managing background tasks, data streams, and UI state.
*   **Dependency Injection:** [**Hilt**](https://developer.android.com/training/dependency-injection/hilt-android) manages dependencies throughout the app. [**Hilt-Navigation-Compose**](https://developer.android.com/jetpack/compose/libraries#hilt-navigation) is used to inject ViewModels into Composables.
*   **Local Storage:** [**Room**](https://developer.android.com/training/data-storage/room) provides an abstraction layer over SQLite to allow for robust database access. The database is pre-populated from a local asset (`hebrew_bible_asset.db`).
*   **Networking:**
    *   [**Retrofit**](https://square.github.io/retrofit/): A type-safe HTTP client for Android and Java to fetch Strong's word definitions from the BollsBible API.
    *   [**OkHttp Logging Interceptor**](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor): Logs HTTP request and response data for debugging purposes.
*   **JSON Parsing:**
    *   [**Moshi**](https://github.com/square/moshi): A modern JSON library for Android, used with Retrofit to parse the network API response.
    *   [**Kotlinx.serialization**](https://github.com/Kotlin/kotlinx.serialization): Used for type-safe navigation arguments.
*   **State Management:** `StateFlow` is used within ViewModels to expose UI state, which is then collected in a lifecycle-aware manner in the UI using `collectAsStateWithLifecycle`.
*   **ViewModel:** [**Jetpack ViewModel**](https://developer.android.com/topic/libraries/architecture/viewmodel) is used to store and manage UI-related data. The `@HiltViewModel` annotation enables DI, and `SavedStateHandle` retrieves navigation arguments robustly.

## 🚀 Setup and Build

To build and run this project, follow these steps:

1.  **Clone the repository:**

2.  **Open in Android Studio:**
    *   Open Android Studio (latest stable version recommended).
    *   Select `File > Open` and navigate to the cloned project directory.

3.  **Database Asset:**
    *   **Important:** The database file (`hebrew_bible_asset.db`) used for the full version of this application is proprietary and is **not** included in this repository.
    *   The project is configured to look for this file in `app/src/main/assets/`.
    *   A sample version of the database will be uploaded at a later time to allow for building and testing the application.

4.  **Build the project:**
    *   Once the sample database is in place, Android Studio should automatically sync the Gradle files.
    *   Run the app on an emulator or a physical device.
