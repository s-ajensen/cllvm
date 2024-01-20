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
            (sut/parse "\"test\n\""))))

      (context "number"

        (context "long"

          (it "positive"
            (should= [:exp [:lit [:num [:long "123"]]]]
              (sut/parse "123")))

          (it "negative"
            (should= [:exp [:lit [:num [:long "-123"]]]]
              (sut/parse "-123")))

          (it "with suffix"
            (should= [:exp [:lit [:num [:long "-123L"]]]]
              (sut/parse "-123L"))))

        (context "double"

          (it "positive"
            (should= [:exp [:lit [:num [:double "1.23"]]]]
              (sut/parse "1.23")))

          (it "negative"
            (should= [:exp [:lit [:num [:double "-1.23"]]]]
              (sut/parse "-1.23")))

          (it "positive infinity"
            (should= [:exp [:lit [:num [:double "Infinity"]]]]
              (sut/parse "Infinity")))

          (it "negative infinity"
            (should= [:exp [:lit [:num [:double "-Infinity"]]]]
              (sut/parse "-Infinity")))

          (it "positive NaN"
            (should= [:exp [:lit [:num [:double "NaN"]]]]
              (sut/parse "NaN")))

          (it "negative NaN"
            (should= [:exp [:lit [:num [:double "-NaN"]]]]
              (sut/parse "-NaN")))

          (context "scientific notation"

            (it "positive power"
              (should= [:exp [:lit [:num [:double "1.0e23"]]]]
                (sut/parse "1.0e23")))

            (it "negative power"
              (should= [:exp [:lit [:num [:double "1.0e-23"]]]]
                (sut/parse "1.0e-23")))))

        (context "ratio"

          (context "positive numerator"

            (it "positive denominator"
              (should= [:exp [:lit [:num [:ratio "1/2"]]]]
                (sut/parse "1/2")))

            (it "negative denominator"
              (should= [:exp [:lit [:num [:ratio "1/-2"]]]]
                (sut/parse "1/-2"))))

          (context "negative numerator"

            (it "positive denominator"
              (should= [:exp [:lit [:num [:ratio "-1/2"]]]]
                (sut/parse "-1/2")))

            (it "negative denominator"
              (should= [:exp [:lit [:num [:ratio "-1/-2"]]]]
                (sut/parse "-1/-2")))))

        (context "bigint"

          (it "positive"
            (should= [:exp [:lit [:num [:bign "123N"]]]]
              (sut/parse "123N")))

          (it "negative"
            (should= [:exp [:lit [:num [:bign "-123n"]]]]
              (sut/parse "-123n"))))

        (context "octal"

          (it "positive"
            (should= [:exp [:lit [:num [:octal "012"]]]]
              (sut/parse "012")))

          (it "negative"
            (should= [:exp [:lit [:num [:octal "-012"]]]]
              (sut/parse "-012"))))

        (context "hexadecimal"

          (it "positive"
            (should= [:exp [:lit [:num [:hex "0x123"]]]]
              (sut/parse "0x123")))

          (it "negative"
            (should= [:exp [:lit [:num [:hex "-0x123"]]]]
              (sut/parse "-0x123"))))))))