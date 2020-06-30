package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.ReminderData
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.getOrAwaitValue
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    //Set the main coroutines dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeDataSource: FakeDataSource

    //subject under test
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    private lateinit var context: Application


    @Before
    fun setupViewModel() = mainCoroutineRule.runBlockingTest {
        //do not use Koin in testing
        stopKoin()

        //Initialize the data source with no reminders
        fakeDataSource = FakeDataSource()
        context = ApplicationProvider.getApplicationContext()

        saveReminderViewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeDataSource
        )
    }

    @Test
    fun saveReminder_checkLoading() = mainCoroutineRule.runBlockingTest {
        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(ReminderData.getSample())
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(true))

        //Hide Loading
        mainCoroutineRule.resumeDispatcher()
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun saveReminder_showToast() {
        saveReminderViewModel.saveReminder(ReminderData.getSample())
        assertEquals(
            saveReminderViewModel.showToast.getOrAwaitValue(),
            context.getString(R.string.reminder_saved)
        )

        assertEquals(
            saveReminderViewModel.navigationCommand.getOrAwaitValue(),
            NavigationCommand.Back
        )
    }

    @Test
    fun validateEnteredReminderData() {
        //Valid Data
        assertThat(
            saveReminderViewModel.validateEnteredData(ReminderData.getSample()),
            `is`(true)
        )

        //Empty Title
        assertThat(
            saveReminderViewModel.validateEnteredData(ReminderData.getEmptyTitle()),
            `is`(false)
        )

        assertThat(
            saveReminderViewModel.showSnackBarInt.getOrAwaitValue(),
            `is`(R.string.err_enter_title)
        )

        //Empty Location
        assertThat(
            saveReminderViewModel.validateEnteredData(ReminderData.getEmptyLocation()),
            `is`(false)
        )
        assertThat(
            saveReminderViewModel.showSnackBarInt.getOrAwaitValue(),
            `is`(R.string.err_select_location)
        )
    }
}