package it.luccap11.android.weatherconditions.model

object JsonResponseExample {
    fun fetchJson(): String {
        return "{\n" +
                "   \"cod\":\"200\",\n" +
                "   \"message\":0,\n" +
                "   \"cnt\":40,\n" +
                "   \"list\":[\n" +
                "      {\n" +
                "         \"dt\":1608811200,\n" +
                "         \"main\":{\n" +
                "            \"temp\":277.93,\n" +
                "            \"feels_like\":271.73,\n" +
                "            \"temp_min\":277.93,\n" +
                "            \"temp_max\":278.43,\n" +
                "            \"pressure\":1021,\n" +
                "            \"sea_level\":1021,\n" +
                "            \"grnd_level\":1019,\n" +
                "            \"humidity\":71,\n" +
                "            \"temp_kf\":-0.5\n" +
                "         },\n" +
                "         \"weather\":[\n" +
                "            {\n" +
                "               \"id\":802,\n" +
                "               \"main\":\"Clouds\",\n" +
                "               \"description\":\"scattered clouds\",\n" +
                "               \"icon\":\"03d\"\n" +
                "            }\n" +
                "         ],\n" +
                "         \"clouds\":{\n" +
                "            \"all\":37\n" +
                "         },\n" +
                "         \"wind\":{\n" +
                "            \"speed\":6.01,\n" +
                "            \"deg\":351\n" +
                "         },\n" +
                "         \"visibility\":10000,\n" +
                "         \"pop\":0,\n" +
                "         \"sys\":{\n" +
                "            \"pod\":\"d\"\n" +
                "         },\n" +
                "         \"dt_txt\":\"2020-12-24 12:00:00\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"dt\":1608822000,\n" +
                "         \"main\":{\n" +
                "            \"temp\":278.62,\n" +
                "            \"feels_like\":271.87,\n" +
                "            \"temp_min\":278.62,\n" +
                "            \"temp_max\":278.94,\n" +
                "            \"pressure\":1023,\n" +
                "            \"sea_level\":1023,\n" +
                "            \"grnd_level\":1021,\n" +
                "            \"humidity\":67,\n" +
                "            \"temp_kf\":-0.32\n" +
                "         },\n" +
                "         \"weather\":[\n" +
                "            {\n" +
                "               \"id\":802,\n" +
                "               \"main\":\"Clouds\",\n" +
                "               \"description\":\"scattered clouds\",\n" +
                "               \"icon\":\"03d\"\n" +
                "            }\n" +
                "         ],\n" +
                "         \"clouds\":{\n" +
                "            \"all\":40\n" +
                "         },\n" +
                "         \"wind\":{\n" +
                "            \"speed\":6.77,\n" +
                "            \"deg\":355\n" +
                "         },\n" +
                "         \"visibility\":10000,\n" +
                "         \"pop\":0,\n" +
                "         \"sys\":{\n" +
                "            \"pod\":\"d\"\n" +
                "         },\n" +
                "         \"dt_txt\":\"2020-12-24 15:00:00\"\n" +
                "      }],\n" +
                "   \"city\":{\n" +
                "      \"id\":2643743,\n" +
                "      \"name\":\"London\",\n" +
                "      \"coord\":{\n" +
                "         \"lat\":51.5085,\n" +
                "         \"lon\":-0.1257\n" +
                "      },\n" +
                "      \"country\":\"GB\",\n" +
                "      \"population\":1000000,\n" +
                "      \"timezone\":0,\n" +
                "      \"sunrise\":1608797116,\n" +
                "      \"sunset\":1608825306\n" +
                "   }\n" +
                "}"
    }
}