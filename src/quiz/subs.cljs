(ns quiz.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::dots
 (fn [db]
   (:dots db)))

(re-frame/reg-sub
 ::note-to-id
 (fn [db]
   (:note-to-id db)))
