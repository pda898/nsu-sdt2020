(ns logic-dnf
    (:use logic-base)
    (:gen-class))

(declare convert-to-basis)

(def convert_rules
    (list 
        ;constant and variables are the same
        [(fn [expr] (constant? expr)) (fn [expr] expr)]
        [(fn [expr] (variable? expr)) (fn [expr] expr)]
        ;logic operationss from basis just pass conversion into it
        [(fn [expr] (lneg? expr)) (fn [expr] (lneg (convert-to-basis (rest expr))))]
        [(fn [expr] (lor? expr)) (fn [expr] (apply lor (map #(convert-to-basis %) (rest expr))))]
        [(fn [expr] (land? expr)) (fn [expr] (apply land (map #(convert-to-basis %) (rest expr))))]))

(defn convert-to-basis [expr]
    ((some (fn [rule] 
                (if ((first rule) expr)
                    (second rule)
                    false))
            convert_rules) expr))
            
(defn pass-negation [expr]
    (if (baseop? expr)
        (if (lneg? expr)
            (let [nextop (rest expr)]
                (cond
                    (lneg? nextop) (pass-negation (rest nextop))
                    (lor? nextop) (apply land (map #(pass-negation (lneg %)) (rest nextop)))
                    (land? nextop) (apply lor (map #(pass-negation (lneg %)) (rest nextop)))
                    :else (lneg nextop)))
            (cons (first expr) (map #(pass-negation %) (rest expr)))
        )
        expr))
        
(defn simplify [expr]
    (cond
        (lor? expr) 
            (apply lor (reduce #(if (lor? %2) (concat %1 (rest %2)) (cons %2 %1)) (cons '() (distinct (map simplify (rest expr))))))
        (land? expr) 
            (apply land (reduce #(if (land? %2) (concat %1 (rest %2)) (cons %2 %1)) (cons '() (distinct (map simplify (rest expr))))))
        ;we can skip lneg? because all logic NOT are already before variables/constants
        :else expr))
        
;cartesian product with twist
(defn glue [expr1 expr2] 
    (if (lor? expr1)
        (if (lor? expr2)
            (for [x (rest expr1) y (rest expr2)] (land x y))
            (for [x (rest expr1)] (land x expr2)))
        (if (lor? expr2)
            (for [x (rest expr2)] (land expr1 x))
            (land expr1 expr2))))
    
(defn use-dlaw [expr]
        (cond 
            (lor? expr) (apply lor (map use-dlaw (rest expr)))
            (land? expr) 
                (let [args (map use-dlaw (rest expr))] 
                     (reduce #(apply lor (glue %1 %2)) args))
            :else expr))

;(def example (lneg (lor (lor (lneg (variable :x)) (variable :y)) (lneg (lor (lneg (variable :y)) (variable :z))))))
(defn convert-to-dnf [expr]
    "convert expression expr to DNF"
    (->> expr
        ;step 1 - remove everything not from basis
        (convert-to-basis)
        ;step 2 - pass negation to the variables, also step 3 - remove double negations
        (pass-negation)
        ;step 4 - remove excess brackets and remember about idempotence
        (simplify)
        ;step 5 - use distrubutive law
        (use-dlaw)
        ;step 6 - again remove excess brackets and remember about idempotence
        (simplify)
       ))