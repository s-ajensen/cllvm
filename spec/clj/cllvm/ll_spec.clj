(ns cllvm.ll-spec
  (:require [cllvm.util :as util]
            [speclj.core :refer :all]
            [cllvm.ll :as sut :refer [_i32]]))

(describe "LLVM API"

  (context "meta"

    (it "module"
      (should= "; ModuleID = 'test'"
        (sut/module "test")))

    (it "source file"
      (should= "source_filename = \"test.ll\""
        (sut/source "test"))))

  (context "type definitions"

    (it "with single section"
      (should= "%Test = type { [8 x i8] }"
        (sut/type "Test" "[8 x i8]")))

    (it "with two sections"
      (should= "%Test = type { [8 x i8], [4 x i8] }"
        (sut/type "Test" "[8 x i8]" "[4 x i8]"))))

  (context "function definitions"

    (it "with no body"
      (should= (util/lines->str
                 "define %Type* @name() {"
                 "entry:"
                 "ret %Type* null"
                 "}")
        (sut/func "%Type*" "name")))

    (it "with body"
      (should= (util/lines->str
                 "define %Type* @name() {"
                 "entry:"
                 ";line 1"
                 ";line 2"
                 "ret %Type* null"
                 "}")
        (sut/func "%Type*" "name" ";line 1\n;line 2\nret %Type* null"))))

  (context "instructions"

    (context "alloca"

      (it "without alignment"
        (should= "%symbol = alloca %Type"
          (sut/alloca "%symbol" "%Type")))

      (it "with alignment"
        (should= "%symbol = alloca %Type, align 8"
          (sut/alloca "%symbol" "%Type" 8))))

    (context "store"

      (it "without alignment"
        (should= "store i32 0, i32* %sym"
          (sut/store "%sym" _i32 0)))

      (it "with alignment"
        (should= "store i32 0, i32* %sym, align 8"
          (sut/store "%sym" _i32 0 8))))

    (context "getelementptr"

      (it "single index"
        (should= "%symbol = getelementptr i32, i32* %base, i32 0"
          (sut/get-element* "%symbol" "i32" "%base" [_i32 0])))

      (it "two indices"
        (should= "%symbol = getelementptr %Type, %Type* %base, i32 0, i32 1"
          (sut/get-element* "%symbol" "%Type" "%base" [_i32 0] [_i32 1])))

      (it "many indices"
        (should= "%symbol = getelementptr %Type, %Type* %base, i32 0, i32 1, i32 5, i32 9, i32 0"
          (sut/get-element* "%symbol" "%Type" "%base" [_i32 0] [_i32 1] [_i32 5] [_i32 9] [_i32 0]))))

    (it "bitcast"
      (should= "%sym2 = bitcast [8 x i8]* %sym1 to i64*"
        (sut/bitcast "%sym2" "%sym1" "[8 x i8]*" "i64*")))

    (it "ret"
      (should= "ret i32 0"
        (sut/ret _i32 0)))))