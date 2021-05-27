# Ozono App
![GitHub branch checks state](https://img.shields.io/github/checks-status/luccap11/WeatherConditions/master)
![GitHub issues](https://img.shields.io/github/issues/luccap11/WeatherConditions)
![GitHub pull requests](https://img.shields.io/github/issues-pr/luccap11/WeatherConditions)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/luccap11/WeatherConditions)
![GitHub milestones](https://img.shields.io/github/milestones/open/luccap11/WeatherConditions)
![GitHub milestones](https://img.shields.io/github/milestones/closed/luccap11/WeatherConditions)
[![GitHub Workflow Status](https://github.com/luccap11/WeatherConditions/actions/workflows/android.yml/badge.svg)](https://github.com/luccap11/WeatherConditions/actions)



Good app display next 5 days weather conditions
example: http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=7d85604d75067f7a0da53ac8f70c5364


Cites API:
https://community.algolia.com/places/api-clients.html

## Notes
I can improve the app:
1. introducing dagger2 for DI
1. Introducing RxJava
1. Introducing DataBinding
1. Design dark/light mode (Only dark mode was considered).
2. memory management, performance tuning and profiling tools (LeakCanary, AndroidProfiler)
3. 

### Build & run the app
#### From command line
Follow this [instructions](https://developer.android.com/studio/build/building-cmdline)

#### From Android Studio
Follow this [instructions](https://developer.android.com/studio/run)

### Tests
#### Unit Tests
`./gradlew test` command to run unit tests


#### Instrumented Tests
##### Set up
Before launching instrumented test you have to set up you test environment as described here: [Google developers guide](https://developer.android.com/training/testing/espresso/setup#set-up-environment)
`./gradlew connectedAndroidTest` command to run instrumented tests
  

## Tech Stack
- Kotlin (Coroutines, completion handler)
- MVVM
- Dependency Injection
- Retrofit2 - as HTTP client
- Moshi
- Room - for persistence layer
- Glide - for image loading.
- LruCache - for caching data
- LiveData - use LiveData to see UI update with data changes.
- Espresso for instrumented tests (new for me)

## Application Architecture
1. follow the rules from Architecture guidelines recommended by Google.
1. use latest Android Components, like ViewModel and LiveData.
1. use Kotlin Coroutines for async tasks.
1. keep Activity only responsible for UI related code.
1. ViewModel provides data required by the UI class.
1. Repository Layer provides data to ViewModel classes. (single source of truth)
1. unit tests for testing the main logic.
