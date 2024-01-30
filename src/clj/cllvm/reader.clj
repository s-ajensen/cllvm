(ns cllvm.reader
  (:require [cllvm.ll :as ll :refer [->* _i32 _i64 _double]]
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
      (ll/get-element* type*-sym prim prim*-sym [_i32 0] [_i32 0])
      (ll/store type*-sym _i32 0)
      (ll/get-element* val*-sym prim prim*-sym [_i32 0] [_i32 1])
      (ll/bitcast long*-sym val*-sym (->* "[8 x i8]") (->* _i64))
      (ll/store long*-sym _i64 body 8)
      (ll/ret prim* prim*-sym))))
; TODO refactor these! They're almost exactly the same!
(defmethod expr->ir :double [[_ body]]
  (let [prim*-sym (->ptr-sym!)
        type*-sym (->ptr-sym!)
        val*-sym  (->ptr-sym!)
        long*-sym (->ptr-sym!)]
    (util/lines->str
      (ll/alloca prim*-sym prim 8)
      (ll/get-element* type*-sym prim prim*-sym [_i32 0] [_i32 0])
      (ll/store type*-sym _i32 1)
      (ll/get-element* val*-sym prim prim*-sym [_i32 0] [_i32 1])
      (ll/bitcast long*-sym val*-sym (->* "[8 x i8]") (->* _double))
      (ll/store long*-sym _double body 8)
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