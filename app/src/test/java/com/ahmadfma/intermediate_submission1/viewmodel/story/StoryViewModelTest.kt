package com.ahmadfma.intermediate_submission1.viewmodel.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import com.ahmadfma.intermediate_submission1.ui.adapter.StoryAdapter
import com.ahmadfma.intermediate_submission1.MainCoroutineRule
import com.ahmadfma.intermediate_submission1.DataDummy
import com.ahmadfma.intermediate_submission1.viewmodel.StoryViewModel
import com.ahmadfma.intermediate_submission1.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyViewModel: StoryViewModel
    private val dummyStories = DataDummy.generateDummyStories()

    @Test
    fun `when getStories should not null`() = mainCoroutineRule.runBlockingTest {
        val expectedStories = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStories.value = PagingData.from(dummyStories)
        `when`(storyViewModel.stories).thenReturn(expectedStories)

        val actualStories = storyViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher,
        )
        differ.submitData(actualStories)
        advanceUntilIdle()

        Mockito.verify(storyViewModel).stories
        Assert.assertNotNull(actualStories)
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
    }

    @Test
    fun `when getStoriesWithLocation stories should has latitude and longitude`() = mainCoroutineRule.runBlockingTest {
        val expectedStories = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStories.value = PagingData.from(dummyStories)
        `when`(storyViewModel.stories).thenReturn(expectedStories)

        val actualStories = storyViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher,
        )
        differ.submitData(actualStories)
        advanceUntilIdle()

        Mockito.verify(storyViewModel).stories
        Assert.assertNotNull(actualStories)
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertTrue(differ.snapshot()[0]?.latitude != null && differ.snapshot()[0]?.longitude != null)
    }

    @Test
    fun `when image and description is not empty addNewStories should success`() {
        val expectedMessage = "Add Story Success"
        val expectedResponse = MessageResponse(false, expectedMessage)
        val expectedValue = MutableLiveData<Result<MessageResponse?>>()
        expectedValue.value = Result.Success(expectedResponse)

        val file = File("testing")
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        val description = "testing description".toRequestBody()

        `when`(storyViewModel.addNewStories(imageMultipart, description)).thenReturn(expectedValue)
        val actualValue = storyViewModel.addNewStories(imageMultipart, description).getOrAwaitValue()

        Mockito.verify(storyViewModel).addNewStories(imageMultipart, description)

        Assert.assertTrue(actualValue is Result.Success)
        Assert.assertEquals(expectedMessage, (actualValue as Result.Success).data?.message)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}