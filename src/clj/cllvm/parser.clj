(ns cllvm.parser
  (:require [instaparse.core :as insta]))

(def grammar-file "./resources/cllvm.bnf")
(def grammar (-> (slurp grammar-file)
               insta/parser))

(defn parse
  ([parser code] (parser code))
  ([code] (grammar code)))

; TODO: add semantic analysis step after parsing (for radix + converting long to bigint/bigdecimal if necessary + map prefixes for keyword keys + whatever else)