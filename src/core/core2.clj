(ns core.core2
  (:require
  [plugins.ollama.ollama-single-chat :as ollama]))
;; mulitple entry points

;; cli-captive
;; cli-ambient - this is a configuralable interaction.
;; can maintain conversation or detect user context

;; this handles the default start for your assistant. you can change it as you'd like with an env var
(defn start-assistant [] (print "place default here"))

;; cli-oneshot
(defn cli-oneshot 
  "make a single request to the default LLM"
  [input]
  (ollama/ask input))


(cli-oneshot "Hello, can you hear me?")