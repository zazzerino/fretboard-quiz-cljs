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

(re-frame/reg-event-db
 ::correct-guess
 [check-spec-interceptor]
 (fn [db [_ location]]
   (assoc db
          :clicked-location location
          :user-score (inc (:user-score db))
          :app-state :show-result)))

(re-frame/reg-event-db
 ::incorrect-guess
 [check-spec-interceptor]
 (fn [db [_ location]]
   (assoc db
          :clicked-location location
          :user-score 0)))

(re-frame/reg-event-fx
 :fretboard/clicked
 [check-spec-interceptor]
 (fn [{:keys [db]} [_ location]]
   (condp = (:app-state db)
     :playing (if (theory/note= (:note-to-guess db)
                                (theory/note-at location))
                {:dispatch [::correct-guess location]}
                {:dispatch [::incorrect-guess location]})
     :show-result nil)))

(re-frame/reg-event-db
 ::reset-game
 [check-spec-interceptor]
 (fn [db _]
   (merge db {:app-state        :playing
              :clicked-location {:string 5 :fret 3}
              :note-to-guess    (theory/random-note-in-range "E3" "G#5")})))
