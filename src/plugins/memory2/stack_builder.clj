(ns plugins.memory2.stack-builder)


;; All I am dealing with is a JSON object describing a list of 2 value maps in a vector
;; literally [(role: "text", data: text)]
;; 
;; can be transformed as needed
;; generic enough to build on? 

; create an preconfigured baseline chat
; This is where personality is getting called
(def empty-chat [{:role "system" :content personality/standard}])

; create a new message (?)
(defn add-new-message [message]
  (let [new-map {:role "user" :content message}]
    ; take the existing chat (no longer empty) and add the new text to it
    (conj empty-chat new-map)))