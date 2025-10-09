# Assistant (Navi)

## EASY ENTRY

Go to src/plugins/sandbox/scratch to play around with ask(), ask-agent(), and others


A command-line AI assistant built in Clojure, designed to interact with local LLM servers (LM Studio, Jan, Cortex, or llama.cpp). Features a plugin-based architecture with personality switching, chat memory, and extensible command system.

## Features

- **Chat Interface**: Interactive TUI for conversing with your AI assistant
- **Plugin System**: Modular architecture with support for custom plugins
- **Personality Switching**: Change assistant behavior and personas on the fly
- **Memory Management**: Maintains conversation context across interactions
- **Command System**: Extensible command framework for adding custom functionality
- **Local LLM Support**: Connect to self-hosted language models
- **Prompt Builder**: Advanced prompt construction and processing

## Prerequisites

- [Clojure](https://clojure.org/guides/getting_started)
- [Leiningen](https://leiningen.org/)
- Local LLM server (LM Studio, Jan, Cortex, or llama.cpp)

## Installation

```bash
git clone https://github.com/Tadashi-Hikari/assistant
cd assistant
```

## Usage

### Interactive Mode

Start the interactive chat interface:

```bash
lein run
```

### Single Question Mode

Ask a single question without entering the interactive loop:

```bash
lein run -q "Your question here"
```

## Configuration

Edit `src/plugins/personality/personality.clj` to configure:

- **LLM Server URL**: Default is `http://localhost:1234`
- **Model**: Default is `llama-3.2-8b-instruct`
- **Assistant Name**: Default is `Megaman`
- **Personalities**: Choose from predefined personas or create your own

### Available Personalities

- `standard` - Helpful assistant for schedule, projects, and ADHD management
- `enlisted` - Succinct, military-style responses
- `assistant` - Focus on ADHD, productivity, and time management
- `dev` - Software development problem-solving
- `netnavi-dev` - Clojure/ClojureScript project assistance
- `guru` - Ethical and spiritual guidance

## Project Structure

```
assistant/
├── src/
│   ├── core/              # Core application logic
│   │   ├── core.clj       # Main entry point and REPL loop
│   │   └── features.clj   # Command system and plugin loader
│   └── plugins/           # Plugin modules
│       ├── memory/        # Chat memory and storage
│       ├── personality/   # Personality switching and prompts
│       ├── prompt_builder/ # Prompt construction system
│       ├── sandbox/       # Utility functions and subprocess handling
│       └── tools/         # Tool integrations
├── project.clj            # Leiningen project configuration
└── README.md
```

## Plugin System

Available command namespaces:
- `core.features` - Core commands (help, exit, clear)
- `plugins.memory.memory-manipulation` - Memory operations
- `plugins.personality.switch` - Personality switching
- `plugins.sandbox.clipboard` - Clipboard operations

Add new commands by implementing public functions in these namespaces. The command system automatically discovers and executes them.

## Development

### Building

```bash
lein uberjar
```

### Running Tests

```bash
lein test
```

## Roadmap

- [ ] ClojureScript web interface
- [ ] Mobile device self-hosting
- [ ] Enhanced tool integrations
- [ ] Persistent memory storage
- [ ] Multi-modal support

## License

MIT License - see LICENSE file for details

## Contributing

Contributions welcome! Please open an issue or submit a pull request.

## Acknowledgments

Built with:
- [openai-clojure](https://github.com/wkok/openai-clojure) - OpenAI-compatible API client
- [clj-http](https://github.com/dakrone/clj-http) - HTTP client
- [tools.cli](https://github.com/clojure/tools.cli) - Command-line parsing
