package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, Set<TrainingSession>>> timetable = new HashMap<>();
    // Создаю именно Set<TrainingSession> для исключения одинаковых экземпляров типа TrainingSession и
    // возможности использования TreeSet для лексикографической сортировки при записи множества

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        // получили расписание на день недели
        TreeMap<TimeOfDay, Set<TrainingSession>> dayTimetable = timetable.get(dayOfWeek);

        if (dayTimetable == null) { // если отсутствует день недели
            dayTimetable = new TreeMap<>();
            timetable.put(dayOfWeek, dayTimetable); // записали день недели и пустой объект dayTimetable
        }

        // получили расписание на время
        Set<TrainingSession> timeTrainings = dayTimetable.get(timeOfDay);

        if (timeTrainings == null) { // если отсутствует тренировки на время
            timeTrainings = new TreeSet<>();
            dayTimetable.put(timeOfDay, timeTrainings); // добавили в расписание на день данные
        }

        timeTrainings.add(trainingSession); // добавляем тренировку
    }

    //выбираю SortedMap чтобы возвращать иммутабельную мапу
    public SortedMap<TimeOfDay, Set<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        TreeMap<TimeOfDay, Set<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptySortedMap();
        }
        return Collections.unmodifiableSortedMap(daySchedule);
    }

    public Set<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        TreeMap<TimeOfDay, Set<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(daySchedule.getOrDefault(timeOfDay, Collections.emptySet()));
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCount = new HashMap<>();

        // цикл по расписанию недели
        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, Set<TrainingSession>>> entry : timetable.entrySet()) {
            TreeMap<TimeOfDay, Set<TrainingSession>> dayTimetable = entry.getValue();
            // цикл по расписанию дня
            for (TimeOfDay timeOfDay : dayTimetable.keySet()) {
                // цикл по тренировкам
                for (TrainingSession trainingSession : dayTimetable.get(timeOfDay)) {
                    coachCount.put(trainingSession.getCoach(), coachCount.getOrDefault(trainingSession.getCoach(), 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> counterOfTrainings = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCount.entrySet()) {
            counterOfTrainings.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(counterOfTrainings.reversed());

        return counterOfTrainings;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "timetable=" + timetable +
                '}';
    }
}
