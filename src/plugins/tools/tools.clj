(ns plugins.tools.tools)

; I may need to check for tools in external directories

(def demo-tool {:type "function"
                 :function {
                            :name "demo-tool"
                            :description "check the internet for a search"
                            :parameters {
                                         :type "object"
                                         :properties {
                                                      :order_id {
                                                                 :type "string"
                                                      }}
                                         :required ["order_id"]}}})

(defn add-tools-to-prompt
  "This is a way to add a list of tool definitions to the prompt"
  []
  ) 

(defn extract-tool
  "This function extracts all tools from a given string"
  [response])

(defn check-for-tool-loop 
  "This expression checks the output of chat-with-assistant, and if it needs tools, it gives a tool response, runs the tool, and adds the message to the result w/ the normal response"
  [response]
  (if (contains? response "<tools>")
    (do (extract-tool response))
    response))