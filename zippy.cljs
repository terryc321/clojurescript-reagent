;;
;; npm install -g google-closure-library
;;
;;

(ns zippy.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   [goog.dom :as dom]
   [goog.style :as style]
   [goog.dom.TagName :as tagname]
   [goog.ui.AnimatedZippy :as azippy]
   
   )
            
  (:import
   [goog.ui Zippy]
   [goog.crypt Sha1]
   ))



(enable-console-print!)

(println "This text is printed from src/zippy/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

;;
;;-------------------------------------------------------------------------
;;
;; shameless copied from 
;; https://gist.github.com/prio/7528880
;; notepad.cljs
;;
;;
;; (:require [goog.dom :as dom])
;; (:import [goog.ui Zippy]))
;;
;;
(defn create-note [item el]
  (let [data (js->clj item)]
    {:title (data "title") :content (data "content") :parent el}))

(defn make-note-dom [note]
  (let [header-el (dom/createDom "div" 
                                 (clj->js {"style" "background-color:#EEE"})
                                 (:title note))
        content-el (dom/createDom "div" nil (:content note))
        new-note (dom/createDom "div" nil header-el content-el)]
    (dom/append (:parent note) new-note)
    (Zippy. header-el content-el)))

(defn ^:export makeNotes [notes container]
    (doseq [note notes]
(make-note-dom (create-note note container))))

;;
;;-------------------------------------------------------------------------
;;

;; goog.require('goog.dom');
;; goog.require('goog.dom.TagName');

;; function sayHi() {
;;   var newHeader = goog.dom.createDom(goog.dom.TagName.H1, {'style': 'background-color:#EEE'},
;;     'Hello world!');
;;   goog.dom.appendChild(document.body, newHeader);
;; }

;;(defn sayHi []
;;  (let [newHeader (dom/createDom tagname/H1 "my header sayHi")]
;;   (dom/appendChild js/document.body newHeader)))


;;(defn sayHi []
;;  (js/document.body


(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:div {:id "special"}]
   [:h3 {:id "the-head"} "has it changed yet"]
   [:p {:id "the-content" :made-up-tag "mr potato head"} "this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text "]
   [:h3 {:id "the-head2"} "has it changed yet"]
   [:p {:id "the-content2" :made-up-tag "mr potato head"} "this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text this is the text "]
   [:h3 {:id "myheader"} "this is my header"]
   [:div {:class "zippy1"}
   [:div {:class "content1" :style {:display "none"}} "this is some hidden text" ]]

   ;;[make-note-dom (create-note {:content "this is the content" :title "this is the title" } (. js/document getElementById "zippy1" ))]
   
   (println dom/getElement)
   
   [:p "we got here"]
   ])



;; render
(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

;; do some post-render stuff
(println "my header" (dom/getElement "myheader"))

(println "hiding header")
(-> (dom/getElement "myheader")
    (style/setElementShown false))

;;[sayHi]


;; (let [header-el (js/document.getElementById "zippy1")
;;       content-el (js/document.getElementById "content1")]
;;   (goog.ui.AnimatedZippy header-el content-el))

(let [special (js/document.getElementById "special")]
  (set! (.-innerHTML special) "<h1>This is some crrafty shit terry</h1>")
  )

(let [mydiv (js/document.createElement "div")]
  (js/document.body.appendChild mydiv)
  (set! (.-id mydiv) "very-special"))

(let [very-special (js/document.getElementById "very-special")]
  (set! (.-innerHTML very-special) "<h1>This is some EXTREMELY crrafty shit terry</h1>")
  )


;; here we show we can get access to element attribute
(let [potato (js/document.getElementById "the-content")]
  (println "we got the made up message = " (.getAttribute potato "madeuptag")))

(let [potato (js/document.getElementById "the-content")]
  (.setAttribute potato "madeuptag" "youve been framed!")
  (println "now the message reads = " (.getAttribute potato "madeuptag")))


(goog.ui.AnimatedZippy. "the-head" "the-content")
(goog.ui.Zippy. "the-head2" "the-content2")



(println "we almost done ...")

;; sha1
(let [s (Sha1)]
  (.update s "foobar")
  (println "sha1 foobar" (.digest s)))



   
(println "yep we're done.")

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
