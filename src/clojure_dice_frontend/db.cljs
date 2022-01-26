(ns clojure-dice-frontend.db
  (:require
   [clojure.spec.alpha :as s]))

;; Define the specifications for the app-db.
;; The data in the db is always in this shape, this is enforced by an interceptor
;; that runs before every event and enforces these specs.
(s/def ::value string?)
(s/def ::valid? boolean?)
(s/def ::form (s/keys :req-un [::value ::valid?]))

(s/def ::dice string?)
(s/def ::results (s/coll-of number? :kind vector?))
(s/def ::total int?)
(s/def ::rolls (s/coll-of (s/keys :req-un [::dice ::results ::total]) :kind vector?))

(s/def ::db (s/keys :req-un [::form ::rolls]))

(def default-db
  {:form {:value "1d20"
          :valid? true}
   :rolls []})
