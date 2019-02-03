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

(re-frame/reg-event-fx
 :fretboard/clicked
 (fn [{:keys [db]} [_ dot]]
   {:dispatch (if (some #{dot} (:fretboard/dots db))
                [:fretboard/remove-dot dot]
                [:fretboard/add-dot dot])}))
