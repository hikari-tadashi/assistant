(ns plugins.memory.thread-manager
  (:require [plugins.memory.memory-ring :as memory-ring]
            [plugins.memory.chat-memory :as chat-memory]
            [plugins.personality.personality :as personality]
            [plugins.sandbox.util :as util]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defrecord ThreadManager [threads current-thread])

; Global thread manager instance
(def thread-manager (atom (ThreadManager. {} nil)))

; Storage directory for thread files
(def threads-dir "memory/threads/")

; Ensure threads directory exists
(defn ensure-threads-dir []
  (when-not (.exists (io/file threads-dir))
    (.mkdirs (io/file threads-dir))))

; Save thread to file (forward declaration fixed by moving up)
(defn save-thread [thread-name]
  (ensure-threads-dir)
  (when-let [thread-data (get-in @thread-manager [:threads thread-name])]
    (let [file-path (str threads-dir thread-name ".edn")
          ; Convert atoms to regular data for serialization
          serializable-data (-> thread-data
                                (assoc :running-log @(:running-log thread-data))
                                (assoc-in [:memory-ring :memory-vectors :current-branch] 
                                         @(get-in thread-data [:memory-ring :memory-vectors :current-branch])))]
      (spit file-path (pr-str serializable-data)))))

; Delete thread file (forward declaration fixed by moving up)
(defn delete-thread-file [thread-name]
  (let [file-path (str threads-dir thread-name ".edn")]
    (when (.exists (io/file file-path))
      (.delete (io/file file-path)))))

; Create a new thread with given name and optional personality
(defn create-thread [thread-name & [personality-type]]
  (ensure-threads-dir)
  (let [personality-content (or personality-type personality/standard)
        new-thread {:name thread-name
                    :personality personality-content
                    :memory-ring (memory-ring/->Memory-Ring {:current-branch (atom [])})
                    :running-log (atom [{:role "system" :content personality-content}])
                    :created-at (java.util.Date.)
                    :last-active (java.util.Date.)}]
    (swap! thread-manager 
           #(-> %
                (assoc-in [:threads thread-name] new-thread)
                (assoc :current-thread thread-name)))
    (save-thread thread-name)
    (println (format "%sCreated thread '%s'%s" util/GREEN thread-name util/RESET))
    thread-name))

; Switch to an existing thread
(defn switch-thread [thread-name]
  (if (get-in @thread-manager [:threads thread-name])
    (do
      (swap! thread-manager assoc :current-thread thread-name)
      (swap! thread-manager assoc-in [:threads thread-name :last-active] (java.util.Date.))
      ; Update the global assistant atom to point to this thread's running-log
      (reset! (:running-log chat-memory/assistant) 
              @(get-in @thread-manager [:threads thread-name :running-log]))
      (println (format "%sSwitched to thread '%s'%s" util/BLUE thread-name util/RESET))
      thread-name)
    (do
      (println (format "%sThread '%s' not found. Use /threads list to see available threads.%s" 
                util/RED thread-name util/RESET))
      nil)))

; Get current thread name
(defn get-current-thread []
  (:current-thread @thread-manager))

; Get current thread data
(defn get-current-thread-data []
  (when-let [current-name (get-current-thread)]
    (get-in @thread-manager [:threads current-name])))

; List all available threads
(defn list-threads []
  (let [threads (:threads @thread-manager)
        current (:current-thread @thread-manager)]
    (if (empty? threads)
      (println (format "%sNo threads found. Create one with /thread create <name>%s" 
                util/YELLOW util/RESET))
      (do
        (println (format "%sAvailable threads:%s" util/CYAN util/RESET))
        (doseq [[name thread-data] threads]
          (let [active-marker (if (= name current) " *" "  ")
                last-active (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm") 
                                   (:last-active thread-data))]
            (println (format "%s%s%s%s (last active: %s)" 
                           (if (= name current) util/GREEN util/WHITE)
                           active-marker
                           name
                           util/RESET
                           last-active))))))))

; Rename a thread
(defn rename-thread [old-name new-name]
  (if (get-in @thread-manager [:threads old-name])
    (if (get-in @thread-manager [:threads new-name])
      (println (format "%sThread '%s' already exists%s" util/RED new-name util/RESET))
      (do
        (let [thread-data (get-in @thread-manager [:threads old-name])]
          (swap! thread-manager 
                 #(-> %
                      (assoc-in [:threads new-name] (assoc thread-data :name new-name))
                      (update :threads dissoc old-name)
                      (assoc :current-thread (if (= (:current-thread %) old-name) 
                                               new-name 
                                               (:current-thread %)))))
          ; Remove old file and save new one
          (delete-thread-file old-name)
          (save-thread new-name)
          (println (format "%sRenamed thread '%s' to '%s'%s" 
                   util/GREEN old-name new-name util/RESET)))))
    (println (format "%sThread '%s' not found%s" util/RED old-name util/RESET))))

; Delete a thread
(defn delete-thread [thread-name]
  (if (get-in @thread-manager [:threads thread-name])
    (do
      (swap! thread-manager 
             #(-> %
                  (update :threads dissoc thread-name)
                  (assoc :current-thread (if (= (:current-thread %) thread-name) 
                                           nil 
                                           (:current-thread %)))))
      (delete-thread-file thread-name)
      (println (format "%sDeleted thread '%s'%s" util/RED thread-name util/RESET))
      ; If we deleted the current thread, show available threads
      (when (nil? (get-current-thread))
        (println (format "%sNo current thread. Switch to another or create a new one.%s" 
                 util/YELLOW util/RESET))
        (list-threads)))
    (println (format "%sThread '%s' not found%s" util/RED thread-name util/RESET))))


; Load thread from file
(defn load-thread [thread-name]
  (let [file-path (str threads-dir thread-name ".edn")]
    (when (.exists (io/file file-path))
      (try
        (let [thread-data (edn/read-string (slurp file-path))
              ; Convert regular data back to atoms
              restored-data (-> thread-data
                                (assoc :running-log (atom (:running-log thread-data)))
                                (assoc-in [:memory-ring :memory-vectors :current-branch] 
                                         (atom (get-in thread-data [:memory-ring :memory-vectors :current-branch]))))]
          restored-data)
        (catch Exception e
          (println (format "%sError loading thread '%s': %s%s" 
                   util/RED thread-name (.getMessage e) util/RESET))
          nil)))))


; Load all threads from disk
(defn load-all-threads []
  (ensure-threads-dir)
  (let [thread-files (filter #(.endsWith (.getName %) ".edn") 
                            (.listFiles (io/file threads-dir)))]
    (doseq [file thread-files]
      (let [thread-name (.substring (.getName file) 0 
                                   (- (.length (.getName file)) 4))]
        (when-let [thread-data (load-thread thread-name)]
          (swap! thread-manager assoc-in [:threads thread-name] thread-data))))
    (println (format "%sLoaded %d threads from disk%s" 
             util/GREEN (count thread-files) util/RESET))))

; Save all threads to disk
(defn save-all-threads []
  (doseq [thread-name (keys (:threads @thread-manager))]
    (save-thread thread-name)))

; Initialize with a default thread if none exist
(defn init-thread-manager []
  (load-all-threads)
  (when (empty? (:threads @thread-manager))
    (create-thread "default" personality/standard))
  ; Set current thread to first available if none set
  (when (nil? (:current-thread @thread-manager))
    (when-let [first-thread (first (keys (:threads @thread-manager)))]
      (switch-thread first-thread))))

; Update current thread's running log (called after chat interactions)
(defn sync-current-thread []
  (when-let [current-name (get-current-thread)]
    (reset! (get-in @thread-manager [:threads current-name :running-log])
            @(:running-log chat-memory/assistant))
    (swap! thread-manager assoc-in [:threads current-name :last-active] (java.util.Date.))
    (save-thread current-name)))