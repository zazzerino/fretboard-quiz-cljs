(ns quiz.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :fretboard/dots
 (fn [db]
   (:fretboard/dots db)))
