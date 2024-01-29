(ns cllvm.ll
  (:require [clojure.string :as str]))

(defn ->module [name]
  (str "; ModuleID = '" name "'"))
(defn ->source [name]
  (str "source_filename = \"" name ".ll\""))

(defn ->type [name & sections]
  (str \% name " = type { " (str/join ", " sections) " }"))