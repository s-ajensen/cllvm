(ns cllvm.reader
  (:require [c3kit.apron.corec :as ccc]
            [cllvm.ll :as ll]
            [clojure.string :as str]))

(def var-idx (atom 0))
(defn var-idx! []
  (let [idx @var-idx]
    (swap! var-idx inc)
    idx))

(defn ->ptr-sym! []
  (str "%ptr_" (var-idx!)))

(def prim "%Primitive")
(def prim* (str prim "*"))

(defn lines->str [& lines]
  (->> lines
    (filter some?)
    (str/join "\n")))

(defn ->func-def [type name body]
  (lines->str
    (str "define " type " @" name "() {")
    "entry:"
    (or
      body
      (str "ret " type " null"))
    "}"))

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
    (lines->str
      (str prim*-sym " = alloca " prim ", align 8")
      (str type*-sym " = getelementptr " prim ", " prim* " " prim*-sym ", i32 0, i32 0")
      (str "store i32 0, i32* " type*-sym)
      (str val*-sym " = getelementptr " prim ", " prim* " " prim*-sym ", i32 0, i32 1")
      (str long*-sym " = bitcast [8 x i8]* " val*-sym " to i64*")
      (str "store i64 " body ", i64* " long*-sym ", align 8")
      (str "ret " prim* " " prim*-sym))))

(defn ast->ir
  ([expr] (ast->ir "user" expr))
  ([ns expr]
   (lines->str
     (ll/->module ns)
     (ll/->source ns)
     (ll/->type "TypeTag" "i32")
     (ll/->type "Primitive" "%TypeTag" "[8 x i8]")
     (->func-def prim* "eval" (expr->ir expr)))))