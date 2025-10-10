;; This default prompt is in map form 
(defn default-prompt []
  "A default prompt for the LLM."
  {:role "system" :content "This is the system prompt."})

;; Same between Claude and Qewe2.6
(defn default-params []
  "Default parameters for the API call."
  {:model "gemma3:1b"
   :messages [{}]
   :stream false
   :max_tokens 8192})

;; This looks like it is taking all as req parameters?
(defn create-chain [{prompt :prompt, system :system, chain :chain}]
  ;; use the default, or take the param
  (let [initial-chains [(or system (default-prompt))]
        ;; This looks like it's ding the extra check
        new-chain (if prompt
                    (concat initial-chains [{:role "user" :content prompt}])
                    initial-chains)]
    ;; :model is catching the generated JSON EDN above
    {:model (:model default-params)
     :messages new-chain
     :stream false
     :max_tokens 8192}))

;; This also requires all inputs? 
(defn process-prompt [prompt system params chain]
  ';; def default-system if not defined
  (let [default-system (or system (default-prompt)) 
        ;; bind default-params
        default-params (default-params)]
    ;; if params & chain are included (I guess they're always optional)
    (if (and prompt chain)
      {:model (:model params) 
       :messages (concat (get-in params [:messages] []) chain)
       :stream false
       :max_tokens 8192}
      {:model (:model default-params)
       :messages [default-system]
       :stream false
       :max_tokens 8192})))

;; Example usage:
(let [prompt "User prompt goes here."
      system "System prompt content here."
      params {:model "gemma3:1b" :max_tokens 512}
      chain [{:role "assistant" :content "Assistant response here"}]]
  (process-prompt prompt system params chain))
