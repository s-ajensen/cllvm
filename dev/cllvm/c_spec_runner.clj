(ns cllvm.c-spec-runner
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]))

(defn run-command [command]
  (let [{:keys [out err]} (sh "/bin/sh" "-c" command)]
    (print out)
    (when err (print err))
    (flush)))

(defn compile [flags file]
  (run-command (str "gcc " flags " " file)))

(defn compile-to-object [file]
  (compile "-c" file))

(defn files-ending-with [suffix directory]
  (->> (.listFiles (clojure.java.io/file directory))
       (map #(.getName %))
       (filter #(str/ends-with? % suffix))
       (doall)))

(defn o-files [directory]
  (files-ending-with ".o" directory))

(defn c-files [directory]
  (map #(str directory %) (files-ending-with ".c" directory)))

(defn link-o-files [exec-name]
  (let [files-str (str/join " " (o-files "."))]
    (print "linking .o files...")
    (run-command (str "gcc " files-str " -o " exec-name))
    (println "done")))

(defn delete-o-files []
  (print "deleting *.o files...")
  (run-command "rm *.o")
  (println "done"))

(defn run-specs [exec-name]
  (println "running specs...\n")
  (run-command (str "./" exec-name)))

(defn compile-specs []
  (print "compiling specs...")
  (let [paths ["src/c/" "spec/c/"]
        src-files (apply concat (map c-files paths))]
    (doall (map compile-to-object src-files)))
  (println "done"))

(defn -main []
  (let [exec-name "c-specs"]
    (compile-specs)
    (link-o-files exec-name)
    (delete-o-files)
    (run-specs exec-name)
    (System/exit 0)))