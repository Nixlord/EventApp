package com.example.overlord.eventapp.main.event


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import java.io.Serializable
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.R.id.event_header
import com.example.overlord.eventapp.model.Event
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_event_item.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.example.overlord.eventapp.R.id.toast_icon
import com.example.overlord.eventapp.base.BaseFragment


class EventFragment : BaseFragment() {

    //Declare your data here
    class FragmentInputs(val firstName: String = "Diksha", val surname: String = "Agarwal") : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onButtonPressed(message: String)
    }

    private var inputs: FragmentInputs? = null
    private var interactor: FragmentInteractor? = null

    companion object {
        @JvmStatic
        fun newInstance(inputs: FragmentInputs?, interactor: FragmentInteractor) =
            EventFragment().apply {
                this.interactor = interactor
                arguments = Bundle().apply { putSerializable("inputs", inputs) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputs = arguments?.getSerializable("inputs") as FragmentInputs?
        //Initialize Heavier things here because onCreateView and onViewCreated are called much more number of times
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        val events = createEventList()

        event_recycler_view.adapter = EventAdapter(events)
    }

    private fun createEventList() : ArrayList<Event> {
        val events = ArrayList<Event>()
        val format = SimpleDateFormat("yyyy-mm-dd")

        events.add(Event("Pool Party",
            format.parse("2019-02-08"),
            "10:00 AM onwards",
            "Lunch at 1:00 PM",
            "Palm Heights Gymkhana, Bhubaneswar",
            R.drawable.poolparty,
            "Come in blue and white and enjoy our party with great delight!!")
        )

        events.add(Event("Haldi",
            format.parse("2019-02-08"),
            "4:00 PM onwards",
            "Dinner at 7:30 PM",
            "Palm Heights Gymkhana, Bhubaneswar",
            R.drawable.haldi,
            "Together, lets make yellow an auspicious color for the bride.")
        )

        events.add(Event("Mehendi",
            format.parse("2019-02-09"),
            "8:00 AM onwards",
            "Lunch at 12:30 PM",
            "Palm Heights Gymkhana, Bhubaneswar",
            R.drawable.mehendi,
            "Darker the color, darker is his love. Lets shower our blessings on this dove.")
        )

        events.add(Event("Wedding",
            format.parse("2019-02-10"),
            "7:00 AM onwards",
            "Lunch at 12:30 PM",
            "Mayfair Convention, Bhubaneswar",
            R.drawable.wedding,
            "Shaaadiii Shaadiiiiiiiii")
        )


        events.add(Event("Reception",
            format.parse("2019-02-14"),
            "7:30 PM onwards",
            "Dinner at 7:30 PM",
            "Sri Venkateshwara Swamy Kalyana Mandapam, SriNagar Colony, Hyderabad",
            R.drawable.reception,
            "Let's bless the newly married couple for a long and a happy life together forever.")
        )

        return events
    }

    inner class EventAdapter(val eventList: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_event_item, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
            holder.bindItems(eventList[position])
        }

        override fun getItemCount(): Int {
            return eventList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(event : Event) {
                itemView.event_header.text = event.name
                Glide.with(itemView).load(event.image).into(itemView.event_image)
                itemView.event_time.text = event.eventTime
                itemView.event_food.text = event.foodTime
                itemView.event_address.text = event.location
                itemView.event_message.text = event.message

                val calendar = Calendar.getInstance()
                calendar.time = event.date
                itemView.event_year.text = calendar.get(Calendar.YEAR).toString()
                itemView.event_month.text = "Feb"
                itemView.event_date.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

                itemView.map_icon.setOnClickListener {
                    val uri = "http://maps.google.co.in/maps?q=${event.location}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(intent)
                }
            }
        }
    }
}
