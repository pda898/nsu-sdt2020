(ns logic-base-test
    (:use logic-base)
    (:require [clojure.test :as test]))

(test/deftest constants
    (test/testing "constants"
        (test/is (constant? (constant true)))
        (test/is (constant-value (constant true)))))

(test/deftest variables
    (test/testing "variables"
        (test/is (variable? (variable :x)))
        (test/is (= :x (variable-name (variable :x))))
        (test/is (same-variables? (variable :x) (variable :x)))
        (test/is (not (same-variables? (variable :x) (variable :y))))))

(test/deftest base_ops
    (test/testing "check op types"
        (test/is (land? (land (variable :x) (constant true))))
        (test/is (lor? (lor (variable :x) (constant true))))
        (test/is (lneg? (lneg (variable :x)))))
    (test/testing "check base operation check"
        (test/is (baseop? (land (variable :x) (constant true))))
        (test/is (baseop? (lor (variable :x) (constant true))))
        (test/is (baseop? (lneg (variable :x)))))
    (test/testing "check basis check"
        (test/is (basis? (land (variable :x) (constant true))))
        (test/is (basis? (lor (variable :x) (constant true))))
        (test/is (basis? (lneg (variable :x))))
        (test/is (basis? (constant true)))
        (test/is (basis? (variable :x)))))

(test/run-tests 'logic-base-test)
