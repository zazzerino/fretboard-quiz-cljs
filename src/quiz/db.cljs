(ns quiz.db
  (:require [clojure.spec.alpha :as s]))

(def note-regex #"([a-gA-G])(#{1,2}||b{1,2})?(\d)")

(s/def ::name string?)
(s/def ::string int?)
(s/def ::fret int?)
(s/def ::location (s/keys :req-un [::string ::fret]))
(s/def ::dots (s/coll-of ::location))
(s/def ::note-name #(re-matches note-regex %))
(s/def ::note-to-id ::note-name)
(s/def ::db (s/keys :req-un [::name ::note-to-id ::dots]))

(def default-db
  {:name "quiz"
   :note-to-id "Cbb4"
   :dots [{:string 5 :fret 3}
          {:string 1 :fret 0}]})
