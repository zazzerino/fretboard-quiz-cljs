(ns quiz.events
  (:require [re-frame.core :as re-frame]
            [clojure.spec.alpha :as s]
            [quiz.db :as db]))

(defn check-and-throw [spec db]
  (when-not (s/valid? spec db)
    (throw (ex-info (str "spec check failed: " (s/explain spec db)) {}))))

(def check-spec-interceptor (re-frame/after
                             (partial check-and-throw :quiz.db/db)))

(re-frame/reg-event-db
 ::initialize-db
 [check-spec-interceptor]
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :fretboard/add-dot
 [check-spec-interceptor]
 (fn [db [_ dot]]
   (assoc db :dots (conj (:dots db) dot))))

(re-frame/reg-event-db
 :fretboard/remove-dot
 [check-spec-interceptor]
 (fn [db [_ dot]]
   (assoc db :dots (vec (remove #(= % dot) (:dots db))))))

(re-frame/reg-event-fx
 :fretboard/clicked
 [check-spec-interceptor]
 (fn [{:keys [db]} [_ dot]]
   {:dispatch (if (some #{dot} (:dots db))
                [:fretboard/remove-dot dot]
                [:fretboard/add-dot dot])}))

(re-frame/reg-event-db
 ::set-note-to-id
 [check-spec-interceptor]
 (fn [db [_ new-note]]
   (assoc db :note-to-id new-note)))
