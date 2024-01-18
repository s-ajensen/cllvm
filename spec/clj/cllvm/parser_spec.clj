(ns cllvm.parser-spec
  (:require [speclj.core :refer :all]
            [cllvm.parser :as sut]))

(describe "Clojure parser"

  (context "expression"

    (context "literal"

      (context "string"

        (it "plain"
          (should= [:exp [:lit [:str "\"test\""]]]
            (sut/parse "\"test\"")))

        (it "with escape characters"
          (should= [:exp [:lit [:str "\"test\n\""]]]
            (sut/parse "\"test\n\"")))))))