(ns quiz.subs
  (:require [re-frame.core :as re-frame]
            [quiz.theory :as theory]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::clicked-location
 (fn [db]
   (:clicked-location db)))

(re-frame/reg-sub
 ::note-to-guess
 (fn [db]
   (:note-to-guess db)))

(re-frame/reg-sub
 ::correct-guess?
 (fn [db]
   (if-let [location (:clicked-location db)]
     (theory/note= (:note-to-guess db)
                   (theory/note-at location)))))

(re-frame/reg-sub
 ::user-score
 (fn [db]
   (:user-score db)))

(re-frame/reg-sub
 ::app-state
 (fn [db]
   (:app-state db)))
