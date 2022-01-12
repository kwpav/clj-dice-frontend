(ns clojure-dice-frontend.views
  (:require
   [clojure-dice-frontend.events :as events]
   [clojure-dice-frontend.subs :as subs]
   [re-frame.core :as rf]))

(defn rolls
  []
  (let [rolls @(rf/subscribe [::subs/results])]
    [:<>
     [:p "Rolls: "]
     [:ul (for [roll rolls] [:li roll])]]))

(defn total
  []
  (let [total @(rf/subscribe [::subs/total])]
    [:p "Total: " total]))

(defn results
  []
  [:<>
   [rolls]
   [total]])

(defn dice-input
  [id dice valid-dice?]
  [:div.field
   [:div.control
    [:input.input {:on-change   #(rf/dispatch
                                [::events/update-form id (-> % .-target .-value)])
                   :class       (str "input is-large " (if valid-dice? "is-primary" "is-danger"))
                   :type        "text"
                   :placeholder dice}]]])

(defn roll-dice-button
  [dice valid-dice?]
  [:button
   {:on-click #(rf/dispatch [::events/process-form dice])
    :class    "button is-large is-fullwidth is-primary"
    :disabled (not valid-dice?)}
   "Roll Dice"])

(defn dice-form
  [id]
  (let [dice @(rf/subscribe [::subs/form-value])
        valid-dice? @(rf/subscribe [::subs/valid-form?])]
    [:<>
     [dice-input id dice valid-dice?]
     [roll-dice-button dice valid-dice?]]))

(defn history
  []
  (let [history @(rf/subscribe [::subs/rolls])]
    [:<>
     (for [roll history]
       [:div {:class "card"}
        [:pre (str roll)]])]))

(defn main-panel
  []
  [:div {:class "container"}
   [:h1 "Dice Roller"]
   [dice-form]
   [results]
   [history]])
