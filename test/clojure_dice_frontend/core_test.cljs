(ns clojure-dice-frontend.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [clojure-dice-frontend.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
