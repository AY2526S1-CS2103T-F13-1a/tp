---
layout: default.md
title: "User Guide"
---

# User Guide
## 1. Welcome to UniContactsPro!

Welcome to **UniContactsPro**! We built this desktop app specifically for you, an undergraduate student, to finally get your school contacts organized and accessible.

Think of it as your personal digital address book for university. Whether you're managing project groupmates, classmates for a single module, or your CCA contacts, UniContactsPro is here to help. Our goal is to make sure you never lose touch, especially with those people you aren't close to but still need to contact.


### Target Users

UniContactsPro targets undergraduate students who:
- Need to organize and manage university-related contacts, such as project groupmates, classmates, and CCA members.
- Are comfortable using a Command Line Interface (CLI) for efficient data entry and management.
- Prefer a keyboard-driven workflow for speed.
- Want a dedicated, local desktop application to keep track of connections.

### What you need

All you need is a Mac, Window or Linux laptop with around 50MB of storage space! Have these and you are good to go!

### Using this guide

**For new users...**
- We suggest that you start from the [Quick Start](#2-quick-start) section, to learn how to set up and learn about UniContactsPro from scratch

**For existing users...**
- Head over to [Features](#5-features) to learn or refresh yourself with the features we have in store

**For experienced users...**
- Head over to our Command Summary table to quickly find the command format you are looking for!

If you are looking for a specific section of the guide, have a look at our [Table of Contents](#table-of-contents) below.

## Table of Contents

1. [Welcome to UniContactsPro!](#1-welcome-to-unicontactspro)
2. [Quick Start](#2-quick-start)
3. [Understanding the Interface](#3-understanding-the-interface)
4. [Command Basics](#4-command-basics)
5. [Features](#5-features)
6. [FAQ](#6-faq)
7. [Glossary](#7-glossary)
8. [Acknowledgements](#8-acknowledgements)

---

## 2. Quick Start

Get up and running with UniContactsPro in minutes! Let's go through a few simple steps to get you there...

### System Requirements

* **Operating System:** Windows, macOS, or Linux
* **Java Version:** Java 17 or above

<details>
<summary>How to check your Java version?</summary>

1.  Open your command terminal (e.g., Command Prompt on Windows, Terminal on macOS/Linux).
2.  Type `java -version` and press `Enter`.
3.  Ensure the output shows a version number of `17` or higher.
</details>

<details>
<summary>How to open your command terminal?</summary>

***For Linux users***
1. Press `Ctrl` + `Alt` + `T`.

***For Mac users***
1. Press `F4` or press `Command (⌘) + Space` to open Spotlight.
2. Type "Terminal" and click the terminal app.


***For Windows users***
1. Press Windows key or click the Start menu.
2. Type cmd or Command Prompt.
3. Press Enter.

After doing these steps, the command terminal window will open
</details>

<details>
<summary>How to install Java 17?</summary>
If your version of Java is not 17 or above, follow these steps to install Java 17 in your device.

***For Linux users***
Follow the Linux Java 17 Installation Guide [here](https://se-education.org/guides/tutorials/javaInstallationLinux.html).

***For macOS users***
Follow the macOS Java 17 Installataion Guide [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

***For Windows users***
Follow the Windows Java 17 Installation Guide [here](https://se-education.org/guides/tutorials/javaInstallationWindows.html).
</details>

### Installation & Launch

1.  **Download:** Go to our [GitHub releases page](https://github.com/AY2526S1-CS2103T-F13-1a/tp/releases). In the most recent release, scroll down to the `Assets` section and download `UniContactsPro.jar` by clicking on the file.
    ![image](https://hackmd.io/_uploads/S1NaEMS1be.png)
2.  **Store:** Create a new folder, named `UniContactsPro` for the application and place the downloaded `.jar` file inside it.
3.  **Run:**
    * Open a command terminal (instructions for this is in previous section).
    * Navigate (using `cd`) to the folder you created in step 2.
    * Execute the following command:
        ```bash
        java -jar UniContactsPro.jar
        ```

<details>
<summary>How to navigate to the folder using the cd command?</summary>

The `cd` command stands for "change directory". It has the same functionality as when you manually click on a folder in your file explorer.

***Example usage***
Let's assume the folder `UniContactsPro`is in Desktop. Executing the following command in your terminal will bring you to the folder.

```bash
cd Desktop/UniContactsPro
```
</details>

<div markdown="block" class="alert alert-info">

**:bulb: Pro Tip**
After typing `cd` (with a space), you can often drag and drop the folder directly from your file explorer onto the terminal window. This will paste the full, correct path for you.
</div>

The application will launch, pre-filled with sample data for you to explore.

![image](https://hackmd.io/_uploads/B1ZtN5AAlx.png)


### Your First Commands

Once the app is open, try these commands in the command box:

1.  **Get directed to the User Guide:**
    ```
    help
    ```
2.  **Add a new contact:**
    ```
    add n/John Doe p/91234567 e/john@example.com a/123 Street h/@johndoe c/5
    ```
3.  **View all contacts:**
    ```
    list
    ```
4.  **Clear the sample data:** (When you're ready to add your own contacts)
    ```
    clear
    ```

---

## 3. Understanding the Interface

### Main Components

- **Menu Bar:** Access File and Help menus (press `F1` for help)
- **Command Box:** Type commands here and press `Enter` to execute
- **Result Display:** Shows feedback messages and command results
- **Contact List:** Displays contacts with index numbers
- **Status Bar:** Shows data file location and total contacts

![photo_6057644066834418466_y](https://hackmd.io/_uploads/ryOZB5CAeg.jpg)


### Contact Card Details

Each contact shows:
- **Index:** Position in list (for commands)
- **Name:** Contact's full name
- **Phone:** 8-digit number
- **Email:** Email address
- **Address:** Physical address
- **Telegram Handle:** Telegram handle
- **Closeness:** Closeness rating
- **Tags:** Categories/labels (if any)

![image](https://hackmd.io/_uploads/HJckA9B1Wg.png)


### Navigating Command History

| Shortcut | Action |
|----------|--------|
| `↑` | Previous command in history |
| `↓` | Next command in history |

<div markdown="block" class="alert alert-warning">

**:book: Note**
The last input typed by the user (before pressing enter) will be shown if there are no more stored commands after pressing `↓`
</div>

### Using Command Autocomplete
UniContactsPro uses an autocomplete system to help you type commands faster and reduce errors

**How it works**
As you type in the command box, a dropdown box will appear with recognized command word suggestions

![photo_6057644066834418453_y](https://hackmd.io/_uploads/ryNWZ5AAlg.jpg)


**Keyboard Controls:**

| Key | Action |
|-----|--------|
| `Tab` or `Enter` | Accept highlighted suggestion |
| `↑` | Move highlight up |
| `↓` | Move highlight down |
| `Esc` | Close dropdown |

**Mouse Support:**
- Hover over a suggestion to highlight it
- Click to accept the suggestion


---

## 4. Command Basics

### Command Structure

Commands in UniContactsPro are simple to follow! Below is the typical command format...

```
command_word prefix/ARGUMENT [prefix/ARGUMENT]...
```

**Prefixes** are in the format:

`prefix/ARGUMENT`

Prefixes have several variations with different notations:

|| Mandatory | Optional |
|--------|---------|--------|
| Not variadic | prefix/ARGUMENT | 	[prefix/ARGUMENT] |
| Variadic | prefix/ARGUMENT... | [prefix/ARGUMENT]... |

### Valid Prefixes

| Prefix | Meaning | Format | Mandatory Field|
|--------|---------|--------|--------|
| `n/` | Name | Alphabets, numbers, spaces, hyphens | Yes |
| `p/` | Phone | 8 digits, starts with 8 or 9 | Yes |
| `e/` | Email | Valid email format | Yes |
| `a/` | Address | Any text | Yes |
| `h/` | Telegram Handle | Alphabets, integers, underscores, 5 to 32 characters, starts with an `@` |Yes |
|`c/`| Closeness | Numbers from 1 to 5 (inclusive) | Yes
| `t/` | Tag | Alphanumeric, max 15 characters | No |

### Parameter Rules

**Name (`n/`):**
- Alphabets, numerical digits, spaces, and hyphens only
- Automatically capitalized
- Extra spaces trimmed

**Phone (`p/`):**
- Exactly 8 numerical digits
- Must start with 8 or 9
- No spaces allowed

**Email (`e/`):**
- Must be valid email format (`Local-part`@`Domain name`)
    - Local-part
        - Alphanumeric characters and these special characters `+`, `_` + `.`, `-` only.
        - May not start or end with any special characters.
    -  Domain name
        - Made up of domain labels separated by periods.
        - Must end with a domain label at least 2 characters long
        - Must have each domain label start and end with alphanumeric characters
        - Must have each domain label consist of alphanumeric characters, separated only by hyphens, if any.
- Example: `user@example.com`

**Telegram handle (`h/`):**
- Alphabets, integers and underscores
- Must begin with an `@`
- Must be between 5 and 32 characters inclusive
- Must not start with a number

**Closeness (`c/`):**
- Numerical values between 1 and 5 inclusive

**Tags (`t/`):**
- Alphanumeric only
- Can specify multiple

---

## 5. Features

### Adding Contacts

**Command:** `add n/NAME p/PHONE e/EMAIL a/ADDRESS h/TELEGRAM HANDLE [t/TAG]..`

**Example:**
```
add n/John Doe p/91234567 e/john@example.com a/123 Street h/@johndoe t/friend
```
**Name formatting** :
- Name is not case-sensitive.  The first character of each word is captalised, leaving the rest of the characters in lower case (eg. `JOHN DOE`, `jOhN dOe` will all be recorded as `John Doe`)
- When the name starts with the digit, the digit will remain while the rest of the characters are set to lower case (eg. `1JOhN dOE` will be set to `1john Doe`)
- Additional spaces between two words will be trimmed to one, trailing white spaces are trimmed

**Duplicates**
- Two people are considered duplicates if they have either of the same fields :
    - Phone number
    - Email address
    - Telegram handle

**Success:** Contact added with confirmation message:
:::success
<pre> New person added: Mary; Phone: 98765432; Email: marylim@gmail.com; Address: Ang Mo Kio; Tags: </pre>
:::

**Errors:**
- `Invalid name` - Contains special characters that is not a hyphen
- `Invalid phone number`
- `Invalid email format`
- `Invalid telegram handle`
- `Invalid tag format`
- `This person already exists`

---

### Editing Contacts

**Command:** `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [h/TELEGRAM HANDLE] [c/closeness] [t/TAG]...`

**Example:**
```
edit 1 p/98765432 e/newemail@example.com
```

**Note:** At least one field must be provided. Tags replace all existing tags.

---

### Adding Profile Picture

Adding a new image with a from a local file path

**Command:** `addProfilePic INDEX pp/ImageFilePath.png`

**Example:**
```
addProfilePic 1 pp/~Downloads/example.png
```

**Success:** ![image](https://hackmd.io/_uploads/ryVW0KRCxl.png)


Adding an existing image with with file name

**Command:** `addProfilePic INDEX pp/imagename.png`

**Example:**
```
addProfilePic 1 pp/example.png
```

**Success:** ![image](https://hackmd.io/_uploads/ryVW0KRCxl.png)

**Note:** Only .png images can be added

---

### Deleting Contacts

**Command:**
```
delete INDEX [MORE_INDEXES]...
delete all t/TAG
```

**Examples:**
```
delete 1
delete 1 2 3
delete all t/friends
```

**Success:**
Single Deletion: Shows deleted contact(s) details
Multiple Deletion: Shows number of deleted contats

**Errors:**
- No contact found with index: `The person index provided is invalid`
- No contact found with delete all tag: `No persons found with tag: <TAG>`

---

### Listing Contacts

**Command:** `list`

Shows all contacts in order added (newest first).

---

### Searching Contacts

The `find` command allows users to search for contacts according to their names. Even partial matches to the `KEYWORD` are supported.

**Command:** `find KEYWORD`

**Example:**

Given a contact book with the following state:
![Screenshot 2025-10-29 at 2.50.06 AM](https://hackmd.io/_uploads/rJCvlc0Age.png)

All of the following commands should result in only `Alex` being displayed:
```
find Al
find Alex
find ex
```

**Success:**

Shows the list of contacts with name matching the keyword. Successful execution message also states number of contacts displayed.

**Errors:**
- No keyword input: "Invalid command format" message is shown

---

### Filtering by Tag

The `filter` command allows users to filter their contact list by tags. All contacts with a tag matching the `TAG` arguments will be shown.

**Command:** `filter t/TAG [t/MORE_TAGS] ...`

**Example:**

Given a contact book with the following state:
![Screenshot 2025-10-29 at 3.08.05 AM](https://hackmd.io/_uploads/SkriN9ACxl.png)

The following command will result in only `Alex` and `Bernice` being displayed:
```
filter t/friends
```

**Success:**

Shows the list of contacts with the respective tag. Successful execution message also states number of contacts displayed.

**Errors:**
- Empty tag input: "Invalid command format" message is shown
- No prefix `t/`: "Invalid command format" message is shown
- Invalid tag: Tag specifications message is shown

---

### Sorting Contacts

Contacts can be sorted by `Closeness`. The `sortByCloseness` command can sort the contacts either by ascending or descending order in terms of `Closeness`.

**Command:** `sortByCloseness o/[asc|desc]`

**Examples:**
Given a contact book with the following state:
![Screenshot 2025-10-29 at 3.13.06 AM](https://hackmd.io/_uploads/ryGRBc0Rex.png)

The following command:
```
sortByCloseness o/desc
```

results in the contact book looking like this:
![Screenshot 2025-10-29 at 3.17.43 AM](https://hackmd.io/_uploads/rk41PcRCge.png)

**Success:**

Shows . Successful execution message states order in which sort has been performed.

**Errors:**
- Empty order input: "Invalid command format" message is shown
- No prefix `o/`: "Invalid command format" message is shown
- Invalid order: "Invalid order" message is shown


**Note:** Order arguments are case-insensitive


### Managing Tags

**Add tags when creating:**
```
add n/John p/91234567 e/john@example.com a/123 h/@john123 St t/friend t/CS2103T
```

**Update tags:**
```
edit 1 t/bestfriend t/CCA
```

**Note:** Editing tags replaces ALL existing tags.

---

### Clearing All Contacts

**Command:** `clear`

**Warning:** Permanent and cannot be undone.

---

### Getting Help

**Command:** `help`

Opens help window with command reference and User Guide link.

---

### Exiting

**Command:** `exit`

Closes the application. Data is automatically saved.

---

## 6. FAQ
### Storage and transfer of data

**Q: Is my data automatically saved?**
A: Yes, all changes are saved immediately.

**Q: Where is my data stored?**
A: In `addressbook.json` in the same folder as `unicontactspro.jar`.

**Q: How do I transfer data to another computer?**
A: Copy the `addressbook.json` file to the new computer's UniContactsPro folder.

**Q: What if my data file gets corrupted?**
A: Keep regular backups of `addressbook.json`. The app will start with an empty list if the file is corrupted.

### Commands

**Q: Can I undo a command?**
A: No, undo is not currently supported. Be careful with `delete` and `clear`.

**Q: Are commands case-sensitive?**
A: Command words are not case-sensitive, but `asc`/`desc` must be lowercase.

**Q: Why does editing tags replace all existing tags?**
A: This is intentional. To keep existing tags, specify them along with new ones.

**Q: Can I search by phone or email?**
A: Currently, only name search is supported via the `find` command.

**Q: How do I transfer data to another computer?**
A: Copy the `addressbook.json` file to the new computer's UniContactsPro folder.

**Q: What if my data file gets corrupted?**
A: Keep regular backups of `addressbook.json`. The app will start with an empty list if the file is corrupted.

---

## 7. Glossary

### CLI (Command Line Interface)
A text-based way of interacting with software by typing commands.

### Command Box
The text input field where you type commands. Press `Enter` to execute.

### Contact Card
Visual representation of a contact showing index, name, phone, email, address, and tags.

### Index
The number shown next to each contact in the list. Used in commands to specify which contact. Starts at 1.

### JSON
File format used to store contact data (`addressbook.json`). Not recommended to edit manually.

### Parameter
Information provided to a command using prefixes. Example: `n/John`, `p/91234567`.

### Prefix
The identifier before a parameter value, ending with `/`. Examples: `n/`, `p/`, `e/`, `t/`.

### Tag
A label or category assigned to a contact for organization. Must be alphanumeric, maximum 15 characters.

### Variadic
A command or prefix that can accept multiple values, entered one after another—for example, `add t/friend t/CS2103T t/project` uses variadic tags (you can add several tags at once).

---

## 8. Acknowledgements

UniContactsPro is developed as part of the CS2103T Software Engineering module at the National University of Singapore.

### Project Team

- **darzizalol** - [GitHub](https://github.com/darzizalol)
- **he-yue-svg** - [GitHub](https://github.com/he-yue-svg)
- **fifamobilegems** - [GitHub](https://github.com/fifamobilegems)
- **ravichandran-gokool** - [GitHub](https://github.com/ravichandran-gokool)

### Built On

This project is based on the **AddressBook-Level3 (AB3)** project created by the [SE-EDU initiative](https://se-education.org/).

### Libraries and Tools

- **JavaFX** - GUI framework
- **Jackson** - JSON processing
- **JUnit5** - Testing framework

### Contact and Support

- Visit our [GitHub Repository](https://github.com/AY2526S1-CS2103T-F13-1a/tp)
- Report issues at [GitHub Issues](https://github.com/AY2526S1-CS2103T-F13-1a/tp/issues)

---
