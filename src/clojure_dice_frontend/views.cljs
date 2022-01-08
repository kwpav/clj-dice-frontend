(ns clojure-dice-frontend.views
  (:require
   [clojure.spec.alpha :as spec]
   [re-frame.core :as re-frame]
   [clojure-dice-frontend.subs :as subs]
   [clojure-dice-frontend.events :as events]))

(spec/def ::valid-dice #(re-matches #"\d+d\d+[\+|\-]?[\d+]?" %))

(defn valid-dice?
  "Use the spec to check if the user input is valid."
  [dice]
  (spec/valid? ::valid-dice dice))

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
    [:button {:on-click #(re-frame/dispatch [::events/process-form @dice])} "Roll Dice"]))

(defn rolls
  []
  (let [rolls (re-frame/subscribe [::subs/rolls])]
    [:ul (for [roll @rolls] [:li roll])]))

(defn total
  []
  (let [total (re-frame/subscribe [::subs/total])]
    [:p "Total: " @total]))

(defn main-panel
  []
  [:div
   [:h1 "Dice Roller"]
   [rolls]
   [total]
   [free-input :dice]
   [roll-dice-button]])
