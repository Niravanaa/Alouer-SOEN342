| Revision Number | Date       | Description of Changes | Author / Editor            | Communication of Change |
| --------------- | ---------- | ---------------------- | -------------------------- | ----------------------- |
| 1.0             | 2024-11-13 | Initial version        | Nirav Patel, Laurenz Gomez | Internal Announcement   |

# OCL Requirements of the Alouer System

## Table of Contents

- [Unique Offerings](#unique-offerings)
- [Underage Dependents](#underage-dependents)
- [Instructor's City Availabilities](#instructors-city-availabilities)
- [Non-overlapping Bookings](#non-overlapping-bookings)

## Unique Offerings

### Verbose

Offerings are unique. In other words, multiple offerings on the same day and time slot must be offered at a different location.

### Technical

```
context LessonCollection
inv:
    self.lessons -> forAll(l1, l2 | (l1 <> l2 and l1.schedule = l2.schedule and l1.startTime = l2.startTime and l2.endTime = l2.endTime) implies l1.locationId <> l2.locationId)
```

## Underage Dependents

### Verbose

Any client who is underage must necessarily be accompanied by an adult who acts as their guardian.

### Technical

```
context BookingCollection
inv:
    self.bookings -> forAll(booking | booking.childId <> null implies booking.clientId <> null)
```

## Instructor's City Availabilities

### Verbose

The city associated with an offering must be one of the cities that the instructor has indicated in their availabilities.

### Technical

```
context Lesson
inv:
    self.location.city in self.instructor.availabilities.cities
```

## Non-overlapping Bookings

### Verbose

Client does not have multiple bookings on the same day and time slot.” (for simplicity we consider only identical day and time slots, even though in reality a booking on Monday 3pm – 4pm and another also on Monday 3:30pm – 4:30pm should not be acceptable.)

### Technical

```
context Client
inv:
    self.bookings -> forAll(b1, b2 |
        b1 <> b2 and b1.lesson.startTime = b2.lesson.startTime and b1.lesson.endTime = b2.lesson.endTime implies b1.lesson.schedule <> b2.lesson.schedule
    )

inv:
    self.bookings -> forAll(b1, b2 |
        b1 <> b2 and b1.lesson.schedule = b2.lesson.schedule implies b1.lesson.startTime <> b2.lesson.startTime and b1.lesson.endTime <> b2.lesson.endTime
    )
```
