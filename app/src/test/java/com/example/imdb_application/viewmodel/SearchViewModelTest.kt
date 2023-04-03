@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.imdb_application.viewmodel

import com.example.imdb_application.TestCoroutineRule
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.NetworkResponseWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SearchViewModelTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SearchViewModel(repository)
    }

    /**
     * handleSearchResult
     * network available & data available
     * network available & data unavailable
     * network unavailable & data available or data unavailable
     */

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun `fetch search data catch error`() = testCoroutineRule.testDispatcher.runBlockingTest {

        `when`(repository.searchMovies("string")).thenAnswer {throw Exception()}

        viewModel.fetchSearchData("string")

        Assert.assertEquals(STATUS.NO_INET, viewModel.searchStatus.value)
        Assert.assertEquals(viewModel.searchData.value, listOf<List<Movie>>())
    }

    @Test
    fun `fetch search data then exception catch status error`() = runTest {
        `when`(repository.searchMovies("string")).thenAnswer{ throw Exception() }
        viewModel.fetchSearchData("string")

    }


    @Test
    fun `network available and data available handleSearchResult`() = testCoroutineRule.testDispatcher.runBlockingTest {
        val network = StateOnline.NetworkAvailable
        val data = listOf(Movie("Title", "Id", "Image"))
        val networkWrapper = NetworkResponseWrapper(network, data)

        `when`(repository.searchMovies("string")).thenReturn(flowOf(networkWrapper))

        viewModel.fetchSearchData("string")
        Assert.assertTrue(viewModel.searchStatus.value == STATUS.HAS_DATA)
        Assert.assertEquals(viewModel.searchData.value, data)
    }

    @Test
    fun `network available and data unavailable handleSearchResult`() = testCoroutineRule.testDispatcher.runBlockingTest {
        val network = StateOnline.NetworkAvailable
        val data = null
        `when`(repository.searchMovies("string")).thenReturn(flowOf(NetworkResponseWrapper(network, data)))

        viewModel.fetchSearchData("string")

        Assert.assertTrue(viewModel.searchStatus.value == STATUS.NO_DATA)
        Assert.assertEquals(viewModel.searchData.value, listOf<List<Movie>>())
    }

    @Test
    fun `network unavailable and data available or data unavailable handleSearchResult`() = testCoroutineRule.testDispatcher.runBlockingTest {
        val network = StateOnline.NetworkUnavailable
        val data = listOf(Movie("Title", "Id", "Image"))
        `when`(repository.searchMovies("string")).thenReturn(flowOf(NetworkResponseWrapper(network, data)))

        viewModel.fetchSearchData("string")

        Assert.assertTrue(viewModel.searchStatus.value == STATUS.NO_INET)
        Assert.assertEquals(viewModel.searchData.value, listOf<List<Movie>>())
    }


}