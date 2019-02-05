(ns quiz.theory
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test.check.generators]))

(def note-regex #"([a-gA-G])(#{1,2}||b{1,2})?(\d)")

(def max-strings 6)
(def max-frets 4)

(def min-octave 2)
(def max-octave 6)

(def white-keys #{"A" "B" "C" "D" "E" "F" "G"})
(def accidentals #{"bb" "b" "" "#" "##"})

(s/def ::name string?)
(s/def ::string (s/int-in 1 max-strings))
(s/def ::fret (s/int-in 0 (inc max-frets)))
(s/def ::location (s/keys :req-un [::string ::fret]))
(s/def ::notename #(re-matches note-regex %))

(s/def ::dots (s/coll-of ::location :into #{}))
(s/def ::note-to-id ::notename)
(s/def ::db (s/keys :req-un [::name ::note-to-id ::dots]))

(s/def ::white-key white-keys)
(s/def ::octave (s/int-in min-octave max-octave))
(s/def ::accidental accidentals)
(s/def ::note (s/keys :req-un [::white-key ::octave ::accidental]))

(s/def ::tuning (s/coll-of ::notename))

(defn notename [note]
  (str (:white-key note) (:accidental note) (:octave note)))

(defn random-note []
  (gen/generate (s/gen ::note)))

(defn random-notename []
  (notename (random-note)))

(defn random-location []
  (gen/generate (s/gen ::location)))

(defn parse-notename [notename]
  (if-let [[_ white-key accidental octave] (re-matches note-regex notename)]
    {:white-key white-key :accidental accidental :octave octave}))

;; (defn location->notename [loc]
;;   )
