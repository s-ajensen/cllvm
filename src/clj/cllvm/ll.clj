(ns cllvm.ll
  (:require [cllvm.util :as util]
            [clojure.string :as str]))

(defn ->module [name]
  (str "; ModuleID = '" name "'"))
(defn ->source [name]
  (str "source_filename = \"" name ".ll\""))

(defn ->type [name & sections]
  (str \% name " = type { " (str/join ", " sections) " }"))

(defn ->func
  ([type name body]
   (if body
     (util/lines->str
       (str "define " type " @" name "() {")
       "entry:"
       body
       "}")
     (->func type name)))
  ([type name]
   (->func type name (str "ret " type " null"))))