(ns logic-dnf-test
    (:use logic-dnf logic-base)
    (:require [clojure.test :as test]))

(test/deftest dnf-parts
    (test/testing "First step - at this stage it should do nothing"
        (test/is 
            (= 
            (lneg (lor (lor (lneg (variable :x)) (variable :y)) (lneg (lor (lneg (variable :y)) (variable :z)))))
            (convert-to-basis (lneg (lor (lor (lneg (variable :x)) (variable :y)) (lneg (lor (lneg (variable :y)) (variable :z)))))))))
    (test/testing "Second and third step - negation passing and double negation removal"
        (test/is
            (=
            (pass-negation (lneg (lor (lor (lneg (variable :x)) (variable :y)) (lneg (lor (lneg (variable :y)) (variable :z))))))
            (land (land (variable :x) (lneg (variable :y))) (lor (lneg (variable :y)) (variable :z))))))
    (test/testing "Fourth and sixth step - simplify"
        (test/is 
            (=
            (simplify (land (land (variable :x) (lneg (variable :y))) (lor (lneg (variable :y)) (variable :z))))
            (land (lor (variable :z) (lneg (variable :y))) (lneg (variable :y)) (variable :x)))))
    (test/testing "Subfunction of fifth step - glue aka cartesian product"
        (test/is
            (= 
            (glue (lor (variable :a) (variable :b)) (lor (variable :c) (variable :d)))
            (list (land (variable :a) (variable :c)) (land (variable :a) (variable :d)) (land (variable :b) (variable :c)) (land (variable :b) (variable :d)))))
        (test/is
            (= 
            (glue (lor (variable :a) (variable :b)) (variable :c))
            (list (land (variable :a) (variable :c)) (land (variable :b) (variable :c)))))
        (test/is
            (= 
            (glue (variable :a) (lor (variable :c) (variable :d)))
            (list (land (variable :a) (variable :c)) (land (variable :a) (variable :d)))))
        (test/is
            (= 
            (glue (variable :a) (variable :c)))
            (list (land (variable :a) (variable :c)))))
    (test/testing "Fifth step - distrubutive law"
        (test/is
            (=
            (use-dlaw (land (lor (variable :z) (lneg (variable :y))) (lneg (variable :y)) (variable :x)))
            (lor (land (land (variable :z) (lneg (variable :y))) (variable :x)) (land (land (lneg (variable :y)) (lneg (variable :y))) (variable :x)))))))
            
(test/deftest dnf
    (test/testing "Whole convert to dnf function"
        (test/is 
            (=
            (convert-to-dnf (lneg (lor (lor (lneg (variable :x)) (variable :y)) (lneg (lor (lneg (variable :y)) (variable :z))))))
            (lor (land (variable :x) (lneg (variable :y))) (land (variable :x) (lneg (variable :y)) (variable :z)))))))

(test/run-tests 'logic-dnf-test)    
            