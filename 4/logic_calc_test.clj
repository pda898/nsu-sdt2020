(ns logic-calc-test
    (:use logic-calc logic-base)
    (:require [clojure.test :as test]))
	
(test/deftest calculation
	(test/testing "Testing constants and variables"
		(test/is (calculate (constant true) (variable :x) (variable :x)))
		(test/is (same-variables? (calculate (variable :x) (variable :x) (variable :y)) (variable :y)))
		(test/is (same-variables? (calculate (variable :x) (variable :y) (variable :y)) (variable :x))))
	(test/testing "Testing negation"
		(test/is (calculate (lneg (constant false)) (variable :y) (variable :y)))
		(test/is (calculate (lneg (variable :x)) (variable :x) (constant false)))
		(test/is (lneg? (calculate (lneg (variable :x)) (variable :y) (constant false)))))
	(test/testing "Testing logic OR"
		(test/is (calculate (lor (constant true) (constant false)) (variable :y) (variable :y)))
		(test/is (calculate (lor (variable :x) (constant true)) (variable :x) (constant false)))
		(test/is (calculate (lor (variable :x) (variable :y)) (variable :x) (constant true)))
		(test/is (lor? (calculate (lor (variable :x) (variable :y)) (variable :y) (constant false)))))
	(test/testing "Testing logic AND"
		(test/is (calculate (land (constant true) (constant true)) (variable :y) (variable :y)))
		(test/is (calculate (land (variable :x) (constant true)) (variable :x) (constant true)))
		(test/is (not (constant-value (calculate (land (variable :x) (variable :y)) (variable :y) (constant false)))))
		(test/is (land? (calculate (land (variable :x) (variable :y)) (variable :y) (constant true))))))
		
(test/run-tests 'logic-calc-test)