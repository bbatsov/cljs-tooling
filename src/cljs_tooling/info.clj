(ns cljs-tooling.info
  (:require [cljs-tooling.util.analysis :as a]
            [cljs-tooling.util.misc :as u]))

;;; TODO
(defn format-ns
  [ns]
  (dissoc ns :defs))

;;; TODO
(defn format-var
  [v]
  v)

(defn info
  "Returns an info map on the symbol in the context of the namespace, resolving aliases.
'sym' can refer to a top-level var, a namespace, or an alias, the context-ns is optional"
  [env sym & [context-ns]]
  (u/cond-let
   [ns (a/find-ns env sym)] (format-ns ns)

   [ns-alias (-> (a/aliased-nses env context-ns)
                 (get sym))] (format-ns (a/find-ns env ns-alias))

   [context-var (get (a/ns-vars env context-ns) sym)] (format-var context-var)

   [scoped-context (-> (a/aliased-nses env context-ns)
                       (get (symbol (namespace sym))))] (info env (symbol (name sym)) scoped-context)))