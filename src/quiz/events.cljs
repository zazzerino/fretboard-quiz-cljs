(ns quiz.events
  (:require [re-frame.core :as re-frame]
            [clojure.spec.alpha :as s]
            [quiz.db :as db]
            [quiz.theory :as theory]))

(defn check-and-throw [spec db]
  (when-not (s/valid? spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str spec db)) {}))))

(def check-spec-interceptor
  (re-frame/after (partial check-and-throw ::db/db)))

(re-frame/reg-event-db
 ::initialize-db
 [check-spec-interceptor]
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :fretboard/add-dot
 [check-spec-interceptor]
 (fn [db [_ location]]
   (assoc db :clicked-location location)))

;; (re-frame/reg-event-db
;;  :fretboard/remove-dot
;;  [check-spec-interceptor]
;;  (fn [db [_ dot]]
;;    (assoc db :dots (set (remove (partial = dot) (:dots db))))))

;; (re-frame/reg-event-fx
;;  :fretboard/clicked
;;  [check-spec-interceptor]
;;  (fn [{:keys [db]} [_ location]]
;;    {:dispatch (condp (:app-state db) =
;;                 :playing [::check-guess location])}))

(re-frame/reg-event-fx
 :fretboard/clicked
 [check-spec-interceptor]
 (fn [{:keys [db]} [_ location]]
   (let [correct-guess? (theory/note= (:note-to-guess db)
                                      (theory/note-at location))]
     {:db (assoc db
                 :user-score (if correct-guess?
                               (inc (:user-score db))
                               (dec (:user-score db)))
                 :note-to-guess (if correct-guess?
                                  (theory/random-notename)
                                  (:note-to-guess db)))
      :dispatch [:fretboard/add-dot location]})))

(re-frame/reg-event-db
 ::set-note-to-guess
 [check-spec-interceptor]
 (fn [db [_ new-note]]
   (assoc db :note-to-guess new-note)))
