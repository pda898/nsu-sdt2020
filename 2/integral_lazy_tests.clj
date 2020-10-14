(ns integral-lazy-test
    (:use integral-lazy)
    (:require [clojure.test :as test]))

(test/with-test (def res (integral (fn [x] x) 1))
    (test/testing "Integral of f(x) = x when b > 0"
        (test/is (= (res 5) 12.5))
        (test/is (= (res 10) 50.0))
        (test/is (= (res 15) 112.5))
        (test/is (= (res 30) 450.0)))
    (test/testing "Integral of f(x) = x when b < 0"
        (test/is (= (res -5) 12.5))
        (test/is (= (res -10) 50.0))
        (test/is (= (res -15) 112.5))
        (test/is (= (res -30) 450.0)))
    (test/testing "Integral of f(x) = x when b = 0"
        (test/is (= (res 0) 0.0))))

(test/with-test (def res2 (integral (fn [x] x) 2))
    (test/testing "Integral of f(x) = x when b > 0, step = 2"
        (test/is (= (res2 5) 12.5))
        (test/is (= (res2 10) 50.0))
        (test/is (= (res2 15) 112.5))
        (test/is (= (res2 30) 450.0)))
    (test/testing "Integral of f(x) = x when b < 0, step = 2"
        (test/is (= (res2 -5) 12.5))
        (test/is (= (res2 -10) 50.0))
        (test/is (= (res2 -15) 112.5))
        (test/is (= (res2 -30) 450.0)))
    (test/testing "Integral of f(x) = x when b = 0, step = 2"
        (test/is (= (res2 0) 0.0))))

(test/run-tests 'integral-lazy-test)