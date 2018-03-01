package com.jakdor.labday.view.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.jakdor.labday.R
import com.jakdor.labday.common.model.Event
import com.jakdor.labday.common.model.Speaker
import com.jakdor.labday.common.model.Timetable
import com.jakdor.labday.databinding.FragmentEventBinding

import com.jakdor.labday.di.InjectableFragment
import com.jakdor.labday.rx.RxResponse
import com.jakdor.labday.rx.RxStatus
import com.jakdor.labday.view.utils.GlideApp
import com.jakdor.labday.viewmodel.EventViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Fragment displaying event info after
 */
class EventFragment : Fragment(), InjectableFragment {

    var viewModel: EventViewModel? = null
    lateinit var binding: FragmentEventBinding

    private lateinit var timetable: Timetable

    private var testMode = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            timetable = arguments!!.getSerializable("timetable") as Timetable
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_event, container, false)

        binding.eventTitleBar.setNavigationOnClickListener { _ -> activity?.onBackPressed() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(viewModel == null){
            viewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(EventViewModel::class.java)
        }

        observeData()
        viewModel?.loadAppData(context, timetable.eventId)
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        if (testMode) return
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setShowHideAnimationEnabled(false)
        actionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (testMode) return
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.show()
    }

    /**
     * Observe Event/Speaker pair
     */
    fun observeData() {
        viewModel?.eventSpeakerPair?.observe(
                this, Observer<RxResponse<Pair<Event?, Speaker?>>> { response ->
            if (response != null) {
                this.processResponse(response)
            } else {
                Log.e(CLASS_TAG, "RxResponse returned null")
            }
        })
    }

    /**
     * Checks received data
     */
    fun processResponse(response: RxResponse<Pair<Event?, Speaker?>>) {
        if (response.status == RxStatus.SUCCESS) {
            if(response.data?.first != null && response.data.second != null) {
                loadView(response.data.first!!, response.data.second!!)
            }
        } else {
            if (response.error != null) {
                Log.e(CLASS_TAG, response.error.toString())
            }
        }
    }

    fun loadView(event: Event, speaker: Speaker) {
        binding.event = event
        binding.speaker = speaker

        if(!event.img.isEmpty()) {
            GlideApp.with(this)
                    .load(event.img)
                    .centerCrop()
                    .transition(withCrossFade())
                    .into(binding.imgToolbar)

            binding.imgToolbar.setOnClickListener {
                switchToImgFragment(event.img, event.name)
            }
        }

        if(!speaker.speakerImg.isEmpty()) {
            GlideApp.with(this)
                    .load(speaker.speakerImg)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_foreground)
                    .error(R.mipmap.ic_launcher_foreground)
                    .into(binding.eventHostCard?.eventHostImage)

            binding.eventHostCard?.eventHostImage?.setOnClickListener { _ ->
                switchToImgFragment(speaker.speakerImg, speaker.speakerName)
            }
        }

        if(!event.dor1Img.isEmpty()) {
            GlideApp.with(this)
                    .load(event.dor1Img)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_foreground)
                    .error(R.mipmap.ic_launcher_foreground)
                    .transition(withCrossFade())
                    .into(binding.eventDoorsItem?.door1)

            binding.eventDoorsItem?.doorCard1?.setOnClickListener { _ ->
                switchToImgFragment(event.dor1Img, getString(R.string.entrence_info))
            }
        }

        if(!event.dor2Img.isEmpty()) {
            GlideApp.with(this)
                    .load(event.dor2Img)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_foreground)
                    .error(R.mipmap.ic_launcher_foreground)
                    .transition(withCrossFade())
                    .into(binding.eventDoorsItem?.door2)

            binding.eventDoorsItem?.doorCard2?.setOnClickListener { _ ->
                switchToImgFragment(event.dor2Img, getString(R.string.entrence_info))
            }
        }

        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.GERMAN)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT+1")
        val start = Date((timetable.timeStart).toLong() * 1000)
        val end = Date((timetable.timeEnd).toLong() * 1000)

        binding.time = simpleDateFormat.format(start) + " - " + simpleDateFormat.format(end)
    }

    fun switchToImgFragment(imgUrl: String, title: String){
        if (activity != null) {
            activity!!.supportFragmentManager.beginTransaction()
                    .addToBackStack(ImageFragment.CLASS_TAG)
                    .replace(R.id.fragmentLayout, ImageFragment.newInstance(imgUrl, title))
                    .commit()
        }
    }

    companion object {
        const val CLASS_TAG: String = "EventFragment"

        fun newInstance(timetable: Timetable): EventFragment {
            val eventTimetable = EventFragment()

            val args = Bundle()
            args.putSerializable("timetable", timetable)
            eventTimetable.arguments = args

            return eventTimetable
        }
    }

}