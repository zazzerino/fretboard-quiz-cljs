(ns quiz.theory)

(def note-regex #"([a-gA-G])(#{1,2}||b{1,2})?(\d)")

(defn parse-note [notename]
  (if-let [[_ white-key accidental octave] (re-matches note-regex notename)]
    {:white-key white-key :accidental accidental :octave octave}))
