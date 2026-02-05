# Kolejność implementacji serwisów (notatka dla mnie)

## 1) Szablony (fundament)
# Kolejność implementacji (dla mnie)

## Najpierw szablony
1) TrainingPlanServiceImpl  
- start całej struktury: tworzenie i ogarnianie "planów"

2) WorkoutTemplateServiceImpl  
- do planu dodaję "szablony treningów"  
- korzysta z TrainingPlanRepository, żeby to sensownie powiązać

3) ExerciseTemplateServiceImpl  
- do szablonu treningu dodaję "szablony ćwiczeń"  
- korzysta z WorkoutTemplateRepository

## Potem sesje (na bazie szablonów)
4) WorkoutSessionServiceImpl  
- najważniejszy: start sesji z WorkoutTemplate  
- tworzy WorkoutSession + od razu ExerciseSession i SetSession z template (w transakcji)

5) ExerciseSessionServiceImpl  
- głównie odczyt ExerciseSession, które już powstały przy starcie sesji

6) SetSessionServiceImpl  
- na koniec: update i usuwanie SetSession (czyli realne "wpisywanie progresu" w trakcie treningu)



