(ns quiz.theory
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test.check.generators]))

(def note-regex #"([a-gA-G])(#{1,2}||b{1,2})?(\d)")

(def max-strings 6)
(def max-frets 5)

(def min-octave 3)
(def max-octave 6)

(def white-keys  #{"A" "B" "C" "D" "E" "F" "G"})
(def accidentals #{"bb" "b" "#" "##"})

(s/def ::string (s/int-in 1 (inc max-strings)))
(s/def ::fret (s/int-in 0 (inc max-frets)))
(s/def ::fretboard-location (s/keys :req-un [::string ::fret]))

(s/def ::white-key white-keys)
(s/def ::octave (s/int-in min-octave max-octave))
(s/def ::accidental accidentals)
(s/def ::note-map (s/keys :req-un [::white-key ::octave]
                          :opt-un [::accidental]))

(s/def ::note #(re-matches note-regex %))

(s/def ::tuning (s/coll-of ::notename))

(defn notename [note-map]
  (str (:white-key note-map) (:accidental note-map) (:octave note-map)))

(def white-key-offset  {"C" 0 "D" 2 "E" 4 "F" 5 "G" 7 "A" 9 "B" 11})
(def accidental-offset {"bb" -2 "b" -1 "" 0 "#" 1 "##" 2})

(defn parse-notename [notename]
  (if-let [[_ white-key accidental octave] (re-matches note-regex notename)]
    {:white-key white-key
     :accidental accidental
     :octave (js/parseInt octave)}))

(defn midi-num [notename]
  (let [note (parse-notename notename)]
    (+ (white-key-offset (:white-key note))
       (accidental-offset (:accidental note))
       (* 12 (inc (:octave note))))))

(defn random-note-map []
  (gen/generate (s/gen ::note-map)))

(defn random-note []
  (notename (random-note-map)))

(defn random-note-in-range [lowest-note highest-note]
  (loop [note (random-note)]
    (let [midi (midi-num note)]
      (if (and (>= midi (midi-num lowest-note))
               (<= midi (midi-num highest-note)))
        note
        (recur (random-note))))))

(defn random-fretboard-location []
  (gen/generate (s/gen ::fretboard-location)))

(def standard-tuning ["E3" "A3" "D4" "G4" "B4" "E5"])

(def chromatic-sharps ["C" "C#" "D" "D#" "E" "F" "F#" "G" "G#" "A" "A#" "B"])
(def chromatic-flats  ["C" "Db" "D" "Eb" "E" "F" "Gb" "G" "Ab" "A" "Bb" "B"])

(defn transpose-note [notename half-steps]
  (let [{:keys [white-key
                accidental
                octave]}  (parse-notename notename)
        pitch             (str white-key accidental)
        index-sharps      (.indexOf chromatic-sharps pitch)
        index-flats       (.indexOf chromatic-flats pitch)
        [arr index]       (if-not (neg? index-sharps)
                            [chromatic-sharps index-sharps]
                            [chromatic-flats index-flats])
        offset            (+ half-steps index)
        transposed-pitch  (nth arr (mod offset 12))
        transposed-octave (+ octave (quot offset 12) (if (neg? offset) -1))]
    (str transposed-pitch transposed-octave)))

(defn fretboard-notes [{:keys [tuning fret-count]
                        :or   {tuning     standard-tuning
                               fret-count max-frets}}]
  (for [open-string-note tuning
        fret             (range (inc fret-count))]
    {:string (Math/abs (- (.indexOf tuning open-string-note) 6))
     :fret   fret
     :note   (transpose-note open-string-note fret)}))

(defn note-at [{:keys [string fret]} & {:keys [tuning fret-count]
                                        :or   {tuning     standard-tuning
                                               fret-count max-frets}}]
  (:note (first (filter #(and (= string (:string %))
                              (= fret   (:fret %)))
                        (fretboard-notes {:keys [tuning fret-count]})))))

(defn note= [& notenames]
  (apply = (map midi-num notenames)))
