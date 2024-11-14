| Revision Number | Date       | Description of Changes | Author / Editor            | Communication of Change |
| --------------- | ---------- | ---------------------- | -------------------------- | ----------------------- |
| 1.0             | 2024-11-13 | Initial version        | Nirav Patel, Laurenz Gomez | Internal Announcement   |

# OCL Requirements of the Alouer System

## Table of Contents

- [com.alouer.collections](#comalouercollections)

  - [BookingCollection](#bookingcollection)
  - [ChildCollection](#childcollection)
  - [ClientCollection](#clientcollection)
  - [InstructorCollection](#instructorcollection)
  - [LessonCollection](#lessoncollection)
  - [LocationCollection](#locationcollection)

- [com.alouer.commands](#comalouercommands)

  - [admin](#admin)
    - [CreateLessonCommand](#createlessoncommand)
    - [DeleteUserCommand](#deleteusercommand)
    - [ViewDeleteLessonsCommand](#viewdeletelessonscommand)
  - [client](#client)
    - [AddDependentCommand](#adddependentcommand)
    - [CreateBookingCommand](#createbookingcommand)
    - [ViewEditBookingsCommand](#vieweditbookingscommand)
  - [instructor](#instructor)
    - [AcceptOfferingCommand](#acceptofferingcommand)
    - [ViewEditOfferingsCommand](#vieweditofferingscommand)
  - [registration](#registration)
    - [RegisterClientCommand](#registerclientcommand)
    - [RegisterInstructorCommand](#registerinstructorcommand)
    - [LogOutCommand](#logoutcommand)

- [com.alouer.factories](#comalouerfactories)

  - [CommandFactory](#commandfactory)
  - [RegistrationFactory](#registrationfactory)

- [com.alouer.models.lessonManagement](#comalouermodelslessonmanagement)

  - [Booking](#booking)
  - [Lesson](#lesson)

- [com.alouer.models](#comalouermodels)

  - [Administrator](#administrator)
  - [Child](#child)
  - [Client](#client)
  - [Instructor](#instructor)
  - [Location](#location)

- [com.alouer.utils](#comalouerutils)

  - [BackendUtils](#backendutils)
  - [ConsoleUtils](#consoleutils)
  - [DatabaseManager](#databasemanager)
  - [Session](#session)

- [com.alouer](#comalouer)
  - [Terminal](#terminal)

---

## OCL Requirements by Package

### com.alouer.collections

#### BookingCollection

1. A Booking should only be created if the clientId, lessonId, and childId are valid.

```
context BookingCollection::createBooking(clientId: Integer, lessonId: Integer, childId: Integer) : Integer
pre:
    -- Ensure that the lessonId and clientId exist and are valid.
    LessonCollection.getById(lessonId) <> null and
    ClientCollection.getById(clientId) <> null and
    -- Ensure that childId is valid if provided.
    (childId = null or ClientCollection.getById(childId) <> null)
post:
    -- Ensure that a new booking ID is created after the booking.
    self.getById(result) <> null
```

2. A booking must be validated before it is added to the collection.

```
context BookingCollection::add(booking: Booking) : Boolean
pre:
    -- The clientId and lessonId of the booking must exist and be valid.
    ClientCollection.getById(booking.clientId) <> null and
    LessonCollection.getById(booking.lessonId) <> null
post:
    -- The new booking must be successfully added and saved.
    self.getById(booking.id) <> null
```

3. A booking cannot be added if the client has already booked the same lesson.

```
context BookingCollection::validateBooking(selectedLessonId: Integer, clientId: Integer) : Boolean
pre:
-- Validate that the selected lesson exists.
LessonCollection.getById(selectedLessonId) <> null
post:
-- If a booking already exists for this client with the selected lesson, return false.
self.getByClientId(clientId)->forAll(b | b.lessonId <> selectedLessonId) and
self.getByLessonId(selectedLessonId)->forAll(b | b.clientId <> clientId)
```

4. A booking must ensure that no overlap occurs for a client’s schedule.

```
context BookingCollection::validateBooking(selectedLessonId: Integer, clientId: Integer) : Boolean
pre:
-- Ensure that the selected lesson and existing client bookings do not conflict.
selectedLessonId <> null and clientId <> null
post:
-- If the client has an existing booking with a time conflict, the booking is not valid.
self.getByClientId(clientId)->forAll(b |
let clientLesson : Lesson = LessonCollection.getById(b.lessonId) in
clientLesson.schedule->intersection(LessonCollection.getById(selectedLessonId).schedule)->isEmpty())
```

5. Bookings cannot overlap for private lessons.

```
context BookingCollection::validateBooking(selectedLessonId: Integer, clientId: Integer) : Boolean
pre:
-- Ensure the lesson is private.
LessonCollection.getById(selectedLessonId).type = LessonType.PRIVATE
post:
-- There should not be any existing bookings for the private lesson.
self.getByLessonId(selectedLessonId)->size() = 0
```

6. Deleting a booking should remove it from the database and all collections.

```
context BookingCollection::delete(id: Integer) : Boolean
pre:
-- Ensure that the booking with the provided id exists.
self.getById(id) <> null
post:
-- Ensure that the booking with the provided id is deleted.
self.getById(id) = null
```

#### ChildCollection

1. Adding a Child to the Collection should only occur if all fields (firstName, lastName, dateOfBirth, parentId) are provided.

```
context ChildCollection::add(child: Child) : Boolean
pre:
    -- Ensure all required fields of child are non-null and valid.
    child.firstName <> null and child.firstName <> '' and
    child.lastName <> null and child.lastName <> '' and
    child.dateOfBirth <> null and
    child.parentId <> null and ClientCollection.getById(child.parentId) <> null
post:
    -- Ensure that a new child is added and has a valid ID.
    self.getById(child.id) <> null
```

2. A Child cannot be added if a record already exists with the same parentId, firstName, lastName, and dateOfBirth.

```
context ChildCollection::validateChild(clientId: Integer, firstName: String, lastName: String, dateOfBirth: LocalDate) : Boolean
pre:
    -- Ensure all required fields are provided and valid.
    clientId <> null and ClientCollection.getById(clientId) <> null and
    firstName <> null and firstName <> '' and
    lastName <> null and lastName <> '' and
    dateOfBirth <> null
post:
    -- If a child with the same details exists, return false.
    self.getChildrenByClientId(clientId)->forAll(c |
        c.firstName <> firstName or
        c.lastName <> lastName or
        c.dateOfBirth <> dateOfBirth) implies result = false
```

3. Retrieving a Child by ID should return null if the child does not exist.

```
context ChildCollection::getById(id: Integer) : Child
pre:
    -- Ensure the id parameter is valid.
    id <> null and id > 0
post:
    -- Return null if no child with the specified ID exists in the collection.
    (self.getById(id) = null) implies result = null
```

4. Retrieving all Children associated with a specific clientId should only retrieve records with that parentId.

```
context ChildCollection::getChildrenByClientId(clientId: Integer) : List(Child)
pre:
    -- Ensure clientId is valid.
    clientId <> null and ClientCollection.getById(clientId) <> null
post:
    -- Ensure all retrieved children have the specified parentId.
    result->forAll(child | child.parentId = clientId)
```

5. Creating a new Child should return a valid ID only if the Child was successfully added.

```
context ChildCollection::createChild(clientId: Integer, firstName: String, lastName: String, dateOfBirth: LocalDate) : Integer
pre:
    -- Ensure all required parameters are provided and valid.
    clientId <> null and ClientCollection.getById(clientId) <> null and
    firstName <> null and firstName <> '' and
    lastName <> null and lastName <> '' and
    dateOfBirth <> null
post:
    -- Ensure that the new child has a valid ID if successfully created.
    let newChild : Child = ChildCollection.getById(result) in
    (newChild <> null and newChild.parentId = clientId and
    newChild.firstName = firstName and
    newChild.lastName = lastName and
    newChild.dateOfBirth = dateOfBirth) implies result > 0
```

6. Validation of a Child should confirm that a Child with the provided details exists for the specified clientId.

```
context ChildCollection::validateChild(clientId: Integer, firstName: String, lastName: String, dateOfBirth: LocalDate) : Boolean
pre:
    -- Ensure all required details are valid and provided.
    clientId <> null and ClientCollection.getById(clientId) <> null and
    firstName <> null and firstName <> '' and
    lastName <> null and lastName <> '' and
    dateOfBirth <> null
post:
    -- Ensure a matching child exists for the given clientId, returning true if found.
    result = (self.getChildrenByClientId(clientId)->exists(child |
        child.firstName = firstName and
        child.lastName = lastName and
        child.dateOfBirth = dateOfBirth))
```

7. Retrieving all Children should only return records from the 'child' table.

```
context ChildCollection::getChildren() : List(Child)
post:
    -- Ensure all children in the list are valid entries from the child table.
    result->forAll(child |
        child.firstName <> null and child.firstName <> '' and
        child.lastName <> null and child.lastName <> '' and
        child.dateOfBirth <> null and
        child.parentId <> null and
        ClientCollection.getById(child.parentId) <> null)
```

#### ClientCollection

1. A client can only be added to the collection if all required fields are provided and unique constraints are met.

```
context ClientCollection::add(client: Client) : Boolean
pre:
    -- The client object must contain non-null, unique fields for email.
    client.email <> null and
    client.password <> null and
    client.firstName <> null and
    client.lastName <> null and
    ClientCollection.getByEmail(client.email) = null
post:
    -- A new client is successfully added and retrievable by ID.
    self.getById(client.id) <> null

```

2. Fetching a client by ID should only proceed if the ID is valid.

```
context ClientCollection::getById(id: Integer) : Client
pre:
    -- Ensure that the provided ID is not null and exists.
    id <> null and self.getById(id) <> null
post:
    -- A valid client object is returned matching the ID.
    result.id = id
```

3. Fetching a client by email should only proceed if the email exists in the collection.

```
context ClientCollection::getByEmail(email: String) : Client
pre:
    -- Ensure that the email is provided and exists within the collection.
    email <> null and self.getByEmail(email) <> null
post:
    -- A client is returned with a matching email.
    result.email = email
```

4. Validating a client’s credentials should only return a client if email and password match an existing record.

```
context ClientCollection::validateCredentials(email: String, password: String) : Client
pre:
    -- Email and password must be provided.
    email <> null and password <> null and
    -- Ensure that the client with this email and password exists.
    ClientCollection.getByEmail(email) <> null and
    ClientCollection.getByEmail(email).password = password
post:
    -- A client is returned with the matching credentials.
    result.email = email and result.password = password
```

5. Deleting a client should only proceed if the client with the specified ID exists.

```
context ClientCollection::delete(id: Integer) : Boolean
pre:
    -- Ensure that the client with the given ID exists.
    self.getById(id) <> null
post:
    -- The client with the given ID should no longer exist after deletion.
    self.getById(id) = null
```

6. Retrieving all clients should return a collection that reflects all entries in the database.

```
context ClientCollection::getClients() : List(Client)
post:
    -- All clients in the database are represented in the returned collection.
    result->forAll(c | ClientCollection.getById(c.id) <> null)
```

#### InstructorCollection

1. An instructor can only be retrieved if the ID provided exists in the database.

```
context InstructorCollection::getById(id: Integer) : Instructor
pre:
    -- Ensure the provided instructor ID exists in the database.
    DatabaseManager.getConnection().prepareStatement(SELECT_INSTRUCTOR_BY_ID_SQL).executeQuery().next() = true
post:
    -- The result should be a valid Instructor object if the ID is valid.
    result <> null implies result.id = id
```

2. An instructor can only be retrieved by email if the email exists in the database.

```
context InstructorCollection::getByEmail(email: String) : Instructor
pre:
    -- Ensure the email provided corresponds to an existing instructor.
    DatabaseManager.getConnection().prepareStatement(SELECT_INSTRUCTOR_BY_EMAIL_SQL).executeQuery().next() = true
post:
    -- The result should be a valid Instructor object if the email exists.
    result <> null implies result.email = email
```

3. An instructor can only be added to the collection if the email does not already exist.

```
context InstructorCollection::add(instructor: Instructor) : Boolean
pre:
    -- Ensure the email is unique and does not already exist.
    DatabaseManager.getConnection().prepareStatement(SELECT_INSTRUCTOR_BY_EMAIL_SQL).executeQuery().next() = false
post:
    -- The instructor should be successfully added, and their ID set.
    result = true implies instructor.id <> null
```

4. A new instructor should have a unique ID after insertion.

```
context InstructorCollection::createInstructor(firstName: String, lastName: String, email: String, password: String, phoneNumber: String) : Boolean
pre:
    -- Ensure the instructor with the same email does not already exist.
    DatabaseManager.getConnection().prepareStatement(SELECT_INSTRUCTOR_BY_EMAIL_SQL).executeQuery().next() = false
post:
    -- After creation, the instructor should have a unique, generated ID.
    result = true implies InstructorCollection.getByEmail(email).id <> null
```

5. Credentials must be validated before returning an instructor instance.

```
context InstructorCollection::validateCredentials(email: String, password: String) : Instructor
pre:
    -- Ensure email and password match an existing instructor.
    DatabaseManager.getConnection().prepareStatement(VALIDATE_CREDENTIALS_SQL).executeQuery().next() = true
post:
    -- The result should be the instructor object if credentials are correct.
    result <> null implies result.email = email and result.password = password
```

6. An instructor can only be deleted if the ID provided exists.

```
context InstructorCollection::delete(id: Integer) : Boolean
pre:
    -- Ensure the instructor ID exists before attempting deletion.
    InstructorCollection.getById(id) <> null
post:
    -- The instructor should be removed from the collection after deletion.
    InstructorCollection.getById(id) = null
```

7. An instructor update should modify only the specified fields without affecting others.

```
context InstructorCollection::update(instructor: Instructor) : Boolean
pre:
    -- Ensure the instructor ID exists for updating.
    InstructorCollection.getById(instructor.id) <> null
post:
    -- Ensure the fields are updated as per the instructor parameter values.
    result = true implies (
        InstructorCollection.getById(instructor.id).firstName = instructor.firstName and
        InstructorCollection.getById(instructor.id).lastName = instructor.lastName and
        InstructorCollection.getById(instructor.id).email = instructor.email and
        InstructorCollection.getById(instructor.id).password = instructor.password and
        InstructorCollection.getById(instructor.id).phoneNumber = instructor.phoneNumber
    )
```

#### LessonCollection
1. The result of getLessons returns a set of lessons, and each lesson is valid
```
context LessonCollection::getLessons() : Set(Lesson)
inv: 
    self->forAll(lesson | lesson.title->notEmpty() and lesson.type <> null and lesson.locationId > 0)
```

2. The schedule for the lesson is sorted by day of the week and is not empty
```
context LessonCollection::getScheduleByLessonId(lessonId: Integer) : Set(DayOfWeek)
inv:
    result->forAll(day | day <> null) and result->size() > 0
```

3. If the lesson is added, it must have a valid title, start time, end time, and schedule
```
context LessonCollection::add(lesson: Lesson) : Boolean
inv:
    lesson.title->notEmpty() and lesson.startTime > lesson.endTime and
    lesson.schedule->notEmpty() and lesson.locationId > 0
```

4. There are no overlapping lessons at the same location
```
context LessonCollection::validateLesson(locationId: Integer, startTime: String, endTime: String, schedule: String) : Boolean
inv:
    let startMinutes: Integer = BackendUtils.convertTimeToMinutes(startTime)
    in let endMinutes: Integer = BackendUtils.convertTimeToMinutes(endTime)
    in
        getLessons()->forAll(existingLesson |
            existingLesson.locationId <> locationId or
            (existingLesson.schedule->intersect(BackendUtils.convertScheduleToDays(schedule))->notEmpty() implies
                startMinutes >= BackendUtils.convertTimeToMinutes(existingLesson.startTime) or
                endMinutes <= BackendUtils.convertTimeToMinutes(existingLesson.endTime)
            )
        )
```

5. Creating a lesson results in a valid lesson and schedule
```
context LessonCollection::createLesson(locationId: Integer, title: String, lessonType: LessonType, startTime: String, endTime: String, schedule: String) : Boolean
inv:
    self.add(Lesson.allInstances->last()) -- Adding the lesson must succeed
```

6. Unassigned lessons have no instructor assigned and are not booked
```
context LessonCollection::getUnassignedLessons(locationId: Integer) : Set(Lesson)
inv:
    result->forAll(lesson |
        lesson.assignedInstructorId = null and
        BookingCollection.getBookings()->forAll(booking | booking.lessonId <> lesson.id)
    )
```
7. Available lessons are assigned and either a group lesson or an unbooked private lesson
```
context LessonCollection::getAvailableLessons(locationId: Integer) : Set(Lesson)
inv:
    result->forAll(lesson |
        lesson.assignedInstructorId <> null and
        (lesson.type = LessonType.GROUP or
         (lesson.type = LessonType.PRIVATE and
          BookingCollection.getBookings()->forAll(booking | booking.lessonId <> lesson.id))
        )
    )
```

8. When a lesson is updated, all attributes (title, times, schedule) are properly changed and the update is successful
```
context LessonCollection::updateLesson(lesson: Lesson) : Boolean
inv:
    lesson.title->notEmpty() and lesson.startTime < lesson.endTime and
    lesson.schedule->notEmpty() and
    lesson.locationId > 0 and
    BookingCollection.getBookings()->forAll(booking | booking.lessonId <> lesson.id)
```

9. The result only includes lessons for the specific locationId
```
context LessonCollection::getLessonsByLocationId(locationId: Integer) : Set(Lesson)
inv:
    result->forAll(lesson | lesson.locationId = locationId)
```

#### LocationCollection
1. The add method must successfully insert a Location entity into the database.
```
context LocationCollection::add(location: Location) : Boolean
inv:
    -- A Location object must have a name (non-null and non-empty) before being added.
    location.name <> null and location.name <> '' implies
        -- The insert operation must succeed by updating the database and setting a generated ID.
        result = (location.id <> null)
```

2. The getLocations method must return a list of all available Location entities.
```
context LocationCollection::getLocations() : List(Location)
inv:
    -- The returned list of locations cannot be null.
    result <> null and
    -- Each location in the returned list must have a non-null name and valid attributes (address, city, etc.).
    result->forAll(l | l.name <> null and l.name <> '' and l.address <> null and l.city <> null and l.province <> null and l.postalCode <> null)
```

3. The getById method must retrieve a Location based on the given ID.
```
context LocationCollection::getById(id: Integer) : Location
inv:
    -- If a location is found with the given ID, the returned location must have a non-null name.
    (result <> null implies result.name <> null and result.name <> '') and
    -- If no location is found, the result must be null.
    (result = null implies id not in LocationCollection.getLocations()->select(l | l.id = id)->collect(l | l.id))
```

### com.alouer.commands

#### admin

##### CreateLessonCommand

##### DeleteUserCommand

##### ViewDeleteLessonsCommand

#### client

##### AddDependentCommand

##### CreateBookingCommand

##### ViewEditBookingsCommand

#### instructor

##### AcceptOfferingCommand

##### ViewEditOfferingsCommand

#### registration

##### RegisterClientCommand

##### RegisterInstructorCommand

##### LogOutCommand

### com.alouer.factories

#### CommandFactory

#### RegistrationFactory

### com.alouer.models.lessonManagement

#### Booking

#### Lesson

### com.alouer.models

#### Administrator

#### Child

#### Client

#### Instructor

#### Location

### com.alouer.utils

#### BackendUtils

#### ConsoleUtils

#### DatabaseManager

#### Session

### com.alouer

#### Terminal
