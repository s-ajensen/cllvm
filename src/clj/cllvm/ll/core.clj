(ns cllvm.ll.core
  (:require [cllvm.util :as util]
            [clojure.string :as str]))

(def _ptr "ptr")
(def _i32 "i32")
(def _i64 "i64")
(def _double "double")

(def type-codes
  {
   _i64    0
   _double 1
   })

(defn module [name]
  (str "; ModuleID = '" name "'"))
(defn source [name]
  (str "source_filename = \"" name ".ll\""))

(defn type [name & sections]
  (str \% name " = type { " (str/join ", " sections) " }"))
(defn ->* [type]
  (str type "*"))
(defn align [alignment]
  (str ", align " alignment))

(defn bblock [name body]
  (str name ":\n" body))

(defn func
  ([type name body]
   (if body
     (util/lines->str
       (str "define " type " @" name "() {")
       (bblock "entry" body)
       "}")
     (func type name)))
  ([type name]
   (func type name (str "ret " type " null"))))

(defn alloca
  ([sym type alignment]
   (str sym " = alloca " type (when alignment (align alignment))))
  ([sym type] (alloca sym type nil)))

(defn get-element* [sym type base & idx-pairs]
  (let [idxs (reduce (fn [s [type idx]]
                       (str s ", " type " " idx))
               "" idx-pairs)]
    (str sym " = getelementptr " type ", " (->* type) " " base idxs)))

(defn store
  ([sym src-type result-type val alignment]
   (str "store " src-type " " val ", " result-type " " sym (when alignment (align alignment))))
  ([sym type val alignment]
   (store sym type (->* type) val alignment))
  ([sym type val] (store sym type val nil)))

(defn bitcast [sym target from to]
  (str sym " = bitcast " from " " target " to " to))

(defn ret [type val]
  (str "ret " type " " val))