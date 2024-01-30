(ns cllvm.reader
  (:require [cllvm.ll :as ll :refer [->* i32 i64]]
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

(defmethod expr->ir :num [[_ body]]
  (expr->ir body))
(defmethod expr->ir :long [[_ body]]
  (let [prim*-sym (->ptr-sym!)
        type*-sym (->ptr-sym!)
        val*-sym  (->ptr-sym!)
        long*-sym (->ptr-sym!)]
    (util/lines->str
      (ll/alloca prim*-sym prim 8)
      (ll/get-element* type*-sym prim prim*-sym [i32 0] [i32 0])
      (ll/store type*-sym i32 0)
      (ll/get-element* val*-sym prim prim*-sym [i32 0] [i32 1])
      (ll/bitcast long*-sym val*-sym (->* "[8 x i8]") (->* i64))
      (ll/store long*-sym i64 body 8)
      (ll/ret prim* prim*-sym))))

(defn ast->ir
  ([expr] (ast->ir "user" expr))
  ([ns expr]
   (util/lines->str
     (ll/module ns)
     (ll/source ns)
     (ll/type "TypeTag" "i32")
     (ll/type "Primitive" "%TypeTag" "[8 x i8]")
     (ll/func prim* "eval" (expr->ir expr)))))