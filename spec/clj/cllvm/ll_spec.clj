(ns cllvm.ll-spec
  (:require [cllvm.reader :as reader]
            [cllvm.util :as util]
            [speclj.core :refer :all]
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

    (it "with no body"
      (should= (util/lines->str
                 "define %Type* @name() {"
                 "entry:"
                 "ret %Type* null"
                 "}")
        (sut/->func "%Type*" "name")))

    (it "with body"
      (should= (util/lines->str
                 "define %Type* @name() {"
                 "entry:"
                 ";line 1"
                 ";line 2"
                 "ret %Type* null"
                 "}")
        (sut/->func "%Type*" "name" ";line 1\n;line 2\nret %Type* null")))))