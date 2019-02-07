(ns quiz.fretboard
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            ["fretboard-diagram" :as fd]
            [quiz.subs :as subs]
            [quiz.utils :as utils]))

(defn make-fretboard-diagram [parent-id dots]
  (utils/remove-children (.getElementById js/document parent-id))
  (fd/FretboardDiagram.
   (clj->js (merge
             (if-not (or (empty? dots)
                         (every? nil? dots))
               {:dots dots})
             {:parentId parent-id
              ;; :dots dots
              :onClickCallback
              (fn [string fret]
                (re-frame/dispatch [:fretboard/clicked
                                    {:string string :fret fret}]))}))))

(defn fretboard-inner []
  (let [id "fd-node"
        draw (fn [this]
               (make-fretboard-diagram id (:clicked-location
                                           (reagent/props this))))]
    (reagent/create-class
     {:display-name "fretboard-inner"
      :reagent-render (fn [] [:div {:id id}])
      :component-did-mount draw
      :component-did-update draw})))

(defn fretboard-outer []
  (let [clicked-location (re-frame/subscribe [::subs/clicked-location])]
    (fn []
      [:div {:class "fretboard-outer"}
       [fretboard-inner {:clicked-location [@clicked-location]}]])))
