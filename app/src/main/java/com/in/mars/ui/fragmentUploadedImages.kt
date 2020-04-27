package com.`in`.mars.ui

import android.content.Context

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.`in`.mars.R
import com.`in`.mars.adapters.AdapterRecylerView
import com.netree.marketing.Room.GenericViewModel


class FragmentUploadedImages : Fragment() {

    private var rv: RecyclerView? = null
    private var adapter: AdapterRecylerView? = null
    private lateinit var vm: GenericViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.lyt_uploaded_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.rv)
        rv!!.setLayoutManager(GridLayoutManager(context, 2))
        adapter = AdapterRecylerView(context!!)
        rv!!.adapter = adapter
        setViewModel()
    }

    private fun setViewModel() {
        activity?.let {
            vm =
                ViewModelProvider(this).get<GenericViewModel>(GenericViewModel::class.java)
        }
        vm.getAllUrls()
            .observe(viewLifecycleOwner, Observer<List<Url>> { list ->
                adapter!!.setData(list!!)
            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()

    }

    companion object {
        fun newInstance(): FragmentUploadedImages {
            val frg = FragmentUploadedImages()
            return frg
        }

    }


}