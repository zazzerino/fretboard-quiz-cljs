(ns quiz.db
  (:require [clojure.spec.alpha :as s]))

(s/def ::name string?)
(s/def ::string int?)
(s/def ::fret int?)
(s/def ::location (s/keys :req-un [::string ::fret]))
(s/def ::dots (s/coll-of ::location))
(s/def ::db (s/keys :req-un [::name ::dots]))

(def default-db
  {:name "quiz"
   :dots [{:string 5 :fret 3}
          {:string 1 :fret 0}]})
