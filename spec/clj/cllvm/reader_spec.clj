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
               "define i32 @main() {\n"
               "store i64 123, i64* %result"
               "}\n")
      (sut/clj->ir "test" [:exp [:lit [:long "123"]]]))))