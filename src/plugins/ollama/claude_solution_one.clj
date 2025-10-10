(ns your-namespace.llm
  (:require [clojure.walk :as walk]))

;; Default values
(def default-system-prompt "You are a helpful assistant.")

(def default-params
  {:model "gemma3:1b"
   :stream false
   :max_tokens 8192})

;; Helper functions to build message maps
(defn system-message [content]
  {:role "system" :content content})

(defn user-message [content]
  {:role "user" :content content})

(defn assistant-message [content]
  {:role "assistant" :content content})

;; Core function - idiomatic Clojure approach
(defn build-llm-request
  "Builds an LLM API request with flexible parameters.
  
  Parameters:
  - prompt: (required) User prompt string
  - system: (optional) System prompt string, defaults to default-system-prompt
  - chain: (optional) Existing conversation chain (vector of message maps)
  - params: (optional) Additional parameters to merge with defaults
  
  Returns: A map ready to send to the LLM API"
  [prompt & {:keys [system chain params]
             :or {system default-system-prompt
                  chain nil
                  params {}}}]
  (let [;; Build the messages chain
        messages (cond
                   ;; If chain is provided, use it and append prompt
                   (seq chain)
                   (if system
                     ;; Replace system message (first item) if new system provided
                     (into [(system-message system)]
                           (concat (rest chain)
                                   [(user-message prompt)]))
                     ;; Just append user message
                     (conj chain (user-message prompt)))
                   
                   ;; No chain provided, build from scratch
                   :else
                   [(system-message system)
                    (user-message prompt)])
        
        ;; Merge params with defaults
        final-params (merge default-params
                           params
                           {:messages messages})]
    final-params))

;; Alternative: More explicit multi-arity version
(defn build-llm-request-v2
  "Multi-arity version for even clearer usage"
  ;; Just prompt - use all defaults
  ([prompt]
   (build-llm-request-v2 prompt default-system-prompt nil {}))
  
  ;; Prompt + system
  ([prompt system]
   (build-llm-request-v2 prompt system nil {}))
  
  ;; Prompt + system + chain
  ([prompt system chain]
   (build-llm-request-v2 prompt system chain {}))
  
  ;; All parameters
  ([prompt system chain params]
   (let [messages (if (seq chain)
                    (if system
                      (into [(system-message system)]
                            (concat (rest chain)
                                    [(user-message prompt)]))
                      (conj chain (user-message prompt)))
                    [(system-message system)
                     (user-message prompt)])
         final-params (merge default-params
                            params
                            {:messages messages})]
     final-params)))