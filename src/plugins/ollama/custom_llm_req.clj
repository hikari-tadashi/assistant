(ns plugins.ollama.custom-llm-req
  (:require [clojure.walk :as walk]))

;; I might want to store this somewhere else, and have a centralized reference point
(def default-system-prompt "You are a helpful assistant")
;; This is basically the same as above, just formatting it for a prompt
(defn system-message [content]
  {:role "system" :content content})

;; simple function for making our user map for the stack
(defn user-message [content]
  {:role "user" :content content})

(def default-model "gemma3:1b")

;; This also should be moved to a central point, a default EDN for the param JSON
;; Perhaps, call this from 'custom_paramater_generater' or have it too ref from a central source
(def default-params
  {:model default-model
   ;; This line mignt not *yet* be doable
   ;; Oh, I see, I can just *not yet* initialize it
   ;;:messages messages
   :stream false
   :max_tokens 8192
   :frequency_penalty 0
   :presence_penalty 0
   :temperature 0.7
   :top_p 0.95})


(defn build-llm-request
  "Builds an Ollama API request (should be OpenAI format) with flexible parameters
   Alaways a prompt, if there's a chain add to it, if not then replace it.
   parameters get merged if not provided   
   
  Parameters:
  - prompt: (required) User prompt string
  - system: (optional) System prompt string, defaults to default-system-prompt
  - chain: (optional) Existing conversation chain (vector of message maps)
  - params: (optional) Additional parameters to merge with defaults
  
  Returns: A map ready to send to the LLM API"

  [prompt & {:keys [system chain params]
             :or {system default-system-prompt ;; This uses the 'default' as defined above, move to central loc
                  chain nil ;; if Chain not given, it's nil
                  params {}}}]

  ;; let = temp bind 
  (let [messages (cond ;; starts the eval logic 
                   (seq chain) ;; first step, break down chain (to find system/inject messages)
                   (if system ;;If there's a system prompt already
                     ;; system-message is an expression, not a binding
                     (into [(system-message system)] ;; replace the existing one w/ the system message here 
                           (concat (rest chain) ;; add the rest of the data back to the new sys message (clojure immutable struct 
                                   [(user-message prompt)])) ;; user-message is an expression, not a binding. create the new user prompt map
                     (conj chain (user-message prompt))) ;; add the newest prompt to the end
                   :else ;; Else, make a simple chain of the system message, and the first prompt
                   ;; Note that both are wrapped in a vector, thus producing it in a usable form 
                   [(system-message system) ;; make the system prompt map
                    (user-message prompt)]) ;; make the user prompt map 

        ;; Merge params w/ default, means that new params overwrite the default, but the default is the fallback 
        final-params (merge default-params
                            params
                            {:messages messages})]

    ;; final-params is what gets returned to be sent as the comprehensive JSON/EDN for the API call 
    final-params))


                   
              
                   