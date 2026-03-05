import re

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        text = f.read()

    # Utils.java mappings
    utils_fixes = {
        'toStringList': 'Converte una lista generica in una lista di stringhe chiamando toString() su ciascun elemento.',
        'concat': 'Concatena due array generici restituendo un nuovo array unito.',
        'concat (Component)': 'Concatena due array di Componenti testuali.',
        'ordinalIndexOf': 'Trova l\'indice dell\'N-esima occorrenza di una sottostringa.',
        'join': 'Unisce un array di stringhe utilizzando un delimitatore.',
        'join (Collection)': 'Unisce gli elementi di una Collection in una stringa utilizzando un delimitatore.',
        'containsIgnoreCase': 'Verifica se una determinata stringa è presente in una lista ignorando maiuscole e minuscole.',
        'isNameValid': 'Valida il nome di un giocatore assicurandosi che sia alfanumerico e non superi i 16 caratteri.',
        'safeConnect': 'Connette in sicurezza un giocatore a un server specificato.',
        'formatTime': 'Formatta un timestamp (in millisecondi) come data leggibile.',
        'runAsync': 'Esegue un Runnable in modo asincrono sul thread pool di Velocity.',
        'sendMessage (legacy)': 'Invia un messaggio di testo utilizzando la sintassi legacy Bungee/Spigot (con i codici colore standard).',
        'sendMessage (Component)': 'Invia un messaggio Component Kyori Adventure nativo al giocatore.'
    }

    # UltimateFriends.java mappings (currently they miss the method name!)
    # E.g. currently we have: 
    # /**
    #  * Recupera il profilo di un giocatore dalla memoria cache.
    #  * 
    #  * @param var0...
    
    # We will just rewrite UltimateFriends completely.

    
    if 'Utils.java' in filepath:
        for k, v in utils_fixes.items():
            pattern = r'(\*\s*' + re.escape(k) + r')\n\s*\*'
            text = re.sub(pattern, r'\1\n    * ' + v + r'\n    *', text)
            
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(text)

process_file('src/main/java/com/velocityPort/Utils.java')
