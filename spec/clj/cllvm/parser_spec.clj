(ns cllvm.parser-spec
  (:require [speclj.core :refer :all]
            [cllvm.parser :as sut])
  (:import (instaparse.gll Failure)))

(defn should-fail-parse [form]
  (should (instance? Failure form)))

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

      ;region character
      (context "character"

        (it "single"
          (should= [:exp [:lit [:char "\\a"]]]
            (sut/parse "\\a")))

        (it "unicode"
          (should= [:exp [:lit [:char [:char_uni "\\u1F4A"]]]]
            (sut/parse "\\u1F4A")))

        (it "octal"
          (should= [:exp [:lit [:char [:char_oct "\\o123"]]]]
            (sut/parse "\\o123")))

        (context "named"

          (it "newline"
            (should= [:exp [:lit [:char [:char_named "\\newline"]]]]
              (sut/parse "\\newline")))

          (it "newline"
            (should= [:exp [:lit [:char [:char_named "\\space"]]]]
              (sut/parse "\\space")))

          (it "newline"
            (should= [:exp [:lit [:char [:char_named "\\tab"]]]]
              (sut/parse "\\tab")))

          (it "newline"
            (should= [:exp [:lit [:char [:char_named "\\formfeed"]]]]
              (sut/parse "\\formfeed")))

          (it "newline"
            (should= [:exp [:lit [:char [:char_named "\\backspace"]]]]
              (sut/parse "\\backspace")))

          (it "newline"
            (should= [:exp [:lit [:char [:char_named "\\return"]]]]
              (sut/parse "\\return")))))                    ;endregion

      ;region nil
      (it "nil"
        (should= [:exp [:lit [:nil "nil"]]]
          (sut/parse "nil")))                               ;endregion

      ;region boolean
      (context "boolean"

        (it "true"
          (should= [:exp [:lit [:bool "true"]]]
            (sut/parse "true")))

        (it "false"
          (should= [:exp [:lit [:bool "false"]]]
            (sut/parse "false")))
        )                                                   ;endregion

      ;region keyword
      (context "keyword"

        (it "starts with colon"
          (should= [:exp [:lit [:kw ":key"]]]
            (sut/parse ":key")))

        (it "forbidden characters"
          (should-fail-parse (sut/parse ":1"))
          (should-fail-parse (sut/parse ":^"))
          (should-fail-parse (sut/parse ":\\"))
          (should-fail-parse (sut/parse ":\""))
          (should-fail-parse (sut/parse ":#"))
          (should-fail-parse (sut/parse ":~"))
          (should-fail-parse (sut/parse ":@"))
          (should-fail-parse (sut/parse ":("))
          (should-fail-parse (sut/parse ":)"))
          (should-fail-parse (sut/parse ":["))
          (should-fail-parse (sut/parse ":]"))
          (should-fail-parse (sut/parse ":{"))
          (should-fail-parse (sut/parse ":}"))
          (should-fail-parse (sut/parse ": "))
          (should-fail-parse (sut/parse ":,")))
        )                                                   ;endregion
      )                                                     ;endregion
    ;region symbol
    (context "symbol"

      (it "doesn't start with a number"
        (should-fail-parse (sut/parse "1sym")))

      (it "allows numbers after alphabetic character"
        (should= [:exp [:sym "a1"]]
          (sut/parse "a1")))

      (it "allow non-numeric characters"
        (should= [:exp [:sym "*a"]] (sut/parse "*a"))
        (should= [:exp [:sym "+a"]] (sut/parse "+a"))
        (should= [:exp [:sym "!a"]] (sut/parse "!a"))
        (should= [:exp [:sym "-a"]] (sut/parse "-a"))
        (should= [:exp [:sym "_a"]] (sut/parse "_a"))
        (should= [:exp [:sym "?a"]] (sut/parse "?a"))
        (should= [:exp [:sym "<a"]] (sut/parse "<a"))
        (should= [:exp [:sym ">a"]] (sut/parse ">a"))
        (should= [:exp [:sym "=a"]] (sut/parse "=a"))
        (should= [:exp [:sym ".a"]] (sut/parse ".a")))

      (it "allows slashes (but not at the beginning)"
        (should-fail-parse (sut/parse "/ab"))
        (should= [:exp [:sym "a/b"]]
          (sut/parse "a/b"))))                              ;endregion
    ;region list
    (context "list"

      (it "begins and ends with parenthesis (but excludes them from structure)"
        (should= [:exp [:list]]
          (sut/parse "()")))

      (it "with single child expression"
        (should= [:exp [:list [:exp [:lit [:nil "nil"]]]]]
          (sut/parse "(nil)")))

      (it "with many child expressions"
        (should= [:exp
                  [:list
                   [:exp [:lit [:nil "nil"]]]
                   [:exp [:lit [:nil "nil"]]]]]
          (sut/parse "(nil nil)")))

      (it "with child lists"
        (should= [:exp
                  [:list
                   [:exp [:list
                          [:exp [:lit [:nil "nil"]]]
                          [:exp [:lit [:nil "nil"]]]]]
                   [:exp [:lit [:nil "nil"]]]]]
          (sut/parse "((nil nil) nil)")))
      )                                                     ;endregion
    ;region vector
    (context "vector"

      (it "begins and ends with square brackets (but excludes them from structure)"
        (should= [:exp [:vec]]
          (sut/parse "[]")))

      (it "with single child expression"
        (should= [:exp [:vec [:exp [:lit [:nil "nil"]]]]]
          (sut/parse "[nil]")))

      (it "with many child expressions"
        (should= [:exp
                  [:vec
                   [:exp [:lit [:nil "nil"]]]
                   [:exp [:lit [:nil "nil"]]]]]
          (sut/parse "[nil nil]")))

      (it "with child vectors"
        (should= [:exp
                  [:vec
                   [:exp [:vec
                          [:exp [:lit [:nil "nil"]]]
                          [:exp [:lit [:nil "nil"]]]]]
                   [:exp [:lit [:nil "nil"]]]]]
          (sut/parse "[[nil nil] nil]")))
      )                                                     ;endregion
    ;region map
    (context "map"

      (it "begins and ends with curly brackets (but excludes them from structure)"
        (should= [:exp [:map]]
          (sut/parse "{}")))

      (it "contains an even number of expressions"
        (should= [:exp [:map [:pair
                              [:key [:exp [:lit [:nil "nil"]]]]
                              [:val [:exp [:lit [:nil "nil"]]]]]]]
          (sut/parse "{nil nil}"))
        (should-fail-parse (sut/parse "{nil nil nil}")))

      (it "allows namespace prefix"
        (should= [:exp [:map
                        [:prefix ":pre"]
                        [:pair
                         [:key [:exp [:lit [:nil "nil"]]]]
                         [:val [:exp [:lit [:nil "nil"]]]]]]]
          (sut/parse "#:pre{nil nil}")))
      )                                                     ;endregion
    ;region set
    (context "set"

      (it "begins with # and is surrounded by curly brackets (but excludes them from structure)"
        (should= [:exp [:set]]
          (sut/parse "#{}")))

      (it "with single child expression"
        (should= [:exp [:set [:exp [:lit [:nil "nil"]]]]]
          (sut/parse "#{nil}")))

      (it "with many child expressions"
        (should= [:exp
                  [:set
                   [:exp [:lit [:nil "nil"]]]
                   [:exp [:lit [:nil "nil"]]]]]
          (sut/parse "#{nil nil}")))
      )                                                     ;endregion
    )                                                       ;endregion
  )                                                         ;endregion