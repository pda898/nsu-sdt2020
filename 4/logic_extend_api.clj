(ns logic-extend-api
    (:use logic-base logic-calc logic-dnf)
    (:gen-class))
    
(defn register 
     "register new logic function with pre prefix which is calculated by def_func and translated into basis by convert_func\n  def_func should take 3 args - expression, variable to replace, new value for replaced variable\n  convert_func should take 1 arg - expression which will be converted"
     [pre def_func convert_func] 
     (do
        (swap! extended-types concat pre)
        (swap! calc_func concat [[(fn [expr vr newvr] (= (first expr) pre)) def_func]])
        (swap! convert_rules concat [[(fn [expr] (= (first expr) pre)) convert_func]]))
		;hiding our atoms
		nil)