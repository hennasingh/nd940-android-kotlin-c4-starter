package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    //    TODO: Add testing implementation to the RemindersLocalRepository.kt
    private lateinit var database: RemindersDatabase
    private lateinit var remindersRepository: RemindersLocalRepository

    private lateinit var reminder: ReminderDTO

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDB() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        remindersRepository = RemindersLocalRepository(database.reminderDao(), Dispatchers.Main)

        reminder = ReminderDTO(
            title = "AndroidX",
            description = "Reminder DAO Test",
            location = "Udacity",
            latitude = 37.399437,
            longitude = -122.108060
        )
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveReminder_retrievesReminder() = runBlocking {
        //GIVEN - A new reminder is saved in the database
        remindersRepository.saveReminder(reminder)

        //WHEN - Reminder retrived by ID
        val result = remindersRepository.getReminder(reminder.id)

        //THEN - Same reminder is returned
        assertThat(result as Result.Success, `is`(true))
        assertThat(result.data.title, `is`(reminder.title))
        assertThat(result.data.description, `is`(reminder.description))
        assertThat(result.data.location, `is`(reminder.location))
        assertThat(result.data.latitude, `is`(reminder.latitude))
        assertThat(result.data.longitude, `is`(reminder.longitude))
    }

    @Test
    fun deleteReminder_returnEmptyList() = runBlocking {
        //GIVEN - Retrieve Reminders
        val reminder = remindersRepository.getReminders() as Result.Success
        assertThat(reminder.data, hasItem(reminder))

        //WHEN - Delete all reminders
        remindersRepository.deleteAllReminders()

        //THEN - Returns an empty list
        val result = remindersRepository.getReminders() as Result.Success
        assertThat(result.data.isEmpty(), `is`(true))
    }

    @Test
    fun incorrectReminder_returnsError() = runBlocking {
        val incorrectReminder =
            remindersRepository.getReminder(UUID.randomUUID().toString()) as Result.Error
        assertThat(incorrectReminder.message, `is`("Reminder Not Found"))
    }


}