(ns cllvm.reader
  (:require [cllvm.ll.core :as ll :refer [->* _double _i32 _i64 _ptr
                                          type-codes]]
            [cllvm.util :as util]
            [clojure.string :as str]))

(def var-idx (atom 0))
(defn var-idx! []
  (let [idx @var-idx]
    (swap! var-idx inc)
    idx))

(defn ->ptr-sym! []
  (str "%ptr_" (var-idx!)))

(defn ->val-sym! []
  (str "%val_" (var-idx!)))

(def str-idx (atom 0))
(defn str-idx! []
  (let [idx @var-idx]
    (swap! str-idx inc)
    idx))

(defn ->str-sym! []
  (str "%str_" (str-idx!)))

(def prim "%Primitive")
(def prim* (str prim "*"))

(defmulti expr->ir first)

(defmethod expr->ir nil [_]
  nil)

(defmethod expr->ir :exp [[_ body]]
  (expr->ir body))
(defmethod expr->ir :lit [[_ body]]
  (expr->ir body))

(defn ret-prim [type val]
  (let [prim*-sym (->ptr-sym!)
        type*-sym (->ptr-sym!)
        address*-sym  (->ptr-sym!)
        value*-sym (->val-sym!)]
    (util/lines->str
      (ll/alloca prim*-sym prim 8)
      (ll/get-element* type*-sym prim prim*-sym [_i32 0] [_i32 0])
      (ll/store type*-sym _i32 (type-codes type))
      (ll/get-element* address*-sym prim prim*-sym [_i32 0] [_i32 1])
      (ll/alloca value*-sym type 8)
      (ll/store value*-sym type val 8)
      (ll/store address*-sym (->* type) _ptr value*-sym 8)
      (ll/ret prim* prim*-sym))))

(defn ret-str [val]
  (let [evaluated-str (read-string val)
        str-size (inc (count evaluated-str))
        str-type (str "[" str-size " x i8]")
        str*-sym (->str-sym!)
        prim*-sym (->ptr-sym!)
        type*-sym (->ptr-sym!)
        address*-sym  (->ptr-sym!)
        str-ptr*-sym (->ptr-sym!)]
    (util/lines->str 
      (ll/def-str str*-sym evaluated-str)
      (ll/alloca prim*-sym prim 8) 
      (ll/get-element* type*-sym prim prim*-sym [_i32 0] [_i32 0]) 
      (ll/store type*-sym _i32 (type-codes "str")) 
      (ll/get-element* address*-sym prim prim*-sym [_i32 0] [_i32 1])
      (ll/get-element* str-ptr*-sym str-type str*-sym [_i64 0] [_i64 0])
      (ll/store address*-sym "i8*" _ptr str-ptr*-sym 8)
      (ll/ret prim* prim*-sym))))

(defmethod expr->ir :num [[_ body]]
  (expr->ir body))
(defmethod expr->ir :long [[_ body]]
  (ret-prim _i64 body))

(defmethod expr->ir :double [[_ body]]
  (ret-prim _double body))

(defmethod expr->ir :str [[_ body]]
  (ret-str body))

(defn ast->ir
  ([expr] (ast->ir "user" expr))
  ([ns expr]
   (util/lines->str
     (ll/module ns)
     (ll/source ns)
     (ll/type "TypeTag" "i32")
     (ll/type "Primitive" "%TypeTag" "ptr")
     (ll/func prim* "eval" (expr->ir expr)))))