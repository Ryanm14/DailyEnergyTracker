package me.ryanmiles.dailyenergytracker.entrylist

import io.realm.RealmList
import me.ryanmiles.dailyenergytracker.capture
import me.ryanmiles.dailyenergytracker.data.cache.EntryRepository
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.Entry
import me.ryanmiles.dailyenergytracker.data.model.HourlyEntry
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Unit tests for the implementation of {@link EntryListPresenter}
 */
class EntryListPresenterTest {

    companion object {
        private val ENTRIES = arrayListOf(Entry("04/19/2018", "Description1", RealmList(HourlyEntry("3:20 pm", "Test", 5))),
                Entry("02/24/2018", "Description2"))

        private val EMPTY_ENTRIES = ArrayList<Entry>(0)
    }

    @Mock
    private lateinit var entryRepository: EntryRepository

    @Mock
    private lateinit var entryListView: EntryListContract.View

    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private lateinit var loadEntriesCallbackCaptor: ArgumentCaptor<EntryDataSource.LoadEntriesCallback>

    private lateinit var entryListPresenter: EntryListPresenter

    @Before
    fun setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)
        entryListPresenter = EntryListPresenter(
                entryRepository, entryListView)
        // The presenter wont't update the view unless it's active.
        `when`(entryListView.isActive).thenReturn(true)
    }

    @Test
    fun createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test

        // Then the presenter is set to the view
        verify(entryListView).presenter = entryListPresenter
    }


    @Test
    fun loadEntriesFromRepositoryAndLoadIntoView() {
        // Given an initialized NotesPresenter with initialized notes
        // When loading of Notes is requested
        entryListPresenter.loadEntriesFromStart(true)

        // Callback is captured and invoked with stubbed notes
        verify(entryRepository).getEntries(capture(loadEntriesCallbackCaptor))
        loadEntriesCallbackCaptor.value.onEntriesLoaded(ENTRIES)

        // Then progress indicator is hidden and notes are shown in UI
        verify(entryListView).showEntries(ENTRIES)
    }

    @Test
    fun clickOnFab_ShowsAddsEntryUi() {
        // When adding a new Entry
        entryListPresenter.addNewEntry()

        // Then add Entry UI is shown
        verify(entryListView).showAddEntry()
    }
}