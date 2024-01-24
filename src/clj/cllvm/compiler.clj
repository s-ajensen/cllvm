(ns cllvm.compiler)

(defn ->module-def [ns]
  (str "; ModuleID = '" ns "'\n"))
(defn ->src-def [ns]
  (str "source_filename = \"" ns ".ll\"\n"))
(defn ->main-def []
  (str "define i32 @main() {\n}\n"))

(defn clj->ir
  ([expr] (clj->ir "user" expr))
  ([ns expr]
   (str
     (->module-def ns)
     (->src-def ns)
     (->main-def))))