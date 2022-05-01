package controllers

import models.Event
import persistence.Serializer
import utils.Utilities.isValidListIndex

/**
 * This class provides ArrayList methods for the Main.kt to function
 *
 * @author Pawel Tetera (20070573)
 * @version v1.0
 */

class EventAPI(serializerType: Serializer) {
    private var events = ArrayList<Event>()
    private var serializer: Serializer = serializerType

    /**
     * Adds an item entry to an ArrayList
     *
     * @param event Adds the parameter as item to the ArrayList
     * @return Returns a modified ArrayList collection
     */

    fun add(event: Event): Boolean {
        return events.add(event)
    }

    /**
     * Counts the number of unbooked items in the ArrayList
     *
     * @param () Lists all unbooked events
     * @return Returns the number of yet to be booked events
     */
    fun numberOfBookableEvents(): Int = events.count { event: Event -> !event.isBooked }

    /*
     * Counts the number of booked items in the ArrayList
     */
    private fun numberOfBookedEvents(): Int = events.count { event: Event -> event.isBooked }

    /**
     * Counts the number of items in the ArrayList
     *
     * @param () Lists number of ArrayList items
     * @return Returns the number items
     */
    fun numberOfEvents(): Int = events.size

    /**
     * Lists all event entries
     *
     * @param () Lists all ArrayList items
     * @return Returns all events in array or an empty list response
     */
    fun listAllEvents(): String =
        if (events.isEmpty()) "No events stored"
        else formatListString(events)

    /**
     * Lists all events which are available for booking
     *
     * @param () List of all bookable events
     * @return Returns all bookable Arraylist entries or an empty list response
     */
    fun listBookableEvents(): String =
        if (numberOfBookableEvents() == 0) "No bookable events stored"
        else formatListString(events.filter { event -> !event.isBooked })

    /**
     * Lists all booked events
     *
     * @param () List of all booked events
     * @return Returns all booked Arraylist entries or an empty list response
     */
    fun listBookedEvents(): String =
        if (numberOfBookedEvents() == 0) "No booked events stored"
        else formatListString(events.filter { event -> event.isBooked })

    /*
     * Finds an event entry for the specified index
     */
    private fun findEvent(index: Int): Event? {
        return if (isValidListIndex(index, events)) {
            events[index]
        } else null
    }

    /**
     * Update method to modify an event entry without having to create a new one
     *
     * @param indexToUpdate Uses the passed through index to modify an event entry
     * @param event The Arraylist to use
     * @return Returns the modified event entry
     */
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
    /**
     * Delete an event by its index
     *
     * @param indexToDelete Takes an index and deletes the chosen event entry
     * @return Returns an updated entry list with the specified entry deleted
     */
    fun deleteEvent(indexToDelete: Int): Event? {
        return if (isValidListIndex(indexToDelete, events)) {
            events.removeAt(indexToDelete)
        } else null
    }

    /**
     * Validates the passed index is a valid ArrayList entry
     *
     * @param index Takes an index and validates it
     * @return Returns the validation result as a boolean
     */
    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, events)
    }

    /**
     * Checks if the specified index is an available event for booking or not
     *
     * @param indexToBook Takes an index and confirms its booking status
     * @return Returns the booking status result as a boolean
     */
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

    /**
     * Searches the event list for entries by name
     *
     * @param searchString Takes a name as a string
     * @return Returns a new list containing the searched field
     */
    fun searchByName(searchString: String) =
        formatListString(
            events.filter { event -> event.eventName.contains(searchString, ignoreCase = true) }
        )

    /**
     * Searches the event list for entries by location
     *
     * @param searchString Takes a location as a string
     * @return Returns a new list containing the searched field
     */
    fun searchByLocation(searchString: String) =
        formatListString(
            events.filter { event -> event.eventLocation.contains(searchString, ignoreCase = true) }
        )

    /**
     * Searches the event list for entries by date
     *
     * @param searchString Takes a date as a string
     * @return Returns a new list containing the searched field
     */
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
