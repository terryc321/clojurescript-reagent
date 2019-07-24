(ns clock.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   ;;[goog.Timer :as Timer]
   ;;[clojure.math.numeric-tower :as math]
   ;;[clojure.math.combinatorics :as combo]
   ))



(enable-console-print!)

(defn abs [x]  (.abs js/Math x))
(defn sqrt [x]  (.sqrt js/Math x))
(defn square [x] (* x x))
(defn hypt [x y]  (sqrt (+ (square x) (square y))))
(defn sin [x] (.sin js/Math x))
(defn cos [x] (.cos js/Math x))
(def pi (.-PI js/Math))
(defn floor [x] (.floor js/Math x))
;;(defn mod (.mod js/Math x))


(def sx1 (atom 0))
(def sx2 (atom 0))
(def sx3 (atom 100))
(def sx4 (atom 0))


(def sy1 (atom 0))
(def sy2 (atom 0))
(def sy3 (atom 100))
(def sy4 (atom 0))

(def win-width (atom 0))
(def win-height (atom 0))
(def win-centre-x (atom 0))
(def win-centre-y (atom 0))
(def clock-radius (atom 0))

(def clock-edge (atom []))

(def dx (atom 0))
(def tick (atom 0))

;; interval clock javascript
(def one-second 1000)

(js/setInterval (fn [] (swap! tick inc)) one-second)



;; calc
(defn seconds-hand [r t]
  [
   (+ (floor (* r (sin (/ (* pi t) 30)))))
   (- (floor (* r (cos (/ (* pi t) 30)))))
   ])

(defn second-hand [t]
  (let [cx @win-centre-x
        cy @win-centre-y]
  (let [[x1 y1] (seconds-hand (* 0.1 @clock-radius) t)]
    (reset! sx1 (+ cx x1))
    (reset! sy1 (+ cy y1)))
  (let [[x1 y1] (seconds-hand (* 0.2 @clock-radius) t)]
    (reset! sx2 (+ cx x1))
    (reset! sy2 (+ cy y1)))
  (let [[x1 y1] (seconds-hand (* 0.4 @clock-radius) t)]
    (reset! sx3 (+ cx x1))
    (reset! sy3 (+ cy y1)))
  (let [[x1 y1] (seconds-hand @clock-radius t)]
    (reset! sx4 (+ cx x1))
    (reset! sy4 (+ cy y1)))))

    
  ;; (let [cx 250
  ;;         cy 250
  ;;         cx2 250
  ;;         cy2 350]
  ;;     (let [tock (mod time 60)
  ;;           [x2 y2] (seconds-hand 100 tock)]
  ;;       ;;(println "x2 = " x2)
  ;;       [:circle {:cx (+ 250 x2) :cy (+ 250 y2) :r 10 :stroke "black" :fill "blue"}]
  ;;       )
  ;;     )
  ;; )


;; clock radius about 80 percent of available square space in window
(defn grab-jswin-dimensions []
  (let [h (.-innerHeight js/window)
        w (.-innerWidth js/window)]
    ;; take account of fact big menu at top of window
    (if (not (= (- h 100) @win-height))
      (reset! win-height (- h 100)))
    (if (not (= w @win-width))
      (reset! win-width w)))
  (reset! win-centre-x (floor (/ @win-width 2)))
  (reset! win-centre-y (floor (/ @win-height 2)))
  (reset! clock-radius (* 0.80 (/ (min @win-height @win-width) 2)))
  )



;; given win-centre-x win-centre-y clock-radius
;; draw out 12 hour hands
;; ;; 2 pi radians all way around clock once
;; (defn ^:export hour-marks []
;;   (reset! clock-edge 
;;           (map
;;            (fn [y] (seconds-hand @clock-radius y))
;;            (map (fn [x] (* x (/ pi 6)))
;;                 (rest (range 13)))))
;;   @clock-edge)



;; (defn ^:export test-marks []
;;   (let [cx 250
;;         cy 250
;;         radius 100
;;         key 0]    
;;     (for [point
;;           (map
;;            (fn [y] (seconds-hand radius y))
;;            (map (fn [x] (* x (/ pi 6)))
;;                 (rest (range 13))))]
;;       (let [[px py] point]
;;         (println "point = "  px py)))))

(defn test-marks []
  (map (fn [xd]
         (let [px (first xd)
               py (second xd)]
           [(+ px @win-centre-x) (+ py @win-centre-y)]))
       (map (fn [y] (seconds-hand 100 (* y 30)))
            (map (fn [x] (* x (/ pi 6)))
                 (rest (range 14))))))

;;let* [angle (/ pi 6)
(defn test-marks2-helper [angle]
  (let [x (+ @win-centre-x (* @clock-radius (sin angle)))
        y (+ @win-centre-y (* @clock-radius (cos angle)))]
    [x y]))


(defn test-marks2 []
  (map test-marks2-helper
       (map (fn [x] (* x (/ pi 6))) (range 12))))

  
  ;; (map (fn [xd]
  ;;        (let [px (first xd)
  ;;              py (second xd)]
  ;;          [(+ px @win-centre-x) (+ py @win-centre-y)]))
  ;;      (map (fn [y] (seconds-hand @clock-radius y))
  ;;           (map (fn [x] (* x (/ pi 6)))
  ;;                (rest (range 14))))))




;;
(defn clock []
  (if (> @dx 450)
    (reset! dx 50))
  (swap! dx (fn [x] (+ @dx 1)))
  
  (second-hand @tick)
  ;; (let [[x1 y1] (seconds-hand 60 @tick)]
  ;;   (reset! sx3 (+ 250 x1))
  ;;   (reset! sy3 (+ 250 y1)))

  (grab-jswin-dimensions)
  
  [:div {:class "clock"}
   [:p (str "second elapsed : " @tick )]
   [:svg {:height @win-height :width @win-width}

    [:circle {:cx @win-centre-x :cy @win-centre-y :r 10 :stroke "black" :stroke-width 10 :fill "blue" }]
    [:circle {:cx @win-centre-x :cy @win-centre-y :r @clock-radius :stroke "black" :stroke-width 1 :fill "none" }]

    ;; [:circle {:cx (+ @win-centre-x @clock-radius) :cy @win-centre-y :r 10 :stroke "black" :fill "blue"}]
    ;; [:circle {:cx (- @win-centre-x @clock-radius) :cy @win-centre-y :r 10 :stroke "black" :fill "blue"}]
    ;; [:circle {:cx @win-centre-x :cy (+ @win-centre-y @clock-radius) :r 10 :stroke "black" :fill "blue"}]
    ;; [:circle {:cx @win-centre-x :cy (- @win-centre-y @clock-radius) :r 10 :stroke "black" :fill "blue"}]
    
   ;; [:circle {:cx @sx2 :cy @sy2 :r 10 :stroke "black" :fill "blue"}]
   ;; [:circle {:cx @sx3 :cy @sy3 :r 10 :stroke "black" :fill "blue"}]
    [:circle {:cx @sx4 :cy @sy4 :r 10 :stroke "black" :stroke-width 5 :fill "blue"}]

    [:line {:x1 @win-centre-x :y1 @win-centre-y :x2 @sx4 :y2 @sy4 :stroke "yellow" :stroke-width 2}]
    ;;[:line {:x1 250 :y1 250 :x2 @sx4 :y2 @sy4 :style "stroke:rgb(255,0,0);stroke-width:2"}]

    (for [[px py] (test-marks2)]
      ;;(println "coord px = " px " , py = " py)
      [:circle {:cx px :cy py :r 10 :stroke "black" :stroke-width 10 :fill "blue"}]
      )
    
      ;;(let [[px py] coord]
      
      ;;  (println "px = "  px  " py = "  py)
      ;;  ))

    
    
    ;;[:circle {:cx @dx :cy @dx :r 100 :stroke "black" :fill "red"}]
    ;;[:circle {:cx @dx :cy (- 500 @dx) :r 100 :stroke "black" :fill "lime"}]
    ;;[:circle {:cx (- 500 @dx) :cy (- 500 @dx) :r 100 :stroke "black" :fill "blue"}]
    ;;[:circle {:cx (- 500 @dx) :cy (- 500 @dx) :r 100 :stroke "black" :fill "pink"}]
    
    ]
   ]
  )

;;[:polygon {:points (str "200,10 " @dx ",190 160,210") :stroke "red" :fill "lime"}]
;;(sleep (fn [] [splodge]) 500)
      



(reagent/render-component [clock]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)



