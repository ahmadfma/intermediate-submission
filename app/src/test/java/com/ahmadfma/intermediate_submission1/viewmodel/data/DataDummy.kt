package com.ahmadfma.intermediate_submission1.viewmodel.data

import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem

object DataDummy {

    fun generateDummyStories() : List<ListStoryItem> {
        val newList = arrayListOf<ListStoryItem>()
        for(i in 0..10) {
            newList.add(ListStoryItem(
                id = i.toString(),
                photoUrl = "url",
                name = "Ahmad $i",
                createdAt = "2022-01-08T06:34:18.598Z",
                description = "desc $i"
            ))
        }
        return newList
    }

}