package me.ryanmiles.dailyenergytracker.singleentrygraph


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import me.ryanmiles.dailyenergytracker.R


/**
 * A simple [Fragment] subclass.
 */
class SingleEntryGraphFragment : Fragment(), SingleEntryGraphContract.View {

    override lateinit var presenter: SingleEntryGraphContract.Presenter
    override var isActive = false
        get() = isAdded

    private lateinit var graph: TextView

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_single_entry_graph, container, false)
        with(root) {

        }
        setHasOptionsMenu(true)
        return root
    }

    override fun setGraph() {

    }

    override fun showEmptyEntryError() {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_addeditfragment, menu)
    }


    companion object {
        const val ARGUMENT_GRAPH_ENTRY_ID = "GRAPH_ENTRY_ID"

        fun newInstance(entryId: String?) =
                SingleEntryGraphFragment().apply {
                    arguments = Bundle().apply {
                        putString(SingleEntryGraphFragment.ARGUMENT_GRAPH_ENTRY_ID, entryId)
                    }
                }
    }
}