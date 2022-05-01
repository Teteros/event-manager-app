package controllers

import models.Event
import persistence.Serializer
import utils.Utilities.isValidListIndex

class EventAPI(serializerType: Serializer) {
    private var events = ArrayList<Event>()
    private var serializer: Serializer = serializerType

    fun add(event: Event): Boolean {
        return events.add(event)
    }

    fun numberOfBookableEvents(): Int = events.count { event: Event -> !event.isBooked }
    private fun numberOfBookedEvents(): Int = events.count { event: Event -> event.isBooked }
    fun numberOfEvents(): Int = events.size

    fun listAllEvents(): String =
        if (events.isEmpty()) "No events stored"
        else formatListString(events)

    fun listBookableEvents(): String =
        if (numberOfBookableEvents() == 0) "No bookable events stored"
        else formatListString(events.filter { event -> !event.isBooked })

    fun listBookedEvents(): String =
        if (numberOfBookedEvents() == 0) "No booked events stored"
        else formatListString(events.filter { event -> event.isBooked })

    private fun findEvent(index: Int): Event? {
        return if (isValidListIndex(index, events)) {
            events[index]
        } else null
    }

    fun updateEvent(indexToUpdate: Int, event: Event?): Boolean {
        val foundEvent = findEvent(indexToUpdate)
        if ((foundEvent != null) && (event != null)) {
            foundEvent.eventName = event.eventName
            foundEvent.eventDescription = event.eventDescription
            foundEvent.eventLocation = event.eventLocation
            foundEvent.eventDate = event.eventDate
            foundEvent.eventTime = event.eventTime
            foundEvent.eventPrice = event.eventPrice
            return true
        }
        return false
    }

    fun deleteEvent(indexToDelete: Int): Event? {
        return if (isValidListIndex(indexToDelete, events)) {
            events.removeAt(indexToDelete)
        } else null
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, events)
    }

    fun eventToBook(indexToBook: Int): Boolean {
        if (isValidIndex(indexToBook)) {
            val eventToBook = events[indexToBook]
            if (!eventToBook.isBooked) {
                eventToBook.isBooked = true
                return true
            }
        }
        return false
    }

    fun searchByName(searchString: String) =
        formatListString(
            events.filter { event -> event.eventName.contains(searchString, ignoreCase = true) }
        )

    fun searchByLocation(searchString: String) =
        formatListString(
            events.filter { event -> event.eventLocation.contains(searchString, ignoreCase = true) }
        )

    fun searchByDate(searchString: String) =
        formatListString(
            events.filter { event -> event.eventDate.contains(searchString, ignoreCase = true) }
        )

    private fun formatListString(eventsToFormat: List<Event>): String =
        eventsToFormat
            .joinToString(separator = "\n") { event ->
                events.indexOf(event).toString() + ": " + event.toString()
            }

    @Throws(Exception::class)
    @Suppress("UNCHECKED_CAST")
    fun load() {
        events = serializer.read() as ArrayList<Event>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(events)
    }
}
