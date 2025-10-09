(ns plugins.memory2.stack-builder)

; create a new message (?)
(defn add-new-message [message]
  (let [new-map {:role "user" :content message}]
    ; take the existing chat (no longer empty) and add the new text to it
    (conj empty-chat new-map)))