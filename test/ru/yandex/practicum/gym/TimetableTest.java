package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        //Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        SortedMap<TimeOfDay, Set<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());

        Iterator<Map.Entry<TimeOfDay, Set<TrainingSession>>> iterator =
                thursdaySessions.entrySet().iterator();

        // Первая должна быть детская тренировка в 13:00
        Map.Entry<TimeOfDay, Set<TrainingSession>> first = iterator.next();
        Assertions.assertEquals(new TimeOfDay(13, 0), first.getKey());
        Assertions.assertTrue(first.getValue().contains(thursdayChildTrainingSession));

        // Вторая – взрослая в 20:00
        Map.Entry<TimeOfDay, Set<TrainingSession>> second = iterator.next();
        Assertions.assertEquals(new TimeOfDay(20, 0), second.getKey());
        Assertions.assertTrue(second.getValue().contains(thursdayAdultTrainingSession));

        // Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());

    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)).size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0)).size());
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        // Проверка, что несколько тренировок могут начинаться в одно время
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Алексей", "Петрович");
        Coach coach2 = new Coach("Петрова", "Мария", "Ивановна");

        Group groupChild = new Group("Гимнастика для детей", Age.CHILD, 45);
        Group groupAdult = new Group("Гимнастика для взрослых", Age.ADULT, 60);

        TrainingSession session1 = new TrainingSession(groupChild, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(groupAdult, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        Set<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(session1));
        Assertions.assertTrue(result.contains(session2));
    }

    @Test
    void testOrderIsMaintainedAfterAddingOutOfOrder() {
        // Проверка, что тренировки в дне всегда возвращаются в хронологическом порядке,
        // даже если добавлялись не по порядку времени
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Сидр", "Сидорович");
        Group group = new Group("Плавание", Age.ADULT, 60);

        TrainingSession late = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0));
        TrainingSession noon = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(12, 0));
        TrainingSession morning = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(8, 0));

        timetable.addNewTrainingSession(late);
        timetable.addNewTrainingSession(noon);
        timetable.addNewTrainingSession(morning);

        SortedMap<TimeOfDay, Set<TrainingSession>> fridaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);

        Assertions.assertEquals(3, fridaySessions.size());

        Iterator<Map.Entry<TimeOfDay, Set<TrainingSession>>> iterator =
                fridaySessions.entrySet().iterator();

        // 08:00
        Map.Entry<TimeOfDay, Set<TrainingSession>> first = iterator.next();
        Assertions.assertEquals(new TimeOfDay(8, 0), first.getKey());
        Assertions.assertTrue(first.getValue().contains(morning));

        // 12:00
        Map.Entry<TimeOfDay, Set<TrainingSession>> second = iterator.next();
        Assertions.assertEquals(new TimeOfDay(12, 0), second.getKey());
        Assertions.assertTrue(second.getValue().contains(noon));

        // 20:00
        Map.Entry<TimeOfDay, Set<TrainingSession>> third = iterator.next();
        Assertions.assertEquals(new TimeOfDay(20, 0), third.getKey());
        Assertions.assertTrue(third.getValue().contains(late));
    }

    @Test
    void testEmptyTimetable() {
        // Проверка на пустое расписание – методы не падают и возвращают пустые списки
        Timetable timetable = new Timetable();

        SortedMap<TimeOfDay, Set<TrainingSession>> daySessions = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);
        Assertions.assertTrue(daySessions.isEmpty());

        Set<TrainingSession> timeSessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.SUNDAY, new TimeOfDay(9, 0));
        Assertions.assertTrue(timeSessions.isEmpty());
    }


    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Петр", "Петрович");
        Coach coach3 = new Coach("Сидоров", "Сидр", "Сидорович");

        Group group = new Group("Спортивная гимнастика", Age.ADULT, 30);

        // coach1 ведёт 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(8, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(8, 0)));

        // coach2 ведёт 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));

        // coach3 ведёт 1 тренировку
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));

        List<CounterOfTrainings> stats = timetable.getCountByCoaches();

        // Должно быть 3 тренера
        Assertions.assertEquals(3, stats.size());

        // Проверяем сортировку по убыванию количества
        Assertions.assertEquals(coach2, stats.get(0).getCoach());
        Assertions.assertEquals(3, stats.get(0).getNumberOfTrainings());

        Assertions.assertEquals(coach1, stats.get(1).getCoach());
        Assertions.assertEquals(2, stats.get(1).getNumberOfTrainings());

        Assertions.assertEquals(coach3, stats.get(2).getCoach());
        Assertions.assertEquals(1, stats.get(2).getNumberOfTrainings());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Петров", "Петр", "Петрович");
        Group group = new Group("Легкая атлетика", Age.CHILD, 60);

        // добавляем 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));

        List<CounterOfTrainings> stats = timetable.getCountByCoaches();

        Assertions.assertEquals(1, stats.size());
        Assertions.assertEquals(coach, stats.getFirst().getCoach());
        Assertions.assertEquals(2, stats.getFirst().getNumberOfTrainings());
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> stats = timetable.getCountByCoaches();
        Assertions.assertTrue(stats.isEmpty());
    }

}
