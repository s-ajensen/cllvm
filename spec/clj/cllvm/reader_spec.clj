(ns cllvm.reader-spec
  (:require [cllvm.util :as util]
            [speclj.core :refer :all]
            [cllvm.reader :as sut]))

;; TODO - [GMJ] let me know what you think about this
(defn with-boilerplate [& lines]
  (apply util/lines->str
         (concat ["; ModuleID = 'test'"
                  "source_filename = \"test.ll\""
                  "%TypeTag = type { i32 }"
                  "%Primitive = type { %TypeTag, [8 x i8] }"
                  "define %Primitive* @eval() {"]
                 lines
                 ["}"])))

(describe "Reader"
  (before (reset! sut/var-idx 0))

  (context "boilerplate"

    (it "defines default module and filename"
      (should-start-with (str
                 "; ModuleID = 'user'\n"
                 "source_filename = \"user.ll\"\n")
        (sut/ast->ir [:exp])))

    (it "defines specific module and filename"
      (should-start-with (str
                 "; ModuleID = 'test'\n"
                 "source_filename = \"test.ll\"\n")
        (sut/ast->ir "test" [:exp])))

    (it "defines main entry point"
      (should= (with-boilerplate
                 "entry:"
                 "ret %Primitive* null")
        (sut/ast->ir "test" [:exp]))))

  (context "expressions"

    (context "literal"

      (context "number"

        (it "long"
          (should= (with-boilerplate
                     "entry:"
                     "%ptr_0 = alloca %Primitive, align 8"
                     "%ptr_1 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 0"
                     "store i32 0, i32* %ptr_1"
                     "%ptr_2 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 1"
                     "%ptr_3 = bitcast [8 x i8]* %ptr_2 to i64*"
                     "store i64 123, i64* %ptr_3, align 8"
                     "ret %Primitive* %ptr_0")
            (sut/ast->ir "test" [:exp [:lit [:num [:long "123"]]]])))

        (it "double"
          (should= (with-boilerplate
                     "entry:"
                     "%ptr_0 = alloca %Primitive, align 8"
                     "%ptr_1 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 0"
                     "store i32 1, i32* %ptr_1"
                     "%ptr_2 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 1"
                     "%ptr_3 = bitcast [8 x i8]* %ptr_2 to double*"
                     "store double 1.23, double* %ptr_3, align 8"
                     "ret %Primitive* %ptr_0")
            (sut/ast->ir "test" [:exp [:lit [:num [:double "1.23"]]]])))))))