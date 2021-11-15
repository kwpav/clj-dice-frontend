(ns clojure-dice-frontend.events
  (:require
   [clojure.string :as str]
   [re-frame.core :as re-frame]
   [clojure-dice-frontend.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced
  [_ _]
  db/default-db))

(re-frame/reg-event-db
 ::update-name
 (fn-traced
  [db [_ val]]
  (assoc db :name val)))

(defn parse-dice
  [dice]
  (clojure.string/split dice #"d"))

(defn roll-die
  "Roll a single die. The rand-int function returns an integer between 0 to n-1,
  so adding 1 to that will give us the desired result."
  [sides]
  (inc (rand-int sides)))

(defn roll-dice
  "Roll the given number of dice."
  [number sides]
  (take number (repeatedly #(roll-die sides))))

(re-frame/reg-event-db
 ::roll-dice
 (fn-traced
  [db [_ val]]
  (let [dice (parse-dice val)
        number (js/parseInt (first dice))
        sides (js/parseInt (second dice))
        rolls (roll-dice number sides)]
    (assoc db :roll rolls))))

(re-frame/reg-event-db
::update-form
(fn [db [_ id val]]
  (assoc db :dice val)))
