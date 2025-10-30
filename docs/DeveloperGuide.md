---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

## Acknowledgements

- **AddressBook-Level3 (AB3) by SE-EDU** ([GitHub](https://github.com/se-edu/addressbook-level3), [Documentation](https://se-education.org/))
  - UniContactsPro is adapted from AB3, with extensive additions and customizations.
  - Documentation and diagrams (such as sequence/activity/class diagrams) and setup instructions were adapted from AB3 and SE-EDU documentation guides.
  - Project and codebase are licensed under the MIT License, as per AB3 and SE-EDU.

- **JavaFX** ([https://openjfx.io/]), **Jackson** ([https://github.com/FasterXML/jackson]), **JUnit5** ([https://junit.org/junit5/]) — Used as core libraries and tools.

- **Checkstyle** ([https://github.com/checkstyle/checkstyle]) — Used for code quality and style checking.

- **JaCoCo** ([https://www.jacoco.org/jacoco/]) — Used for Java code coverage analysis.

- **Gradle Shadow Plugin** ([https://github.com/johnrengelman/shadow]) — Used for building fat/uber jar files.

- **Jekyll** ([https://jekyllrb.com/]) — Used for static website generation and documentation.

- **JavaFX 8 Tutorial by Marco Jakob**  
  - Some code adapted for UI components: [JavaFX 8 Tutorial](http://code.makery.ch/library/javafx-8-tutorial/).

- **Icons and Images**  
  - Susumu Yoshida ([address_book_32.png, AddressApp.ico](http://www.mcdodesign.com/))
  - Jan Jan Kovařík ([calendar.png, edit.png](http://glyphicons.com/))
  - See `copyright.txt`.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* undergraduate student

**Value proposition**: It helps undergraduates keep track of school contacts, making it easier to remember acquaintances they don’t interact with often. The app acts like a personalized contact book designed for students, ensuring connections are organized and accessible when needed.


### User stories

Priorities: High (must have) - `* * *`, Medium (should have) - `* *`, Low (nice to have) - `*`

| Priority | As a …           | I want to …                                                        | So that I can …                                                     |
|----------|------------------|--------------------------------------------------------------------|----------------------------------------------------------------------|
| * * *    | user             | search for a contact by name or tag                                | quickly find the contact I need                                     |
| * * *    | user             | view the contact list                                              | see all my contacts at a glance                                     |
| * * *    | user             | add a contact                                                      | keep new people I meet in my address book                           |
| * * *    | user             | delete a contact                                                   | remove entries I no longer need                                     |
| * * *    | user             | edit a contact                                                     | keep contact details up to date                                     |
| * *      | user             | filter my contacts by tag                                          | find people in specific groups easily                               |
| * *      | user             | create a new tag                                                   | categorize my contacts the way I want                               |
| * *      | user             | delete an existing tag                                             | clean up tags I no longer use                                       |
| * *      | student          | search for coursemates in the same tutorial                        | find group mates for a project                                      |
| * *      | user             | sort in alphabetical order and search by first letter of name      | find my friends quicker                                             |
| * *      | user             | merge duplicate contacts                                           | organize my list by combining duplicates                            |
| * *      | user             | assign multiple tags to each contact                               | find them via multiple categories                                   |
| *        | new user         | view the user guide easily                                         | learn more about the product whenever I need                        |
| *        | experienced user | pin important contacts to the top of my address book               | access them quickly                                                 |
| *        | student          | add a profile picture to my profile or another contact's profile   | recognize and recall people more easily                             |
| *        | user             | add notes about a person                                           | remember details about where and when I met them                    |
| *        | user             | indicate how close a person is to me                               | make efforts to contact closer friends more regularly               |
| *        | user             | log interactions with a contact                                    | keep track of how often I connect with them                         |
| *        | user             | sort my contacts by date added                                     | catch up with old or new friends                                    |
| *        | user             | import contacts from my phone and email                            | avoid typing them manually                                          |
| *        | user             | export (filtered) contact list as a CSV file                       | share and store it externally                                       |
| *        | user             | be reminded to maintain connections                                | catch up with friends I haven’t talked to for ~2 months             |
| *        | user             | create and switch between custom themes                            | customize the app’s appearance to my liking                         |


### Use cases

(For all use cases below, the **System** is the `UniContactsPro` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete a contact**

**MSS**

1.  User requests to list persons
2.  UniContactsPro shows a list of persons
3.  User requests to delete a specific person in the list
4.  UniContactsPro deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: Add a contact**

**MSS**

1.  User requests to create a person based on their name, phone number, email address, date, profile picture (optional)
2.  UniContactsPro adds a person
3.  UniContactsPro displays the updated list of contacts
    Use case ends.

**Extensions**

* 2a. The command does not follow the command format

  * 2a1. UniContactsPro shows an error message

    Use case ends.

**Use case: List all contacts**

**MSS**

1.  User requests to list all contacts in UniContactsPro at that point in time.
2.  UniContactsPro displays current list of contacts.

**Extensions**

* 2a. The command does not follow the command format

    * 2a1. UniContactsPro shows an error message

      Use case ends.

**Use case: Exit**

**MSS**

1.  User requests to exit UniContactsPro
2. UniContactsPro exits

**Extensions**

* 2a. The command does not follow the command format

    * 2a1. UniContactsPro shows an error message

      Use case ends.

### Non-Functional Requirements

1. Should work on any mainstream OS with Java 17+.
2. Should store up to 1000+ contacts with tags without sluggishness.
3. A student with above-average typing speed should be able to manage contacts faster via CLI than GUI.
4. Should load the contact list within 2 seconds for up to 1000 entries.
5. Should persist data locally so that contacts are not lost after application shutdown.
6. Should have a consistent and intuitive command syntax (e.g., `add`, `delete`, `list`).
7. Should prevent accidental data corruption by validating input before saving.
8. Should allow undo/redo of recent commands for error recovery.
9. Should not require an internet connection for core features (offline-first).
10. Should allow exporting data (e.g., CSV) in under 3 seconds for 500 contacts.
11. Should provide error messages that are clear and easy for students to understand.
12. Should be maintainable by new developers, with clear documentation and modular design.
13. Should be extensible to add new features (e.g., reminders, themes) without major refactoring. 
14. Should ensure tags/categories remain consistent (no duplicates unless explicitly allowed). 
15. Should recover gracefully from corrupted storage files (e.g., reset to last known good state). 
16. Should support basic accessibility, e.g., adjustable font size in the GUI. 
17. Should consume no more than 200MB of RAM during typical usage with 1000 contacts.


### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **CLI (Command Line Interface)**: A text-based interface where users type commands (e.g., `add`, `list`, `delete`) to interact with the app.
* **Contact**: An individual entry in the address book, typically containing a name, phone number, email, and optional tags.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.


### Finding contacts by name

1. Prerequisites: Multiple persons in the list.

   1. Test case: `find alex david`<br>
      Expected: Lists contacts whose names contain "alex" or "david" (case-insensitive). Status shows the number of persons listed.

   1. Test case: `find` (empty)<br>
      Expected: Error message with usage: `find KEYWORD [MORE_KEYWORDS]...`.


### Filtering contacts by tag

1. Prerequisites: At least some contacts have tags (e.g., `friends`, `colleagues`).

   1. Test case: `filter t/friends`<br>
      Expected: Only contacts with the `friends` tag are shown. Status shows number of persons listed.

   1. Test case: `filter t/friends t/colleagues`<br>
      Expected: Shows contacts that have at least one of the tags `friends` or `colleagues`.

   1. Test case: `filter` (missing tag prefixes) or `filter friends` (missing `t/`)<br>
      Expected: Error message with usage: `filter t/KEYWORD [t/MORE_KEYWORDS]...`.


### Deleting contacts

1. Deleting by displayed indices

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted. Details of the deleted contact shown in the result message. Timestamp in the status bar is updated.

   1. Test case: `delete 1 2 2 5`<br>
      Expected: Contacts at indices 1, 2, and 5 are deleted (duplicates ignored). Result message lists all deleted persons.

   1. Test case: `delete 0` or `delete 999` (index out of range)<br>
      Expected: Error message: invalid person displayed index. No one is deleted.

2. Deleting all contacts matching tags (union)

   1. Prerequisites: Multiple persons in the list, with tags set (e.g., `friends`, `noisy`).

   1. Test case: `delete all t/friends`<br>
      Expected: All currently displayed contacts having the `friends` tag are deleted. Result message lists all deleted persons.

   1. Test case: `delete all t/superhero t/noisy`<br>
      Expected: Deletes all currently displayed contacts that have either `superhero` or `noisy`. If none match, shows: `No persons found with tag(s): superhero, noisy`.

   1. Test case: `delete all` (no `t/` provided)<br>
      Expected: Error with usage instructions.


### Sorting by closeness

1. Prerequisites: Contacts have a closeness rating set.

   1. Test case: `sortByCloseness o/asc`<br>
      Expected: List is sorted by closeness in ascending order. Result message: `Sorted contact list by closeness (ascending)`.

   1. Test case: `sortByCloseness o/desc`<br>
      Expected: List is sorted by closeness in descending order. Result message: `Sorted contact list by closeness (descending)`.

   1. Test case: `sortByCloseness` or `sortByCloseness o/invalid`<br>
      Expected: Error message with usage: `sortByCloseness: Parameters: o/{asc|desc}`.


### Adding/updating a profile picture

1. Prerequisites: At least one person exists in the list (index 1 available). Ensure you have a `.png` image at `~/Downloads/example.png`.

   1. Test case (copy from local path): `addProfilePic 1 pp/~/Downloads/example.png`<br>
      Expected: The image is copied into `docs/images/` with a unique filename. Contact 1 shows the updated profile picture. Success message includes the contact name. Only `.png` is accepted.

   1. Test case (use existing filename already in docs/images): `addProfilePic 1 pp/example.png`<br>
      Expected: Uses existing image `docs/images/example.png`. Profile picture updated. Success message shown.

   1. Test case (non-PNG): `addProfilePic 1 pp/~/Downloads/example.jpg`<br>
      Expected: Error message: only `.png` images are supported.

   1. Test case (file already exists in docs/images when copying): ensure `docs/images/example.png` exists, then run `addProfilePic 1 pp/~/Downloads/example.png`<br>
      Expected: Error message indicating the file already exists in `docs/images` and how to reference it directly using `pp/<image name>`.

   1. Test case (invalid index): `addProfilePic 0 pp/example.png` or `addProfilePic 999 pp/example.png`<br>
      Expected: Error message: invalid person displayed index.


### Saving data

1. Dealing with missing/corrupted data files

   1. Missing file
      - Close the app. Delete `data/addressbook.json`.
      - Launch the app.<br>
        Expected: App starts with an empty list (or sample data if the app provides it). No crash.

   1. Corrupted file
      - Close the app. Open `data/addressbook.json` in a text editor and replace its contents with an invalid JSON (e.g., `{ not: valid }`). Save the file.
      - Launch the app.<br>
        Expected: App handles the error gracefully and starts with an empty list (no crash). Consider checking logs for a warning about corrupted storage.

1. _{ more test cases …​ }_


--------------------------------------------------------------------------------------------------------------------

## Appendix: Effort

This appendix summarizes the total effort and achievements for UniContactsPro, to facilitate evaluation.

### Project Difficulty and Challenges
- **Complexity Beyond AB3**: While AB3 comprises mostly single-entity (Person) management, UniContactsPro extends or customizes many features (such as tagging, closeness, profile pictures, filtering, and reminders) requiring broader and deeper changes to the model, parser, command, and UI layers.
- **JavaFX Customizations**: Significant effort was spent modernizing the UI, introducing new FXML views, and handling advanced JavaFX behaviors such as theme switching and dynamic filtering, which go beyond the base AB3 layout.
- **Data Model Enhancements**: Many new attributes (profile picture, closeness, handle) and features such as multiple tags, filtering, advanced searching, and custom sorting increased the challenge compared to AB3’s flat data structure.
- **Code Quality and Testing**: Maintaining high-quality code, documentation, and extensive unit and integration testing for all new features to ensure the product remains robust and extensible.

### Major Effort Spent
- Requirements gathering, architecture planning, and design revisions to support student-specific features (e.g., coursemate search, reminders, tracking last contacted date).
- Implementing and integrating new commands for tags, themes, closeness, and profile pictures, each requiring multi-layer changes (parser, command, UI, storage, model).
- Manual and automated testing (unit, system, and acceptance).
- Updating and maintaining comprehensive documentation and diagrams for all new behaviors and structural changes.

### Reuse and Adaptation
- **SE-EDU AB3 Base: Significant Reuse**: Core infrastructure (logic, UI, storage patterns) and some commands are inherited/adapted from AB3. This saved foundational setup effort.
    - However, most major features added required rewriting or significant refactoring of nearly all subsystems (model, command, UI, storage, test).
    - Examples: Tagging, profile pictures, reminders, and custom filters required original solutions, test cases, and new classes.
- **Third-party libraries (JavaFX, Jackson, JUnit, Jekyll)** provided building blocks (GUI, JSON handling, testing, docs), saving development time on those core functionalities.
- **Attribution Transparency**: All reused code/assets were documented (see Acknowledgements) and integrated according to open-source licensing requirements. Where adaptation was significant (e.g., UI components or advanced command parsing), new code was written.

### Effort Estimate
- The bulk of team effort was focused on:
    - Modifying the data model for greater extensibility and richer user experience
    - Implementing, testing, and documenting new or enhanced CLI commands and UI workflows
    - Ensuring a seamless, robust, and user-friendly application beyond AB3 capabilities
- **Estimated AB3 Reuse Impact**: Foundational AB3 code (~20-30% of the codebase) provided basic address book structure; however, over 70% of features, logic, UI, and documentation reflect new or heavily extended work.

### Achievements
- UniContactsPro delivers features highly tailored to undergraduate student needs (tracking, reminders, advanced filtering), all integrated in a modernized, user-friendly, and extensible JavaFX application.
- Our team successfully tackled technical and design challenges across multiple software layers, upholding strong software engineering and documentation standards.

## Appendix: Planned Enhancements

- ZhengHao
ToBeDone

- He Yue
ToBeDone

- Gokul
ToBeDone

- Max
ToBeDone