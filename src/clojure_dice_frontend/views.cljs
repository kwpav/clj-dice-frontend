(ns clojure-dice-frontend.views
  (:require
   [clojure.spec.alpha :as spec]
   [re-frame.core :as re-frame]
   [clojure-dice-frontend.subs :as subs]
   [clojure-dice-frontend.events :as events]))

;;(spec/def ::valid-dice #(re-matches #"\d+[dD]\d+[+-]?\d+" %))
(spec/def ::valid-dice #(re-matches #"\d*[dD]\d*[-+]?\d+" %))

(defn valid-dice?
  "Use the spec to check if the user input is valid.
  Examples:
  - 1d6
  - 10d20
  - 1d10-5
  - 4d20-10"
  [dice]
  (spec/valid? ::valid-dice dice))

(defn rolls
  []
  (let [rolls @(re-frame/subscribe [::subs/rolls])]
    [:ul (for [roll rolls] [:li roll])]))

(defn total
  []
  (let [total @(re-frame/subscribe [::subs/total])]
    [:p "Total: " total]))

(defn results
  []
  [:<>
   [rolls]
   [total]])

(defn dice-input
  [id dice]
  [:div.field
   [:div.control
    [:input.input {:on-change #(re-frame/dispatch
                                [::events/update-form id (-> % .-target .-value)])
                   :class (str "input is-large " (if (valid-dice? dice) "is-primary" "is-danger"))
                   :type "text"
                   :placeholder dice}]]])

(defn roll-dice-button
  [dice]
  [:button
   {:on-click #(re-frame/dispatch [::events/process-form dice])
    :class "button is-primary"
    :disabled (not (valid-dice? dice))}
   "Roll Dice"])

(defn dice-form
  [id]
  (let [dice @(re-frame/subscribe [::subs/dice])]
    [:<>
     [dice-input id dice]
     [roll-dice-button dice]]))

(defn history
  []
  (let [history @(re-frame/subscribe [::subs/history])]
    [:div {:class "card"}
     [:pre (str history)]]))

(defn main-panel
  []
  [:div {:class "container"}
   [:h1 "Dice Roller"]
   [dice-form]
   [results]
   [history]])
