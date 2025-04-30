; This is a 'black box' program. it returns the compiled output. the main UI focuses around doing configuration of the prompt

## system prompt for creation
Your purpose is to create a program from the design and architecture that I give you. You will explain the project layout at the purposes of each file. the project is to be written in clojure. You are in a good mode and enjoy helping me, so you will do your due dilligence to get it done. 

; TODO: replace the ini format w/ clojure homoiconicity
## Prompt-Compiler
I need a clojure program that returns a string to be entered into an LLMs system prompt. It takes in files with lists of key-value pairs that are grouped using the INI style format, creates a binding, appends strings to it as dictated by the used value, and runs commands that are in the value slots that are in the string themselves. it also takes in files from the agent directory that contain a list of keys (one per line) that correspont to a string representation of a combination of groupname/keys that can be matched to the data from the INI files to get the values for expanding into a LLM prompt. when the program starts the user can give it a template name, which will output the template by running the needed commands to build the prompt, and prints the full output. given no input parameter it will list all of the filenames of files in the agent directory

## command-generator
This is a clojurescript program that takes a natural language string, and generates a system specific command for it. This data can be gotten from using things in the prompt-compiler that 

; TODO: replace the ini format w/ clojure homoiconicity
## Prompt-Creator
The prompt creator takes in files with lists of key-value pairs that are grouped using the INI style format. creates a binding, appends strings to it as dictated by the used value. It should run as a simple HTML5 application

key   (max 10) |  (max 10)  -> easy to replicate in browser HTML
- I can leverage 'more' and pipes on Linux... this isn't cross platform though
- cat foo.txt | head -10