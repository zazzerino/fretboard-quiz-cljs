(ns quiz.views
  (:require [re-frame.core :as re-frame]
            [quiz.subs :as subs]
            [quiz.fretboard :as fretboard]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.main-panel
     [fretboard/fretboard-outer]]))
