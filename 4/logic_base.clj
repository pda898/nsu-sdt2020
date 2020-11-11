(ns logic-base
    (:gen-class))
;Class for base logic operations and definitions

(defn variable [nm]
    "variable constructor"
    {:pre [(keyword? nm)]}
    (list ::var nm))

(defn variable? [expr]
    "check if expr variable"
    (= (first expr) ::var))

(defn variable-name [v]
    "get variable name"
    (second v))

(defn same-variables? [v1 v2]
	"check if variables equal"
	(and (variable? v1) (variable? v2) (= (variable-name v1) (variable-name v2))))

(defn constant [vl]
    "constant constructor"
    {:pre [(boolean? vl)]}
    (list ::cnt vl))

(defn constant? [expr]
    "check if expr constant"
    (= (first expr) ::cnt))

(defn constant-value [v]
    "get constant value"
    (second v))

(defn land [expr & rest] 
    "logic and"
    (cons ::and (cons expr rest)))

(defn land? [expr]
    "check if expr and operation"
    (= (first expr) ::and))

(defn lor [expr & rest] 
    "logic or"
    (cons ::or (cons expr rest)))

(defn lor? [expr]
    "check if expr or operation"
    (= (first expr) ::or))

(defn lneg [expr] 
    "logic not"
    (cons ::neg expr))

(defn lneg? [expr]
    "check if expr neg operation"
    (= (first expr) ::neg))
 
(defn baseop? [expr]
    "check if expr base operation"
    (or (lneg? expr) (lor? expr) (land? expr)))

(defn basis? [expr]
    "check if expr from the basis"
    (or (constant? expr) (variable? expr) (baseop? expr)))
