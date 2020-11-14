(ns logic-calc
    (:use logic-base)
    (:gen-class))
    
(declare calculate)
(defn and_calc [expr vr newvr]
    (let [res (map #(calculate % vr newvr) (rest expr))]
        (if (every? #(constant? %) res)
            (constant (every? true? (map constant-value res)))
            (if (some #(and (constant? %) (not (constant-value %))) res)
                (constant false)
                (land res)))))

(defn or_calc [expr vr newvr]
    (let [res (map #(calculate % vr newvr) (rest expr))]
        (if (every? #(constant? %) res)
            (constant (boolean (some true? (map constant-value res))))
            (if (some #(and (constant? %) (constant-value %)) res)
                (constant true)
                (lor res)))))

(defn neg_calc [expr vr newvr]
    (let [res (calculate (rest expr) vr newvr)]
        (if (constant? res)
            (constant (not (constant-value res)))
            (lneg res))))

(def calc_func
    (atom (list 
        ;constant stays the same
        [(fn [expr vr newvr] (constant? expr)) (fn [expr vr newvr] expr)]
        ;if variable is vr - replace with new variable (or expression)
        [(fn [expr vr newvr] (and (variable? expr) (same-variables? expr vr))) (fn [expr vr newvr] newvr)]
        ;if not - stays the same
        [(fn [expr vr newvr] (variable? expr)) (fn [expr vr newvr] expr)]
        ;logic operationss from basis
        [(fn [expr vr newvr] (lneg? expr)) neg_calc]
        [(fn [expr vr newvr] (lor? expr)) or_calc]
        [(fn [expr vr newvr] (land? expr)) and_calc])))

(defn calculate 
     "replace variable vr with expression newvr in expression expr and simplify where possible"
     [expr vr newvr] ((some 
        (fn [rule] 
            (if ((first rule) expr vr newvr)
                (second rule)
                false))
        @calc_func)
        expr vr newvr))