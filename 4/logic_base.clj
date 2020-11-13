(ns logic-base
    (:gen-class))
;Class for base logic operations and definitions

(defn variable "variable constructor"
    [nm] {:pre [(keyword? nm)]} (list ::var nm))

(defn variable? "check if expr variable"
	[expr] (= (first expr) ::var))

(defn variable-name "get variable name" 
	[v] (second v))
   
(defn same-variables? "check if variables equal" 
	[v1 v2] (and (variable? v1) (variable? v2) (= (variable-name v1) (variable-name v2))))

(defn constant "constant constructor" 
	[vl] {:pre [(boolean? vl)]} (list ::cnt vl))

(defn constant? "check if expr constant" 
	[expr] (= (first expr) ::cnt))

(defn constant-value "get constant value" 
	[v] (second v))

(defn land "logic and" 
	[expr & rest] (cons ::and (cons expr rest)))

(defn land? "check if expr and operation"
	[expr] (= (first expr) ::and))

(defn lor "logic or" 
	[expr & rest] (cons ::or (cons expr rest)))

(defn lor? "check if expr or operation"
    [expr] (= (first expr) ::or))

(defn lneg "logic not"
    [expr] (cons ::neg expr))

(defn lneg? "check if expr neg operation"
     [expr] (= (first expr) ::neg))
 
(defn baseop? "check if expr base operation"
    [expr] (or (lneg? expr) (lor? expr) (land? expr)))

(defn basis? "check if expr from the basis"
    [expr] (or (constant? expr) (variable? expr) (baseop? expr)))

(def extended-types
    (atom (list)))

(defn lexpression? "check if expr logic expression (only top level)"
	[expr] (or (basis? expr) (some #(= (first expr) %)) @extended-types))