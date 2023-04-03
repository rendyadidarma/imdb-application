package com.example.imdb_application.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.imdb_application.TestCoroutineRule
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class DetailViewModelTest {
    private lateinit var viewModel: DetailViewModel
    private lateinit var repository: MovieRepository
    private val savedState = SavedStateHandle(mapOf("movieId" to "tt1630029"))


    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        repository = mock(MovieRepository::class.java)
        viewModel = DetailViewModel(savedState, repository)
    }

    @Test
    fun `test savedState`() {
        Assert.assertTrue(savedState.get<String>("movieId") == "tt1630029")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getDetailMovie() emit to StateFlow`() = testCoroutineRule.testDispatcher.runBlockingTest {
        val movieId = "tt1630029"
        val expected = flow {
            emit(false)
        }
        `when`(repository.isDetailEmpty(movieId)).thenReturn(expected)
        val expectedMovieDetail = flow {
            emit(MovieDetail("123"))
        }
        `when`(repository.getDetailNotReturnNetworkState(false, movieId)).thenReturn(expectedMovieDetail)

        viewModel.getDetailMovie()

        viewModel.detailMovie.test {
            val emission = awaitItem()
            Assert.assertEquals(MovieDetail( "123"), emission)
            cancelAndConsumeRemainingEvents()
        }
    }

}

