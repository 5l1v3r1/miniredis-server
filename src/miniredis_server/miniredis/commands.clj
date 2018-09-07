(ns miniredis-server.miniredis.commands)


(def database-string (atom {})) 
(def database-list   (atom {})) 
(def database-map    (atom {})) 
(def crlf "\r\n")

(def commands-list
  #{"GET"     "SET"     "DELETE" 
    "LISTGET" "LISTSET" "LISTDELETE" "LISTAPPEND" "LISTPOP" 
    "MAPGET"  "MAPSET"  "MAPDELETE"  "MAPMAPGET"  "MAPMAPSET" "MAPMAPDELETE"
    "SEARCHKEYS"})


(defn boolean? [x]
  (instance? Boolean x))


(defn command-found 
  [command]
  {:pre  [(string? command)] 
   :post [(boolean? %)]}
  (condp contains? command commands-list
    true
    false))


(defn valid-command-arguments-size
  [command arguments-size]
  {:pre  [(string? command) (integer? arguments-size)] 
   :post [(boolean? %)]}
  (case command
    "SET"         (= 2 arguments-size)
    "GET"         (= 1 arguments-size)
    "DELETE"      (= 1 arguments-size)
    "LISTGET"     (= 1 arguments-size)
    "LISTSET"     (= 2 arguments-size)
    "LISTDELETE"  (= 1 arguments-size)
    "LISTAPPEND"  (= 2 arguments-size)
    "LISTPOP"     (= 1 arguments-size)
    "MAPGET"      (= 1 arguments-size)
    "MAPSET"      (= 2 arguments-size)
    "MAPDELETE"   (= 1 arguments-size)
    "MAPMAPGET"   (= 2 arguments-size)
    "MAPMAPSET"   (= 3 arguments-size)
    "MAPMAPDELETE"(= 2 arguments-size)
    "SEARCHKEYS"  (= 2 arguments-size)
                  false))


(defn get-command
  "return the string value identified by key"
  [keyparam]
  {:pre  [(string? keyparam)] 
   :post [(string? %)]}
   (let [keyval (get @database-string keyparam)
         keyvalsize (count keyval)]
      (cond 
        (= 0 keyvalsize) (str "+OK nil" crlf)
        :else (str "+OK " keyval crlf))))


(defn set-command
  "Instantiate or overwrite a String identified by key with value value"
  [keyparam valueparam]
  {:pre  [(string? keyparam), (string? valueparam)] 
   :post [(string? %)]}
  (do 
    (swap! database-string assoc keyparam valueparam)
    (str "+OK" crlf)))


(defn del-command
  "Delete the String identified by key"
  [keyparam]
  {:pre  [(string? keyparam)] 
   :post [(string? %)]}
  (do 
    (swap! database-string dissoc keyparam)
    (str "+OK" crlf)))


(defn listget-command
  "Return the List value identified by key"
  [keyparam]
  {:pre  [(string? keyparam)] 
   :post [(string? %)]}
   (let [keyval (get @database-list keyparam)
         keyvalsize (count keyval)]
      (cond 
        (= 0 keyvalsize) (str "+OK nil" crlf)
        :else (str "+OK " keyval crlf))))


(defn listset-command
  "Instantiate or overwrite a List identified by key with value value"
  [keyparam valueparam]
  {:pre  [(string? keyparam), (string? valueparam)]
   :post [(string? %)]}
  (let [new-list (conj [] valueparam)]
    (swap! database-list assoc keyparam new-list)
    (str "+OK" crlf)))


(defn listdel-command
  "Delete the List identified by key"
  [keyparam]
  {:pre  [(string? keyparam)] 
   :post [(string? %)]}
  (do 
    (swap! database-list dissoc keyparam)
    (str "+OK" crlf)))


(defn listappend-command
  "Append a String value to the end of the List identified by key"
  [keyparam, valueparam]
  {:pre  [(string? keyparam), (string? valueparam)] 
   :post [(string? %)]}
  (condp contains? keyparam @database-list 
    (let [current-list (get @database-list keyparam)
          updated-list (conj current-list valueparam)]
      (swap! database-list assoc keyparam updated-list)
      (str "+OK" crlf))
    (str "-ERR key not found '" keyparam "'" crlf)))


(defn listpop-command
  "Remove the last element in the List identified by key, 
   and return that element."
  [keyparam]
  {:pre  [(string? keyparam)] 
   :post [(string? %)]}
  (condp contains? keyparam @database-list 
    (let [current-list (get @database-list keyparam)
          updated-list (pop current-list)]
      (swap! database-list assoc keyparam updated-list)
      (str "+OK" crlf))
    (str "-ERR key not found '" keyparam "'" crlf)))


(defn mapget-command
  "Return the Map value identified by key"
  [keyparam]
  {:pre  [(string? keyparam)] 
   :post [(string? %)]}
   (let [keymap (get @database-map keyparam)
         keyval (get keymap :value)
         keyvalsize (count keyval)]
      (cond 
        (= 0 keyvalsize) (str "+OK nil" crlf)
        :else (str "+OK " keyval crlf))))


(defn mapset-command
  "Instantiate or overwrite a Map identified by key with value value"
  [keyparam valueparam]
  {:pre  [(string? keyparam), (string? valueparam)]
   :post [(string? %)]}
  (let [new-map (assoc {:dict {}} :value valueparam)]
    (swap! database-map assoc keyparam new-map)
    (str "+OK" crlf)))


(defn mapdel-command
  "Delete the Map identified by key"
  [keyparam]
  {:pre  [(string? keyparam)] 
   :post [(string? %)]}
  (do 
    (swap! database-map dissoc keyparam)
    (str "+OK" crlf)))


(defn mapmapget-command
  "Return the String identified by mapkey from within the Map 
   identified by key"
  [keyparam, mapkeyparam]
  {:pre  [(string? keyparam), (string? mapkeyparam)] 
   :post [(string? %)]}
  (condp contains? keyparam @database-map 
    (let [current-map (get @database-map keyparam)
          current-map-dict (get current-map :dict)
          mapkeyval (get current-map-dict mapkeyparam)
          mapkeyvalsize (count mapkeyval)]
      (cond 
        (= 0 mapkeyvalsize) (str "+OK nil" crlf)
        :else (str "+OK " mapkeyval crlf)))
    (str "-ERR key not found '" keyparam "'" crlf)))


(defn mapmapset-command
  "Add the mapping mapkey -> mapvalue to the Map identified by key"
  [keyparam, mapkeyparam, mapvalueparam]
  {:pre  [(string? keyparam), (string? mapkeyparam), (string? mapvalueparam)] 
   :post [(string? %)]}
  (condp contains? keyparam @database-map 
    (let [current-map (get @database-map keyparam)
          current-map-dict (get current-map :dict)
          update-map-dict (assoc current-map-dict mapkeyparam mapvalueparam)
          update-map (assoc current-map :dict update-map-dict)]
      (swap! database-map assoc keyparam update-map)
      (str "+OK" crlf))
    (str "-ERR key not found '" keyparam "'" crlf)))


(defn mapmapdel-command
  "Delete the value identified by mapkey from the Map identified by key"
  [keyparam, mapkeyparam]
  {:pre  [(string? keyparam), (string? mapkeyparam)] 
   :post [(string? %)]}
  (condp contains? keyparam @database-map 
    (let [current-map (get @database-map keyparam)
          current-map-dict (get current-map :dict)
          update-map-dict (dissoc current-map-dict mapkeyparam)
          update-map (assoc current-map :dict update-map-dict)]
      (swap! database-map assoc keyparam update-map)
      (str "+OK" crlf))
    (str "-ERR key not found '" keyparam "'" crlf)))


(defn searchkeys-command
  "Delete the String identified by key"
  [key-category key-query]
  {:pre  [(string? key-category), (string? key-query)] 
   :post [(string? %)]}
  (let [filter-query (fn [v] (clojure.string/includes? v key-query))]
    (case (clojure.string/upper-case key-category)
      "STRING"  (let [search-list (into [] (keys @database-string))
                      filtered-list (filter filter-query search-list)]
                  (str "+OK " (into [] filtered-list) crlf))
    "MAP"       (let [search-list (into [] (keys @database-list))
                      filtered-list (filter filter-query search-list)]
                  (str "+OK " (into [] filtered-list) crlf))
    "LIST"      (let [search-list (into [] (keys @database-map))
                      filtered-list (filter filter-query search-list)]
                  (str "+OK " (into [] filtered-list) crlf))
                (str "-ERR no keys found using '" key-query "'" crlf))))


(defn run-command [command params]
  (case command
    "SET"          (apply set-command params)
    "GET"          (apply get-command params)
    "DELETE"       (apply del-command params)
    "LISTGET"      (apply listget-command params)
    "LISTSET"      (apply listset-command params)
    "LISTDELETE"   (apply listdel-command params)
    "LISTAPPEND"   (apply listappend-command params)
    "LISTPOP"      (apply listpop-command params)
    "MAPGET"       (apply mapget-command params)
    "MAPSET"       (apply mapset-command params)
    "MAPDELETE"    (apply mapdel-command params)
    "MAPMAPGET"    (apply mapmapget-command params)
    "MAPMAPSET"    (apply mapmapset-command params)
    "MAPMAPDELETE" (apply mapmapdel-command params)
    "SEARCHKEYS"   (apply searchkeys-command params)
                   (str "+OK" crlf)))


(defn run 
  [command-string]
  (if empty? command-string)
    (str "+OK") 
    (let [splt (clojure.string/split command-string #" ")
          cmd  (clojure.string/upper-case (first splt))
          args (rest splt)]
      (if (command-found cmd) 
        (if (valid-command-arguments-size cmd (count args)) 
          (run-command cmd (reverse (into (list) args)))
          (str "-ERR wrong number of arguments for '" cmd "' command" crlf))
        (str "-ERR unknown command '" cmd "'" crlf))))
