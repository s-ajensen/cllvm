(ns cllvm.util
  (:require [clojure.string :as str]))

(defn lines->str [& lines]
  (->> lines
    (filter some?)
    (str/join "\n")))