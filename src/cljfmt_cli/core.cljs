(ns cljfmt-cli.core
  (:require
   [clojure.string :as string]
   [cljs.tools.reader.edn :as edn]
   [cljs.tools.cli :as cli]
   [cljs.nodejs :as nodejs]
   [cljfmt.core :as cljfmt]
   [rewrite-clj.parser :as rewrite-parser]
   [cljfmt-cli.io :as io]))

#_(defn find-files-to-format
    [search-path]
    (let [clj-file-re #"\.(clj|cljs|cljc|cljx|cljs\.hl|boot)$"]
      (filter (fn [path]
                (and (io/file? path)
                     (re-find clj-file-re path)))
              (io/file-seq search-path))))

(defn exit
  [status msg]
  (when-not (string/blank? msg) (println msg))
  (js/process.exit status))

(defn exit-message
  [msgs]
  (string/join \newline msgs))

(defn error-msg
  [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (exit-message errors)))

(defn check-files
  [{:keys [files]}]
  (let [invalid-files (keep (fn [f]
                              (let [content (io/slurp f)]
                                (when-not (= content (cljfmt/reformat-string content))
                                  f))) files)]
    {:exit-message (exit-message (map #(str "Invalid format: " %) invalid-files)) :ok? true}))

(defn fix-files
  [{:keys [files indents]}]
  (doseq [f files]
    (io/spit f (cljfmt/reformat-string (io/slurp f) :indents indents))))

(defn validate-opts
  [parsed-opts]
  (let [{:keys [options arguments errors]} parsed-opts]
    (cond
      errors
      {:exit-message (error-msg errors)}

      (not (or (:fix options) (:check options)))
      {:exit-message (error-msg ["You must specify either fix or check."])}

      (empty? arguments)
      {:exit-message (error-msg ["No files provided."])}

      (not (every? io/exists? arguments))
      {:exit-message (error-msg ["Some files passed do not exist."
                                 (str "Nonexistent files: " (string/join ", " (filter (complement io/exists?) arguments)))])}

      :else
      (let [indent-opts (when (io/exists? (:indents options))
                          (try
                            (edn/read-string (io/slurp (:indents options)))
                            (catch js/Error _
                              {:exit-message (error-msg [(str "Invalid EDN in: " (:indents options))])})))]
        {:action  (cond (:fix options) :fix
                        (:check options) :check)
         :options {:files   arguments
                   :indents (or (:indents-map options) indent-opts)}}))))

(def cli-opts
  [["-f" "--fix" "Fixes input files"]
   ["-c" "--check" "Checks input files"]
   ["-i" "--indents PATH" "Custom cljfmt indent options stored in a file. Defaults to .cljfmt.edn."
    :default ".cljfmt.edn"]
   [nil "--indents-map VAL"
    :parse-fn #(edn/read-string %)]])

(defn -main
  [& args]
  (nodejs/enable-util-print!)
  (let [{:keys [action options] :as validation-result} (validate-opts (cli/parse-opts args cli-opts))
        result (if (:exit-message validation-result)
                 validation-result
                 (case action
                   :fix (fix-files options)
                   :check (check-files options)))]
    (exit (if (:ok? result) 0 1) (:exit-message result))))

(set! *main-cli-fn* -main)