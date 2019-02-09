(ns quiz.db
  (:require [clojure.spec.alpha :as s]
            [quiz.theory :as theory]))

(s/def ::name string?)
(s/def ::clicked-location ::theory/fretboard-location)
(s/def ::note-to-guess ::theory/note)
(s/def ::note-to-guess-history (s/coll-of ::note-to-guess :kind vector?))
(s/def ::user-score int?)
(s/def ::app-state #{:playing :show-result})

(s/def ::db (s/keys :req-un [::name ::note-to-guess ::user-score
                             ::app-state ::note-to-guess-history]
                    :opt-un [::clicked-location]))

(def default-db
  {:name "fretboard-quiz"
   :app-state :playing
   :note-to-guess (theory/random-note)
   :note-to-guess-history []
   :user-score 0})
