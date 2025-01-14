package com.zionhuang.music.models

import androidx.lifecycle.MediatorLiveData
import com.zionhuang.music.R
import com.zionhuang.music.constants.ORDER_NAME
import com.zionhuang.music.models.base.IMutableSortInfo
import com.zionhuang.music.extensions.getApplication
import com.zionhuang.music.extensions.preference
import com.zionhuang.music.extensions.preferenceLiveData

object PreferenceSortInfo : IMutableSortInfo {
    override var type by getApplication().preference(R.string.pref_sort_type, ORDER_NAME)
    override var isDescending by getApplication().preference(R.string.pref_sort_descending, true)
    private val typeLiveData = getApplication().preferenceLiveData(R.string.pref_sort_type, ORDER_NAME)
    private val isDescendingLiveData = getApplication().preferenceLiveData(R.string.pref_sort_descending, true)

    override val liveData = MediatorLiveData<SortInfo>().apply {
        addSource(typeLiveData) {
            value = SortInfo(typeLiveData.value, isDescendingLiveData.value)
        }
        addSource(isDescendingLiveData) {
            value = SortInfo(typeLiveData.value, isDescendingLiveData.value)
        }
    }
}