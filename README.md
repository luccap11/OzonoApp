# WeatherConditions
Good app display next 5 days weather conditions
example: http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=7d85604d75067f7a0da53ac8f70c5364

## Notes
1. For sake of simplicity I omitted the Repository class, like suggested in Google documentation
1. I can improve the app introducing cached data with Repository pattern 

### Build & run the app
#### From command line
Follow this [instructions](https://developer.android.com/studio/build/building-cmdline)

#### From Android Studio
Follow this [instructions](https://developer.android.com/studio/run)

### Run tests
`./gradlew test` command to run unit tests
`./gradlew connectedAndroidTest` command to run instrumented tests
  

## Tech Stack
- MVVM (new for me)
- Glide - for image loading.
- LiveData - use LiveData to see UI update with data changes.
- Espresso for instrumented tests (new for me)

## Application Architecture
1. follow the rules from Architecture guidelines recommended by Google.
1. use latest Android Components, like ViewModel and LiveData.
1. keep Activity only responsible for UI related code
1. ViewModel provides data required by the UI class
