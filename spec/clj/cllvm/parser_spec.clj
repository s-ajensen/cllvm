(ns cllvm.parser-spec
  (:require [speclj.core :refer :all]
            [cllvm.parser :as sut]))

;region clojure parser
(describe "Clojure parser"
  ;region expression
  (context "expression"
    ;region literal
    (context "literal"

      ;region string
      (context "string"

        (it "plain"
          (should= [:exp [:lit [:str "\"test\""]]]
            (sut/parse "\"test\"")))

        (it "with escape characters"
          (should= [:exp [:lit [:str "\"test\n\""]]]
            (sut/parse "\"test\n\""))))                     ;endregion

      ;region number
      (context "number"
        ;region long
        (context "long"

          (it "positive"
            (should= [:exp [:lit [:num [:long "123"]]]]
              (sut/parse "123")))

          (it "negative"
            (should= [:exp [:lit [:num [:long "-123"]]]]
              (sut/parse "-123")))

          (it "with suffix"
            (should= [:exp [:lit [:num [:long "-123L"]]]]
              (sut/parse "-123L"))))                        ;endregion
        ;region double
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
          ;region scientific notation
          (context "scientific notation"

            (it "positive power"
              (should= [:exp [:lit [:num [:double "1.0e23"]]]]
                (sut/parse "1.0e23")))

            (it "negative power"
              (should= [:exp [:lit [:num [:double "1.0e-23"]]]]
                (sut/parse "1.0e-23"))))                    ;endregion
          )                                                 ;endregion
        ;region ratio
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
                (sut/parse "-1/-2")))))                     ;endregion
        ;region bigint
        (context "bigint"

          (it "positive"
            (should= [:exp [:lit [:num [:bign "123N"]]]]
              (sut/parse "123N")))

          (it "negative"
            (should= [:exp [:lit [:num [:bign "-123n"]]]]
              (sut/parse "-123n"))))                        ;endregion
        ;region bigdecimal
        (context "bigdecimal"

          (it "positive"
            (should= [:exp [:lit [:num [:bigdec "1.23M"]]]]
              (sut/parse "1.23M")))

          (it "negative"
            (should= [:exp [:lit [:num [:bigdec "-1.23M"]]]]
              (sut/parse "-1.23M")))

          ;region scientific notation
          (context "scientific notation"

            (it "positive power"
              (should= [:exp [:lit [:num [:bigdec "1.0e23M"]]]]
                (sut/parse "1.0e23M")))

            (it "negative power"
              (should= [:exp [:lit [:num [:bigdec "1.0e-23M"]]]]
                (sut/parse "1.0e-23M"))))                    ;endregion
          )                                                 ;endregion
        ;region octal
        (context "octal"

          (it "positive"
            (should= [:exp [:lit [:num [:octal "012"]]]]
              (sut/parse "012")))

          (it "negative"
            (should= [:exp [:lit [:num [:octal "-012"]]]]
              (sut/parse "-012"))))                         ;endregion
        ;region hexadecimal
        (context "hexadecimal"

          (it "positive"
            (should= [:exp [:lit [:num [:hex "0x123"]]]]
              (sut/parse "0x123")))

          (it "negative"
            (should= [:exp [:lit [:num [:hex "-0x123"]]]]
              (sut/parse "-0x123"))))                       ;endregion
        ;region radix
        (context "radix"
          (it "base2"
            (should= [:exp [:lit [:num [:radix "2r0110"]]]]
              (sut/parse "2r0110")))

          (it "base10"
            (should= [:exp [:lit [:num [:radix "10r001"]]]]
              (sut/parse "10r001")))

          (it "base36"
            (should= [:exp [:lit [:num [:radix "36rz123a"]]]]
              (sut/parse "36rz123a")))

          (it "negative"
            (should= [:exp [:lit [:num [:radix "-10r001"]]]]
              (sut/parse "-10r001")))
          )                                                 ;endregion
        )                                                   ;endregion
      )                                                     ;endregion
    )                                                       ;endregion
  )                                                         ;endregion