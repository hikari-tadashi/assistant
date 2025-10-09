(ns plugins.sandbox.scratch
  (:require [plugins.ollama.ollama-single-chat :as ollama]))

(ollama/ask "Hello, how are you?")