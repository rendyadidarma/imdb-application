@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.imdb_application.data.repository

import android.content.Context
import com.example.imdb_application.TestCoroutineRule
import com.example.imdb_application.data.local.database.DetailEntity
import com.example.imdb_application.data.local.database.MovieDao
import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.remote.api.APIService
import com.example.imdb_application.data.remote.dto.ItemsMovieContainer
import com.example.imdb_application.data.remote.dto.MovieDto
import com.example.imdb_application.data.remote.dto.MovieDtoSearch
import com.example.imdb_application.data.remote.dto.ResultsMovieContainer
import com.example.imdb_application.data.remote.dto.detail.DetailDto
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.NetworkResponseWrapper
import com.example.imdb_application.data.utils.MovieObjectMapper
import com.example.imdb_application.data.utils.NetworkChecker
import io.mockk.*
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.openMocks


class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepository
    @Mock
    private lateinit var databaseDao: MovieDao
    @Mock
    private lateinit var network: APIService
    @Mock
    private lateinit var context: Context

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        openMocks(this)
        mockkObject(NetworkChecker)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        repository = MovieRepositoryImpl(databaseDao, network, context)
    }



    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `test context`() {
        Assert.assertNotNull(context)
    }

    /**
     * getMovies
     * when network Unavailable AndDataNotEmpty
     * networkUnavailableAndDataEmpty
     * networkAvailable
     */
    @Test
    fun `test getMovies when network is unavailable and data not empty`() =
        testCoroutineRule.testDispatcher.runBlockingTest {

            val expected = flow {
                emit(
                    listOf(
                        MovieEntity(
                            id = "tempId",
                            fullTitle = "tempTitle",
                            image = "tempImage"
                        )
                    )
                )
            }

            every { NetworkChecker.isOnline(context) } returns false
            `when`(databaseDao.getMovies()).thenReturn(expected)

            val result = repository.getMovies(false)

            val expectedEqual = expected.map {
                NetworkResponseWrapper(
                    StateOnline.NetworkUnavailable,
                    MovieObjectMapper.mapMovieEntityListToMovieList(it)
                )
            }

            Assert.assertEquals(expectedEqual.first(), result.first())
        }

    @Test
    fun `test getMovies when network is unavailable and data empty`() =
        testCoroutineRule.testDispatcher.runBlockingTest {

            every { NetworkChecker.isOnline(context) } returns false

            val result = repository.getMovies(true)

            val expectedEqual = flow {
                emit(
                    NetworkResponseWrapper(
                        StateOnline.NetworkUnavailable,
                        null
                    )
                )
            }

            Assert.assertEquals(expectedEqual.first(), result.first())
        }

    @Test
    fun `test getMovies when network is available`() =
        testCoroutineRule.testDispatcher.runBlockingTest {

            val expected = ItemsMovieContainer(listOf(MovieDto()))
            every { NetworkChecker.isOnline(context) } returns true
            `when`(network.getMovieInTheaters()).thenReturn(expected)

            val result = repository.getMovies(false)

            val results = result.first().isInternetAvailable

            Assert.assertEquals(StateOnline.NetworkAvailable, results)
        }

    /**
     * searchMovie(keyword: String
     * network is available and not available
     * */

    @Test
    fun `test searchMovie when network is available`() =
        testCoroutineRule.testDispatcher.runBlockingTest {
            val keyword = "string"
            val listExpected = listOf<MovieDtoSearch>()
            every { NetworkChecker.isOnline(context) } returns true
            `when`(network.searchMovie(keyword)).thenReturn(ResultsMovieContainer(listExpected))

            val expected = NetworkResponseWrapper(StateOnline.NetworkAvailable, listExpected)
            val actual = repository.searchMovies(keyword).first()
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `test searchMovie when network is unavailable`() =
        testCoroutineRule.testDispatcher.runBlockingTest {
            val keyword = "string"
            every { NetworkChecker.isOnline(context) } returns false

            val expected = NetworkResponseWrapper(StateOnline.NetworkUnavailable, null)
            val actual = repository.searchMovies(keyword).first()
            Assert.assertEquals(expected, actual)
        }

    /**
     * isDatabaseEmpty true
     * isDatabaseEmpty false
     * */

    @Test
    fun `test isDatabaseEmpty return true`() = testCoroutineRule.testDispatcher.runBlockingTest {
        `when`(databaseDao.isMovieEmpty()).thenReturn(flowOf(true))

        val res = repository.isDatabaseEmpty().first()
        Assert.assertTrue(res)
    }

    @Test
    fun `test isDatabaseEmpty return false`() = testCoroutineRule.testDispatcher.runBlockingTest {
        `when`(databaseDao.isMovieEmpty()).thenReturn(flowOf(false))

        val res = repository.isDatabaseEmpty().first()
        Assert.assertFalse(res)
    }

    /**
     * isDetailEmpty true
     * isDetailEmpty false
     * */

    @Test
    fun `test isDetailEmpty return true`() = testCoroutineRule.testDispatcher.runBlockingTest {
        val id = "string"
        `when`(databaseDao.isDetailEmpty(id)).thenReturn(flowOf(true))

        val res = repository.isDetailEmpty(id).first()
        Assert.assertTrue(res)
    }

    @Test
    fun `test isDetailEmpty return false`() = testCoroutineRule.testDispatcher.runBlockingTest {
        val id = "string"
        `when`(databaseDao.isDetailEmpty(id)).thenReturn(flowOf(false))

        val res = repository.isDetailEmpty(id).first()
        Assert.assertFalse(res)
    }

    /**
     * getDetailNotReturnNetworkState
     * network is available and detail empty
     * network is unavailable and detail empty
     * detail not empty
     * */

    @Test
    fun `test getDetailNotReturnNetworkState when network is available and detail is empty`() =
        testCoroutineRule.testDispatcher.runBlockingTest {
            val id = "string"
            val dto = DetailDto()
            `when`(network.getDetail(id)).thenReturn(dto)
            every { NetworkChecker.isOnline(context) } returns true

            val res = repository.getDetailNotReturnNetworkState(true, id).first()

            val expected = MovieObjectMapper.mapDetailDtoToMovieDetail(dto)
            Assert.assertEquals(expected, res)
        }

    @Test
    fun `test getDetailNotReturnNetworkState when network is unavailable and detail is empty`() =
        testCoroutineRule.testDispatcher.runBlockingTest {
            val id = "string"
            every { NetworkChecker.isOnline(context) } returns false

            val res = repository.getDetailNotReturnNetworkState(true, id).first()

            val expected = null
            Assert.assertEquals(expected, res)
        }

    @Test
    fun `test getDetailNotReturnNetworkState when detail is not empty`() =
        testCoroutineRule.testDispatcher.runBlockingTest {
            val id = "string"
            val detailEntity = flowOf(DetailEntity())
            `when`(databaseDao.getDetail(id)).thenReturn(detailEntity)
            every { NetworkChecker.isOnline(context) } returns false

            val res = repository.getDetailNotReturnNetworkState(false, id).first()

            val expected =
                flowOf(MovieObjectMapper.mapDetailEntityToMovieDetail(detailEntity.first())).first()
            Assert.assertEquals(expected, res)
        }

    @Test
    fun `test getMovies insertAll to database when network is available`() = testCoroutineRule.testDispatcher.runBlockingTest {
        val lists =
            listOf(MovieDto(id = "1", title = "wakanda"), MovieDto(id = "2", title = "sopo jarwo"))
        val expected = ItemsMovieContainer(lists)
        every { NetworkChecker.isOnline(context) } returns true

        `when`(network.getMovieInTheaters()).thenReturn(expected)
        repository.getMovies(false)

        Mockito.verify(databaseDao).insertAll(MovieObjectMapper.mapMovieDtoToMovieEntity(expected.movies))
    }

    @Test
    fun `test getDetailNotReturnNetworkState insertDetail when network is available and detail is empty`() = testCoroutineRule.testDispatcher.runBlockingTest {
        val id = "string"
        val expected = DetailDto()
        every { NetworkChecker.isOnline(context) } returns true

        `when`(network.getDetail(id)).thenReturn(expected)
        repository.getDetailNotReturnNetworkState(true, id)

        Mockito.verify(databaseDao).insertDetail(MovieObjectMapper.mapDetailDtoToDetailEntity(expected))
    }

    @Test
    fun `test getAllDetailData return observable`() {
        val testObserver = TestObserver<List<DetailEntity>>()
        val temp : Observable<List<DetailEntity>> = Observable.just(listOf(DetailEntity()))
        `when`(databaseDao.getAllDetail()).thenReturn(temp)

        repository.getAllDetailData().subscribe(testObserver)

        testObserver.assertSubscribed()
    }

}