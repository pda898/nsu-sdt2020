(ns logic-extend-api-test
    (:use logic-base logic-calc logic-dnf logic-extend-api)
    (:require [clojure.test :as test]))
    
(defn impl [expr1 expr2]
    (cons ::impl (list expr1 expr2)))
    
(defn impl_calc [expr vr newvr]
    (let [res (map #(calculate % vr newvr) (rest expr))]
        (cond
            (every? constant? res) (constant (or (not (constant-value (first res))) (constant-value (second res))))
            (and (constant? (first res)) (not (constant-value (first res)))) (constant true)
            (and (constant? (second res)) (constant-value (second res))) (constant true)
            :else (apply impl res))))
            
(defn impl_conv [expr]
    (let [res (map #(convert-to-basis %) (rest expr))]
        (lor (lneg (first res)) (second res))))

            
(test/deftest extend-api
	(test/testing "Register function"
		(test/is (not (register ::impl impl_calc impl_conv))))
    (test/testing "Extension API through implication - calculation part"
        (test/is (constant-value (calculate (impl (variable :x) (variable :y)) (variable :x) (constant false))))
        (test/is (constant-value (calculate (impl (variable :x) (variable :y)) (variable :y) (constant true))))
        (test/is (constant-value (calculate (impl (variable :x) (constant true)) (variable :x) (constant true)))))
    (test/testing "Extension API through implication - dnf part"
        (test/is 
            (=
            (convert-to-dnf (lneg (lor (lor (lneg (variable :x)) (variable :y)) (lneg (impl (variable :y) (variable :z))))))
            (lor (land (variable :x) (lneg (variable :y))) (land (variable :x) (lneg (variable :y)) (variable :z)))))))


(test/run-tests 'logic-extend-api-test)