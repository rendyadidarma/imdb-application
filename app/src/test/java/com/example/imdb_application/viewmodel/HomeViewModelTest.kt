package com.example.imdb_application.viewmodel

import app.cash.turbine.test
import com.example.imdb_application.TestCoroutineRule
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.sealed.StateLoad
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.NetworkResponseWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
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
    fun `test refreshDataListMovie emit success to SharedFlow`() = runBlocking {

        val databaseTestEmpty = false
        val flowOfDatabaseEmpty = flowOf(databaseTestEmpty)

        `when`(repository.isDatabaseEmpty()).thenReturn(flowOfDatabaseEmpty)

        val listMovie =
            listOf(Movie("Avatar", "tt12345", "image1"), Movie("Creed 6", "tt67890", "image2"))
        val response = NetworkResponseWrapper(StateOnline.NetworkUnavailable, listMovie)
        val flowOfMovieList = flowOf(response)
        `when`(repository.getMovies(false)).thenReturn(
            flowOfMovieList
        )

        viewModel.movieList.test {
            viewModel.refreshDataListMovie()
            val loadingState = awaitItem()
            Assert.assertEquals(StateLoad.Loading, loadingState.state)
            Assert.assertEquals(null, loadingState.value)
            val successState = awaitItem()
            Assert.assertEquals(StateLoad.Success, successState.state)
            Assert.assertEquals(listMovie, successState.value)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test refreshDataListMovie emit Error to SharedFlow`() = runBlocking {

        val databaseTestEmpty = true
        val flowOfDatabaseEmpty = flowOf(databaseTestEmpty)

        `when`(repository.isDatabaseEmpty()).thenReturn(flowOfDatabaseEmpty)

        val response : NetworkResponseWrapper<List<Movie>> = NetworkResponseWrapper(StateOnline.NetworkUnavailable, null)

        val flowOfMovieList = flow {
            emit(response)
        }
        `when`(repository.getMovies(true)).thenReturn(flowOfMovieList)

        viewModel.movieList.test {
            viewModel.refreshDataListMovie()
            val loadingState = awaitItem()
            Assert.assertEquals(StateLoad.Loading, loadingState.state)
            Assert.assertEquals(null, loadingState.value)
            val errorState = awaitItem()
            Assert.assertEquals(StateLoad.Error, errorState.state)
            Assert.assertEquals(null, errorState.value)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test refreshDataListMovie emit Error Exception to SharedFlow`() = runBlocking {

        val databaseTestEmpty = true
        val flowOfDatabaseEmpty = flowOf(databaseTestEmpty)

        `when`(repository.isDatabaseEmpty()).thenReturn(flowOfDatabaseEmpty)

        `when`(repository.getMovies(true)).thenAnswer { Exception() }

        viewModel.movieList.test{
            viewModel.refreshDataListMovie()
            val loadingState = awaitItem()
            Assert.assertEquals(StateLoad.Loading, loadingState.state)
            Assert.assertEquals(null, loadingState.value)
            val errorState = awaitItem()
            Assert.assertEquals(StateLoad.Error, errorState.state)
            Assert.assertEquals(null, errorState.value)
            cancelAndConsumeRemainingEvents()
        }
    }

}


