# Kolejność implementacji serwisów (notatka dla mnie)

## Wydzielenie autoryzacji do aspektów

## Potem sesje (na bazie szablonów)
4) WorkoutSessionServiceImpl  
- najważniejszy: start sesji z WorkoutTemplate  
- tworzy WorkoutSession + od razu ExerciseSession i SetSession z template (w transakcji)

5) ExerciseSessionServiceImpl  
- głównie odczyt ExerciseSession, które już powstały przy starcie sesji

6) SetSessionServiceImpl  
- na koniec: update i usuwanie SetSession (czyli realne "wpisywanie progresu" w trakcie treningu)





