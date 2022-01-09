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

;; TODO - parse modifiers
(defn parse-dice
  "Get the number, sides, and any modifiers of dice to roll."
  [dice]
  (map #(js/parseInt %) (str/split dice #"d|\+|\-")))

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
 ::process-form
 (fn-traced
  [db [_ dice]]
  (let [[number sides mod] (parse-dice dice)
        rolls              (roll-dice number sides)
        modfn              (cond (str/includes? dice "+") +
                                 (str/includes? dice "-") -)
        total              (cond-> (reduce + rolls)
                             (fn? modfn) (modfn mod))
        history            (if (>= (count (:history db)) 20)
                             (vec (rest (:history db))) (:history db))]
    (-> db
        (assoc :rolls rolls)
        (assoc :total total)
        (assoc :history (conj history (dissoc db :history)))))))

(re-frame/reg-event-db
 ::update-form
 (fn-traced
  [db [_ id dice]]
  (assoc db :dice dice)))
