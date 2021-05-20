package it.luccap11.android.ozono.model

object WeatherDataJsonResponseExample {
    fun fetchJson(timeInSecs: Long): String {
        return """
         {
            "cod":"200",
            "message":0,
            "cnt":40,
            "list":[
               {
                  "dt":"$timeInSecs",
                  "main":{
                     "temp":277.93,
                     "feels_like":271.73,
                     "temp_min":277.93,
                     "temp_max":278.43,
                     "pressure":1021,
                     "sea_level":1021,
                     "grnd_level":1019,
                     "humidity":71,
                     "temp_kf":-0.5
                  },
                  "weather":[
                     {
                        "id":802,
                        "main":"Clouds",
                        "description":"scattered clouds",
                        "icon":"03d"
                     }
                  ],
                  "clouds":{
                     "all":37
                  },
                  "wind":{
                     "speed":6.01,
                     "deg":351
                  },
                  "visibility":10000,
                  "pop":0,
                  "sys":{
                     "pod":"d"
                  },
                  "dt_txt":"2020-12-24 12:00:00"
               },
               {
                  "dt":" $timeInSecs ",
                  "main":{
                     "temp":278.62,
                     "feels_like":271.87,
                     "temp_min":278.62,
                     "temp_max":278.94,
                     "pressure":1023,
                     "sea_level":1023,
                     "grnd_level":1021,
                     "humidity":67,
                     "temp_kf":-0.32
                  },
                  "weather":[
                     {
                        "id":802,
                        "main":"Clouds",
                        "description":"scattered clouds",
                        "icon":"03d"
                     }
                  ],
                  "clouds":{
                     "all":40
                  },
                  "wind":{
                     "speed":6.77,
                     "deg":355
                  },
                  "visibility":10000,
                  "pop":0,
                  "sys":{
                     "pod":"d"
                  },
                  "dt_txt":"2020-12-24 15:00:00"
               }],
            "city":{
               "id":2643743,
               "name":"London",
               "coord":{
                  "lat":51.5085,
                  "lon":-0.1257
               },
               "country":"GB",
               "population":1000000,
               "timezone":0,
               "sunrise":1608797116,
               "sunset":1608825306
            }
         }
         """
    }
}