import controllers.EventAPI
import models.Event
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.Utilities
import utils.Utilities.isValidString
import java.io.File
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
//private val eventAPI = EventAPI(XMLSerializer(File("events.xml")))
private val eventAPI = EventAPI(JSONSerializer(File("events.json")))

fun main() {
    runMenu()
}

fun runMenu() {
    //logger.info { "runMenu() function invoked" }
    do when (mainMenu()) {
        1 -> addEvent()
        2 -> listEventMenu()
        3 -> updateEvent()
        4 -> deleteEvent()
        5 -> bookEvent()
        6 -> searchEventMenu()
        7 -> save()
        8 -> load()
        0 -> exitApp()
        else -> logger.error {"Please choose an option from 0-8"}
    } while (true)
}

fun mainMenu(): Int {
    //logger.info { "mainMenu() function invoked" }
    return readNextInt(
        """
         > -------------------------
         > |   EVENT MANAGER APP   |
         > -------------------------
         > |   1) Add Event        |
         > |   2) List all Events  |
         > |   3) Update Event     |
         > |   4) Delete Event     |
         > -------------------------
         > |   5) Book a Venue     |
         > |   6) Search Events    |
         > -------------------------
         > |   7) Save             |
         > |   8) Load             |
         > -------------------------
         > |   0) Exit             |
         > -------------------------
         > ==>> """.trimMargin(">")
    )
}

fun addEvent() {
    logger.info { "addEvent() function invoked" }

    var eventName = readNextLine("Enter a name for the event: ")
    while (!isValidString(eventName))
        eventName = readNextLine("Please enter a name for the event: ")

    var eventDescription = readNextLine("Enter a description for the event: ")
    while (!isValidString(eventDescription))
        eventDescription = readNextLine("Please enter a description for the event: ")

    var eventLocation = readNextLine("Enter the location for the event: ")
    while (!isValidString(eventLocation))
        eventLocation = readNextLine("Please enter the location for the event: ")

    var eventDate = readNextLine("Enter the date for the event: ")
    while (!isValidString(eventDate))
        eventDate = readNextLine("Please enter the date for the event: ")

    var eventTime = readNextLine("Enter the time for the event: ")
    while (!isValidString(eventTime))
        eventTime = readNextLine("Please enter the time for the event: ")

    var eventPrice = readNextInt("Enter the ticket price for the event: ")
    while (Utilities.isValidInteger(eventPrice))
        eventPrice = readNextInt("Please enter a valid ticket price [e.g. 29]: ")

    val isAdded = eventAPI.add(Event(eventName, eventDescription, eventLocation, eventDate, eventTime, eventPrice, false))

    if (isAdded) logger.info{ "Added Successfully" } else logger.info { "Add Failed" }
}

fun listEventMenu() {
    logger.info { "listEventMenu() function invoked" }
    if (eventAPI.numberOfEvents() > 0) {
        val option = readNextInt(
            """
               -------------------------------
               |   1) View All Events        |
               |   2) View Bookable Events   |
               |   3) View Booked Events     |
               -------------------------------
               ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> println(eventAPI.listAllEvents())
            2 -> println(eventAPI.listBookableEvents())
            3 -> println(eventAPI.listBookedEvents())
            else -> logger.error{"Invalid option entered: $option"}
        }
    } else logger.error { "Option Invalid - No events stored" }
}

fun updateEvent() {
    logger.info { "updateEvent() function invoked" }
    listEventMenu()
    if (eventAPI.numberOfEvents() > 0) {
        val indexToUpdate = readNextInt("Enter the index of the event you want to update: ")
        if (eventAPI.isValidIndex(indexToUpdate)) {
            var eventName = readNextLine("Enter a name for the event to update: ")
            while (!isValidString(eventName))
                eventName = readNextLine("Please enter a name for the event: ")

            var eventDescription = readNextLine("Enter a description for the event to update: ")
            while (!isValidString(eventDescription))
                eventDescription = readNextLine("Please enter a description for the event: ")

            var eventLocation = readNextLine("Enter the location for the event to update: ")
            while (!isValidString(eventLocation))
                eventLocation = readNextLine("Please enter the location for the event: ")

            var eventDate = readNextLine("Enter the date for the event to update: ")
            while (!isValidString(eventDate))
                eventDate = readNextLine("Please enter the date for the event: ")

            var eventTime = readNextLine("Enter the time for the event to update: ")
            while (!isValidString(eventTime))
                eventTime = readNextLine("Please enter the time for the event: ")

            var eventPrice = readNextInt("Enter the ticket price for the event to update: ")
            while (Utilities.isValidInteger(eventPrice))
                eventPrice = readNextInt("Please enter a valid ticket price [e.g. 29]: ")

            if (eventAPI.updateEvent(indexToUpdate, Event(eventName, eventDescription, eventLocation, eventDate, eventTime, eventPrice, false))) {
                logger.info{"Update Successful"}
            } else {
                logger.info{"Update Failed"}
            }
        } else {
            logger.error("There are no events for this index number")
        }
    }
}

fun deleteEvent() {
    logger.info { "deleteEvent() function invoked" }
    println(eventAPI.listAllEvents())
    if (eventAPI.numberOfEvents()> 0) {
        val indexToDelete = readNextInt("Enter the index of the event to delete: ")
        val eventToDelete = eventAPI.deleteEvent(indexToDelete)
        if (eventToDelete != null) {
            logger.info {"Delete Successful! Deleted event: ${eventToDelete.eventName}" }
        } else {
            logger.error { "Delete Not Successful" }
        }
    } else logger.error { "Option invalid - No events stored" }
}

fun bookEvent() {
    //logger.info { "bookEvent() function invoked" }
    println(eventAPI.listBookableEvents())
    if (eventAPI.numberOfBookableEvents()> 0) {
        val indexToBook = readNextInt("Enter the index of the event you wish to book: ")
        if (eventAPI.eventToBook(indexToBook)) {
            logger.info { "Booking Successful" }
        } else {
            logger.error { "Booking Not Successful" }
        }
    } else logger.error { "Option invalid - No events stored" }
}

fun searchEventMenu() {
    if (eventAPI.numberOfEvents() > 0) {
        val option = readNextInt(
            """
                  > ---------------------------------
                  > |   1) Search by name           |
                  > |   2) Search by location       |
                  > |   3) Search by date           |
                  > ---------------------------------
         > ==>> """.trimMargin(">")
        )
        when (option) {
            1 -> searchByName()
            2 -> searchByLocation()
            3 -> searchByDate()
            else -> logger.error { "Invalid option entered: $option" }
        }
    } else {
        logger.error { "Option invalid - No events stored" }
    }
}

fun searchByName() {
    var searchName = readNextLine("Enter the event name to search by: ")
    while (!isValidString(searchName)) {
        searchName = readNextLine("Please enter a valid name: ")
    }
    val searchResults = eventAPI.searchByName(searchName)
    if (searchResults.isEmpty()) {
        println("Name not Found")
    } else {
        println(searchResults)
    }
}

fun searchByLocation() {
    var searchLocation = readNextLine("Enter the location to search by: ")
    while (!isValidString(searchLocation)) {
        searchLocation = readNextLine("Please enter a valid location: ")
    }
    val searchResults = eventAPI.searchByLocation(searchLocation)
    if (searchResults.isEmpty()) {
        println("Location not found")
    } else {
        println(searchResults)
    }
}

fun searchByDate() {
    var searchByDate = readNextLine("Enter the date to search by: ")
    while (!isValidString(searchByDate)) {
        searchByDate = readNextLine("Please enter a valid date: ")
    }
    val searchResults = eventAPI.searchByDate(searchByDate)
    if (searchResults.isEmpty()) {
        println("Date not found")
    } else {
        println(searchResults)
    }
}

fun save() {
    logger.info {"save() function invoked"}
    try {
        eventAPI.store()
    } catch (e: Exception) {
        logger.error { "Error writing to file: $e" }
    }
}

fun load() {
    logger.info {"load() function invoked"}
    try {
        eventAPI.load()
    } catch (e: Exception) {
        logger.error { "Error reading from file: $e" }
    }
}

fun exitApp() {
    logger.info {"exitApp() function invoked"}
    exitProcess(0)
}
