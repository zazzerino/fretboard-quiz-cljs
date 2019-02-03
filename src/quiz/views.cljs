(ns quiz.views
  (:require [re-frame.core :as re-frame]
            [quiz.subs :as subs]
            [quiz.fretboard :as fretboard]))

(defn dot-display []
  (let [dots (re-frame/subscribe [::subs/dots])]
    [:div.dot-display
     [:p (str @dots)]]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.main-panel
     [dot-display]
     [fretboard/fretboard-outer]]))
