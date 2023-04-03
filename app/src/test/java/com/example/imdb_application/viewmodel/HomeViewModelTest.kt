package com.example.imdb_application.viewmodel

import app.cash.turbine.test
import com.example.imdb_application.TestCoroutineRule
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.sealed.StateLoad
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.NetworkResponseWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: MovieRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        repository = mock(MovieRepository::class.java)
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `fetch sharedFlow item`() {

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test refreshDataListMovie emit to SharedFlow`() = testCoroutineRule.testDispatcher.runBlockingTest {

        val databaseTestEmpty = false
        val flowOfDatabaseEmpty = flowOf(databaseTestEmpty)
        `when`(repository.isDatabaseEmpty()).thenReturn(flowOfDatabaseEmpty)

        val listMovie =
            listOf(Movie("Avatar", "tt12345", "image1"), Movie("Creed 6", "tt67890", "image2"))
        val response = NetworkResponseWrapper(StateOnline.NetworkUnavailable, listMovie)
        val flowOfMovieList = flowOf(response)
        `when`(repository.getMovies(databaseTestEmpty)).thenReturn(
            flowOfMovieList
        )

        viewModel.movieList.test {
            val emission = awaitItem()
            Assert.assertEquals(StateLoad.Error, emission.state)
            cancelAndConsumeRemainingEvents()
        }

        viewModel.refreshDataListMovie()

    }
}


