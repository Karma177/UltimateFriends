import re

with open('src/main/java/com/velocityPort/UltimateFriends.java', 'r', encoding='utf-8') as f:
    text = f.read()

replacements = [
    (r'public static UltimateFriends getInstance\(\) {',
     r'/**\n    * Recupera l\'istanza principale del plugin.\n    * \n    * @return L\'istanza di UltimateFriends\n    */\n   public static UltimateFriends getInstance() {'),
    (r'public static void setConfig\(Config var0\) {',
     r'/**\n    * Imposta la configurazione del plugin.\n    * \n    * @param var0 Il nuovo oggetto Config\n    */\n   public static void setConfig(Config var0) {'),
    (r'public static Config getConfig\(\) {',
     r'/**\n    * Recupera la configurazione corrente del plugin.\n    * \n    * @return L\'oggetto Config in uso\n    */\n   public static Config getConfig() {'),
    (r'public static void setLanguageLoader\(LanguageLoader var0\) {',
     r'/**\n    * Imposta il sistema di caricamento delle stringhe localizzate.\n    * \n    * @param var0 Il nuovo LanguageLoader\n    */\n   public static void setLanguageLoader(LanguageLoader var0) {'),
    (r'public static LanguageLoader getLanguageLoader\(\) {',
     r'/**\n    * Recupera il gestore delle lingue.\n    * \n    * @return Il LanguageLoader in uso\n    */\n   public static LanguageLoader getLanguageLoader() {'),
    (r'public static void setStorage\(Storage var0\) {',
     r'/**\n    * Imposta il sistema di archiviazione dati.\n    * \n    * @param var0 Il modulo Storage (es. MySQL o file locale)\n    */\n   public static void setStorage(Storage var0) {'),
    (r'public static Storage getStorage\(\) {',
     r'/**\n    * Recupera il sistema di archiviazione dati attivo.\n    * \n    * @return L\'oggetto Storage\n    */\n   public static Storage getStorage() {'),
    (r'public static PlayerProfile getPlayerProfile\(String var0\) {',
     r'/**\n    * Recupera il profilo di un giocatore.\n    * \n    * @param var0 Il nome del giocatore\n    * @return L\'oggetto PlayerProfile associato, o null se non trovato\n    */\n   public static PlayerProfile getPlayerProfile(String var0) {'),
    (r'public static PlayerProfile addPlayerProfile\(String var0\) {',
     r'/**\n    * Crea o registra un nuovo profilo giocatore.\n    * \n    * @param var0 Il nome del giocatore\n    * @return Il nuovo PlayerProfile\n    */\n   public static PlayerProfile addPlayerProfile(String var0) {'),
    (r'public static PlayerProfile removePlayerProfile\(String var0\) {',
     r'/**\n    * Rimuove il profilo di un giocatore dalla cache memory.\n    * \n    * @param var0 Il nome del giocatore\n    * @return L\'oggetto PlayerProfile rimosso\n    */\n   public static PlayerProfile removePlayerProfile(String var0) {'),
    (r'public static void reloadCache\(\) {',
     r'/**\n    * Ricarica la cache svuotandola e caricando nuovamente profili online.\n    */\n   public static void reloadCache() {'),
    (r'public static Collection<PlayerProfile> getPlayerProfiles\(\) {',
     r'/**\n    * Recupera una collezione contenente tutti i profili attivi in memoria.\n    * \n    * @return Collezione immutabile o copia dei profili\n    */\n   public static Collection<PlayerProfile> getPlayerProfiles() {'),
    (r'public static HookManager getHookManager\(\) {',
     r'/**\n    * Recupera il gestore degli hook (integrazioni esterne).\n    * \n    * @return HookManager\n    */\n   public static HookManager getHookManager() {'),
    (r'public static CommunicationModule getCommunicationModule\(\) {',
     r'/**\n    * Recupera il modulo di comunicazione network.\n    * \n    * @return L\'oggetto CommunicationModule in uso (es. VelocityModule)\n    */\n   public static CommunicationModule getCommunicationModule() {'),
    (r'public static void setCommunicationModule\(CommunicationModule var0\) {',
     r'/**\n    * Imposta il modulo di comunicazione network.\n    * \n    * @param var0 Il nuovo modulo da usare\n    */\n   public static void setCommunicationModule(CommunicationModule var0) {'),
    (r'public void reload\(\) {',
     r'/**\n    * Esegue il reload globale del plugin (config, storage, lingue, eventi).\n    */\n   public void reload() {')
]

for pat, rep in replacements:
    text = re.sub(pat, rep, text, count=1)

with open('src/main/java/com/velocityPort/UltimateFriends.java', 'w', encoding='utf-8') as f:
    f.write(text)
