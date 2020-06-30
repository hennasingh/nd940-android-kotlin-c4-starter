package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var reminder: ReminderDTO

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
        reminder = ReminderDTO(
            title = "AndroidX",
            description = "Reminder DAO Test",
            location = "Udacity",
            latitude = 37.399437,
            longitude = -122.108060
        )
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertReminderAndGetId() = runBlockingTest {
        //GIVEN - insert a reminder
        database.reminderDao().saveReminder(reminder)

        //WHEN - Get the reminder by id from the database
        val loaded = database.reminderDao().getReminderById(reminder.id)

        //THEN - the loaded data contains the expected values
        assertThat(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(reminder.id))
        assertThat(loaded.title, `is`(reminder.title))
        assertThat(loaded.description, `is`(reminder.description))
        assertThat(loaded.location, `is`(reminder.location))
        assertThat(loaded.latitude, `is`(reminder.latitude))
        assertThat(loaded.longitude, `is`(reminder.longitude))

    }

    @Test
    fun getIncorrectReminderAndReturnsNull() = runBlockingTest {
        //GIVEN - check for reminder
        assertThat(database.reminderDao().getReminders(), notNullValue())

        //WHEN - Get an incorrect from the database
        val reminder = database.reminderDao().getReminderById(UUID.randomUUID().toString())

        //THEN - the reminder is not found
        assertThat(reminder, nullValue())
    }


}