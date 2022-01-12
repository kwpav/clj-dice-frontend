(ns clojure-dice-frontend.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 ::rolls
 (fn [db]
   (:rolls db)))

(rf/reg-sub
 ::dice
 (fn [db]
   (:dice db)))

(rf/reg-sub
 ::total
 (fn [db]
   (:total db)))

(rf/reg-sub
 ::valid-dice?
 (fn [db]
   (:valid-dice? db)))

(rf/reg-sub
 ::history
 (fn [db]
   (:history db)))
