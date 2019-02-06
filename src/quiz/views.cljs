(ns quiz.views
  (:require [re-frame.core :as re-frame]
            [quiz.subs :as subs]
            [quiz.fretboard :as fretboard]
            [quiz.stave :as stave]))

(defn note-to-guess-display []
  (let [note (re-frame/subscribe [::subs/note-to-guess])]
    [:div
     [:p (str "note: " @note)]]))

(defn dot-display []
  (let [dots (re-frame/subscribe [::subs/dots])]
    [:div
     [:p (str @dots)]]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.main-panel
     [note-to-guess-display]
     [stave/stave-outer]
     [fretboard/fretboard-outer]
     [dot-display]
     ]))
