(ns clojure-dice-frontend.db
  (:require
   [clojure.spec.alpha :as s]))

;; Define the specifications for the app-db.
;; The data in the db is always in this shape, this is enforced by an interceptor
;; that runs before every event and enforces these specs.
(s/def ::dice string?)
(s/def ::valid-dice? boolean?)
(s/def ::rolls (s/coll-of number? :kind vector?))
(s/def ::total int?)
(s/def ::history (s/coll-of (s/keys :req-un [::dice ::rolls ::total]) :kind vector?))
(s/def ::db (s/keys :req-un [::dice
                             ::valid-dice?
                             ::rolls
                             ::total
                             ::history]))

;; This is put into the app-db upon startup.
(def default-db
  {:dice "1d20"
   :valid-dice? true
   :rolls []
   :total 0
   :history []})
