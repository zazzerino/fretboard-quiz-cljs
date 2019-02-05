(ns quiz.fretboard
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            ["fretboard-diagram" :as fd]
            [quiz.subs :as subs]
            [quiz.utils :as utils]))

(defn make-fretboard-diagram [parent-id dots]
  (utils/remove-children (.getElementById js/document parent-id))
  (fd/FretboardDiagram.
   (clj->js {:parentId parent-id
             :isUpdateable true
             :onClickCallback
             (fn [string fret]
               (re-frame/dispatch [:fretboard/clicked
                                   {:string string :fret fret}]))
             :dots dots})))

(defn fretboard-inner []
  (let [id "fd-node"
        draw (fn [this]
               (make-fretboard-diagram id (:dots (reagent/props this))))]
    (reagent/create-class
     {:display-name "fretboard-inner"
      :reagent-render (fn [] [:div {:id id}])
      :component-did-mount draw
      :component-did-update draw})))

(defn fretboard-outer []
  (let [dots (re-frame/subscribe [::subs/dots])]
    (fn []
      [:div {:class "fretboard-outer"}
       [fretboard-inner {:dots @dots}]])))
