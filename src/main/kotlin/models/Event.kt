package models

data class Event(
    var eventName: String,
    var eventDescription: String,
    var eventLocation: String,
    var eventDate: String,
    var eventTime: String,
    var eventPrice: Int,
    var isBooked: Boolean
)
