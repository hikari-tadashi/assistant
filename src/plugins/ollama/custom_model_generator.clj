(ns plugins.ollama.custom-model-generator)

;; should have an entrypoint for prompt-builder
;; should have an entrypoint for memory
;; - call stack builder for a basic stack


;; stack-builder
;; generate me a new stack, for the model from 'prompt-builder', or a file

;; the ongoing conversation SHOULDN"T be handled here, but in the memory plugin,
;; which can call 'ollama' if it chooses? or should it be ollama memory module


;; - takes in a custom prompt (one block of the stack?)
(defn generate-conversation-with-custom-model [prompt params])


(defn ask-agent [prompt [&personality params]]
  "a simple interface (for scratch demo) that lets me ask an agent by inserting
   a question (prompt), personality (prompt_builder), and parameters (params)"
  
  ;; ask a fully built personality of any flavor
  ;; Task agent
  ;; Character for play
  ;; Multi-step agents
  )
