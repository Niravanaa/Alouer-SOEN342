| Revision Number | Date       | Description of Changes       | Author / Editor            | Communication of Change |
| --------------- | ---------- | ---------------------------- | -------------------------- | ----------------------- |
| 1.0             | 2024-10-16 | Initial version              | Nirav Patel, Laurenz Gomez | Internal Announcement   |
| 1.1             | 2024-10-22 | Refactor operation contracts | Nirav Patel                | Internal Announcement   |
| 1.2             | 2024-11-03 | Refactor operation contracts | Nirav Patel, Laurenz Gomez | Internal Announcement   |
| 1.3             | 2024-11-13 | Refactor operation contracts | Nirav Patel, Laurenz Gomez | Internal Announcement   |

# Table of Contents

- [Administrator Use Cases](#administrator-use-cases)

  - [Create Lesson](#create-lesson)
    - [Operation Contract for `enterCreateLessonCommandId`](#operation-contract-for-entercreatelessoncommandid)
    - [Operation Contract for `enterLocationId(locationId)`](#operation-contract-for-enterlocationidlocationid)
    - [Operation Contract for `enterLessonType(lessonType)`](#operation-contract-for-enterlessontypelessontype)
    - [Operation Contract for `enterStartTime(startTime)`](#operation-contract-for-enterstarttimestarttime)
    - [Operation Contract for `enterEndTime(endTime)`](#operation-contract-for-enterendtimeendtime)
    - [Operation Contract for `enterTitle(title)`](#operation-contract-for-entertitletitle)
    - [Operation Contract for `enterSchedule(schedule)`](#operation-contract-for-enterscheduleschedule)
    - [Operation Contract for `enterConfirmation(confirmation)`](#operation-contract-for-enterconfirmationconfirmation)
  - [Delete User Account](#delete-user-account)
    - [Operation Contract for `enterDeleteUserAccountCommandId`](#operation-contract-for-enterdeleteuseraccountcommandid)
    - [Operation Contract for `enterUserType(userType)`](#operation-contract-for-enterusertypeusertype)
    - [Operation Contract for `enterUserId(userId)`](#operation-contract-for-enteruseriduserid)
    - [Operation Contract for `confirmDeletion(confirmation)`](#operation-contract-for-confirmdeletionconfirmation)
  - [View or Delete Lessons](#view-or-delete-lessons)
    - [Operation Contract for `enterViewDeleteLessonsCommand`](#operation-contract-for-enterviewdeletelessonscommand)
    - [Operation Contract for `enterLocationId(locationId)`](#operation-contract-for-enterlocationidlocationid)
    - [Operation Contract for `enterLessonId(lessonId)`](#operation-contract-for-enterlessonidlessonid)
    - [Operation Contract for `enterResponse(response)`](#operation-contract-for-enterresponseresponse)

- [Instructor Use Cases](#instructor-use-cases)
  - [Register as Instructor](#register-as-instructor)
    - [Operation Contract for `enterRegisterInstructorCommandId()`](#operation-contract-for-enterregisterinstructorcommandid)
    - [Operation Contract for `enterFirstName(firstName)`](#operation-contract-for-enterfirstnamefirstname)
    - [Operation Contract for `enterLastName(lastName)`](#operation-contract-for-enterlastnamelastname)
    - [Operation Contract for `enterEmail(email)`](#operation-contract-for-enteremailemail)
    - [Operation Contract for `enterPassword(password)`](#operation-contract-for-enterpasswordpassword)
  - [Accept Offering](#accept-offering)
    - [Operation Contract for `enterAcceptOfferingCommandId()`](#operation-contract-for-enteracceptofferingcommandid)
    - [Operation Contract for `enterLocationId(locationId)`](#operation-contract-for-enterlocationidlocationid-1)
    - [Operation Contract for `enterLessonId(lessonId)`](#operation-contract-for-enterlessonidlessonid)
  - [View or Edit Offerings](#view-or-edit-offerings)
    - [Operation Contract for `enterViewEditOfferingsCommandId()`](#operation-contract-for-entervieweditofferingscommandid)
    - [Operation Contract for `enterLessonId(lessonId)`](#operation-contract-for-enterlessonidlessonid-1)
    - [Operation Contract for `enterResponse(response)`](#operation-contract-for-enterresponseresponse)
- [Client Use Cases](#client-use-cases)
  - [Register as Client](#register-as-client)
    - [Operation Contract for `enterRegisterClientCommandId()`](#operation-contract-for-enterregisterclientcommandid)
    - [Operation Contract for `enterFirstName(firstName)`](#operation-contract-for-enterfirstnamefirstname-1)
    - [Operation Contract for `enterLastName(lastName)`](#operation-contract-for-enterlastnamelastname-1)
    - [Operation Contract for `enterEmail(email)`](#operation-contract-for-enteremailemail-1)
    - [Operation Contract for `enterPassword(password)`](#operation-contract-for-enterpasswordpassword-1)
  - [Add Dependent](#add-dependent)
    - [Operation Contract for `enterAddDependentCommandId()`](#operation-contract-for-enteradddependentcommandid)
    - [Operation Contract for `enterFirstName(firstName)`](#operation-contract-for-enterfirstnamefirstname-2)
    - [Operation Contract for `enterLastName(lastName)`](#operation-contract-for-enterlastnamelastname-2)
    - [Operation Contract for `enterDateOfBirth(dateOfBirth)`](#operation-contract-for-enterdateofbirthdateofbirth)
  - [Create Booking](#create-booking)
    - [Operation Contract for `enterCreateBookingCommandId()`](#operation-contract-for-entercreatebookingcommandid)
    - [Operation Contract for `enterLocationId(locationId)`](#operation-contract-for-enterlocationidlocationid-2)
    - [Operation Contract for `enterLessonId(lessonId)`](#operation-contract-for-enterlessonidlessonid-1)
    - [Operation Contract for `enterChildId(childId)`](#operation-contract-for-enterchildidchildid)
  - [View or Edit Bookings](#view-or-edit-bookings)
    - [Operation Contract for `enterViewEditBookingsCommandId()`](#operation-contract-for-entervieweditbookingscommandid)
    - [Operation Contract for `enterBookingId(bookingId)`](#operation-contract-for-enterbookingidbookingid)
    - [Operation Contract for `enterResponse(response)`](#operation-contract-for-enterresponseresponse)

# Administrator Use Cases

## Create Lesson

### Operation Contract for `enterCreateLessonCommandId()`

| Contract CO1         | enterCreateLessonCommandId                                                                                                                                                        |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterCreateLessonCommandId()                                                                                                                                                      |
| **Cross References** | Use Case : Create Lesson                                                                                                                                                          |
| **Preconditions**    | 1. The administrator is logged into the system.<br> 2. The list of possible commands is displayed to the administrator. <br> 3. The request to enter the command ID is displayed. |
| **Postconditions**   | 1. The system gets all locations and displays the list to the administrator. <br> 2. The system requests the user to enter a location ID.                                         |

### Operation Contract for `enterLocationId(locationId)`

| Contract CO2         | enterLocationId                                                                                                                                                                               |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterLocationId(locationId: Integer)                                                                                                                                                          |
| **Cross References** | Use Case : Create Lesson                                                                                                                                                                      |
| **Preconditions**    | 1. The administrator has entered the "Create Lesson" command. <br> 2. The list of locations has been displayed to the administrator. <br> 3. The request to enter a location ID is displayed. |
| **Postconditions**   | 1. The system validates the location ID. <br> 2. The system requests the user to enter the lesson type.                                                                                       |

### Operation Contract for `enterLessonType(lessonType)`

| Contract CO3         | enterLessonType                                                                                                                                         |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterLessonType(LessonType lessonType)                                                                                                                  |
| **Cross References** | Use Case : Create Lesson                                                                                                                                |
| **Preconditions**    | 1. The system is temporarily storing the locationId to associate the new lesson to. <br> 2. The system has requested the user to enter the lesson type. |
| **Postconditions**   | 1. The system stores the indicated lesson type to associate the new lesson to. <br> 2. The system requests the user to enter a start time.              |

### Operation Contract for `enterStartTime(startTime)`

| Contract CO4         | enterStartTime                                                                                                                                                               |
| -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterStartTime(startTime: String)                                                                                                                                            |
| **Cross References** | Use Case : Create Lesson                                                                                                                                                     |
| **Preconditions**    | 1. The system is storing the location ID and the lesson type to be associated with the new lesson. <br> 2. The system has requested the administrator to enter a start time. |
| **Postconditions**   | 1. The system stored the start time to associate the new lesson to. <br> 2. The system requests the user to enter an end time.                                               |

### Operation Contract for `enterEndTime(endTime)`

| Contract CO5         | enterEndTime                                                                                           |
| -------------------- | ------------------------------------------------------------------------------------------------------ |
| **Operation**        | enterEndTime(endTime: String)                                                                          |
| **Cross References** | Use Case : Create Lesson                                                                               |
| **Preconditions**    | 1. The start time has been successfully entered. <br> 2. The system requests an end time.              |
| **Postconditions**   | 1. The end time is stored for the lesson. <br> 2. The system prompts the user to enter a lesson title. |

### Operation Contract for `enterTitle(title)`

| Contract CO6         | enterTitle                                                                                      |
| -------------------- | ----------------------------------------------------------------------------------------------- |
| **Operation**        | enterTitle(title: String)                                                                       |
| **Cross References** | Use Case : Create Lesson                                                                        |
| **Preconditions**    | 1. The end time has been successfully stored <br> 2. The system requests a title                |
| **Postconditions**   | 1. The title is stored for the lesson. <br> 2. The system prompts the user to enter a schedule. |

### Operation Contract for `enterSchedule(schedule)`

| Contract CO7         | enterSchedule                                                                                  |
| -------------------- | ---------------------------------------------------------------------------------------------- |
| **Operation**        | enterSchedule(schedule: String)                                                                |
| **Cross References** | Use Case : Create Lesson                                                                       |
| **Preconditions**    | 1. The title has been entered and stored. <br> 2. The system requests a schedule               |
| **Postconditions**   | 1. The schedule is stored. <br> 2. The system prompts for confirmation of the lesson creation. |

### Operation Contract for `enterConfirmation(confirmation)`

| Contract CO8         | enterConfirmation                                                                              |
| -------------------- | ---------------------------------------------------------------------------------------------- |
| **Operation**        | enterConfirmation(confirmation: Boolean)                                                       |
| **Cross References** | Use Case : Create Lesson                                                                       |
| **Preconditions**    | The schedule has been successfully stored.                                                     |
| **Postconditions**   | 1. The system processes the confirmation.<br>2. The lesson is created if confirmation is true. |

## Delete User Account

### Operation Contract for `enterDeleteUserAccountCommandId`

| Contract CO9         | enterDeleteUserAccountCommandId                                                                                                                                                   |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterDeleteUserAccountCommandId()                                                                                                                                                 |
| **Cross References** | Use Case : Delete User Account                                                                                                                                                    |
| **Preconditions**    | 1. The administrator is logged into the system.<br> 2. The list of possible commands is displayed to the administrator. <br> 3. The request to enter the command ID is displayed. |
| **Postconditions**   | 1. The system retrieves user types.<br> 2. The system prompts for user type selection.                                                                                            |

### Operation Contract for `enterUserType(userType)`

| Contract CO10        | enterUserType                                                                                                                                                                     |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterUserType(userType: UserType)                                                                                                                                                 |
| **Cross References** | Use Case : Delete User Account                                                                                                                                                    |
| **Preconditions**    | 1. The administrator is logged into the system.<br> 2. The list of possible commands is displayed to the administrator. <br> 3. The request to enter the command ID is displayed. |
| **Postconditions**   | 1. The system retrieves user types. <br> 2. The system prompts for user type selection.                                                                                           |

### Operation Contract for `enterUserId(userId)`

| Contract CO11        | enterUserId                                                                     |
| -------------------- | ------------------------------------------------------------------------------- |
| **Operation**        | enterUserId(userId: Integer)                                                    |
| **Cross References** | Use Case : Delete User Account                                                  |
| **Preconditions**    | The user type has been stored.                                                  |
| **Postconditions**   | 1. The user ID is stored. <br> 2. The system requests confirmation of deletion. |

### Operation Contract for `confirmDeletion(confirmation)`

| Contract CO12        | confirmationDeletion                                 |
| -------------------- | ---------------------------------------------------- |
| **Operation**        | confirmationDeletion(confirmation: Boolean)          |
| **Cross References** | Use Case : Delete User Account                       |
| **Preconditions**    | The user ID has been stored.                         |
| **Postconditions**   | The user account is deleted if confirmation is true. |

## View or Delete Lessons

### Operation Contract for `enterViewDeleteLessonsCommand()`

| Contract CO13        | enterViewDeleteLessonsCommand                                                              |
| -------------------- | ------------------------------------------------------------------------------------------ |
| **Operation**        | enterViewDeleteLessonsCommand()                                                            |
| **Cross References** | Use Case: View or Delete Lessons                                                           |
| **Preconditions**    | The admin is authenticated.                                                                |
| **Postconditions**   | The system prompts the user to enter the location ID to present its corresponding lessons. |

### Operation Contract for `enterLocationId(locationId)`

| Contract CO14        | enterLocationId                                                                                                                                    |
| -------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterLocationId(locationId: Integer)                                                                                                               |
| **Cross References** | Use Case: View or Delete Lessons                                                                                                                   |
| **Preconditions**    | The `enterViewDeleteLessonsCommand` has been called, and a list of locations is available.                                                         |
| **Postconditions**   | 1. The specified location ID is stored, allowing the system to filter lessons by location.<br>2. The system prompts the user to enter a lesson ID. |

### Operation Contract for `enterLessonId(lessonId)`

| Contract CO15        | enterLessonId                                                                                         |
| -------------------- | ----------------------------------------------------------------------------------------------------- |
| **Operation**        | enterLessonId(lessonId: Integer)                                                                      |
| **Cross References** | Use Case: View or Delete Lessons                                                                      |
| **Preconditions**    | The location ID is entered and relevant lessons are displayed.                                        |
| **Postconditions**   | 1. The selected lesson ID is stored, and the system fetches information related to the lesson. <br>2. |

### Operation Contract for `enterResponse(response)`

| Contract CO16        | enterResponse                                                                                                    |
| -------------------- | ---------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterResponse(response: String)                                                                                  |
| **Cross References** | Use Case: View or Delete Lessons                                                                                 |
| **Preconditions**    | 1. The lesson ID has been provided<br>2. The user is prompted to either delete the lesson or return to the list. |
| **Postconditions**   | If the response is to delete, the lesson is deleted; if not, the lesson list is presented.                       |

# Instructor Use Cases

## Register as Instructor

### Operation Contract for `enterRegisterInstructorCommandId()`

| Contract CO17        | enterRegisterInstructorCommandId                                                                                              |
| -------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterRegisterInstructorCommandId()                                                                                            |
| **Cross References** | Use Case : Register as Instructor                                                                                             |
| **Preconditions**    | 1. The list of possible commands is displayed to the unlogged user. <br> 2. The request to enter the command ID is displayed. |
| **Postconditions**   | The system prompts for the user's first name.                                                                                 |

### Operation Contract for `enterFirstName(firstName)`

| Contract CO18        | enterFirstName                                                  |
| -------------------- | --------------------------------------------------------------- |
| **Operation**        | enterFirstName(firstName: String)                               |
| **Cross References** | Use Case : Register as Instructor                               |
| **Preconditions**    | The command ID for registration as instructor has been entered. |
| **Postconditions**   | The system stored the first name and prompts for the last name. |

### Operation Contract for `enterLastName(lastName)`

| Contract CO19        | enterLastName                                                      |
| -------------------- | ------------------------------------------------------------------ |
| **Operation**        | enterLastName(lastName: String)                                    |
| **Cross References** | Use Case : Register as Instructor                                  |
| **Preconditions**    | The user's first name has been entered.                            |
| **Postconditions**   | The system stored the last name and prompts for the email address. |

### Operation Contract for `enterEmail(email)`

| Contract CO20        | enterEmail                                                |
| -------------------- | --------------------------------------------------------- |
| **Operation**        | enterEmail(email: String)                                 |
| **Cross References** | Use Case : Register as Instructor                         |
| **Preconditions**    | The user's last name has been entered.                    |
| **Postconditions**   | The system stored the email and prompts for the password. |

### Operation Contract for `enterPassword(password)`

| Contract CO21        | enterPassword                                                    |
| -------------------- | ---------------------------------------------------------------- |
| **Operation**        | enterPassword(password: String)                                  |
| **Cross References** | Use Case : Register as Instructor                                |
| **Preconditions**    | The user's email has been stored.                                |
| **Postconditions**   | The system stores the password, and the instructor is registered |

## Accept Offering

### Operation Contract for `enterAcceptOfferingCommandId()`

| Contract CO22        | enterAcceptOfferingCommandId                                                                                                                                                |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterAcceptOfferingCommandId()                                                                                                                                              |
| **Cross References** | Use Case : Accept Offering                                                                                                                                                  |
| **Preconditions**    | 1. The instructor is logged into the system.<br> 2. The list of possible commands is displayed to the instructor. <br> 3. The request to enter the command ID is displayed. |
| **Postconditions**   | The system prompts for a location ID.                                                                                                                                       |

### Operation Contract for `enterLocationId(locationId)`

| Contract CO23        | enterLocationId                                                       |
| -------------------- | --------------------------------------------------------------------- |
| **Operation**        | enterLocationId(locationId: Integer)                                  |
| **Cross References** | Use Case : Accept Offering                                            |
| **Preconditions**    | The offering (available lessons) list is displayed to the instructor. |
| **Postconditions**   | The system prompts for a location ID.                                 |

### Operation Contract for `enterLessonId(lessonId)`

| Contract CO24        | enterLessonId                                                     |
| -------------------- | ----------------------------------------------------------------- |
| **Operation**        | enterLessonId(lessonId: Integer)                                  |
| **Cross References** | Use Case : Accept Offering                                        |
| **Preconditions**    | The location ID has been entered.                                 |
| **Postconditions**   | The lesson ID is stored, and the instructor accepts the offering. |

## View or Edit Lessons

### Operation Contract for `enterViewEditOfferingsCommandId`

| Contract CO25        | enterViewEditOfferingsCommandId                                                                                                                                             |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterViewEditOfferingsCommandId()                                                                                                                                           |
| **Cross References** | Use Case : View or Edit Offerings                                                                                                                                           |
| **Preconditions**    | 1. The instructor is logged into the system.<br> 2. The list of possible commands is displayed to the instructor. <br> 3. The request to enter the command ID is displayed. |
| **Postconditions**   | 1. The system fetches all lessons associated to the instructor. <br> 2. The system displays all lessons associated to the professor.                                        |

### Operation Contract for `enterLessonId(lessonId)`

| Contract CO26        | enterLessonId                                                                                                                              |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| **Operation**        | enterLessonId(lessonId: Integer)                                                                                                           |
| **Cross References** | Use Case : View or Edit Offerings                                                                                                          |
| **Preconditions**    | 1. The system has displayed all lessons associated to the instructor. <br> 2. The system has prompted the instructor to enter a lesson ID. |
| **Postconditions**   | The lesson ID is stored, and the instructor accepts the offering.                                                                          |

### Operation Contract for `enterResponse(response)`

| Contract CO27        | enterResponse                                                                                                                                                                                                                                                                                                                                                          |
| -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterResponse(response: String)                                                                                                                                                                                                                                                                                                                                        |
| **Cross References** | Use Case : View or Edit Offerings                                                                                                                                                                                                                                                                                                                                      |
| **Preconditions**    | 1. The system has displayed more information about a lesson. <br> 2. The system has prompted the instrtuctor to return to menu of lessons or unassign themselves from the one selected.                                                                                                                                                                                |
| **Postconditions**   | 1. If the instructor chooses to unassign themselves, the system will unassign the lesson from the instructor and make the lesson unavailable to the public and available to all instructors to be picked. <br> 2. If the instructor chooses to return to the list of lessons, then the list is displayed once more and the instructor is prompted to enter a response. |

# Client Use Cases

## Register as Client

### Operation Contract for `enterRegisterClientCommandId()`

| Contract CO28        | enterRegisterClientCommandId                                                                                                  |
| -------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterRegisterClientCommandId()                                                                                                |
| **Cross References** | Use Case : Register as Client                                                                                                 |
| **Preconditions**    | 1. The list of possible commands is displayed to the unlogged user. <br> 2. The request to enter the command ID is displayed. |
| **Postconditions**   | The system prompts for the user's first name.                                                                                 |

### Operation Contract for `enterFirstName(firstName)`

| Contract CO29        | enterFirstName                                                  |
| -------------------- | --------------------------------------------------------------- |
| **Operation**        | enterFirstName(firstName: String)                               |
| **Cross References** | Use Case : Register as Client                                   |
| **Preconditions**    | The client registration command ID has been entered.            |
| **Postconditions**   | The system stored the first name and prompts for the last name. |

### Operation Contract for `enterLastName(lastName)`

| Contract CO30        | enterLastName                                                      |
| -------------------- | ------------------------------------------------------------------ |
| **Operation**        | enterLastName(lastName: String)                                    |
| **Cross References** | Use Case : Register as Client                                      |
| **Preconditions**    | The user's first name has been entered.                            |
| **Postconditions**   | The system stores the last name and prompts for the email address. |

### Operation Contract for `enterEmail(email)`

| Contract CO31        | enterEmail                                                |
| -------------------- | --------------------------------------------------------- |
| **Operation**        | enterEmail(email: String)                                 |
| **Cross References** | Use Case : Register as Client                             |
| **Preconditions**    | The user's last name has been entered.                    |
| **Postconditions**   | The system stores the email and prompts for the password. |

### Operation Contract for `enterPassword(password)`

| Contract CO32        | enterPassword                                              |
| -------------------- | ---------------------------------------------------------- |
| **Operation**        | enterPassword(password: String)                            |
| **Cross References** | Use Case : Register as Client                              |
| **Preconditions**    | The user's email has been entered.                         |
| **Postconditions**   | The system stores the password, and the user is registered |

## Add Dependent

### Operation Contract for `enterAddDependentCommandId()`

| Contract CO33        | enterAddDependentCommandId                                                                                                                                          |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | 1. The client is logged into the system.<br> 2. The list of possible commands is displayed to the client. <br> 3. The request to enter the command ID is displayed. |
| **Cross References** | Use Case : Add Dependent                                                                                                                                            |
| **Preconditions**    | The client is registered and authenticated.                                                                                                                         |
| **Postconditions**   | The system prompts for the first name of the dependent. <br>                                                                                                        |

### Operation Contract for `enterFirstName(firstName)`

| Contract CO34        | enterFirstName                                                               |
| -------------------- | ---------------------------------------------------------------------------- |
| **Operation**        | enterFirstName(firstName: String)                                            |
| **Cross References** | Use Case : Add Dependent                                                     |
| **Preconditions**    | The system has prompted the client to enter the first name of the dependent. |
| **Postconditions**   | The system prompts for the dependent's last name.                            |

### Operation Contract for `enterLastName(lastName)`

| Contract CO35        | enterLastName                                     |
| -------------------- | ------------------------------------------------- |
| **Operation**        | enterLastName(lastName: String)                   |
| **Cross References** | Use Case : Add Dependent                          |
| **Preconditions**    | The first name of the dependent has been entered. |
| **Postconditions**   | The last name of the dependent is stored.         |

### Operation Contract for `enterDateOfBirth(dateOfBirth)`

| Contract CO36        | enterDateOfBirth                                                                           |
| -------------------- | ------------------------------------------------------------------------------------------ |
| **Operation**        | enterDateOfBirth(dateOfBirth: Date)                                                        |
| **Cross References** | Use Case : Add Dependent                                                                   |
| **Preconditions**    | The last name of the dependent has been entered.                                           |
| **Postconditions**   | The date of birth o the dependent is stored and the dependent is created under the client. |

## Create Booking

### Operation Contract for `enterCreateBookingCommandId()`

| Contract CO37        | enterCreateBookingCommandId                                                                                                                                         |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterCreateBookingCommandId()                                                                                                                                       |
| **Cross References** | Use Case : Create Booking                                                                                                                                           |
| **Preconditions**    | 1. The client is logged into the system.<br> 2. The list of possible commands is displayed to the client. <br> 3. The request to enter the command ID is displayed. |
| **Postconditions**   | 1. The system fetches all locations. <br> 2. The system prompts for the location ID.                                                                                |

### Operation Contract for `enterLocationId(locationId)`

| Contract CO38        | enterLocationId                                                                                                                         |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterLocationId(locationId: Integer)                                                                                                    |
| **Cross References** | Use Case : Create Booking                                                                                                               |
| **Preconditions**    | 1. The system has displayed all locations and their information. <br> 2. The system has prompted the client to enter the location ID.   |
| **Postconditions**   | 1. The location ID for the booking is stored.<br> 2. The lessons available for booking and associated to the location ID are displayed. |

### Operation Contract for `enterLessonId(lessonId)`

| Contract CO39        | enterLessonId                                                                                                      |
| -------------------- | ------------------------------------------------------------------------------------------------------------------ |
| **Operation**        | enterLessonId(lessonId: Integer)                                                                                   |
| **Cross References** | Use Case : Create Booking                                                                                          |
| **Preconditions**    | 1. The location ID for the booking has been entered.<br> 2. The lessons available for booking have been displayed. |
| **Postconditions**   | The lesson ID for the booking is stored.                                                                           |

### Operation Contract for `enterChildId(childId)`

| Contract CO40        | enterChildId                                                                                                                                                                             |
| -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterChildId(childId: Integer)                                                                                                                                                           |
| **Cross References** | Use Case : Create Booking                                                                                                                                                                |
| **Preconditions**    | 1. The lesson ID for the booking has been entered.<br> 2. The client wishes to register this booking under for a dependent of theirs. <br> 3. The dependents of the client is displayed. |
| **Postconditions**   | The child ID for the booking is stored, and the booking is created.                                                                                                                      |

## View or Edit Bookings

### Operation Contract for `enterViewEditBookingsCommandId()`

| Contract CO41        | enterViewEditBookingsCommandId                                                                                                                                      |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterViewEditBookingsCommandId()                                                                                                                                    |
| **Cross References** | Use Case : View or Edit Bookings                                                                                                                                    |
| **Preconditions**    | 1. The client is logged into the system.<br> 2. The list of possible commands is displayed to the client. <br> 3. The request to enter the command ID is displayed. |
| **Postconditions**   | 1. The system fetches all bookings associated to the client. <br> 2. The system prompts the client to enter a booking ID.                                           |

### Operation Contract for `enterBookingId(bookingId)`

| Contract CO42        | enterBookingId                                                                                                                                                                                                                                                                                               |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Operation**        | enterBookingId(bookingId: Integer)                                                                                                                                                                                                                                                                           |
| **Cross References** | Use Case : View or Edit Bookings                                                                                                                                                                                                                                                                             |
| **Preconditions**    | 1. The bookings associated to the client has been displayed. <br> 2. The system has prompted the client to enter a booking ID                                                                                                                                                                                |
| **Postconditions**   | 1. The system fetches location, instructor, and lesson information associated tot he booking. <br> 2. The system displays more information about the booking using the items determined in the previous step. <br> 3. The system prompts the client to return to the list or to delete the booking selected. |

### Operation Contract for `enterResponse(response)`

| Contract CO43        | enterResponse                                                                                                                                                                                            |
| -------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Operation**        | enterResponse(response: String)                                                                                                                                                                          |
| **Cross References** | Use Case : View or Edit Bookings                                                                                                                                                                         |
| **Preconditions**    | The system has prompted the client to return to the menu of bookings or to delete the one selected.                                                                                                      |
| **Postconditions**   | 1. If the client chose to return to the menu of bookings, the bookings are subsequently displayed. <br> 2. If the client chose to delete the currently selected booking, the system deletes the booking. |
