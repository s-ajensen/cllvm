(ns cllvm.reader
  (:require [cllvm.ll.core :as ll :refer [->* _ptr _i32 _i64 _double type-codes]]
            [cllvm.util :as util]))

(def var-idx (atom 0))
(defn var-idx! []
  (let [idx @var-idx]
    (swap! var-idx inc)
    idx))

(defn ->ptr-sym! []
  (str "%ptr_" (var-idx!)))

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
        value*-sym (->ptr-sym!)]
    (util/lines->str
      (ll/alloca prim*-sym prim 8)
      (ll/get-element* type*-sym prim prim*-sym [_i32 0] [_i32 0])
      (ll/store type*-sym _i32 (type-codes type))
      (ll/get-element* address*-sym prim prim*-sym [_i32 0] [_i32 1])
      (ll/alloca value*-sym type 8)
      (ll/store value*-sym type val 8)
      (ll/store address*-sym (->* type) _ptr value*-sym 8)
      (ll/ret prim* prim*-sym))))

(defmethod expr->ir :num [[_ body]]
  (expr->ir body))
(defmethod expr->ir :long [[_ body]]
  (ret-prim _i64 body))

(defmethod expr->ir :double [[_ body]]
  (ret-prim _double body))

(defn ast->ir
  ([expr] (ast->ir "user" expr))
  ([ns expr]
   (util/lines->str
     (ll/module ns)
     (ll/source ns)
     (ll/type "TypeTag" "i32")
     (ll/type "Primitive" "%TypeTag" "ptr")
     (ll/func prim* "eval" (expr->ir expr)))))