# Kare

## Description
Kare is a comprehensive Android fitness application designed to offer a personalized and efficient workout experience. It allows users to navigate through various exercises, tailor workouts to their preferences, and track their progress over time. The app emphasizes ease of use and provides a range of functionalities from selecting muscle groups to configuring individual workout details. With its user-friendly interface and detailed exercise database, Kare aims to be a versatile companion for fitness enthusiasts of all levels.

## App Navigation and Workflow

## 1. Dashboard Screen
![DashboardScreen](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/70251a76-84b6-4b06-a04b-69ad08022e6d)
- **Overview**: The first screen in the app, displaying a grid of muscle groups.
- **Functionality**: Enables users to select a muscle group to find exercises, particularly useful for adding specific exercises to a workout.

## 2. Muscle Group Screen
![MuscleGroupScreen1](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/56cf4b3e-e80e-430c-9f1f-f95e6d872094)
![MuscleGroupScreen2](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/d239a531-2c57-4ff3-b4da-274450b6f6c7)
- **Overview**: Lists all exercises for the chosen muscle group.
- **Features**: Offers four filter options based on the exercise execution method - Barbell, Dumbbell, Calisthenics, and Machine.

## 3. Exercise Details Screen
![ExerciseDetailsScreen](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/86959f42-6328-4469-869e-c2a09dbce131)
- **Overview**: Provides detailed information about a selected exercise.
- **Content**: Features a YouTube video demonstration, a descriptive summary, and an "Add to workout" button.

## 4. Search Workouts Screen
![SearchWorkoutScreen](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/11d08a8d-5883-4e83-974f-de733f924e4e)
- **Overview**: Accessible after selecting the "Add to workout" option.
- **Functionality**: Includes a search bar and a list view of all user-created workouts.

## 5. Workouts Screen
![WorkoutsScreen1](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/d7702556-c56e-4d25-8272-2391093fdae6)
![MyWorkoutScreen1](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/ab08ae07-d60b-469d-96ab-770d5c6203fc)
![WorkoutsScreen2](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/33b9c878-3995-49a7-94a9-4ac37ad85db1)
![WorkoutsScreen3](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/87d73c0a-3ba3-43e0-a702-16a2bdcff643)
- **Overview**: Can be accessed from the bottom navigation bar, featuring two options: Dashboard and Workouts.
- **Functionality**: Displays either 'MyWorkout' (the selected workout) or all workouts created by the user, based on the selected filter.

## 6. Workout Details Screen
![WorkoutDetailsScreen1](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/a6c6b2c0-5d9f-424c-a670-1b4ff94cd91d)
![WorkoutDetailsScreen2](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/a924d66e-39a7-4804-9c77-871e1982fce3)
- **Overview**: Shows the details of a chosen workout.
- **Content**: Lists all exercises included in the workout, with a persistent footer for adding new exercises.

## 7. Search Exercises Screen
![SearchExercisesScreen](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/c93504c9-3492-4882-8565-b078c4086a3a)
- **Overview**: For adding exercises to a workout from the Workout Details Screen.
- **Content**: Showcases a searchable list of all exercises, sorted by muscle groups.

## 8. Exercise Details Configurator Screen
![ExerciseDetailsConfiguratorScreen](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/6459a2e5-7221-457d-9739-57e30bc9b413)
- **Overview**: Allows users to configure sets, reps, and weights for a selected exercise.
- **Features**: Customization of exercise parameters with the option to add them to the workout.

## 9. Settings Screen
![SettingsScreen](https://github.com/MartinKoleff/KareFitnessApp/assets/52703399/f5d0bc03-f952-4b89-ab30-5d907a73da99)
- **Overview**: A standard settings screen.
- **Options**: Includes configurations such as push notifications, logout, language selection, etc.

## Future Enhancements
- **Onboarding Screen**: To provide a personalized experience based on user details like name, age, weight, and workout experience.
- **Statistics Screen**: To monitor and display the user's progress in various exercises and workouts.
- **Do Workout Screen**: Designed for executing workouts, featuring a video player, timer, and set completion toggles for exercises.

## Architecture and Modularity

Kare is designed with a focus on clean architecture, robust data handling, and modern UI design principles. Below is an overview of the key architectural and modular aspects of the app:

### Clean Architecture
- **Layer Separation**: The app is structured with distinct layers (Presentation, Domain, Data), ensuring a clear separation of concerns.
- **Use Cases**: Each action the user can perform is encapsulated in use cases, making the code more modular and testable.

### Data Handling
- **Repositories and Data Sources**: Data operations are abstractly handled by repositories, which in turn interact with specific data sources (remote API, local database).
- **Room Database**: Utilizes complex structures with entity relationships and DAOs for local data management.
- **Caching Mechanism**: Implements caching to toggle between remote and local data sources, optimizing data retrieval and reducing network load.

### Network Handling
- **Custom Network Handler**: A tailored network handler manages all interactions with the remote API, ensuring efficient and reliable data synchronization.
- **Dependency Injection**: Dagger-Hilt is used for injecting dependencies like data sources and network handlers, promoting scalability and maintainability.

### Architectural Patterns
- **MVVM and MVI**: Combines the best of MVVM (Model-View-ViewModel) and MVI (Model-View-Intent) architectures for a robust and predictable UI layer.
- **Single Activity Architecture**: Utilizes a single activity approach, with Jetpack Compose handling the UI navigation and screen compositions.

### UI and UX
- **Jetpack Compose**: Kare's first version leveraging Jetpack Compose for building native UIs.
- **Material Design 3**: Adheres to the latest Material Design guidelines to provide an intuitive and responsive user experience.

### Testing and Maintenance
- **Unit and UI Testing**: Comprehensive testing strategy covering unit tests for business logic and UI tests for ensuring interface integrity.

This architecture not only ensures that Kare is a cutting-edge fitness app but also makes it highly maintainable and scalable, ready for future enhancements and features.

## Libraries and Frameworks Used

Kare utilizes a variety of modern libraries and frameworks to provide a robust and efficient user experience. Here's an overview of the key technologies:

### Android and Kotlin
- **Android SDK**: Targeting SDK version 34 with a minimum SDK version of 24.
- **Kotlin**: Leveraging Kotlin for modern, concise, and safe code.

### UI and Compose
- **Jetpack Compose**: For building native UIs in a declarative style.
- **Material3**: For implementing Material Design components in Compose.

### Networking
- **Retrofit 2**: For network calls and data handling.
- **OkHttp3**: As the HTTP client, used alongside Retrofit.
- **Moshi & Gson**: For JSON serialization and deserialization.

### Dependency Injection
- **Dagger-Hilt**: For dependency injection, simplifying the architecture and reducing boilerplate.

### Database
- **Room**: As an abstraction layer over SQLite, to manage local databases more efficiently.

### Asynchronous Programming
- **Kotlin Coroutines**: For managing background tasks efficiently and cleanly.

### Additional Libraries
- **YouTube Player Library**: To embed and control YouTube videos.
- **MultiDex**: To overcome the 64K method limit in Android apps.

### Testing
- **JUnit**: For unit testing.
- **Espresso**: For UI testing.

This set of libraries and frameworks ensures that Kare is built on a solid foundation of tried and tested technologies, making it a reliable and scalable fitness app.

