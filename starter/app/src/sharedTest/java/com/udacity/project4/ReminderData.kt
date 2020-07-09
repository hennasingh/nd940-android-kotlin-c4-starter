package com.udacity.project4

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

object ReminderData {

    fun getEmptyTitle(): ReminderDataItem {
        return ReminderDataItem(
            title = "",
            description = "Test Writing",
            location = "Googleplex",
            latitude = 37.422004,
            longitude = -122.086246
        )
    }

    fun getSample(): ReminderDataItem {
        return ReminderDataItem(
            title = "Online MOOC",
            description = "Free programming courses",
            location = "Udacity",
            latitude = 37.399437,
            longitude = -122.108060
        )
    }

    fun getEmptyLocation(): ReminderDataItem {
        return ReminderDataItem(
            title = "Some Title",
            description = "Some description",
            location = "",
            latitude = 37.422004,
            longitude = -122.086246
        )
    }

    fun getReminderDTO(): ReminderDTO {
        return ReminderDTO(
            title = "Android Testing",
            description = "ReminderListFragment",
            location = "Ireland",
            latitude = 37.422004,
            longitude = -122.086246
        )
    }
}