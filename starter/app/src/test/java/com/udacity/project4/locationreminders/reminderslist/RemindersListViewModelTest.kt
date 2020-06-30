package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.getOrAwaitValue
import com.udacity.project4.locationreminders.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //Set the main coroutines dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeDataSource: FakeDataSource

    //subject under test
    private lateinit var reminderListViewModel: RemindersListViewModel

    @Before
    fun setupViewModel() = mainCoroutineRule.runBlockingTest {

        //do not use Koin in testing
        stopKoin()

        //Initialize the data source with no reminders
        fakeDataSource = FakeDataSource()

        reminderListViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeDataSource
        )
    }

    @Test
    fun loadReminders_loading() = mainCoroutineRule.runBlockingTest {

        //Pause Dispatcher to verify initial values
        mainCoroutineRule.pauseDispatcher()
        reminderListViewModel.loadReminders()

        //assert that the progress indicator is shown
        assertThat(reminderListViewModel.showLoading.getOrAwaitValue(), `is`(true))

        //Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        //Then assert that the indicator is hidden
        assertThat(reminderListViewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadRemindersWhenReminderUnavailable_showError() = mainCoroutineRule.runBlockingTest {
        //Make the data source return error
        fakeDataSource.setReturnError(true)
        reminderListViewModel.loadReminders()

        assertThat(reminderListViewModel.showSnackBar.getOrAwaitValue(), `is`("Test Exception"))
    }

    @Test
    fun loadReminders_noReminder_showNoData() = mainCoroutineRule.runBlockingTest {
        reminderListViewModel.loadReminders()

        assertThat(reminderListViewModel.remindersList.getOrAwaitValue().isEmpty(), `is`(true))
        assertThat(reminderListViewModel.showNoData.getOrAwaitValue(), `is`(true))
    }


}