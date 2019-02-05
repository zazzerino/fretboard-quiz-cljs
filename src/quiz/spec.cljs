(ns quiz.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test.check.generators]
            [quiz.theory :as theory]))

(def max-strings 6)
(def max-frets 4)

(def min-octave 2)
(def max-octave 6)

(s/def ::name string?)
(s/def ::string (s/int-in 1 max-strings))
(s/def ::fret (s/int-in 0 (inc max-frets)))
(s/def ::location (s/keys :req-un [::string ::fret]))
(s/def ::dots (s/coll-of ::location :into #{}))
(s/def ::notename #(re-matches theory/note-regex %))
(s/def ::note-to-id ::notename)
(s/def ::db (s/keys :req-un [::name ::note-to-id ::dots]))

(s/def ::white-key #{"A" "B" "C" "D" "E" "F" "G"})
(s/def ::octave (s/int-in min-octave max-octave))
(s/def ::accidental #{"bb" "b" "" "#" "##"})
(s/def ::note (s/keys :req-un [::white-key ::octave ::accidental]))

(defn notename [note]
  (str (:white-key note) (:accidental note) (:octave note)))

(defn random-note []
  (gen/generate (s/gen ::note)))

(defn random-notename []
  (notename (random-note)))

(defn random-location []
  (gen/generate (s/gen ::location)))
