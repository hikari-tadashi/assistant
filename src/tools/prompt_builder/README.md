# File: README.md

# Clojure Prompt Builder

This program generates LLM system prompts by combining data from INI files
and instructions from agent template files.

## TODO: Allow agents to be built from lists of other agents

## Project Structure

-   `project.clj`: Leiningen project configuration.
-   `src/`: Contains the Clojure source code.
    -   `prompt_builder/core.clj`: Main entry point and argument handling.
    -   `prompt_builder/parser.clj`: Logic for parsing INI files.
    -   `prompt_builder/processor.clj`: Logic for processing templates and executing commands.
-   `data/`: Place your `.ini` files here. Sections and keys will be merged.
-   `agents/`: Place your agent template files here. Each file contains `GroupName/KeyName` pairs, one per line.

## Prerequisites

-   Java Development Kit (JDK)
-   [Leiningen](https://leiningen.org/) (Clojure build tool)

## Setup

1.  Clone the repository (or create the files as shown).
2.  Ensure the `data/` and `agents/` directories exist.
3.  Add your `.ini` files to `data/`.
4.  Add your template files to `agents/`.

## Usage

Navigate to the project's root directory (`prompt-builder/`) in your terminal.

**List Available Templates:**

Run the program without arguments:

```bash
lein run
```

This will list the filenames found in the `agents/` directory.

**Generate a Prompt:**

Run the program with the name of an agent template file (without the directory path) as an argument:

```bash
lein run <template_name>
```

For example, if you have a template file named `agents/my_agent`:

```bash
lein run my_agent
```

The program will:
1. Read all `.ini` files from the `data/` directory.
2. Read the `agents/my_agent` file.
3. Look up each `GroupName/KeyName` from the template in the loaded data.
4. Execute any commands found within the values (e.g., `${command}`).
5. Print the resulting concatenated prompt string to standard output.

## Example

**`data/config.ini`:**
```ini
[Details]
User = Bob
Timestamp = ${date +%H:%M:%S}

[Instructions]
Task = Summarize the input.
Format = JSON
```

**`agents/summarizer`:**
```
Details/User
Instructions/Task
Instructions/Format
Details/Timestamp
```

**Command:**
```bash
lein run summarizer
```

**Output (example):**
```
Bob
Summarize the input.
JSON
14:32:15
```
(Timestamp will vary)

## Notes
* The INI parser uses keywords for section and key names (e.g., `:Details`, `:User`).
* Commands are executed via `bash -c "command"`. Ensure the necessary commands are available in your shell environment.
* Error handling for file reading and command execution is basic. Errors are printed to stderr, and placeholder error messages might appear in the output prompt.
