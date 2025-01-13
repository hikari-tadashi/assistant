(ns navi.plugins.system.clipboard
  (:import [java.awt Toolkit] 
           [java.awt.datatransfer Clipboard]
           [java.awt.datatransfer StringSelection]
           [java.awt.datatransfer Transferable DataFlavor ClipboardOwner]))
  

(defn copy-to-clipboard [content]
  (let [clipboard (.getSystemClipboard (Toolkit/getDefaultToolkit))
        string-selection (StringSelection. content)]
    (.setContents clipboard string-selection nil)))

(defn get-clipboard-string []
  (let [clipboard (.getSystemClipboard (Toolkit/getDefaultToolkit))]
    (.getData clipboard DataFlavor/stringFlavor)))

;(copy-to-clipboard "Better is bread with a happy heart, then wealth with vexation")
;(println (format "Clipboard contents: %s" (get-clipboard-string)))