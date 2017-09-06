(ns cljfmt-cli.io
  (:refer-clojure :exclude [exists?])
  (:require
    [cljs.nodejs :as nodejs]))

(def fs (nodejs/require "fs"))

(defn slurp
  [filename]
  (.readFileSync fs filename "utf8"))

(defn spit
  [filename content]
  (.writeFileSync fs filename content "utf8"))

(defn file?
  [path]
  (.. fs (lstatSync path) isFile))

(defn directory?
  [path]
  (.. fs (lstatSync path) isDirectory))

(defn exists?
  [path]
  (.existsSync fs path))

(defn file-seq
  [path]
  (.readdirSync fs path))