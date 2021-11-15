(ns clojure-dice-frontend.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::roll
 (fn [db]
   (:roll db)))

(re-frame/reg-sub
 ::dice
 (fn [db]
   (:dice db)))
