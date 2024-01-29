(ns cllvm.ll-spec
  (:require [speclj.core :refer :all]
            [cllvm.ll :as sut]))

(describe "LLVM API"

  (context "meta"

    (it "module"
      (should= "; ModuleID = 'test'"
        (sut/->module "test")))

    (it "source file"
      (should= "source_filename = \"test.ll\""
        (sut/->source "test"))))

  (context "type definitions"

    (it "with single section"
      (should= "%Test = type { [8 x i8] }"
        (sut/->type "Test" "[8 x i8]")))

    (it "with two sections"
      (should= "%Test = type { [8 x i8], [4 x i8] }"
        (sut/->type "Test" "[8 x i8]" "[4 x i8]"))))

  (context "function definitions"
    ))