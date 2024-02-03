(ns cllvm.llvm-spec-runner
  (:require [cllvm.parser :as parser]
            [cllvm.reader :as reader]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str]))

(def spec-path "spec/llvm/")

(defn generate-src [file expr]
  (let [ast (parser/parse expr)
        ir  (reader/ast->ir ast)]
    (spit file ir)))

(defn run-test [path name actual-expr]
  (generate-src (str spec-path path "/" name "/" name ".ll") actual-expr)
  (let [spec-file (str spec-path path "/" name "/" name "_spec.ll")
        source-file (str spec-path path "/" name "/" name ".ll")
        {:keys [out err]} (sh "/bin/sh" "-c" (str "/opt/homebrew/opt/llvm/bin/llvm-link " spec-file " " source-file " -o - | /opt/homebrew/opt/llvm/bin/lli"))]
    (print out)
    (when err (print err))
    (flush)))

(defn -main []
  (run-test "literals" "long" "19")
  (run-test "literals" "double" "3.14")
  (System/exit 0))