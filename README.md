
# Perplexitor - A Simple Editor

A simple Android text editor app that lets users create, open, and save plain text documents.

## Features

- Create a new empty document
- Open an existing text file (.txt) from device storage
- Save the current document as a text file to device storage
- Runtime permission request for external storage access on Android
- Basic user interface with EditText for editing, and buttons for New, Open, and Save

## Requirements

- Android device or emulator running API level 19 (KitKat) or higher
- Runtime permission to write to external storage (handled by the app)

## How to Use

1. Tap **New** to clear the editor and create a new document.
2. Tap **Open** to choose a text file to load into the editor.
3. Tap **Save** to save the current text; this will prompt you to select or create a file location.

## Implementation Details

- Uses Android Storage Access Framework for file operations (ACTION_CREATE_DOCUMENT and ACTION_OPEN_DOCUMENT).
- Handles IOException during file read/write with user feedback via Toast.
- Requests `WRITE_EXTERNAL_STORAGE` permission at runtime if not granted.
- Uses `EditText` for text input and `Button`s for user actions.
- Supports only plain text files (`text/plain` MIME type).

## Project Structure

- `MainActivity.java` — Main activity with UI and file management logic.
- Layout file `activity_main.xml` with `EditText` and three buttons for New, Open, Save.


