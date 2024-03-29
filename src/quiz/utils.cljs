(ns quiz.utils)

(defn remove-children [node]
  (doseq [child (array-seq (.-childNodes node))]
    (.removeChild node child)))
