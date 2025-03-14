;; aka task 2.2 
(ns integral-lazy
    (:gen-class))
(defn integral [func step]
    (let 
        [sum (map first (iterate (fn [[s i]] [(+ s (/ (* (+ (func i) (func (- i step))) step) 2)) (+ i step)]) [0.0 step]))
        negsum (map first (iterate (fn [[s i]] [(+ s (/ (* (+ (func i) (func (+ i step))) step) 2)) (- i step)]) [0.0 (* -1 step)]))]
    (fn [b] 
    (if (> b 0)
        (let 
            [diff (- b (* (Math/floor (/ b step)) step))] 
             (+ (nth sum (Math/floor (/ b step))) (/ (* (+ (func b) (func (- b diff))) diff) 2)))
        (let 
            [diff (- b (* (Math/floor (/ b step)) step))] 
            (* (+ (nth negsum (Math/floor (/ (* -1 b) step))) (/ (* (+ (func b) (func (+ b diff))) diff) 2)) -1))))))
