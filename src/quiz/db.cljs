(ns quiz.db
  (:require [clojure.spec.alpha :as s]
            [quiz.theory :as theory]))

(s/def ::name string?)
(s/def ::dots (s/coll-of ::theory/fretboard-location :into #{}))
(s/def ::note-to-guess ::theory/notename)
(s/def ::max-notes int?)
(s/def ::user-guess ::theory/notename)

(s/def ::db (s/keys :req-un [::name ::note-to-guess ::dots]
                    :opt [::max-notes ::user-guess]))

(def default-db
  {:name "quiz"
   :note-to-guess (theory/random-notename)
   :dots #{}
   :max-notes 1})
