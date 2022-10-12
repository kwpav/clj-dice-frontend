(ns clojure-dice-frontend.events
  (:require
   [clojure-dice-frontend.db :as db]
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as rf]))

;; -- Interceptors
;; Define an interceptor that ensures `app-db` conforms to the specs.
;; If `app-db` doesn't match the spec after an event, then throw an exception.

(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (rf/after (partial check-and-throw :clojure-dice-frontend.db/db)))

;; -- Helpers

(defn parse-dice
  "Get the number, sides, and any modifiers of dice to roll."
  [dice]
  (mapv #(js/parseInt %) (str/split dice #"[d+-]")))

(defn roll-die
  "Roll a single die. The rand-int function returns an integer between 0 to n-1,
  so adding 1 to that will give us the desired result."
  [sides]
  (inc (rand-int sides)))

(defn roll-dice
  "Roll the given number of dice."
  [number sides]
  (vec (take number (repeatedly #(roll-die sides)))))

(defn valid-dice?
  "Determine if the user input is valid.
  Some examples of valid dice:
  - 1d6
  - 10d20
  - 1d10-5
  - 4d20-10"
  [dice]
  (s/valid? #(re-matches #"\d*[dD]\d*[-+]?\d+" %) dice))

(defn add-roll
  "Add the most recent roll from :rolls in the db.
  If its over the limit, remove the oldest."
  [rolls roll]
  (let [next-rolls (if (>= (count rolls) db/rolls-history-limit)
                    (into [] (rest rolls))
                    rolls)]
    (conj next-rolls roll)))

(defn calc-total
  [number sides mod])

;; -- Event Handlers

(rf/reg-event-db
 ::initialize-db
 [check-spec-interceptor]
 (fn-traced
  [_ _]
  db/default-db))

(defn process-form
  [{:keys [rolls] :as db} dice]
  (when (get-in db [:form :valid?])
    (let [[number sides mod] (parse-dice dice)
          results            (roll-dice number sides)
          modfn              (cond (str/includes? dice "+") #'+
                                   (str/includes? dice "-") #'-
                                   :else                    nil)
          total              (cond-> (reduce + results)
                               (fn? modfn) (modfn mod))
          roll               {:dice dice :results results :total total}
          next-rolls         (add-roll rolls roll)]
      (assoc db :rolls next-rolls))))

(rf/reg-event-db
 ::process-form
 [check-spec-interceptor]
 (fn-traced
  [db [_ dice]]
  (process-form db dice)
  #_(when (get-in db [:form :valid?])
    (let [[number sides mod] (parse-dice dice)
          results            (roll-dice number sides)
          modfn              (cond (str/includes? dice "+") #'+
                                   (str/includes? dice "-") #'-
                                   :else                    nil)
          total              (cond-> (reduce + results)
                               (fn? modfn) (modfn mod))
          roll               {:dice dice :results results :total total}
          next-rolls         (add-roll rolls roll)]
      (assoc db :rolls next-rolls)))))

(rf/reg-event-db
 ::update-form
 [check-spec-interceptor]
 (fn-traced
  [db [_ id dice]]
  (-> db
      (assoc-in [:form :value] dice)
      (assoc-in [:form :valid?] (valid-dice? dice)))))
