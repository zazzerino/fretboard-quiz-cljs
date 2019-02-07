(ns quiz.db
  (:require [clojure.spec.alpha :as s]
            [quiz.theory :as theory]))

(s/def ::name string?)
(s/def ::clicked-location ::theory/fretboard-location)
(s/def ::note-to-guess ::theory/notename)

(s/def ::db (s/keys :req-un [::name ::note-to-guess]
                    :opt-un [::clicked-location]))

(def default-db
  {:name "fretboard-quiz"
   :note-to-guess (theory/random-notename)})
