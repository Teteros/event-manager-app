import mu.KotlinLogging
import utils.ScannerInput.readNextInt
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main() {
    runMenu()
}

fun runMenu() {
    do {
        when (mainMenu()) {
            1 -> addEvent()
            2 -> listEvents()
            3 -> updateEvent()
            4 -> deleteEvent()
            5 -> bookVenue()
            6 -> searchEvent()
            7 -> save()
            8 -> load()
            0 -> exitApp()
            else -> logger.error {"Please choose an option from 0-8"}
        }
    } while (true)
}

fun mainMenu(): Int {
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

}

fun listEvents() {

}

fun updateEvent() {

}

fun deleteEvent() {

}

fun bookVenue() {

}

fun searchEvent() {

}

fun save() {

}

fun load() {

}

fun exitApp() {
    logger.info {"exitApp() function invoked"}
    exitProcess(0)
}