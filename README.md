# Quizoo

## Overview
Quizoo is an Android application that allows users to take quizzes, explore categories, and track their performance. Designed for educational purposes, Quizoo provides an engaging platform for learning through interactive quizzes.

### Purpose
To provide an interactive quiz-taking experience for users.

### Target Audience
Students, educators, and quiz enthusiasts.

### Platform
Android (Minimum SDK 29).

## Features
- **Browse Categories**: Explore quizzes organized by categories (e.g., Programming, Data Structures, AI).
- **Take Quizzes**: Answer multiple-choice questions and get instant feedback on your performance.
- **Track Progress**: View your quiz history and performance analytics.
- **Offline Mode**: Cache quizzes for offline access (requires initial sync).
- **User Authentication**: Log in to save progress and access personalized content.

## Project Structure
```
📂 androidTest
│   └── 📂 java/com/seifeddine/bd/quizoo
│       └── 📄 ExampleInstrumentedTest.kt
📂 main
├── 📄 AndroidManifest.xml
├── 📂 java/com/seifeddine/bd/quizoo
│   ├── 📂 data
│   │   ├── 📂 local
│   │   │   ├── 📄 AppDatabase.java
│   │   │   ├── 📂 dao
│   │   │   │   ├── 📄 AnalyticsDao.java
│   │   │   │   ├── 📄 CategoryDao.java
│   │   │   │   └── 📄 QuizDao.java
│   │   │   ├── 📂 entity
│   │   │   │   ├── 📄 Analytics.java
│   │   │   │   ├── 📄 Category.java
│   │   │   │   ├── 📄 Quiz.java
│   │   │   │   └── 📄 User.java
│   │   │   └── 📄 OptionsConverter.java
│   │   ├── 📂 remote
│   │   │   ├── 📂 api
│   │   │   │   └── 📄 QuizApiService.java
│   │   │   ├── 📂 dto
│   │   │   │   ├── 📄 AnalyticsRequest.java
│   │   │   │   ├── 📄 NetworkCategory.java
│   │   │   │   └── 📄 NetworkQuiz.java
│   │   │   └── 📄 RetrofitClient.java
│   │   └── 📂 repository
│   │       ├── 📄 QuizRepository.java
│   │       └── 📄 UserRepository.java
│   ├── 📂 di
│   │   └── 📄 AppModule.java
│   ├── 📂 ui
│   │   ├── 📂 activities
│   │   │   ├── 📄 LoginActivity.java
│   │   │   ├── 📄 MainActivity.java
│   │   │   ├── 📄 QuizActivity.java
│   │   │   └── 📄 SignupActivity.java
│   │   ├── 📂 adapters
│   │   │   ├── 📄 AnalyticsAdapter.java
│   │   │   ├── 📄 CategoryAdapter.java
│   │   │   ├── 📄 CategoryResultsAdapter.java
│   │   │   ├── 📄 QuizResultsAdapter.java
│   │   │   └── 📄 ResultsAdapter.java
│   │   ├── 📂 fragments
│   │   │   ├── 📄 AnalyticsFragment.java
│   │   │   ├── 📄 CategoriesFragment.java
│   │   │   ├── 📄 MainFragment.java
│   │   │   ├── 📄 QuizFragment.java
│   │   │   ├── 📄 ResultsFragment.java
│   │   │   └── 📄 RewardFragment.java
│   │   └── 📂 utils
│   │       ├── 📄 Constants.java
│   │       └── 📄 ImageUtils.java
└── 📂 res
    ├── 📂 anim, drawable, layout, menu, mipmap, navigation, values, xml
    └── Various XML files for UI elements, themes, and navigation.
📂 test/java/com/seifeddine/bd/quizoo
└── 📄 ExampleUnitTest.kt
```

## Technologies Used
- **Language**: Java
- **Framework**: Android SDK
- **Networking**: Retrofit (for API calls to the Quizoo Admin backend)
- **Database**: Room (for local storage and offline support) & Firebase
- **Build Tool**: Gradle
- **IDE**: Android Studio

## Installation
Follow these steps to set up the Quizoo Android app on your local machine:

### Clone the Repository
```bash
git clone https://github.com/seifeddine-bd/Quizoo.git
cd Quizoo
```

### Open in Android Studio
1. Launch Android Studio.
2. Select **File > Open** and navigate to the cloned repository folder.
3. Click **OK** to open the project.

### Sync Project with Gradle
Android Studio will prompt you to sync the project with Gradle. Click **Sync Project with Gradle Files**.

Alternatively, run the following command in the terminal:
```bash
./gradlew build
```

