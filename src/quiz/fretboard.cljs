(ns quiz.fretboard
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            ["fretboard-diagram" :as fd]
            [quiz.subs :as subs]))

(defn remove-children [node]
  (doseq [child (array-seq (.-childNodes node))]
    (.removeChild node child)))

(defn make-fretboard-diagram [parentId dots]
  (remove-children (.getElementById js/document parentId))
  (fd/FretboardDiagram.
   (clj->js {:parentId parentId
             :isUpdateable true
             :onClickCallback
             (fn [string fret]
               (re-frame/dispatch [:fretboard/clicked
                                   {:string string :fret fret}]))
             :dots dots})))

(defn fretboard-inner []
  (let [id "fd-node"]
    (reagent/create-class
     {:display-name "fretboard-inner"
      :reagent-render (fn [] [:div {:id id}])
      :component-did-mount
      (fn [this]
        (make-fretboard-diagram id (:dots (reagent/props this))))
      :component-did-update
      (fn [this]
        (make-fretboard-diagram id (:dots (reagent/props this))))})))

(defn fretboard-outer []
  (let [dots (re-frame/subscribe [::subs/dots])]
    (fn []
      [:div {:class "fretboard-outer"}
       [fretboard-inner {:dots @dots}]])))
