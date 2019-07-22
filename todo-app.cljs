(ns hello2.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

;;; why choose vector for the todo-items ?


(def todo-entry-key (reagent/atom  0))
(def todo-entry-val (reagent/atom ""))
(def todo-items (reagent/atom [] ))

(defn discard-key-from-vector [v n]
  (apply vector (filter (fn [x] (not (= (get x :key) n)))
                        v)))

;; (defn flip-done-item [v item]
;;   (let [the-key (get item :key)]
;;   (conj (apply vector (filter (fn [x] (not (= (get x :key) the-key)))
;;                               v))
;;         {:key (get item :key)
;;          :val (get item :val)
;;          :done (not (get item :done))})))


;; ;; count number of items
(defn count-all-tasks []
  (count @todo-items))

;; ;; count completed tasks
(defn count-completed-tasks []
  (count (filter (fn [e]
                   (get e :done))
                 @todo-items)))



;; ;; count outstanding tasks - filter items that have :done = false
(defn count-outstanding-tasks []
  (count (filter
          (fn [e]
            (not (get e :done)))
          @todo-items)))



;; try map with destructuring
(defn flip-done-item [v-items item]
  (let [k (get item :key)]
    (mapv
     (fn [an-item]
       (let [ek (get an-item :key)
             ev (get an-item :val)
             ed (get an-item :done)]         
       (if (= k ek)
         {:key ek :val ev :done true}
         {:key ek :val ev :done ed})))
     v-items)))


(defn todo-items-component []
  (conj [:ul {:style {:list-style "none"}}]
        (for [item @todo-items]
          ^{:key (get item :key)}
          
          [:li
           (if (get item :done)
               {:style {:text-decoration "line-through"}})
            [:div
             [:button
              {:on-click (fn [ev]
                           (reset! todo-items
                                   (flip-done-item @todo-items item))
                           (println "item is done ?")
                           )}
              "done"]
             (get item :val)
             [:button
              {:on-click (fn [ev]
                           (reset! todo-items (discard-key-from-vector
                                               @todo-items (get item :key)))
                           (println @todo-items))}
              "delete"]
             ]
            ]
          )
        )
  )


(defn todo-add-item [x]
  (reset! todo-items
          (conj @todo-items
                {:key @todo-entry-key
                 :val @todo-entry-val
                 :done false}))
  (swap! todo-entry-key inc)
  (println "todo-entry-key" @todo-entry-key)
  )

(defn todo-entry-component []
  [:input {:type "text"
           :value @todo-entry-val
           :on-change (fn [ev]
                        ;;(println (str "you entered " (-> ev .-target .-value)))
                        (reset! todo-entry-val (-> ev .-target .-value))
                        )
           :placeholder "something to do..."
           :on-key-up (fn [ev] (and
                                (not (= "" (-> ev .-target .-value)))
                                (if (= "Enter" (.-key ev))
                                 [
                                  (todo-add-item (-> ev .-target .-value))
                                  (reset! todo-entry-val "")
                                  ])))
           }])


(defn hello-world []
  (set! (.-title js/document) "ToDo Application")
  [:div
   [:h1 "To Do Application"] 
   [:p (str "you have "
            (count-outstanding-tasks) " tasks outstanding "
            " and completed " (count-completed-tasks) " in a total of "
            (count-all-tasks) " tasks.") ]

   ;;(println (count-outstanding-tasks))
   ;;[:p (str "you have " (count-outstanding-tasks) " tasks outstanding.") ]
   ;;[:p (str "you have " (count-completed-tasks) " tasks completed.") ]
   
   [todo-entry-component]

   [todo-items-component]
   
   ;;[task-list-components]
   ;;[task-list-components2]
   ;;[task-list-components3]
   ;;[task-list-components4]
   ;;[task-list-components5]
   ;;[task-list-components6]
   ;;[task-list-components7]
   
   
   ;;[:p "and we got to the end!"]
   
   ])

  
(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
