(ns cllvm.reader-spec
  (:require [cllvm.util :as util]
            [speclj.core :refer :all]
            [cllvm.reader :as sut]))

(defn with-boilerplate [& lines]
  (apply util/lines->str
         (concat ["; ModuleID = 'test'"
                  "source_filename = \"test.ll\""
                  "%TypeTag = type { i32 }"
                  "%Primitive = type { %TypeTag, ptr }"]
                 lines)))

(defn in-eval [& lines]
  (apply util/lines->str
         (concat ["define %Primitive* @eval() {"
                  "entry:"]
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
                 (in-eval "ret %Primitive* null"))
        (sut/ast->ir "test" [:exp]))))

  (context "expressions"

    (context "literal"

      (context "number"

        (it "long"
          (should= (with-boilerplate 
                     (in-eval 
                      "%ptr_0 = alloca %Primitive, align 8" 
                      "%ptr_1 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 0" 
                      "store i32 0, i32* %ptr_1" 
                      "%ptr_2 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 1" 
                      "%val_3 = alloca i64, align 8" 
                      "store i64 123, i64* %val_3, align 8" 
                      "store i64* %val_3, ptr %ptr_2, align 8" 
                      "ret %Primitive* %ptr_0"))
            (sut/ast->ir "test" [:exp [:lit [:num [:long "123"]]]])))

        (it "double"
          (should= (with-boilerplate
                     (in-eval 
                      "%ptr_0 = alloca %Primitive, align 8" 
                      "%ptr_1 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 0" 
                      "store i32 1, i32* %ptr_1" 
                      "%ptr_2 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 1" 
                      "%val_3 = alloca double, align 8" 
                      "store double 1.23, double* %val_3, align 8" 
                      "store double* %val_3, ptr %ptr_2, align 8" 
                      "ret %Primitive* %ptr_0"))
            (sut/ast->ir "test" [:exp [:lit [:num [:double "1.23"]]]]))))
            
      (it "string"
        
        (should= (with-boilerplate 
                   (in-eval
                    "%str_0 = alloca [5 x i8], align 1"
                    "store [5 x i8] c\"test\\00\", [5 x i8]* %str_0, align 1"
                    "%ptr_0 = alloca %Primitive, align 8"
                    "%ptr_1 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 0"
                    "store i32 2, i32* %ptr_1"
                    "%ptr_2 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 1"
                    "%ptr_3 = getelementptr [5 x i8], [5 x i8]* %str_0, i64 0, i64 0"
                    "store i8* %ptr_3, ptr %ptr_2, align 8"
                    "ret %Primitive* %ptr_0"))
                 (sut/ast->ir "test" [:exp [:lit [:str "\"test\""]]]))))))