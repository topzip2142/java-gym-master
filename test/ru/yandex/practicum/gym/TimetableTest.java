package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
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
        Map<TimeOfDay, List<TrainingSession>> mondaySession = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(mondaySession, "За понедельник должен вернутся список");
        assertEquals(1, mondaySession.size(),
                "в понедельник должна быть ровна одна тренировка");

        //Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySession.isEmpty(), "Во вторник нет занятий");
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
        Map<TimeOfDay, List<TrainingSession>> mondaySession = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySession.size(), "В понедельник должно быть 1 занятие");

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Map<TimeOfDay, List<TrainingSession>> thursdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySession.size(), "в четверг должно быть 2 занятия");
        NavigableSet<TimeOfDay> sortedKey = ((TreeMap<TimeOfDay, List<TrainingSession>>) thursdaySession).navigableKeySet();
        Assertions.assertEquals(13, sortedKey.first().getHours(), "1-ая тренировка должна быть в 13 часов");
        Assertions.assertEquals(20, sortedKey.last().getHours(), "2-ая тренировка должна быть в 8 часов вечера");

        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySession.isEmpty(), "За вторник не должно быть занятий");
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
        List<TrainingSession> trainingSessionsMonday1300 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, trainingSessionsMonday1300.size(), "В понедельник в 13:00 должно быть 1 занятие");

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> trainingSessionsMonday1400 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(trainingSessionsMonday1400.isEmpty(), "В понедельник в 14:00 не должно быть занятий");

    }

    @Test
    void testGetCountByCoachesSorting() {
        Timetable timetable = new Timetable();
        Coach coachOne = new Coach("Туманов", "Пётр", "Васильевич");
        Coach coachTwo = new Coach("Алексеева", "Алёна", "Дмитриевна");
        Group group = new Group("Аэробика", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coachOne, DayOfWeek.MONDAY,
                new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachOne, DayOfWeek.WEDNESDAY,
                new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachTwo, DayOfWeek.FRIDAY,
                new TimeOfDay(15, 0)));

        List<CounterOfTrainings> counterOfTrainings = timetable.getCountByCoaches();
        assertEquals(2, counterOfTrainings.size(), "Всего тренеров - 2");
        assertEquals("Туманов", counterOfTrainings.get(0).getCoach().getSurname(),
                "Первым должен быть тренер с бОльшим количеством занятий");
        assertEquals(2, counterOfTrainings.get(0).getCount(), "У первого тренера - 2 тренировки");
        assertEquals(1, counterOfTrainings.get(1).getCount(), "У второго тренера - 1 тренировка");
    }

    @Test
    void testMultipleGroupsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        TimeOfDay timeOfDay = new TimeOfDay(14, 0);

        timetable.addNewTrainingSession(new TrainingSession(new Group("Легкая атлетика",
                Age.ADULT, 60), coach, DayOfWeek.MONDAY, timeOfDay));
        timetable.addNewTrainingSession(new TrainingSession(new Group("Румба",
                Age.CHILD, 45), coach, DayOfWeek.MONDAY, timeOfDay));

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, timeOfDay);

        assertEquals(2, sessions.size(), "В одно время может быть несколько групп");
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        Assertions.assertTrue(timetable.getCountByCoaches().isEmpty(), "Список тренеров должен быть пуст");
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY).isEmpty(),
                "Расписание на воскресенье должно быть пустым");
    }

}
