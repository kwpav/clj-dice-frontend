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

(defn parse-dice
  [dice]
  (map #(js/parseInt %) (str/split dice #"d")))

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
  (let [[number sides] (parse-dice val)
        rolls (roll-dice number sides)]
    (-> db
        (assoc :rolls rolls)
        (assoc :total (reduce + rolls))))))

(re-frame/reg-event-db
 ::update-form
 (fn-traced
  [db [_ id val]]
  (assoc db :dice val)))
