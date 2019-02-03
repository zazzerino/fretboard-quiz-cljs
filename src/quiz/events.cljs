(ns quiz.events
  (:require [re-frame.core :as re-frame]
            [quiz.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :fretboard/add-dot
 (fn [db [_ dot]]
   (assoc db :fretboard/dots (conj (:fretboard/dots db) dot))))

(re-frame/reg-event-db
 :fretboard/remove-dot
 (fn [db [_ dot]]
   (assoc db :fretboard/dots (vec (remove #(= % dot) (:fretboard/dots db))))))
