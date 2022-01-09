(ns clojure-dice-frontend.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::rolls
 (fn [db]
   (:rolls db)))

(re-frame/reg-sub
 ::dice
 (fn [db]
   (:dice db)))

(re-frame/reg-sub
 ::total
 (fn [db]
   (:total db)))

(re-frame/reg-sub
 ::history
 (fn [db]
   (:history db)))
