(ns quiz.views
  (:require [re-frame.core :as re-frame]
            [quiz.subs :as subs]
            [quiz.fretboard :as fretboard]
            [quiz.stave :as stave]))

(defn guess-info []
  (let [correct-guess? (re-frame/subscribe [::subs/correct-guess?])]
    [:div
     [:p (str @correct-guess?)]]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.main-panel
     [guess-info]
     [stave/stave-outer]
     [fretboard/fretboard-outer]]))
