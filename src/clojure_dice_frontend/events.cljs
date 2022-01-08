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
  (vec (take number (repeatedly #(roll-die sides)))))

(re-frame/reg-event-db
 ::roll-dice
 (fn-traced
  [db [_ dice]]
  (let [[number sides] (parse-dice dice)
        rolls (roll-dice number sides)
        total (reduce + rolls)]
    (-> db
        (assoc :rolls rolls)
        (assoc :total total)))))

(re-frame/reg-event-db
 ::add-to-history
 (fn-traced
  [db]
  (let [history (if (>= (count (:history db)) 10) (vec (rest (:history db))) (:history db))]
    (assoc db :history (conj history (dissoc db :history))))))

(re-frame/reg-event-fx
 ::process-form
 (fn-traced
  [db [_ dice]]
  {:dispatch-n [[::roll-dice dice]
                [::add-to-history]]}))

(re-frame/reg-event-db
 ::update-form
 (fn-traced
  [db [_ id val]]
  (assoc db :dice val)))
