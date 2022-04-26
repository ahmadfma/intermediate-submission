package com.ahmadfma.intermediate_submission1.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.ahmadfma.intermediate_submission1.data.DataDummy
import com.ahmadfma.intermediate_submission1.data.FakeApiService
import com.ahmadfma.intermediate_submission1.data.FakeStoryDao
import com.ahmadfma.intermediate_submission1.data.local.StoryDao
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import com.ahmadfma.intermediate_submission1.data.remote.ApiService
import com.ahmadfma.intermediate_submission1.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.lang.Exception
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
class StoryRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: ApiService
    private lateinit var storyDao: StoryDao

    @Before
    fun setUp() {
        apiService = FakeApiService()
        storyDao = FakeStoryDao()
    }

    @Test
    fun `getStoriesWithLocation should return stories that has latitude and longitude`() = runTest {
        val expectedValue = DataDummy.generateDummyStories()
        val actualValue = apiService.getStories("token", 1, 1)
        Assert.assertNotNull(actualValue)
        Assert.assertEquals(expectedValue.size, actualValue.body()?.listStory?.size)
        Assert.assertTrue(actualValue.body()?.listStory?.get(0)?.latitude != null)
    }

    @Test
    fun `getStoriesWithPaging should return stories`() = runTest {
        val stories = DataDummy.generateDummyStories()
        storyDao.insertStories(stories)
        val actualValue = storyDao.getStories().getData()
        Assert.assertNotNull(actualValue)
        Assert.assertTrue(actualValue.isNotEmpty())
    }

    @Test
    fun `when no data, getStoriesWithPaging should return emptyList`() {
        val expectedValue = arrayListOf<ListStoryItem>()
        val actualValue = storyDao.getStories().getData()
        Assert.assertNotNull(actualValue)
        Assert.assertTrue(actualValue.isEmpty())
        Assert.assertEquals(expectedValue.size, actualValue.size)
    }

    @Test
    fun `when image and desc is not empty addStories should success`() = runTest {
        val expectedResponse = MessageResponse(false, FakeApiService.SUCCESS)
        val file = File("testing")
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        val description = "testing description".toRequestBody()
        val actualValue = apiService.addNewStories(image = imageMultipart, description = description)
        Assert.assertNotNull(actualValue)
        Assert.assertEquals(expectedResponse, actualValue.body())
    }

    private fun <PaginationKey: Any, Model: Any>PagingSource<PaginationKey, Model>.getData(): List<Model> {
        val data = mutableListOf<Model>()
        val latch = CountDownLatch(1)
        val job = mainCoroutineRule.launch {
            val loadResult: PagingSource.LoadResult<PaginationKey, Model> = this@getData.load(
                PagingSource.LoadParams.Refresh(
                    key = null, loadSize = Int.MAX_VALUE, placeholdersEnabled = false
                )
            )
            when (loadResult) {
                is PagingSource.LoadResult.Error -> throw loadResult.throwable
                is PagingSource.LoadResult.Page -> data.addAll(loadResult.data)
                is PagingSource.LoadResult.Invalid -> throw Exception("LoadResult.Invalid")
            }
            latch.countDown()
        }
        latch.await()
        job.cancel()
        return data
    }
}