# Kare - Fitness App

Welcome to **Kare**, an Android app designed to help you create, manage, and perform workouts with specific exercises tailored to your fitness goals.

## Table of Contents
- [Features](#features)
- [Cool Stuff](#cool-stuff)
- [Screens](#screens)
- [Installation](#installation)
- [Usage](#usage)
- [Dependencies](#dependencies)
- [Contributing](#contributing)
- [License](#license)

## Features

- Create custom workouts with a variety of exercises.
- Filter exercises by muscle group and machine type (barbell, dumbbell, calisthenics, machine).
- View detailed exercise instructions and videos.
- Track and save your workouts.
- Set up notifications and more in settings.

## Cool Stuff

- Push notifications
- Local and remote data sources
- Use cases and MVVM architecture
- Jetpack Compose for UI
- Splash Screen API for splash screen
- Broadcast receivers
- Regenerate access token + access token header for HTTP requests
- Interceptors for OkHttp client
- Complex use cases tests with high test coverage
- Instrumental tests
- Custom navigation
- Multi-modular project using clean architecture (UI, domain, and data layers)

## Screens

### Dashboard
Choose which muscle group catalog of exercises you want to look at.

### Exercise List Screen
Filter exercises based on their machine type (barbell, dumbbell, calisthenics, machine).

### Exercise Details Screen
View a video of the exercise, see how it is done properly, and read a description. Add exercises to workouts.

### Add Exercise to Workout Screen
Select a workout to add the chosen exercise to.

### Workouts Screen
View all user-created workouts, with an option to filter and see only the saved (bookmarked) ones.

### Workout Details Screen
Configure and view all exercises in a specific workout. Edit workout name, save/bookmark workout, delete exercises, and configure workout before starting.

### Add Exercises to Workout Screen
View a list of exercises sorted by muscle group. Add multiple exercises to your selected workout.

### Settings Screen
Logout and enable push notifications. More settings coming soon.

### Do Workout Screen
The main functionality of Kare. Includes a timer, exercise data sheet, cooldown screen, and the ability to skip sets. Ends with workout data saving.

### Login Screen
Log into the app.

### Register Screen
Create a new account.

### Welcome Screen
Features a video background with options to log in or register.

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/kare.git
    ```
2. Open the project in Android Studio.
3. Build the project to download all dependencies.

## Usage

1. Register or log in to your account.
2. Navigate through the dashboard to select a muscle group.
3. Filter exercises and view details.
4. Create workouts by adding exercises.
5. Track your progress and update your exercise data during workouts.
6. Manage your settings as needed.

## Dependencies

- **Dagger Hilt** - Dependency injection
- **Jetpack Compose** - UI
- **Retrofit and OkHttp** - Networking
- **Jackson and Moshi** - JSON parsing
- **Room** - Local database
- **GSM** - Google Services
- **Firebase Analytics, Messaging, and Crashlytics** - Firebase services
- **Media3 Video Player** and **Custom Video Player** - Video playback
- **JUnit 4 and 5** - Testing
- **AndroidX and KotlinX** - Android and Kotlin extensions
- **MockK and Turbine** - Testing utilities
- **Pull to Refresh** - UI component

## Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature-branch-name`
3. Make your changes and commit them: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature-branch-name`
5. Submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
