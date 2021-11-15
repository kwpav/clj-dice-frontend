(ns clojure-dice-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [clojure-dice-frontend.subs :as subs]
   [clojure-dice-frontend.events :as events]))

(defn free-input
  [id]
  (let [dice (re-frame/subscribe [::subs/dice])]
    [:div.field
     [:div.control
      [:input.input {:on-change #(re-frame/dispatch
                                  [::events/update-form id (-> % .-target .-value)])
                     :type "text" :placeholder @dice}]]]))

(defn roll-dice-button
  []
  (let [dice (re-frame/subscribe [::subs/dice])]
    [:button {:on-click #(re-frame/dispatch [::events/roll-dice @dice])} "Roll Dice"]))

(defn rolls
  []
  (let [rolls (re-frame/subscribe [::subs/roll])]
    [:ul (for [roll @rolls] [:li roll])]))

(defn total
  []
  (let [rolls (re-frame/subscribe [::subs/roll])]
    [:p "Total: " (reduce + @rolls)]))

(defn main-panel []
  [:div
   [:h1 "Dice Roller"]
   [rolls]
   [total]
   [free-input :dice]
   [roll-dice-button]])
