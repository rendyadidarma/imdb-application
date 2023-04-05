package com.example.imdb_application.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.imdb_application.data.local.database.DetailEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.utils.MovieObjectMapper
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.openMocks

class HistoryViewModelTest {

    private lateinit var viewModel: HistoryViewModel
    @Mock private lateinit var repository : MovieRepository
    @Mock private lateinit var mockObserverLiveData : Observer<List<Movie>?>
    @Mock private lateinit var context: Context

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()


    @Before
    fun setup() {
        openMocks(this)
        viewModel = HistoryViewModel(repository, context)
    }

    @Test
    fun `test getAllDetail from repository emitted to detailList with data`() {
        val observableFromRepo = Observable.just(listOf(DetailEntity()))
        `when`(repository.getAllDetailData()).thenReturn(observableFromRepo)

        viewModel.getAllDetail()

        Assert.assertEquals(MovieObjectMapper.mapDetailEntityListToMovieList(listOf(DetailEntity())), viewModel.detailList.value)
    }

    @Test
    fun `test getAllDetail from repository emitted to detailList without data or null`() {
        `when`(repository.getAllDetailData()).thenReturn(Observable.error(Throwable()))

        viewModel.getAllDetail()

        Assert.assertEquals(null, viewModel.detailList.value)
    }

//    @Test
//    fun `test getAllDetail from repository emitted to detailList without data or null`() {
//        `when`(repository.getAllDetailData()).thenReturn(Observable.error(Throwable()))
//
//        viewModel.detailList.observeForever(mockObserverLiveData)
//
//        viewModel.getAllDetail()
//
//        verify(mockObserverLiveData, times(0)).onChanged(any())
//    }

}

class RxImmediateSchedulerRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}