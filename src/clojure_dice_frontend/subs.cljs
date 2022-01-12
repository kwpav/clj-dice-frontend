(ns clojure-dice-frontend.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::form-value
 (fn [db]
   (get-in db [:form :value])))

(rf/reg-sub
 ::valid-form?
 (fn [db]
   (get-in db [:form :valid?])))

(rf/reg-sub
 ::results
 (fn [db]
   (:results (last (:rolls db)))))

(rf/reg-sub
 ::total
 (fn [db]
   (:total (last (:rolls db)))))

(rf/reg-sub
 ::rolls
 (fn [db]
   (:rolls db)))
