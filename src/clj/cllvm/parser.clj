(ns cllvm.parser
  (:require [instaparse.core :as insta]))

(def grammar-file "./resources/cllvm.bnf")
(def grammar (-> (slurp grammar-file)
               insta/parser))

(defn parse
  ([parser code] (parser code))
  ([code] (grammar code)))