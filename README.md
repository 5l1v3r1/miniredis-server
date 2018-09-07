# miniredis-server

A simple redis clone implemented in Clojure

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Install dependencies

run:

    lein deps

## Running

To start a web server for the application, run:

    lein ring server


## Usage

Send redis commands to http server ```http://localhost:3000/api```, run:
```
curl http://localhost:3000/api -d 'command=COMMAND-HERE'
```

Successful commands response begin with ```+OK``` with optional message

```+OK message(optional)```

Failed commands response begin with ```+ERR``` with error message

```+ERR message```

see below for commands supported

## Commands Supported:
  
>**GET** - Usage: ``` curl http://localhost:3000/api -d 'command=GET mykey' ``` 

              --> ```+OK myval```

              --> return the string value identified by key

>**SET** - Usage: ``` curl http://localhost:3000/api -d 'command=SET mykey myval' ```

              --> ```+OK```

              --> Instantiate or overwrite a String identified by key with value value

>**DELETE** - Usage: ``` curl http://localhost:3000/api -d 'command=DELETE mykey' ```

              --> ``` +OK ```

              --> Delete the String identified by key
       
>**LISTGET** - Usage: ``` curl http://localhost:3000/api -d 'command=LISTGET mykey' ```

              --> ``` +OK ["myval"] ```

              --> Return the List value identified by key

>**LISTSET** - Usage: ``` curl http://localhost:3000/api -d 'command=LISTSET mykey myval' ```

              --> ``` +OK ```

              --> Instantiate or overwrite a List identified by key with value value
       
>**LISTDELETE** - Usage: ``` curl http://localhost:3000/api -d 'command=LISTDELETE mykey' ```

              --> ``` +OK ```

              --> Delete the List identified by key

>**LISTAPPEND** - Usage: ``` curl http://localhost:3000/api -d 'command=LISTAPPEND mykey myval2' ```

              --> ``` +OK ```

              --> Append a String value to the end of the List identified by key

>**LISTPOP** - Usage: ``` curl http://localhost:3000/api -d 'command=LISTPOP mykey' ```

              --> ``` +OK ```

              --> Remove the last element in the List identified by key, and return that element.

>**MAPGET** - Usage: ``` curl http://localhost:3000/api -d 'command=MAPGET mykey' ```
              --> ``` +OK myval ```
              --> Return the Map value identified by key

>**MAPSET** - Usage: ``` curl http://localhost:3000/api -d 'command=MAPSET mykey myval' ```
              --> ``` +OK ```
              --> Instantiate or overwrite a Map identified by key with value value

>**MAPDELETE** - Usage: ``` curl http://localhost:3000/api -d 'command=MAPDELETE mykey' ```
              --> ``` +OK ```
              --> Delete the Map identified by key

>**MAPMAPGET** - Usage: ``` curl http://localhost:3000/api -d 'command=MAPMAPGET mykey mapkey' ```
              --> ``` +OK mapvalue ```
              --> Return the String identified by mapkey from within the Map identified by key

>**MAPMAPSET** - Usage: ``` curl http://localhost:3000/api -d 'command=MAPMAPSET mykey mapkey mapvalue' ```
              --> ``` +OK ```
              --> Add the mapping mapkey -> mapvalue to the Map identified by key

>**MAPMAPDELETE** - Usage: ``` curl http://localhost:3000/api -d 'command=MAPMAPDELETE mykey mapkey' ```
              --> ``` +OK ```
              --> Delete the value identified by mapkey from the Map identified by key

>**SEARCHKEYS (string keys)** - Usage: ``` curl http://localhost:3000/api -d 'command=SEARCHKEYS string key' ```
              --> ``` +OK ["mykey"] ```
              --> Search string keys identified by query

>**SEARCHKEYS (list keys)** - Usage: ``` curl http://localhost:3000/api -d 'command=SEARCHKEYS list key' ```
              --> ``` +OK ["mykey"] ```
              --> Search list keys identified by query

>**SEARCHKEYS (map keys)** - Usage: ``` curl http://localhost:3000/api -d 'command=SEARCHKEYS map key' ```
              --> ``` +OK ["mykey"] ```
              --> Search map keys identified by query 

## Testing

run:
    
    lein test miniredis-server.test.handler

## License

Copyright © 2018 finixbit
