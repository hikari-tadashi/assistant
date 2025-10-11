(ns plugins.sandbox.scratch
  (:require [plugins.ollama.ollama-single-chat :as ollama]
            [plugins.ollama.custom-llm-req :as custom]))

;;(ollama/ask "Hello, how are you?")

(ollama/ask-custom-json
 (plugins.ollama.custom-llm-req/build-llm-request "Hello, do you understand me?"))

(print "Test")
