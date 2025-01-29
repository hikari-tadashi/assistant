; we are treating memory as if it were an AI primative
(ns plugins.memory.memory-ring
  (:require [plugins.memory.memory-manipulation :as mm]))

; concentric rings... maybe the rings and their interactions can be inspected via graph theory
(defrecord Memory-Ring [memory-vectors])

; I think I'm gonna need crud versions of these
; This needs 
(def memory-ring [Memory-Ring. {:current-branch (atom [])}])

; Insert a new item into the memory ring. If it doesn't exist already,
; create a new conversation branch.
(defn memory-ring-insert [new-item]
  (let [current-branch (:current-branch @memory-ring)
        ; Create a copy of the current conversation state for the new branch
        new-branch (conj (or current-branch []) {:item new-item})
        updated-memory-ring (assoc @memory-ring :current-branch new-branch)]
    (reset! memory-ring updated-memory-ring)))

; Branch off from an existing point in the conversation.
(defn memory-ring-branch [prompt]
  ; Create a copy of the current conversation state for the new branch
  (let [new-branch {:prompt prompt}
        updated-memory-ring (assoc @memory-ring :current-branch new-branch)]
    (reset! memory-ring updated-memory-ring)))

; Merge two branches. For now, just append the second branch to the first.
(defn memory-ring-merge [branch1 branch2]
  ; Append branch2 to branch1
  {:items (concat (:items branch1) (:items branch2))})

; Navigate between conversation branches.
(defn memory-ring-navigate []
  @memory-ring)

; Get the current conversation branch.
(defn get-current-branch []
  (:current-branch @memory-ring))

; Print the entire memory ring for debugging purposes.
(defn print-memory-ring []
  (doseq [branch (:items (get-current-branch)) :let [index (inc (.indexOf (get-current-branch) branch))]]
    (println "Branch" index ":")
    (doseq [item (or (:items branch) [])]
      (println item))))