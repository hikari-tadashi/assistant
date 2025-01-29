; I am using this as scratch space to 'compile' my assistant personality system prompt. I currently need to add tools, and the baseline personality primer. 

; I can see in the future adding in guardrails, time context, etc. both at the beginning, and on the fly. 

(ns plugins.sandbox.assistant
  (:require [plugins.personality.personality :as personality]))

; Start w/ basic personality? 
(def uncompiled-system-prompt personality/assistant)

; should this be called from the tools plugin
(defn add-tools [prompt]
  )