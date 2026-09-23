# To-Do List (Kotlin Multiplatform)

## Overview
A cross‑platform Todo list application built with **Kotlin Multiplatform** and **Compose Multiplatform**. The app runs on Android and iOS while sharing the same business logic, UI, and data layer.

## Features
- **Create / Edit / Delete** tasks (delete only from the detail screen at the moment).
- Mark tasks as completed.
- Filter tasks by status (All, Pending, Completed).
- Basic category management (create, edit, delete categories) – categories can be assigned to tasks.
- Local notifications scheduled for tasks that have a `dueDateTime` (Android uses `AlarmManager`; iOS stub ready for `UNUserNotificationCenter`).
- Persistent storage with **SQLDelight** (SQLite) – schema defined in `TodoDatabase.sq`.
- Navigation handled by **Voyager** (stack‑based navigation).
- Dark‑mode support via Material 3 theme.

> **Note:** The UI does not currently expose a date‑time picker, category filtering, or task deletion directly from the list. These are planned improvements (see `implementation_plan.md`).

## Architecture
- **`commonMain`** – shared UI (`composeApp/src/commonMain/kotlin/com/example/todoapp/ui`), repository (`TodoRepository`), data models, and SQLDelight schema.
- **`androidMain`** – Android‑specific `DatabaseDriverFactory` (uses `AndroidSqliteDriver`) and `NotificationHelper` (uses `AlarmManager`).
- **`iosMain`** – iOS‑specific driver (uses `NativeSqliteDriver`) and a stub `NotificationHelper` ready for implementation with `UNUserNotificationCenter`.
- **Navigation** – Voyager screens (`TaskListScreen`, `TaskDetailScreen`, `CategoryListScreen`).
- **State Management** – `TodoScreenModel` (`ScreenModel`) provides `StateFlow` streams for tasks, categories, and filter state.

## Build & Run
The project uses the Gradle wrapper, pinned to **Gradle 8.5** (`gradle/wrapper/gradle-wrapper.properties`).

```bash
# Clone the repository (if not already)
git clone <repo‑url>
cd To-do-list

# Make the wrapper executable (Linux/macOS)
chmod +x gradlew

# Sync dependencies and assemble the Android debug APK
./gradlew :composeApp:assembleDebug

# Run on an Android device/emulator
adb install composeApp/build/outputs/apk/debug/composeApp-debug.apk

# Build the iOS framework (requires Xcode on macOS)
./gradlew :composeApp:linkDebugFrameworkIosArm64
# Open the generated Xcode project located at composeApp/iosApp and run on a simulator.
```

## Tests
```bash
# Run common unit tests
./gradlew test

# Run Android UI tests (requires an emulator/device)
./gradlew :composeApp:connectedAndroidTest
```

## Project Structure
```
To-do-list/
├─ build.gradle.kts          # Root Gradle configuration
├─ settings.gradle.kts       # Includes :composeApp module
├─ gradle/
│   └─ wrapper/              # Gradle wrapper files
├─ composeApp/               # Multiplatform module
│   ├─ build.gradle.kts      # Module dependencies (Compose, SQLDelight, Voyager)
│   ├─ src/
│   │   ├─ commonMain/       # Shared code – UI, repository, models
│   │   ├─ androidMain/       # Android‑specific implementations
│   │   └─ iosMain/           # iOS‑specific implementations
│   └─ ...
└─ README.md                 # This file
```

## Documentation & Logs
- **Spec.md** – formal specification of the required functionality.
- **BUILD_LOG.md** – step‑by‑step log of all decisions, fixes, and changes performed during development.

## License
This project is provided for educational purposes. Feel free to adapt, extend, or reuse the code as you wish.

## Developers

- [Diego Nunes](https://github.com/Diego-jpeg-27)
- [Victor Soares](https://github.com/VSoares27)