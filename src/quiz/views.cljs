(ns quiz.views
  (:require [re-frame.core :as re-frame]
            [quiz.subs :as subs]
            [quiz.fretboard :as fretboard]
            [quiz.theory :as theory]
            [quiz.events :as events]
            [quiz.stave :as stave]))

(defn user-score-display []
  (let [user-score (re-frame/subscribe [::subs/user-score])]
    [:div
     [:p "Score: " @user-score]]))

(defn reset-button []
  [:button {:on-click #(re-frame/dispatch [::events/reset-game])}
   "Play again"])

(defn main-panel []
  (let [name           (re-frame/subscribe [::subs/name])
        app-state      (re-frame/subscribe [::subs/app-state])
        correct-guess? (re-frame/subscribe [::subs/correct-guess?])]
    [:div.main-panel
     (condp = @app-state
       :playing     [:div
                     [stave/stave-outer]
                     [fretboard/fretboard-outer]
                     [user-score-display]]
       :show-result [:div
                     [stave/stave-outer]
                     [fretboard/fretboard-outer]
                     [:p "You got it!"]
                     [user-score-display]
                     [reset-button]])]))
