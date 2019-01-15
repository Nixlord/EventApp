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


class EventFragment : Fragment() {

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
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        events.add(Event("@string/pool_party",
            format.parse("2019-02-08"),
            "10:00 AM",
            "1:00 PM",
            "Palm Heights Gymkhana, Bhubaneswar",
            R.drawable.poolparty,
            "Come in blue and white and enjoy our party with great delight!!")
        )

        events.add(Event("@string/haldi",
            format.parse("2019-02-08"),
            "4:00 PM",
            "7:30 PM",
            "Palm Heights Gymkhana, Bhubaneswar",
            R.drawable.haldi,
            "Together, lets make yellow an auspicious color for the bride.")
        )

        events.add(Event("@string/mehendi",
            format.parse("2019-02-09"),
            "8:00 AM",
            "12:30 PM",
            "Palm Heights Gymkhana, Bhubaneswar",
            R.drawable.mehendi,
            "Darker the color, darker is his love. Lets shower our blessings on this dove.")
        )

        events.add(Event("@string/wedding",
            format.parse("2019-02-10"),
            "7:00 AM",
            "12:30 PM",
            "Mayfair Convention, Bhubaneswar",
            R.drawable.wedding,
            "Shaaadiii Shaadiiiiiiiii")
        )


        events.add(Event("@string/reception",
            format.parse("2019-02-14"),
            "7:30 PM",
            "7:30 PM",
            "Sri Venkateshwara Swamy Kalyana Mandapam, SriNagar Colony, Hyderabad",
            R.drawable.reception,
            "Let's bless the newly married couple for a long and a happy life together forever.")
        )

        return events
    }

    class EventAdapter(val eventList: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

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

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(event : Event) {
                itemView.event_header.text = event.name
                Glide.with(itemView).load(event.image).into(itemView.event_image)
                itemView.event_time.text = event.eventTime.toString()
                itemView.event_food.text = event.foodTime.toString()
                itemView.event_address.text = event.location
                itemView.event_message.text = event.message

                val calendar = Calendar.getInstance()
                calendar.time = event.date
                itemView.event_year.text = calendar.get(Calendar.YEAR).toString()
                itemView.event_month.text = calendar.get(Calendar.MONTH).toString()
                itemView.event_date.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            }
        }
    }
}
