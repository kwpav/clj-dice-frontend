(ns clojure-dice-frontend.events-test
  (:require [clojure-dice-frontend.events :as sut]
            [cljs.test :as t :refer-macros [deftest testing is]]))

(deftest valid-dice-test
  (testing "test valid dice"
    (is
     (and (false? (sut/valid-dice? "1"))
          (false? (sut/valid-dice? "d"))
          (false? (sut/valid-dice? "1d"))
          (true? (sut/valid-dice? "1d2"))
          (false? (sut/valid-dice? "1d2+"))
          (true? (sut/valid-dice? "1d2+1"))
          (false? (sut/valid-dice? "1d2+1+"))))))

(deftest parse-dice-test
  (testing "Dice is parsed into number, sides, and modifier"
    (is (and (= (sut/parse-dice "1d20") {:number 1 :sides 20})
             (= (sut/parse-dice "1d20+10") {:number 1 :sides 20 :modfn #'+ :mod 10})
             (= (sut/parse-dice "1d20-10") {:number 1 :sides 20 :modfn #'- :mod 10 })))))
