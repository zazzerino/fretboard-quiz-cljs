(ns quiz.db
  (:require[quiz.theory :as theory]
            [quiz.spec]))

(def default-db
  {:name "quiz"
   :note-to-id (quiz.spec/random-notename)
   :dots (repeatedly (rand-int 5) quiz.spec/random-location)})
