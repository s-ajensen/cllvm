(ns cllvm.reader-spec
  (:require [speclj.core :refer :all]
            [cllvm.reader :as sut]))

(describe "Reader"

  (context "boilerplate"

    (it "defines default module and filename"
      (should-start-with (str
                 "; ModuleID = 'user'\n"
                 "source_filename = \"user.ll\"\n")
        (sut/clj->ir [:exp])))

    (it "defines specific module and filename"
      (should-start-with (str
                 "; ModuleID = 'test'\n"
                 "source_filename = \"test.ll\"\n")
        (sut/clj->ir "test" [:exp])))

    (it "defines main entry point"
      (should= (str
                 "; ModuleID = 'test'\n"
                 "source_filename = \"test.ll\"\n"
                 "define i32 @main() {\n"
                 "}\n")
        (sut/clj->ir "test" [:exp]))))

  (context "literal expressions"
    (should= (str
               "; ModuleID = 'test'\n"
               "source_filename = \"test.ll\"\n"
               "define void @eval() {\n"
               "%ptr_0 = alloca %Primitive, align 8\n"
               "%ptr_1 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 0\n"
               "store i32 @LONG, i32* %ptr_1\n"
               "%ptr_3 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 1\n"
               "%ptr_4 = bitcast [8 x i8]* %ptr_3 to i64*\n"
               "store i64 123, i64* %ptr_4, align 8\n"
               "store %Primitive* %ptr_0, %Primitive** @result, align 8\n"
               "ret 0"
               "}\n")
      (sut/clj->ir "test" [:exp [:lit [:long "123"]]]))))