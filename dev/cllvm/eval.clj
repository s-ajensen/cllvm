(ns cllvm.eval
  (:require [cllvm.parser :as parser]
            [cllvm.reader :as reader]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str]))

(defn -main [expr & args]
  (let [ast (parser/parse expr)
        ir  (reader/ast->ir ast)
        path (str/trim (:out (sh "pwd")))]
    (spit "temp.ll" ir)
    (prn path)
    (let [{:keys [exit out err]} (sh "/bin/sh" "-c" (str "/opt/homebrew/opt/llvm/bin/llvm-link " path "/src/ll/cllvm/runtime.ll " path "/temp.ll -o - | /opt/homebrew/opt/llvm/bin/lli"))]
      (print out))))